package com.rdr.avaliacao.ig;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
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

import com.rdr.avaliacao.AvaliacaoInstitucional;
import com.rdr.avaliacao.es.EntradaESaida;
import com.rdr.avaliacao.questionario.Pesquisa;
import com.rdr.avaliacao.relatorio.DadosDeGrafico;
import com.rdr.avaliacao.relatorio.Relatorio;
import com.rdr.avaliacao.relatorio.RelatorioDeParticipantes;

public class IgRelatorio extends JDialog implements PropriedadesDeJanela {
	private ButtonGroup buttonGroup =  new ButtonGroup();
	private JRadioButton radioBtnTabela;
	private JRadioButton radioBtinGrafico;
	private static JPanel panelDados;
	private static JPanel panelTabela;

	private static TipoRelatorio tipoRelatorio;
	private static Pesquisa pesquisa;

	private String tipoGraduacao;

	private static IgRelatorio igRelatorio;

	/**Variáveis que guardam informações que povoaram os gráficos e tabelas*/
	private Relatorio dadosRelatorio;

	private static ConstrutorDeGrafico construtorDeGrafico;


	private IgRelatorio() {
		//construirIg();
		construtorDeGrafico = new ConstrutorDeGrafico();
	}

	private void definriParametrosRelatorio(TipoRelatorio tipoPesquisa, Pesquisa pesquisa, String tipoGraduacao) {
		System.out.println("def param - pesqu " + pesquisa );
		this.tipoRelatorio = tipoPesquisa;
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
		setSize(715, 583);
		panelDados = new JPanel();
		panelDados.setBorder(new TitledBorder(null, "Dados", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelDados.setBounds(10, 11, 689, 458);

		panelDados.setLayout(new BorderLayout(0, 0));

		JPanel panelModoExibicao = new JPanel();
		panelModoExibicao.setBorder(new TitledBorder(null, "Modo de Exibi\u00E7\u00E3o", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelModoExibicao.setBounds(10, 481, 689, 64);
		getContentPane().add(panelModoExibicao);
		panelModoExibicao.setLayout(null);

		radioBtnTabela = new JRadioButton("Tabela");
		radioBtnTabela.setSelected(true);
		radioBtnTabela.setBounds(165, 19, 109, 23);
		panelModoExibicao.add(radioBtnTabela);

		radioBtinGrafico = new JRadioButton("Gr\u00E1fico");
		radioBtinGrafico.setBounds(364, 19, 109, 23);
		panelModoExibicao.add(radioBtinGrafico);

		JButton btnGerarPdf = new JButton("Gerar PDF");
		btnGerarPdf.setEnabled(false);
		btnGerarPdf.setBounds(560, 30, 89, 23);
		panelModoExibicao.add(btnGerarPdf);


		buttonGroup.add(radioBtinGrafico);
		buttonGroup.add(radioBtnTabela);

		definirComportamentoRadioButtons();

		panelTabela = new JPanel();
		panelTabela.setLayout(new BorderLayout());
		definirComportamentoRadioButtons();
		getContentPane().add(panelDados);
		panelDados.add(panelTabela, BorderLayout.CENTER);
		panelDados.add(panelTabela);	
		gerarDadosRelatorio();
		exibirTabela();
	}


	private void definirComportamentoRadioButtons() {
		radioBtnTabela.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				System.out.println("Hey");
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
		if(dadosRelatorio instanceof RelatorioDeParticipantes)
			panelTabela.add(construtorDeGrafico.gerarGraficoBarra3D(
					(RelatorioDeParticipantes) dadosRelatorio, 
					tipoRelatorio.getNomeRelatório(), 
					tipoRelatorio.getOrientacaoGrafico()), BorderLayout.CENTER);

	
		panelDados.add(panelTabela);

		repintarPaineis();
	}


	private void exibirTabela() {

		limparPaineis();

		System.out.println(dadosRelatorio);
		System.out.println(tipoRelatorio);
		JTable tabela = EntradaESaida.gerarTabela(dadosRelatorio.asMatrix(), dadosRelatorio.getHeaders());

		panelTabela.add(new JScrollPane(tabela), BorderLayout.CENTER);
		panelTabela.add(tabela.getTableHeader(), BorderLayout.NORTH);
		panelTabela.add(tabela);
		panelDados.add(new JScrollPane(panelTabela));

		repintarPaineis();
	}

	/**Necessário para limpar o conteúdo dos painéis antes de exibir um novo componente*/
	private void limparPaineis() {

		panelTabela.removeAll();
		panelDados.removeAll();

	}

	/**Necessário para atualizar os painéis depois de mudar*/
	private void repintarPaineis() {
		panelTabela.revalidate();
		panelTabela.repaint();
		panelDados.revalidate();
		panelDados.repaint();

		panelTabela.setVisible(true);
		panelDados.setVisible(true);

	}

	@Override
	public void exibir(Component janelaPai) {
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
		public <T extends DadosDeGrafico> ChartPanel gerarGraficoBarra3D(RelatorioDeParticipantes dadosRelatorio, 
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
			//plot.getDomainAxis().setCategoryMargin(2);

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


			//TODO: Isso deixa as legendas deitadas
			//		 CategoryAxis domainAxis = plot.getDomainAxis();
			//	        domainAxis.setCategoryLabelPositions(
			//	                CategoryLabelPositions.createUpRotationLabelPositions(
			//	                        Math.PI / 6.0));

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

