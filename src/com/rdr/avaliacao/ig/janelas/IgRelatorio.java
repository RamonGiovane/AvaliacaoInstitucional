package com.rdr.avaliacao.ig.janelas;

import static com.rdr.avaliacao.ig.InterfaceConstraints.DESCRICOES_EXTENSOES_PDF;
import static com.rdr.avaliacao.ig.InterfaceConstraints.EXTENSOES_PDF;
import static com.rdr.avaliacao.ig.InterfaceConstraints.PONTO;
import static com.rdr.avaliacao.ig.InterfaceConstraints.TITULO_SALVAR_PDF;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
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

import com.itextpdf.text.DocumentException;
import com.rdr.avaliacao.AvaliacaoInstitucional;
import com.rdr.avaliacao.es.ArquivoPDF;
import com.rdr.avaliacao.es.EntradaESaida;
import com.rdr.avaliacao.ig.LookAndFeel;
import com.rdr.avaliacao.ig.PropriedadesDeJanela;
import com.rdr.avaliacao.ig.TipoRelatorio;
import com.rdr.avaliacao.questionario.Assunto;
import com.rdr.avaliacao.relatorio.MediasDeNotas;
import com.rdr.avaliacao.relatorio.Relatorio;
import com.rdr.avaliacao.relatorio.RelatorioDeMedias;
import com.rdr.avaliacao.relatorio.RelatorioDeParticipantes;

public class IgRelatorio extends JDialog implements PropriedadesDeJanela {

	/**
	 * Componentes da interface, salvos de forma a serem mais acessíveis durante o 
	 * funcionamento da classe
	 */
	private ButtonGroup buttonGroup =  new ButtonGroup();
	private JRadioButton radioBtnTabela;
	private JRadioButton radioBtnGrafico;
	private static JPanel panelDados, panelModoExibicao;
	private static JPanel panelTabela, panelGrafico;
	private static IgRelatorio igRelatorio;
	private static ChartPanel graficoPanel;
	private static JTable tabela;

	/**
	 * Parâmteros de pesquisa que variam de acordo com cada chamada da instância da janela
	 */
	private static TipoRelatorio tipoRelatorio;
	private String tipoGraduacao;


	private static ArquivoPDF arquivoPDF;

	/**Variáveis que guardam informações que povoarão os gráficos e tabelas*/
	private Relatorio dadosRelatorio;


	private static GeradorDeArtefatosDeRelatorio artefatosDeRelatorio;
	private static AvaliacaoInstitucional app;


	/**
	 * Instancia objetos que serão únicos durante todo o tempo que a classe estiver sendo usada.
	 */
	private IgRelatorio() {

		artefatosDeRelatorio = new GeradorDeArtefatosDeRelatorio();
		arquivoPDF = new ArquivoPDF();
		app = AvaliacaoInstitucional.getInstance();
	}

	/**Guarda os parâmtros passados, que podem ser variáveis a cada chamada de uma instância da janela.*/
	private void guardarParametros(TipoRelatorio tipoPesquisa, String tipoGraduacao) {
		IgRelatorio.tipoRelatorio = tipoPesquisa;
		this.tipoGraduacao = tipoGraduacao;


	}


	/**Instancia a classe e construir a interface se ainda não os fez. Do contrário atualiza os dados dos relatórios com os
	 * parâmetros recebidos da forma apropriada. Retorna uma autoreferência.
	 * 
	 * @param tipoRelatorio tipo do relatório passado pelo método getInstance.
	 * @param tipoGraduacao idem. <i>Porém, aceita <code>null</code></i>, se a o relatório não utiliar deste filtro.
	 * @return uma atoreferência, sempre única.
	 * @throws SQLException
	 */
	private static IgRelatorio instanciar(TipoRelatorio tipoRelatorio, String tipoGraduacao) throws SQLException {

		if(igRelatorio == null) {
			igRelatorio = new IgRelatorio();
			igRelatorio.guardarParametros(tipoRelatorio, tipoGraduacao);
			igRelatorio.construirIg();
		}
		else {
			igRelatorio.guardarParametros(tipoRelatorio, tipoGraduacao);
			igRelatorio.atualizarTelaRelatorio();
		}
		return igRelatorio;
	}

	/**Prepara a janela de relatórios para ser exibida. A janela será construída obtendo dados do banco de dados a 
	 * partir da classe principal {@link AvaliacaoInstitucional}. Os dados passados como parâmetro serão usados para 
	 * definir a forma como os relatórios serão gerados e futuramente exibidos.
	 * <b>Nota:</b> Para exibir os dados, consulte o método <code>exibir</code>.
	 * @param tipoRelatorio define se qual o tipo de relatório será gerado
	 * @return uma instânica única desta classe.
	 * @throws SQLException se ocorrer um erro ao gerar o relatório.
	 * 
	 * @see IgRelatorio#exibir()
	 */
	public static IgRelatorio getInstance(TipoRelatorio tipoRelatorio) throws SQLException {
		return instanciar(tipoRelatorio, null);
	}

	/**Prepara a janela de relatórios para ser exibida. A janela será construída obtendo dados do banco de dados a 
	 * partir da classe principal {@link AvaliacaoInstitucional}. Os dados passados como parâmetro serão usados para 
	 * definir a forma como os relatórios serão gerados e futuramente exibidos.
	 * <b>Nota:</b> Para exibir os dados, consulte o método <code>exibir</code>.
	 * @param tipoRelatorio define se qual o tipo de relatório será gerado
	 * @param tipoGraduacao descreve a modalidade dos cursos que serão gerados no relatório. Para que o relatório gerado não
	 * filtre os cursos pelo tipo de graduação, utilize a versão sobrecaregada deste método.
	 * @return uma instânica única desta classe.
	 * @throws SQLException se ocorrer um erro ao gerar o relatório.
	 * 
	 * @see IgRelatorio#exibir()
	 * @see IgRelatorio#getInstance()
	 */
	public static IgRelatorio getInstance(TipoRelatorio tipoRelatorio, String tipoGraduacao) throws SQLException {
		return instanciar(tipoRelatorio, tipoGraduacao);

	}


	/**Constrói a interface gráfica. <b>Não a exibe</b>. Este método deve ser chamado apenas uma vez em toda a aplicação.
	 * Ele desenha todos os componentes e define seus comportamentos.
	 * @throws SQLException se ocorrer um erro quando pela primeira vez estiver estiver gerando os dados do usuário.
	 */
	private void construirIg() throws SQLException {
		setResizable(false);
		setTitle("Relat\u00F3rio de Autoavalia\u00E7\u00E3o Institucional");
		getContentPane().setLayout(null);
		setSize(850, 630);
		panelDados = new JPanel();
		panelDados.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Dados", TitledBorder.LEADING, TitledBorder.TOP, null, Color.DARK_GRAY));
		panelDados.setBounds(10, 11, 824, 504);

		panelDados.setLayout(new BorderLayout(0, 0));

		panelModoExibicao = new JPanel();
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
				gerarESalvarPdf();
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

		//Só será modal se for JDialog
		setModal(true);

		
		LookAndFeel.definirBotaoPrincipal(this, btnGerarPdf);
		//Só define o ícone se for JDialog
		//LookAndFeel.definirIcone(this);
		gerarDadosRelatorio();
		
		exibirTabela();
	}


	/**
	 * Define a operação que cada um dos {@link JRadioButton} (<code>Tabela</code> e <code>Gráfico</code>) realiza quando acionado.
	 */
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

		dadosRelatorio = app.gerarRelatorio(tipoRelatorio, tipoGraduacao);
		IgAvaliacaoInstitucional.mudarCursor(Cursor.DEFAULT_CURSOR);

	}

	private void atualizarTelaRelatorio() throws SQLException {
		igRelatorio.gerarDadosRelatorio();

		if(radioBtnGrafico.isSelected())
			exibirGrafico();
		else
			exibirTabela();
	}

	/**Constrói um gráfico de acordo com o tipo de relatório informado na exibição da janela.
	 */
	private void construirGrafico() {
		graficoPanel = null; 
		if(dadosRelatorio instanceof RelatorioDeParticipantes)
			graficoPanel = artefatosDeRelatorio.gerarGrafico(
					(RelatorioDeParticipantes) dadosRelatorio, 
					dadosRelatorio.title(),
					tipoRelatorio.getOrientacaoGrafico());

		else if(dadosRelatorio instanceof RelatorioDeMedias) {
			graficoPanel = artefatosDeRelatorio.gerarGrafico((RelatorioDeMedias)dadosRelatorio, dadosRelatorio.title());
		}

		panelGrafico.add(graficoPanel, BorderLayout.CENTER);


	}

	/**Constroi e exibe um gráfico a partir dos dados de um relatório*/
	private void exibirGrafico() {
		limparPaineis();

		construirGrafico();

		panelDados.add(panelGrafico);

		repintarPaineis();
	}



	/**
	 * Constrói uma tabela de acordo com o tipo de relatório informado na exibição da janela e adiciona nos painéis.
	 */
	private void construirTabela() {
		tabela = artefatosDeRelatorio.gerarTabela(dadosRelatorio);


		panelTabela.add(tabela, BorderLayout.CENTER);
		panelTabela.add(tabela.getTableHeader(), BorderLayout.NORTH);
		panelTabela.add(tabela);

		panelDados.add(new JScrollPane(panelTabela));

	}

	/**
	 * Exibe de fato uma tabela, antes preparando a tela para tal.
	 */
	private void exibirTabela() {
		
		limparPaineis();

		construirTabela();
		panelModoExibicao.requestFocus();
		
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


	/**
	 * Exibe uma caixa de diálogo solicitando o usuário que informe o local onde o pdf gerado será salvo.
	 * Adiciona a terminação .pdf, caso o usuário não tenha inserido
	 * 
	 * @return uma string com o caminho + o nome do arquivo
	 * 
	 * @throws NullPointerException se o usuário cancelar na escolha do local
	 */
	public String dialogoSalvarArquivoPdf() throws NullPointerException{
		String caminhoArquivo = EntradaESaida.dialogoGravarArquivo(this, TITULO_SALVAR_PDF, DESCRICOES_EXTENSOES_PDF, EXTENSOES_PDF);

		if (caminhoArquivo == null) throw new NullPointerException("Operação cancelada.");

		if(!caminhoArquivo.endsWith(PONTO + EXTENSOES_PDF[0]))
			caminhoArquivo += PONTO + EXTENSOES_PDF[0];

		return caminhoArquivo;
	}

	/**Exibe o diálogo para salvar PDF, gera e salva.*/
	private void gerarESalvarPdf() {

		try {
			//Mostrando ao usuário a janela para salvar o arquivo PDF
			String caminhoArquivo = dialogoSalvarArquivoPdf();
			String titulo = tipoRelatorio.getDescricao() + "s de " + tipoGraduacao;
			artefatosDeRelatorio.gerarPDF(caminhoArquivo, titulo,  dadosRelatorio instanceof RelatorioDeMedias);

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	/**Exibe a janela na tela em relação a janela pai.
	 * 
	 * @param janelPai Component AWT que invocou este. 
	 */
	public void exibir(Component janelaPai) {
		construirGrafico();
		construirTabela();

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

	/**Classe interna responsável por gerar artefatos de relatório, como gráfico de linha, de barra, tabela e exportá-los como PDF.
	 * 
	 * @author RamonGiovane
	 *
	 */
	private class GeradorDeArtefatosDeRelatorio {

		/**Gera um gráfico de barra 3D para representar um {@link RelatorioDeParticipantes} da avaliação.
		 * 
		 * @param dadosRelatorio dados do relatório encapsulados em um {@link Relatorio}.
		 * @param titulo a ser exibido no topo do gráfico
		 * @param orientacao orientação do gráfico. Utilizar os valores estáticos da classe {@link PlotOrientation}: <code>VERTICAL</code> ou
		 *  <code>HORIZONTAL</code>
		 * @return um {@link ChartPanel}, pronto para ser exibido da forma como quiser.
		 */
		public ChartPanel gerarGrafico(RelatorioDeParticipantes dadosRelatorio, 
				String titulo, PlotOrientation orientacao) {

			DefaultCategoryDataset dataset = gerarDataSet(dadosRelatorio);

			//Gerando o gráfico
			JFreeChart chart = ChartFactory.createBarChart3D(titulo, null, null, 
					dataset, orientacao, true, false, false);

			//Customizando o grafico
			customizarGraficoBarra(chart, dataset, orientacao);

			ChartPanel panel = new ChartPanel(chart);

			return panel;
		}



		public JTable gerarTabela(Relatorio dadosRelatorio) {
			return EntradaESaida.gerarTabela(dadosRelatorio.asMatrix(), dadosRelatorio.getHeaders());

		}



		/**Gera um gráfico de linha para representar um {@link RelatorioDeMedias} da avaliação.
		 * 
		 * @param dadosRelatorio dados do relatório encapsulados em um {@link Relatorio}.
		 * @param titulo a ser exibido no topo do gráfico
		 * @return um {@link ChartPanel}, pronto para ser exibido da forma como quiser.
		 */
		public ChartPanel gerarGrafico(RelatorioDeMedias dadosRelatorio, String titulo) {
			DefaultCategoryDataset dataset = gerarDataSet(dadosRelatorio);

			//Gerando gráfico
			JFreeChart chart = ChartFactory.createLineChart(titulo, null, null, 
					dataset, PlotOrientation.VERTICAL, true, true, true);


			//Formatando, colorindo e customizando o grafico
			customizarGraficoLinha(chart, dataset);



			return new ChartPanel(chart);

		}

		/**Formata, colore, customiza o gráfico de linha*/
		private void customizarGraficoLinha(JFreeChart chart, DefaultCategoryDataset dataset) {
			CategoryPlot categoryP = chart.getCategoryPlot();
			CategoryItemRenderer renderer = categoryP.getRenderer();

			//Deixando as linhas mais grossas
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

		}

		/**Formata, colore, customize o gráfico de barra 3D*/
		private void customizarGraficoBarra(JFreeChart chart, DefaultCategoryDataset dataset, PlotOrientation orientation) {
			CategoryPlot plot = chart.getCategoryPlot();
			CategoryItemRenderer renderer = plot.getRenderer();



			BarRenderer3D barRenderer = (BarRenderer3D) plot.getRenderer();

			plot.getDomainAxis().setMaximumCategoryLabelWidthRatio(10);

			//Deixando a barra mais larga. Não muito, pois dá errado se houver poucas séries no gráfico
			barRenderer.setItemMargin(-1.07);
			CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();


			for(int i = 0; i<dataset.getRowCount(); i++) {
				barRenderer.setSeriesItemLabelGenerator(i, generator);
				barRenderer.setSeriesItemLabelsVisible(i, true);
				if(orientation == PlotOrientation.HORIZONTAL)
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

		}

		/**Gera um PDF, contendo uma tablela <code>JTable</code> e um gráfico de barra ou de linha (<code>JFreeChart</code>).
		 * 
		 * @param nomeArquivo caminho do arquivo de onde será gravado o PDF.
		 * @param titulo título do arquivo.
		 * @param usarCoresCustomizadas indica se as cores do relatório serão padrões (<code>false</code>) ou customizadas(<code>true</code>).
		 * As customizadas são adequadas para os relatórios de médias.

		 * @throws DocumentException 
		 * @throws IOException
		 */

		public void gerarPDF(String nomeArquivo, String titulo, boolean usarCoresCustomizadas) throws DocumentException, IOException {

			arquivoPDF.open(nomeArquivo);

			arquivoPDF.adicionarTitulo(titulo);

			//Separa o titulo do proximo conteudo
			arquivoPDF.adicionarNovaLinha();

			//Adiciona o JTable
			arquivoPDF.adicionarJTable(tabela, usarCoresCustomizadas);

			arquivoPDF.adicionarNovaLinha();

			//Adiciona o gráfico
			arquivoPDF.adicionarGrafico(graficoPanel.getChart(), panelDados.getWidth(), panelDados.getHeight());


			arquivoPDF.close();


		}


		/**Insere o conteúdo encapsulado em um {@link RelatorioDeParticipantes} dentro de um dataset compatível com o {@link JFreeChart}
		 * para ser usado como gráfico de barra.
		 * 
		 * @param dadosRelatorio dados a serem plotados no gráfico.
		 * @return um dataset ({@link DefaultCategoryDataset}), contendo os dados que popularam o gráfico.
		 */
		public DefaultCategoryDataset gerarDataSet(RelatorioDeParticipantes dadosRelatorio) {
			DefaultCategoryDataset dataset = new DefaultCategoryDataset( );

			for(int i  = 0; i<dadosRelatorio.size(); i++) {
				dataset.addValue(dadosRelatorio.obter(i).valor(), dadosRelatorio.obter(i).descricao(), 
						dadosRelatorio.obter(i).descricao());
			}

			return dataset;
		}

		/**Insere o conteúdo encapsulado em um {@link RelatorioDeMedias} dentro de um dataset compatível com o {@link JFreeChart}
		 * para ser usado como gráfico de linha.
		 * 
		 * @param dadosRelatorio dados a serem plotados no gráfico.
		 * @return um dataset ({@link DefaultCategoryDataset}), contendo os dados que popularam o gráfico.
		 */
		public DefaultCategoryDataset gerarDataSet(RelatorioDeMedias dadosRelatorio) {
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
			return dataset;
		}

	}
}

