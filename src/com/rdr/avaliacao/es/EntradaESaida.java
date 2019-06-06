package com.rdr.avaliacao.es;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;

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

import com.rdr.avaliacao.relatorio.DadosDeGrafico;
import com.rdr.avaliacao.relatorio.DataSet;


public class EntradaESaida {

	/**Reproduz um som de alerta padrão do sistema operacional*/
	private static void reproduzirSom() {
		Toolkit.getDefaultToolkit().beep();
	}

	/**
	 * Exibe uma mensagem informativa em uma caixa de diálogo com o texto da barra de título definido em 
	 * titulo.
	 * 
	 * @param janelaPai objeto <code>java.awt.Component</code> que identifica a janela pai 
	 * sobre a qual a caixa de diálogo será exibida.
	 * @param titulo da janela a ser exibida.
	 * @param mensagem a ser exibida.
	 * 
	 * @author Prof. Márlon Oliveira da Silva. Modificado por: Ramon Giovane
	 */
	public static void msgInfo(Component janelaPai, String mensagem, String titulo) {
		reproduzirSom();
		showMessageDialog(janelaPai, mensagem, titulo, INFORMATION_MESSAGE);
	}

	/**
	 * Exibe um componente em uma caixa de diálogo com o texto da barra de título definido em titulo.
	 * 
	 * @param janelaPai objeto <code>java.awt.Component</code> que identifica a janela pai 
	 * sobre a qual a caixa de diálogo será exibida.
	 * @param titulo da janela a ser exibida.
	 * @param mensagem a ser exibida.
	 * 
	 * @author Prof. Márlon Oliveira da Silva. Modificado por: Ramon Giovane
	 */
	public static void msgInfo(Component janelaPai, Object componente, String titulo) {
		reproduzirSom();
		showMessageDialog(janelaPai, componente, titulo, INFORMATION_MESSAGE);
	}

	/**
	 * Exibe uma mensagem de erro em uma caixa de diálogo com o texto da barra de título definido em titulo.
	 *
	 * @param janelaPai objeto <code>java.awt.Component</code> que identifica a janela pai 
	 * sobre a qual a caixa de diálogo será exibida.
	 * @param titulo da janela a ser exibida.
	 * @param mensagem a ser exibida.
	 * 
	 * @author Prof. Márlon Oliveira da Silva. Modificado por: Ramon Giovane
	 */
	public static void msgErro(Component janelaPai, String mensagem, String titulo) {
		reproduzirSom();
		showMessageDialog(janelaPai, mensagem, titulo, ERROR_MESSAGE);
	}

	/**
	 * Lê uma string em uma caixa de diálogo com o texto da barra de título definido em titulo.
	 * Retorna a string lida ou null se o usuário cancelar a operação (clicando no botão Cancelar ou Fechar).
	 * 
	 * @param janelaPai objeto <code>java.awt.Component</code> que identifica a janela pai 
	 * sobre a qual a caixa de diálogo será exibida.
	 * @param titulo da janela a ser exibida.
	 * @param mensagem a ser exibida.
	 * 
	 * @author Prof. Márlon Oliveira da Silva. Modificado por: Ramon Giovane
	 */
	public static String lerString(Component janelaPai, String mensagem, String titulo) {
		return showInputDialog(janelaPai, mensagem, titulo, QUESTION_MESSAGE);
	}

	/**
	 * Lê um número inteiro em uma caixa de diálogo com o texto da barra de título definido em titulo.
	 * Retorna o numero lido ou null se o usuário cancelar a operação (clicando no botão Cancelar ou Fechar).
	 * 
	 * @param janelaPai objeto <code>java.awt.Component</code> que identifica a janela pai 
	 * sobre a qual a caixa de diálogo será exibida.
	 * @param titulo da janela a ser exibida.
	 * @param mensagem a ser exibida.
	 * 
	 * @throws NumberFormatException se ocorrer um erro de conversão do texto para número.
	 * 
	 * @author Prof. Márlon Oliveira da Silva. Modificado por: Ramon Giovane
	 */
	public static Integer lerNumeroInteiro(Component janelaPai, String mensagem, String titulo) throws NumberFormatException {
		return Integer.parseInt(lerString(janelaPai, mensagem, titulo)); 
	}

	/**
	 * Lê um número real em uma caixa de diálogo com o texto da barra de título definido em titulo.
	 * Retorna o numero lido ou null se o usuário cancelar a operação (clicando no botão Cancelar ou Fechar).
	 * 
	 * @param janelaPai objeto <code>java.awt.Component</code> que identifica a janela pai 
	 * sobre a qual a caixa de diálogo será exibida.
	 * @param titulo da janela a ser exibida.
	 * @param mensagem a ser exibida.
	 * 
	 * @throws NumberFormatException se ocorrer um erro de conversão do texto para número.
	 * 
	 * @author Prof. Márlon Oliveira da Silva. Modificado por: Ramon Giovane
	 */
	public static Float lerNumeroReal(Component janelaPai, String mensagem, String titulo) throws NumberFormatException {
		return Float.parseFloat(lerString(janelaPai, mensagem, titulo)); 
	}

	/**
	 * Exibe uma pergunta em uma caixa de diálogo para o usuário responder Sim ou Não.
	 * 
	 * @param janelaPai objeto <code>java.awt.Component</code> que identifica a janela pai 
	 * sobre a qual a caixa de diálogo será exibida.
	 * @param prompt pergunta a ser exibido na caixa de diálogo;
	 * @param titulo texto a ser exibido na barra de título da caixa de diálogo.
	 * 
	 * @return os valores <code>YES_OPTION</code>, <code>NO_OPTION</code> ou <code>CLOSED_OPTION</code>, respectivamente, se o usuário responder
	 * Sim, Não ou fechar a caixa de diálogo clicando no botão Fechar.
	 * 
	 * @see javax.swing.JOptionPane
	 * 
	 * @author Prof. Márlon Oliveira da Silva. Modificado por: Ramon Giovane
	 */
	public static int msgConfirma(Component janelaPai, String prompt, String titulo) {
		return showConfirmDialog(janelaPai, prompt, titulo, YES_NO_OPTION);
	}

	/**
	 * Exibe uma tabela em uma caixa de diálogo com o conteúdo dos arrays arrays <code>linhas</code> e
	 * <code>colunas</code>.
	 * 
	 * @param titulo texto a ser exibido na barra de título da caixa de diálogo;
	 * @param linhas conteúdo a ser exibido nas linhas da tabela;
	 * @param colunas nomes das colunas da tabela;
	 * @param larguraColuna define o valor da largura de cada coluna da tabela. Se <code>larguraColuna</code> for <code>null</code> a largura das colunas 
	 *                 será calculada dividindo o valor <code>larguraTabela</code> pelo número de colunas da matriz <code>linhas</code>;
	 * @param alinhamentoColuna define o tipo de alinhamento de cada coluna da tabela. Se <code>alinhamentoColuna</code> for <code>null</code> o 
	 *                  alinhamento à esquerda (<code>SwingConstants.LEFT</code>) será utilizado em todas as colunas da tabela. Os valores válidos para alinhamento, 
	 *                  definidos na interface <code>SwingConstants</code>, são: <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>, 
	 *                  <code>LEADING</code> ou <code>TRAILING</code>;
	 * @param larguraTabela valor, em <i>pixels</i>, correspondente a largura da área de exibição da tabela;
	 * @param alturaTabela valor, em <i>pixels</i>, correspondente a altura da área de exibição da tabela.
	 * 
	 * @see javax.swing.SwingConstants
	 * 
	 * @author Prof. Márlon Oliveira da Silva
	 */
	public static JTable gerarTabela(Object[][] linhas, String[] colunas, int[] larguraColuna, 
			int[] alinhamentoColuna, int larguraTabela, int alturaTabela) {

		// Cria o componente GUI Swing JTable para exibir a tabela.
		JTable table = new JTable(linhas, colunas);

		// Define o tamanho (largura e altura, em pixels, respectivamente) da área de visualização (viewport) da tabela.
		table.setPreferredScrollableViewportSize(new Dimension(larguraTabela, alturaTabela));

		// Define a largura das colunas da tabela.
		if (larguraColuna != null)
			for (int coluna = 0; coluna < larguraColuna.length; coluna++) // A largura das colunas é definido de acordo com os valores do array larguraColuna.
				table.getColumnModel().getColumn(coluna).setPreferredWidth(larguraColuna[coluna]);
		else
			for (int coluna = 0; coluna < colunas.length; coluna++) // A largura das colunas é definido de acordo com a largura da área de exibição da tabela.
				table.getColumnModel().getColumn(coluna).setPreferredWidth(larguraTabela / colunas.length);

		// Define o alinhamento para cada coluna da tabela.
		if (alinhamentoColuna != null) {
			// Cria um objeto para "renderizar" (desenhar) todas as células de uma coluna da tabela.
			DefaultTableCellRenderer[] colunaTableCellRenderer = new DefaultTableCellRenderer[colunas.length];

			// Alinhamento das colunas definido de acordo com os valores do array alinhamentoColuna.
			for (int coluna = 0; coluna < alinhamentoColuna.length; coluna++) {
				colunaTableCellRenderer[coluna] = new DefaultTableCellRenderer();
				colunaTableCellRenderer[coluna].setHorizontalAlignment(alinhamentoColuna[coluna]);
				table.getColumnModel().getColumn(coluna).setCellRenderer(colunaTableCellRenderer[coluna]);
			}
		}
		else 
		{ // Alinhamento das colunas definido por SwingConstants.LEFT.
			DefaultTableCellRenderer colunaTableCellRenderer = new DefaultTableCellRenderer();
			colunaTableCellRenderer.setHorizontalAlignment(SwingConstants.LEFT);

			for (int coluna = 0; coluna < colunas.length; coluna++)
				table.getColumnModel().getColumn(coluna).setCellRenderer(colunaTableCellRenderer);
		}

		return table;
	} // exibirTabela()


	public static void exibirTabela(Component janelaPai, String titulo, Object[][] linhas, String[] colunas, int[] larguraColuna, 
			int[] alinhamentoColuna, int larguraTabela, int alturaTabela) {
		// Exibe a tabela em uma caixa de diálogo usando um painel rolável (JScrollPane).
		msgInfo(janelaPai,  
				new JScrollPane(gerarTabela(linhas, colunas, larguraColuna, alinhamentoColuna, larguraTabela, alturaTabela)), 
				titulo);
	}





	/**
	 * Exibe um texto em uma caixa de diálogo com o texto da barra de título definido em titulo.
	 * 
	 * @param janelaPai objeto <code>java.awt.Component</code> que identifica a janela pai 
	 * sobre a qual a janela <code>JFileChooser</code> será exibida.
	 * @param texto a ser exibido.
	 * 
	 * @author Prof. Márlon Oliveira da Silva
	 * @param titulo <code>String</code> com o nome da barra de título da caixa de diálogo.
	 */
	public static void exibirTexto(Component janelaPai, String texto, String titulo) {
		/* Cria uma área de texto para armazenar o texto a ser exibido. Define, respectivamente, o título, o 
		 * número de linhas e colunas. */
		JTextArea textArea = new JTextArea(titulo, 10, 20);

		// Define a área de texto como não editável.
		textArea.setEditable(false);

		// Define a quebra automática das linha de texto.
		textArea.setLineWrap(true);

		// Define que a quebra automática das linha de texto ocorra entre palavras.
		textArea.setWrapStyleWord(true);

		// Escreve o texto na área de texto.
		textArea.setText(texto);

		// Exibe a área de texto em uma caixa de diálogo usando um painel rolável (JScrollPane).
		msgInfo(janelaPai, new JScrollPane(textArea), titulo);
	}

	/** 
	 * Exibe uma caixa de diálogo <code>javax.swing.JFileChooser</code> para o usuário indicar o 
	 * nome do diretório e arquivo que será aberto. 
	 * 
	 * @param janelaPai objeto <code>java.awt.Component</code> que identifica a janela pai 
	 * sobre a qual a janela <code>JFileChooser</code> será exibida.
	 *  
	 * @param titulo <code>String</code> com o nome da barra de título da caixa de diálogo.
	 *        
	 * @return <code>String</code> com o nome do arquivo a ser aberto. Se o usuário cancelar a 
	 * operação (clicar no botão "Cancelar") será retornado <code>null</code>.
	 *         
	 * @see java.awt.Component
	 * @see javax.swing.JFileChooser
	 * 
	 * @author Prof. Márlon Oliveira da Silva
	 */
	public static String dialogoAbrirArquivo(Component janelaPai, String titulo) {
		return dialogoAbrirArquivo(janelaPai, titulo, null, null);
	}

	/** 
	 * Exibe uma caixa de diálogo <code>javax.swing.JFileChooser</code> para o usuário indicar o
	 * nome do diretório onde será gravado o arquivo e o nome do arquivo.
	 * 
	 * @param janelaPai objeto <code>java.awt.Component</code> que identifica a janela pai 
	 * sobre a qual a janela <code>JFileChooser</code> será exibida.
	 *   
	 * @param titulo <code>String</code> com o nome da barra de título da caixa de diálogo.
	 *        
	 * @return <code>String</code> com o nome do arquivo a ser gravado.  Se o usuário cancelar 
	 * a operação (clicar no botão "Cancelar") será retornado <code>null</code>.
	 *         
	 * @see java.awt.Component
	 * @see javax.swing.JFileChooser
	 * 
	 * @author Prof. Márlon Oliveira da Silva
	 */
	public static String dialogoGravarArquivo(Component janelaPai, String titulo) {
		JFileChooser dialogoGravar = new JFileChooser();

		// Indica que o usuário poderá selecionar apenas nomes de arquivos. 
		dialogoGravar.setFileSelectionMode(JFileChooser.FILES_ONLY);

		// Define o título do diálogo. 
		dialogoGravar.setDialogTitle(titulo);

		// Define qual é o diretório default de gravação.
		dialogoGravar.setCurrentDirectory(new File("." + File.separator + "arquivos"));

		// Exibe o diálogo.
		int opcao = dialogoGravar.showSaveDialog(janelaPai);

		// Verifica se o usuário cancelou a operação (clicou no botão Cancelar); se não, obtém o nome do arquivo digitado ou selecionado pelo usuário no diálogo.
		return (opcao == JFileChooser.CANCEL_OPTION) ? null : dialogoGravar.getSelectedFile().getPath();
	} 

	/** 
	 * Exibe uma caixa de diálogo <code>javax.swing.JFileChooser</code> para o usuário indicar o 
	 * nome do diretório e arquivo que será aberto. 
	 * 
	 * @param janelaPai objeto <code>java.awt.Component</code> que identifica a janela pai 
	 * sobre a qual a janela <code>JFileChooser</code> será exibida.
	 *  
	 * @param titulo <code>String</code> com o nome da barra de título da caixa de diálogo.
	 * 
	 * @param descricoesExtensoes array de objetos <code>String</code> contendo as descrições das 
	 * extensões que podem ser filtradas pela caixa de diálogo. Por exemplo: "Arquivo de Texto".
	 * 
	 * @param extensoes array de objetos <code>String</code> contendo as extensões que podem ser
	 * filtradas pela caixa de diálogo. Por exemplo: "txt", "png", "doc".
	 *        
	 * @return <code>String</code> com o nome do arquivo a ser aberto. Se o usuário cancelar a 
	 * operação (clicar no botão "Cancelar") será retornado <code>null</code>.
	 *         
	 * @see java.awt.Component
	 * @see javax.swing.JFileChooser
	 * 
	 * @author Prof. Márlon Oliveira da Silva. Modificado por: Ramon Giovane
	 */
	public static String dialogoAbrirArquivo(Component janelaPai, String titulo, String[] descricoesExtensoes,
			String[] extensoes) {
		JFileChooser dialogoAbrir = new JFileChooser();

		if(extensoes != null && descricoesExtensoes != null)
			definirExtensoesJFileChooser(dialogoAbrir, descricoesExtensoes, extensoes);

		// Define qual é o diretório default para abrir os arquivos.
		dialogoAbrir.setCurrentDirectory(new File("." + File.separator + "arquivos"));

		// Indica que o usuário poderá selecionar apenas nomes de arquivos. 
		dialogoAbrir.setFileSelectionMode(JFileChooser.FILES_ONLY);

		// Define o título do diálogo. 
		dialogoAbrir.setDialogTitle(titulo);

		// Exibe o diálogo.
		int opcao = dialogoAbrir.showOpenDialog(janelaPai);

		// Verifica se o usuário cancelou a operação (clicou no botão "Cancelar); se não, obtém o nome do arquivo digitado ou selecionado pelo usuário no diálogo.
		return (opcao == JFileChooser.CANCEL_OPTION) ? null : dialogoAbrir.getSelectedFile().getPath();
	} 

	private static void definirExtensoesJFileChooser(JFileChooser fileChooser, String[] descricoesExtensoes, String[]... extensoes) {
		FileNameExtensionFilter filter;
		for(int i =0; i<extensoes.length; i++) {
			filter = new FileNameExtensionFilter(( i >= descricoesExtensoes.length  ? "" : 
				descricoesExtensoes[i]), extensoes[i]);
			fileChooser.addChoosableFileFilter(filter);

			fileChooser.setFileFilter(filter);
		}
	}

	public static<T extends DadosDeGrafico> ChartPanel gerarGraficoBarra3D(DataSet dadosRelatorio, 
			String titulo, String legendaCategoria, String legendaValores,
			PlotOrientation orientacao, int largura, int altura) {

		final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );

		for(int i  = 0; i<dadosRelatorio.tamanho(); i++) {
			dataset.addValue(dadosRelatorio.ober(i).getValorLinha(), dadosRelatorio.ober(i).getValorColuna(), dadosRelatorio.ober(i).getValorColuna());
		}

		JFreeChart chart = ChartFactory.createBarChart3D(titulo, null, null, 
				dataset, orientacao, true, false, false);

		CategoryPlot plot = chart.getCategoryPlot();
		CategoryItemRenderer renderer = plot.getRenderer();



		BarRenderer3D barRenderer = (BarRenderer3D) plot.getRenderer();

		//Adicionando os números na frente do gráfico de barra
		CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();
		for(int i = 0; i<dataset.getRowCount(); i++) {
			barRenderer.setSeriesItemLabelGenerator(i, generator);
			barRenderer.setSeriesItemLabelsVisible(i, true);
			barRenderer.setSeriesPositiveItemLabelPosition(i, new ItemLabelPosition(ItemLabelAnchor.OUTSIDE2,TextAnchor.CENTER_LEFT));
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

		plot.getDomainAxis().setMaximumCategoryLabelWidthRatio(10);
		BarRenderer3D br = (BarRenderer3D) plot.getRenderer();
		br.setItemMargin(-5);

		ChartPanel panel = new ChartPanel(chart);

		return panel;
	}



} // class EntradaESaida