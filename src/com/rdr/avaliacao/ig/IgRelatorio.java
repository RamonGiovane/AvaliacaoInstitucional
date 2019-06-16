package com.rdr.avaliacao.ig;

import static com.rdr.avaliacao.ig.InterfaceConstraints.TITULO_SALVAR_PDF;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.CellRendererPane;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.rdr.avaliacao.AvaliacaoInstitucional;
import com.rdr.avaliacao.es.EntradaESaida;
import com.rdr.avaliacao.questionario.Assunto;
import com.rdr.avaliacao.questionario.Pesquisa;
import com.rdr.avaliacao.relatorio.MediasDeNotas;
import com.rdr.avaliacao.relatorio.Relatorio;
import com.rdr.avaliacao.relatorio.RelatorioDeMedias;
import com.rdr.avaliacao.relatorio.RelatorioDeParticipantes;

import sun.font.FontFamily;

public class IgRelatorio extends JDialog implements PropriedadesDeJanela {
	private ButtonGroup buttonGroup =  new ButtonGroup();
	private JRadioButton radioBtnTabela;
	private JRadioButton radioBtnGrafico;
	private static JPanel panelDados;
	private static JPanel panelTabela, panelGrafico;

	private static TipoRelatorio tipoRelatorio;
	private static Pesquisa pesquisa;

	private String tipoGraduacao;

	private static IgRelatorio igRelatorio;
	private JScrollPane scrollPane;
	private static ChartPanel graficoPanel;
	private static JTable tabela;

	/**Variáveis que guardam informações que povoaram os gráficos e tabelas*/
	private Relatorio dadosRelatorio;

	private boolean graficoDesenhado, tabelaDesenhada;

	private static ConstrutorDeGrafico construtorDeGrafico;


	private IgRelatorio() {

		construtorDeGrafico = new ConstrutorDeGrafico();
	}

	private void definriParametrosRelatorio(TipoRelatorio tipoPesquisa, Pesquisa pesquisa, String tipoGraduacao) {
		System.out.println("def param - pesqu " + pesquisa );
		IgRelatorio.tipoRelatorio = tipoPesquisa;
		IgRelatorio.pesquisa = pesquisa;
		this.tipoGraduacao = tipoGraduacao;


	}


	private static IgRelatorio instanciar(TipoRelatorio tipoPesquisa, Pesquisa pesquisa, String tipoGraduacao) throws SQLException {

		if(igRelatorio == null) {
			igRelatorio = new IgRelatorio();
			igRelatorio.definriParametrosRelatorio(tipoPesquisa, pesquisa, tipoGraduacao);
			igRelatorio.construirIg();
		}
		else {
			igRelatorio.definriParametrosRelatorio(tipoPesquisa, pesquisa, tipoGraduacao);
			igRelatorio.atualizarTelaRelatorio();
		}
		return igRelatorio;
	}

	public static IgRelatorio getInstance(TipoRelatorio tipoPesquisa, Pesquisa pesquisa) throws SQLException {
		return instanciar(tipoPesquisa, pesquisa, null);
	}

	public static IgRelatorio getInstance(TipoRelatorio tipoPesquisa, Pesquisa pesquisa, String tipoGraduacao) throws SQLException {
		return instanciar(tipoPesquisa, pesquisa, tipoGraduacao);

	}


	private void construirIg() throws SQLException {
		setResizable(false);
		setTitle("Relat\u00F3rio de Autoavalia\u00E7\u00E3o Institucional");
		getContentPane().setLayout(null);
		setSize(850, 630);
		panelDados = new JPanel();
		panelDados.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Dados", TitledBorder.LEADING, TitledBorder.TOP, null, Color.DARK_GRAY));
		panelDados.setBounds(10, 11, 824, 504);

		panelDados.setLayout(new BorderLayout(0, 0));

		JPanel panelModoExibicao = new JPanel();
		panelModoExibicao.setBorder(new TitledBorder(null, "Modo de Exibi\u00E7\u00E3o", TitledBorder.LEADING, TitledBorder.TOP, null, Color.DARK_GRAY));
		panelModoExibicao.setBounds(10, 526, 824, 64);
		getContentPane().add(panelModoExibicao);
		panelModoExibicao.setLayout(null);

		radioBtnTabela = new JRadioButton("Tabela");
		radioBtnTabela.setSelected(true);
		radioBtnTabela.setBounds(208, 19, 109, 23);
		panelModoExibicao.add(radioBtnTabela);

		radioBtnGrafico = new JRadioButton("Gr\u00E1fico");
		radioBtnGrafico.setBounds(470, 19, 109, 23);
		panelModoExibicao.add(radioBtnGrafico);

		JButton btnGerarPdf = new JButton("Gerar PDF");
		btnGerarPdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				gerarPdf();
			}
		});
		btnGerarPdf.setBounds(681, 30, 89, 23);
		panelModoExibicao.add(btnGerarPdf);

		Font fonteTitle = new Font(Font.DIALOG, Font.BOLD, 11);
		((TitledBorder) panelDados.getBorder()).
		setTitleFont(fonteTitle);
		((TitledBorder) panelModoExibicao.getBorder()).
		setTitleFont(fonteTitle);

		buttonGroup.add(radioBtnGrafico);
		buttonGroup.add(radioBtnTabela);

		definirComportamentoRadioButtons();

		panelTabela = new JPanel();
		panelTabela.setLayout(new BorderLayout());

		panelGrafico = new JPanel();
		panelGrafico.setLayout(new BorderLayout());

		definirComportamentoRadioButtons();
		getContentPane().add(panelDados);
		panelDados.add(panelTabela, BorderLayout.CENTER);
		panelDados.add(panelTabela);	

		setModal(true);

		gerarDadosRelatorio();
		exibirTabela();
	}


	private void definirComportamentoRadioButtons() {
		radioBtnTabela.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				exibirTabela();
			}
		});

		radioBtnGrafico.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				exibirGrafico();

			}

		});


	}

	/**Gera e armazena os dados de um relatório a ser exibido*/
	private void gerarDadosRelatorio() throws SQLException {

		dadosRelatorio = AvaliacaoInstitucional.gerarRelatorio(pesquisa, tipoRelatorio, tipoGraduacao);
		IgAvaliacaoInstitucional.mudarCursor(Cursor.DEFAULT_CURSOR);

	}

	private void atualizarTelaRelatorio() throws SQLException {
		igRelatorio.gerarDadosRelatorio();

		if(radioBtnGrafico.isSelected())
			exibirGrafico();
		else
			exibirTabela();
	}

	private void gerarGrafico() {
		graficoPanel = null; 
		if(dadosRelatorio instanceof RelatorioDeParticipantes)
			graficoPanel = construtorDeGrafico.gerarGraficoBarra3D(
					(RelatorioDeParticipantes) dadosRelatorio, 
					dadosRelatorio.title(),
					tipoRelatorio.getOrientacaoGrafico());

		else if(dadosRelatorio instanceof RelatorioDeMedias) {
			graficoPanel = construtorDeGrafico.gerarGraficoLinha((RelatorioDeMedias)dadosRelatorio, dadosRelatorio.title(), 
					tipoRelatorio.getOrientacaoGrafico());
		}

		panelGrafico.add(graficoPanel, BorderLayout.CENTER);
		

	}

	/**Constroi e exibe um gráfico a partir dos dados de um relatório*/
	private void exibirGrafico() {
		limparPaineis();

		gerarGrafico();

		panelDados.add(panelGrafico);

		repintarPaineis();
	}


	private void gerarTabela() {
		tabela = EntradaESaida.gerarTabela(dadosRelatorio.asMatrix(), dadosRelatorio.getHeaders());
		scrollPane = new JScrollPane(tabela);
		panelTabela.add(scrollPane, BorderLayout.CENTER);
		panelTabela.add(tabela.getTableHeader(), BorderLayout.NORTH);
		panelTabela.add(tabela);
		panelDados.add(new JScrollPane(panelTabela));

	}

	private void exibirTabela() {

		limparPaineis();

		gerarTabela();

		repintarPaineis();
	}



	/**Necessário para limpar o conteúdo dos painéis antes de exibir um novo componente*/
	private void limparPaineis() {
//		panelGrafico.removeAll();
//		panelTabela.removeAll();
		panelDados.removeAll();

	}
	
	private void limparTudo() {
		panelGrafico.removeAll();
		panelTabela.removeAll();
		panelDados.removeAll();
	}

	/**Necessário para atualizar os painéis depois de mudar*/
	private void repintarPaineis() {
		panelTabela.revalidate();
		panelTabela.repaint();

		panelGrafico.revalidate();
		panelGrafico.repaint();

		panelDados.revalidate();
		panelDados.repaint();

		panelTabela.setVisible(true);
		panelDados.setVisible(true);

	}

	public void gerarPdf() {
		String caminhoArquivo = EntradaESaida.dialogoGravarArquivo(this, TITULO_SALVAR_PDF);
		try {
			construtorDeGrafico.gerarPDF(caminhoArquivo, tipoRelatorio.getDescricao());
			//construtorDeGrafico.writeChartToPDF(graficoPanel.getChart(), 550, 500, caminhoArquivo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void exibir(Component janelaPai) {
		gerarGrafico();
		gerarTabela();
		
		setTitle(tipoRelatorio.getDescricao());
		if(tipoRelatorio == TipoRelatorio.CONCEITO_MEDIO_CURSO)
			((TitledBorder) panelDados.getBorder()).setTitle("Dados de " + tipoGraduacao + "s");
		else
			((TitledBorder) panelDados.getBorder()).setTitle("Dados");
		setLocationRelativeTo(janelaPai);
		setVisible(true);
	}

	@Override
	public void esconder() {
		limparTudo();
		setVisible(false);
	}

	@Override
	public void fechar() {
		dispose();
	}

	private boolean checarPainelVazio(JPanel painel) {
		return painel.getWidth() == 0 ? true : false;
	}

	private class ConstrutorDeGrafico {
		public ChartPanel gerarGraficoLinha(RelatorioDeMedias dadosRelatorio, String titulo, PlotOrientation orientacao) {
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			MediasDeNotas medias;
			Assunto assuntos[]; 
			for(int i = 0; i<dadosRelatorio.size(); i++) {
				medias = dadosRelatorio.obter(i);
				assuntos = medias.obterAssuntos();
				for(Assunto assunto : assuntos) {
					dataset.addValue(medias.obterNota(assunto), medias.getDescricao(), assunto.getDescricao());
				}
				dataset.addValue(medias.obterMediaGeral(), medias.getDescricao(), "Conceito Médio Geral");
			}


			JFreeChart chart = ChartFactory.createLineChart(titulo, null, null, 
					dataset, PlotOrientation.VERTICAL, true, true, true);

			CategoryPlot categoryP = chart.getCategoryPlot();
			CategoryItemRenderer renderer = categoryP.getRenderer();

			for(int i = 0; i<dataset.getRowCount(); i++) {
				renderer.setSeriesStroke(i, new BasicStroke(2));

			}


			//Organizando o posicionamento das legendas abaixo do gráfico
			CategoryAxis domainAxis = categoryP.getDomainAxis();
			domainAxis.setCategoryLabelPositions(
					CategoryLabelPositions.createUpRotationLabelPositions(
							Math.PI / 6.0));

			Font font3 = new Font(Font.DIALOG, Font.PLAIN, 50); 
			domainAxis.setLabelFont(font3);

			//Colorindo o plot
			categoryP.setBackgroundPaint(Color.WHITE);
			categoryP.setDomainGridlinePaint(Color.DARK_GRAY);
			categoryP.setRangeGridlinePaint(Color.DARK_GRAY);
			categoryP.setOutlineVisible(true);
			categoryP.setDomainGridlinesVisible(true);


			renderer.setBaseItemLabelPaint(Color.BLACK);
			//renderer.setBaseItemLabelFont(new Font(Font.DIALOG, Font.BOLD, 14));

			ChartPanel panel = new ChartPanel(chart);

			return panel;

		}

		private void adicionarTitulo(Document documento, String textoTitulo) throws DocumentException {
			BaseColor corTitulo = new BaseColor(134, 148, 196), corFundo = new BaseColor(219, 229, 241);
			com.itextpdf.text.Font fonte = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, Font.BOLD, corTitulo);
			Chunk chunk = new Chunk(textoTitulo, fonte);

			chunk.setBackground(corFundo);
			Paragraph titulo = new Paragraph(chunk);
			titulo.setAlignment(Element.ALIGN_CENTER);

			documento.add(titulo);
		}

		private void adicionarComoImgem(Document documento, PdfWriter pdfWriter, 
				JComponent componente, int escala, boolean renderizado) throws IOException, DocumentException {

			System.out.println(componente);
			BufferedImage image = EntradaESaida.componenteToImage(componente, renderizado);

			//EntradaESaida.msgInfo(this, image, "dw");
			com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(pdfWriter, image, 1);

			img.scalePercent(escala);

			documento.add(img);
		}

		private void novaLinha(Document documento) throws DocumentException {
			documento.add(new Paragraph("\n\n"));
		}

		public void gerarPDF(String localDeSalvamento, String titulo) throws DocumentException, IOException {


			FileOutputStream outputStream = new FileOutputStream(localDeSalvamento);

			Document documento = new Document();

			PdfWriter pdfWriter = PdfWriter.getInstance(documento, outputStream);
			
			documento.open();
			
			adicionarTitulo(documento, titulo);
			
			if(checarPainelVazio(panelTabela)) gerarTabela();
			
			adicionarComoImgem(documento, pdfWriter, panelTabela, 60, radioBtnTabela.isSelected());
			
			novaLinha(documento);
			
			if(checarPainelVazio(panelGrafico)) gerarGrafico();
			
			adicionarComoImgem(documento, pdfWriter, panelGrafico, 65, radioBtnGrafico.isSelected());

			
			documento.close();
		}


		public ChartPanel gerarGraficoBarra3D(RelatorioDeParticipantes dadosRelatorio, 
				String titulo, PlotOrientation orientacao) {

			DefaultCategoryDataset dataset = new DefaultCategoryDataset( );

			for(int i  = 0; i<dadosRelatorio.size(); i++) {
				dataset.addValue(dadosRelatorio.obter(i).getValorLinha(), dadosRelatorio.obter(i).getValorColuna(), 
						dadosRelatorio.obter(i).getValorColuna());
			}

			JFreeChart chart = ChartFactory.createBarChart3D(titulo, null, null, 
					dataset, orientacao, true, false, false);

			CategoryPlot plot = chart.getCategoryPlot();
			CategoryItemRenderer renderer = plot.getRenderer();



			BarRenderer3D barRenderer = (BarRenderer3D) plot.getRenderer();

			plot.getDomainAxis().setMaximumCategoryLabelWidthRatio(10);

			barRenderer.setItemMargin(-1.07);
			CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();


			for(int i = 0; i<dataset.getRowCount(); i++) {
				barRenderer.setSeriesItemLabelGenerator(i, generator);
				barRenderer.setSeriesItemLabelsVisible(i, true);
				if(orientacao == PlotOrientation.HORIZONTAL)
					barRenderer.setSeriesPositiveItemLabelPosition(i, new ItemLabelPosition(ItemLabelAnchor.OUTSIDE3,
							TextAnchor.CENTER_LEFT));
				else
					barRenderer.setSeriesPositiveItemLabelPosition(i, new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
							TextAnchor.BASELINE_CENTER));

				barRenderer.setItemLabelAnchorOffset(15);

			}

			//Retirando legendas inferiores
			renderer.setBaseSeriesVisibleInLegend(false);

			//Colorindo o plot
			plot.setBackgroundPaint(Color.WHITE);
			plot.setDomainGridlinePaint(Color.DARK_GRAY);
			plot.setRangeGridlinePaint(Color.DARK_GRAY);
			plot.setOutlineVisible(false);


			renderer.setBaseItemLabelPaint(Color.BLACK);
			renderer.setBaseItemLabelFont(new Font(Font.DIALOG, Font.BOLD, 14));





			ChartPanel panel = new ChartPanel(chart);

			return panel;
		}


		public void writeChartToPDF(JFreeChart chart, int width, int height, String fileName) {
			PdfWriter writer = null;

			Document document = new Document();

			try {
				writer = PdfWriter.getInstance(document, new FileOutputStream(
						fileName));
				document.open();

				PdfContentByte contentByte = writer.getDirectContent();
				PdfGraphics2D pdfGraphics2D = new PdfGraphics2D(contentByte, width, height);

				PdfTemplate template = contentByte.createTemplate(width, height);
				Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width,
						height);

				chart.draw(pdfGraphics2D, rectangle2d);

				pdfGraphics2D.dispose();
				contentByte.addTemplate(template, 0, 0);

			} catch (Exception e) {
				e.printStackTrace();
			}
			document.close();
		}
	}
}

