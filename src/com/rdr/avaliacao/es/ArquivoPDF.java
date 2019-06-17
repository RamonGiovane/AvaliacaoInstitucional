package com.rdr.avaliacao.es;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JTable;

import org.jfree.chart.JFreeChart;

import com.itextpdf.awt.DefaultFontMapper;
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

public class ArquivoPDF implements AutoCloseable {
	private FileOutputStream outputStream;
	private Document documento;
	private PdfWriter pdfWriter;
	
	private BaseColor colors[];
	private Font fontesColoridas[];
	
	public final static BaseColor COR_TITULO = new BaseColor(134, 148, 196), 
			COR_FUNDO_TITULO = new BaseColor(219, 229, 241);
	

	public void open(String nomeArquivo) throws FileNotFoundException, DocumentException {
		outputStream = new FileOutputStream(nomeArquivo);
		
		//Cria um documento
		documento = new Document();

		//Inicia o objeto responsável por escricer no arquivo.
		pdfWriter = PdfWriter.getInstance(documento, outputStream);
		
		documento.open();
	}
	
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
	
	public void adicionarJTable(JTable table, boolean usarCoresCustomizadas) throws DocumentException, IOException {

		int tamanhoFonte = 9, numeroColunas = table.getColumnCount(), numeroLinhas = table.getRowCount();
		
		PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
		pdfTable.setWidthPercentage(100);
		pdfTable.setHorizontalAlignment(Element.ALIGN_CENTER);
		//TODO CHECK THIS OUT
		pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

		
		Font fontePretaNegrito = new  Font(Font.FontFamily.HELVETICA, tamanhoFonte, 
				Font.BOLD, BaseColor.BLACK);

		Font fontePreta = new  Font(Font.FontFamily.HELVETICA, tamanhoFonte, 
				Font.NORMAL, BaseColor.BLACK);

		//Cria as fontes de acordo com o estilo desejado
		criarFontes(usarCoresCustomizadas, tamanhoFonte);

		System.out.println(fontesColoridas.length);

		//Array que guarda as larguras das colunas
		int larguras[] = new int[numeroColunas];

		for (int i = 0, indexFonte = 0; i < numeroColunas; i++, indexFonte++) {
			if(indexFonte >= fontesColoridas.length) indexFonte = 0;
			if(i == 0) {
				pdfTable.addCell(new Phrase(table.getColumnName(i), fontePretaNegrito));
				larguras[i] = (int) (table.getColumnName(i).length() * 1.5);
			}
			else {
				pdfTable.addCell(new Phrase(table.getColumnName(i), fontesColoridas[indexFonte]));
				larguras[i] = table.getColumnName(i).length() + 5;
			}
		}

		pdfTable.setWidths(larguras);

		Font fonte;
		PdfPCell celula;
		//Extraindo os dados do JTable. Percorrendo as linhas
		for (int rows = 0; rows < numeroLinhas; rows++) {

			//Percorrendo as colunas
			for (int cols = 0, fontesIndex = 0; cols < numeroColunas; cols++, fontesIndex++) {

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


				//Adiciona o texto e a fonte no phrase
				celula = new PdfPCell(new Phrase(table.getModel().getValueAt(rows, cols).toString(), fonte));

				//Define a cor de fundo de uma célua
				if(rows % 2 != 0 || rows == numeroLinhas)
					celula.setBackgroundColor(new BaseColor(242, 242, 242));

				//Define o alinhamento das linhas
				celula.setVerticalAlignment(Element.ALIGN_MIDDLE);
				if(cols != 0) {
					celula.setHorizontalAlignment(Element.ALIGN_CENTER);
				}

				//Adiciona o phrase na tabela
				pdfTable.addCell(celula);

			}
		}
		documento.add(pdfTable);

	}

	private void criarFontes(boolean usarCoresCustomizadas, int tamanhoFonte) {
		//Criar fontes
		if(usarCoresCustomizadas == true) {
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

	@Override
	public void close() throws IOException {
		try{
			if(documento != null) documento.close();
		}catch (Exception e) {
			throw new IOException();
		}
		
	}
	
}
