package com.rdr.avaliacao.es;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JTable;

import org.jfree.chart.JFreeChart;

import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.testutils.ITextTest;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**Classe responsável por abrir, adicionar conteúdo e fechar documentos no formato PDF utlizando a biblioteca
 * <code>Itext</code>.
 * @author Ramon Giovane
 * 
 * @see ITextTest
 *
 */
public class ArquivoPDF implements AutoCloseable {
	private FileOutputStream outputStream;
	private Document documento;
	private PdfWriter pdfWriter;

	private BaseColor colors[];
	private Font fontesColoridas[];

	public final static BaseColor COR_TITULO = new BaseColor(134, 148, 196), 
			COR_FUNDO_TITULO = new BaseColor(219, 229, 241);

	private final int tamanhoFonte = 9;

	private final 	Font fontePretaNegrito = new  Font(Font.FontFamily.HELVETICA, tamanhoFonte, 
			Font.BOLD, BaseColor.BLACK),

			fontePreta = new  Font(Font.FontFamily.HELVETICA, tamanhoFonte, 
					Font.NORMAL, BaseColor.BLACK);

	/**
	 * Abre um documento para  escrito e ser salvo em PDF.
	 * 
	 * @param nomeArquivo caminho onde o arquivo será salvo.
	 * @throws FileNotFoundException se o caminho passado não existe
	 * @throws DocumentException se ocorrer um erro na abertura do documento
	 */
	public void open(String nomeArquivo) throws FileNotFoundException, DocumentException {
		outputStream = new FileOutputStream(nomeArquivo);

		//Cria um documento
		documento = new Document();

		//Inicia o objeto responsável por escricer no arquivo.
		pdfWriter = PdfWriter.getInstance(documento, outputStream);

		documento.open();
	}

	/**
	 * Adiciona um título estilizado dentro de um documento aberto. 
	 * @param textoTitulo conteúdo do título
	 * @throws DocumentException se o documento estiver fechado
	 */
	public void adicionarTitulo(String textoTitulo) throws DocumentException {
		Font fonte = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, COR_TITULO);
		Chunk chunk = new Chunk(textoTitulo, fonte);

		chunk.setBackground(COR_FUNDO_TITULO);
		Paragraph titulo = new Paragraph(chunk);

		titulo.setAlignment(Element.ALIGN_CENTER);

		documento.add(titulo);
	}

	public void novaLinha() throws DocumentException {
		documento.add(new Paragraph("\n"));
	}


	/**
	 * Adiciona uma tabela no documento. Copia o conteúdo de uma JTable em um PdfPTable, formatando e estilizando-o
	 * @param table {@link JTable} contendo o conteúdo a ser adicionado
	 * @param usarEstiloCustomizado se for <code>true</code> usa cores distintas para cada coluna, adiciona o cabeçalho em negrito
	 *  e ajusta o tamanho das colunas
	 *  
	 * @throws DocumentException se o documento estiver fechado
	 */
	public void adicionarJTable(JTable table, boolean usarEstiloCustomizado) throws DocumentException {


		PdfPTable pdfTable = jTableToPDFTable(table, usarEstiloCustomizado);

		documento.add(pdfTable);

	}

	/**
	 * Copia o conteúdo de uma JTable em um PdfPTable, formatando e estilizando-o
	 */
	private PdfPTable jTableToPDFTable(JTable table, boolean usarEstiloCustomizado) throws DocumentException {
		int tamanhoFonte = 9, numeroColunas = table.getColumnCount(), numeroLinhas = table.getRowCount();

		PdfPTable pdfTable = new PdfPTable(table.getColumnCount());

		definirAlinhamentoPadraoTabela(pdfTable);

		//Cria as fontes de acordo com o estilo desejado
		criarFontes(usarEstiloCustomizado, tamanhoFonte);

		System.out.println(fontesColoridas.length);

		adicionarCabecalhosTabela(pdfTable, table, numeroColunas, usarEstiloCustomizado);

		//Fonte que será usada nas tabelas
		Font fonte;

		//Extraindo os dados do JTable. Percorrendo as linhas
		for (int rows = 0; rows < numeroLinhas; rows++) {

			//Percorrendo as colunas
			for (int cols = 0, fontesIndex = 0; cols < numeroColunas; cols++, fontesIndex++) {


				//Colore as celulas se o estilo customizado estiver ativado
				if(usarEstiloCustomizado) {
					//Reseta o indice do array de fontes, se todas ja tiverem sido usadas
					if(fontesIndex >= colors.length) fontesIndex = 0;

					if(cols == 0) {
						//Se for a primeira linha da primeira coluna
						if(rows == numeroLinhas-1)
							fonte = fontePretaNegrito;
						//Se for a primeira linha de qualquer coluna
						else
							fonte = fontePreta;
					}

					//Em nenhum dos casos acima, usa a fonte colorida no indice de fonte atual
					else fonte = fontesColoridas[fontesIndex];
				}



				//Se estiver desativado, usa a fonte padrão
				else fonte =  fontePreta; 

				//Após definir a fonte, adiciona o conteúdo da célula da JTable em uma céula da PdfPTable
				adicionarCelulaTabela(pdfTable, table.getModel().getValueAt(rows, cols).toString(), 
						fonte, rows, cols, numeroLinhas);

			}//for colunas

		}//for linhas

		return pdfTable;
	}

	/**Copia o conteúdo de uma célula do JTable para dentro de uma célula do PdfPTable.*/
	private void adicionarCelulaTabela(PdfPTable pdfTable, String conteudoCelula, Font fonte, int nLinhaAtual,
			int nColunaAtual, int numeroTotalLinhas) {


		//Cria a celual e adiciona o texto e a fonte
		PdfPCell celula = new PdfPCell(new Phrase(conteudoCelula, fonte));

		//Define a cor de fundo de uma célua
		if(nLinhaAtual % 2 != 0 || nLinhaAtual == numeroTotalLinhas)
			celula.setBackgroundColor(new BaseColor(242, 242, 242));

		//Define o alinhamento das linhas
		celula.setVerticalAlignment(Element.ALIGN_MIDDLE);
		if(nColunaAtual != 0) {
			celula.setHorizontalAlignment(Element.ALIGN_CENTER);
		}

		//Adiciona a celula na tabela
		pdfTable.addCell(celula);

	}

	/**Configura o alinhamento da tabela no documento*/
	private void definirAlinhamentoPadraoTabela(PdfPTable pdfTable) {
		pdfTable.setWidthPercentage(100);
		pdfTable.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
	}

	/**Adciona o cabeçalho da tabela no docuemnto*/
	private void adicionarCabecalhosTabela(PdfPTable pdfTable, JTable table, int numeroColunas, boolean usarEstiloCustomizado) throws DocumentException {

		//Array que guarda as larguras das colunas
		int larguras[] = new int[numeroColunas];

		for (int i = 0, indexFonte = 0; i < numeroColunas; i++, indexFonte++) {
			//Reseta o indice de fontes
			if(indexFonte == fontesColoridas.length) indexFonte = 0;

			//Se o estilo customizado não estiver ativo, apenas adiciona a fonte preta
			if(!usarEstiloCustomizado) {
				pdfTable.addCell(new Phrase(table.getColumnName(i), fontePretaNegrito)); 
				continue;
			}


			if(i == 0) {
				pdfTable.addCell(new Phrase(table.getColumnName(i), fontePretaNegrito));
				larguras[i] = (int) (table.getColumnName(i).length() * 1.5);
			}
			else {
				pdfTable.addCell(new Phrase(table.getColumnName(i), fontesColoridas[indexFonte]));
				larguras[i] = table.getColumnName(i).length() + 5;
			}
		}

		if(usarEstiloCustomizado)
			pdfTable.setWidths(larguras);
	}

	/**
	 * Cria as fontes estilizadas, se <code>usarEstiloCustomizado</code> for <code>true</code>.
	 */
	private void criarFontes(boolean usarEstiloCustomizado, int tamanhoFonte) {

		if(usarEstiloCustomizado == true) {
			colors = new BaseColor[] {BaseColor.BLACK, BaseColor.BLUE, new BaseColor(64, 132, 36), BaseColor.RED, new BaseColor(236, 156, 17),
					BaseColor.MAGENTA}; 
			fontesColoridas = new Font[6];
		}
		else {
			colors = new BaseColor[] {BaseColor.BLACK};
			fontesColoridas = new Font[1];
		}

		for(int i = 0; i<fontesColoridas.length; i++) {
			fontesColoridas[i] = new Font(Font.FontFamily.HELVETICA, tamanhoFonte, Font.BOLD, colors[i]);
		}

	}



	/**Adiciona um gráfico ({@link JFreeChart}) em um documento PDF como imagem.
	 * O documento precisa existir e estar aberto.
	 * @param chart gráfico no formato <code>JFreeChart</code>
	 * @param width largura da imagem a ser gerada
	 * @param height altura da imagem a ser gerada
	 * @throws DocumentException caso ocorra um erro ou documento está fechado.
	 */
	public void adicionarGrafico(JFreeChart chart, int width, int height) throws DocumentException {


		PdfContentByte contentByte = pdfWriter.getDirectContent();

		//Cria um template
		PdfTemplate template = contentByte.createTemplate(width, height);

		/*Tive de usar esse método depreciado pois não consegui fazer do modo como a orienta a documentação:
		 * 
		 * 		Graphics2D graphics = new PdfGraphics2D(contentByte, largura, altura);
		 * 
		 * Usando a solução acima, o código compila normal, porém o método algumas linhas abaixo, não consegue mais
		 *  redimensionar a imagem (que tem largura muito maior que o gráfico) sem o objeto template.
		 */

		@SuppressWarnings("deprecation")
		Graphics2D graphics = 
		template.createGraphics(width, height, new DefaultFontMapper());

		//Cria um retângulo, onde a imagem sera desenhada
		Rectangle2D retangulo = new Rectangle2D.Double(0, 0, width, height);

		//Desenha e libera os recursos do grafico
		chart.draw(graphics, retangulo, null);
		graphics.dispose();

		//create Image from PdfTemplate
		Image image = Image.getInstance(template);

		//Redimensiona a imagem para que caiba no PDF sem perder qualidade.
		image.scalePercent(70);

		//Alinha ao centro
		image.setAlignment(Element.ALIGN_CENTER);

		//Finalmente, adiciona a imagem no documento
		documento.add(image);

	}

	/**
	 * Fecha o documento. Quando este método for executado com sucesso, o arquivo com as alterações feitas poderá ser utilizado.
	 * 
	 * @throws IOException se o arquivo já estiver fechado, ou acontecer algum erro.
	 */
	@Override
	public void close() throws IOException {
		try{
			if(documento != null) documento.close();
		}catch (Exception e) {
			throw new IOException(e.getCause());
		}

	}

}
