package com.rdr.avaliacao.ig;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
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

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.rdr.avaliacao.AvaliacaoInstitucional;
import com.rdr.avaliacao.es.EntradaESaida;
import com.rdr.avaliacao.questionario.Assunto;
import com.rdr.avaliacao.questionario.Pesquisa;
import com.rdr.avaliacao.relatorio.MediasDeNotas;
import com.rdr.avaliacao.relatorio.Relatorio;
import com.rdr.avaliacao.relatorio.RelatorioDeMedias;
import com.rdr.avaliacao.relatorio.RelatorioDeParticipantes;
import static com.rdr.avaliacao.ig.InterfaceConstraints.*;

public class IgRelatorio extends JDialog implements PropriedadesDeJanela {
	private ButtonGroup buttonGroup =  new ButtonGroup();
	private JRadioButton radioBtnTabela;
	private JRadioButton radioBtinGrafico;
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

	private static ConstrutorDeGrafico construtorDeGrafico;


	private IgRelatorio() {
//		try {
//			construirIg();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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

		radioBtinGrafico = new JRadioButton("Gr\u00E1fico");
		radioBtinGrafico.setBounds(470, 19, 109, 23);
		panelModoExibicao.add(radioBtinGrafico);

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

		buttonGroup.add(radioBtinGrafico);
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

		radioBtinGrafico.addActionListener(new ActionListener() {

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

		if(radioBtinGrafico.isSelected())
			exibirGrafico();
		else
			exibirTabela();
	}

	/**Constroi e exibe um gráfico a partir dos dados de um relatório*/
	private void exibirGrafico() {
		limparPaineis();
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

		panelDados.add(panelGrafico);

		repintarPaineis();
	}

	
	

	private void exibirTabela() {

		limparPaineis();

		System.out.println(dadosRelatorio);
		System.out.println(tipoRelatorio);
		tabela = EntradaESaida.gerarTabela(dadosRelatorio.asMatrix(), dadosRelatorio.getHeaders());
		scrollPane = new JScrollPane(tabela);
		panelTabela.add(scrollPane, BorderLayout.CENTER);
		panelTabela.add(tabela.getTableHeader(), BorderLayout.NORTH);
		panelTabela.add(tabela);
		panelDados.add(new JScrollPane(panelTabela));
		

		repintarPaineis();
	}



	/**Necessário para limpar o conteúdo dos painéis antes de exibir um novo componente*/
	private void limparPaineis() {
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
			construtorDeGrafico.gerarPDF(caminhoArquivo, tabela, graficoPanel);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void exibir(Component janelaPai) {
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
		setVisible(false);
	}

	@Override
	public void fechar() {
		dispose();
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



//			BarRenderer3D barRenderer = (BarRenderer3D) plot.getRenderer();

			//categoryP.getDomainAxis().setMaximumCategoryLabelWidthRatio(-10);
			//plot.getDomainAxis().setCategoryMargin(2);

		


			for(int i = 0; i<dataset.getRowCount(); i++) {
				renderer.setSeriesStroke(i, new BasicStroke(2));

			}

			
			//Organizando o posicionamento das legendas abaixo do gráfico
			CategoryAxis domainAxis = categoryP.getDomainAxis();
			domainAxis.setCategoryLabelPositions(
					CategoryLabelPositions.createUpRotationLabelPositions(
							Math.PI / 6.0));
			
			Font font3 = new Font("Dialog", Font.PLAIN, 50); 
			domainAxis.setLabelFont(font3);
			
//			domainAxis.setMinorTickMarksVisible(true);
//			domainAxis.setTickLabelsVisible(true);
//			domainAxis.setAxisLineVisible(true);

			
			//Colorindo o plot
			categoryP.setBackgroundPaint(Color.WHITE);
			categoryP.setDomainGridlinePaint(Color.DARK_GRAY);
			categoryP.setRangeGridlinePaint(Color.DARK_GRAY);
			categoryP.setOutlineVisible(true);
			categoryP.setDomainGridlinesVisible(true);
			//plot.setRangeMinorGridlinesVisible(true);

			
			renderer.setBaseItemLabelPaint(Color.BLACK);
			//renderer.setBaseItemLabelFont(new Font(Font.DIALOG, Font.BOLD, 14));

			ChartPanel panel = new ChartPanel(chart);
//			int x = 854, y = 480 ;
//			ChartPanel panel = new ChartPanel(chart,x, y, x, y, x, y, 
//					true, true, true, true, true, true, true);

			return panel;


		}

		public void gerarPDF(String localDeSalvamento, JTable tabela, ChartPanel graficoPanel) throws DocumentException, IOException {
			
			FileOutputStream outputStream = new FileOutputStream(localDeSalvamento);
			
			Document documento = new Document();
			
			 
			
			PdfWriter pdfWriter = PdfWriter.getInstance(documento, outputStream);
			documento.open();
			
			  BufferedImage image = new BufferedImage(panelTabela.getWidth(), panelTabela.getHeight(), BufferedImage.TYPE_INT_RGB);
					 
		      panelTabela.paint(image.getGraphics());
		      
		     com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(pdfWriter, image, 1);
		    
		     img.scalePercent(65);
		     documento.add(img);
		    
		     
////		     BufferedImage image1 = new BufferedImage(graficoPanel.getWidth(), graficoPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
////			 
////		      graficoPanel.paint(image.getGraphics());
////		      
////		     com.itextpdf.text.Image img1 = com.itextpdf.text.Image.getInstance(pdfWriter, image1, 1);
////		    
////		     img1.scalePercent(65);
//		     documento.add(img1);
		     
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



	}
}

