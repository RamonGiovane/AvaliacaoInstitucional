package com.rdr.avaliacao.es;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;


public class EntradaESaida {
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
	public static Float lerNumeroReal(Component janelaPai, String mensagem, String titulo) {
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
	public static void exibirTabela(Component janelaPai, String titulo, Object[][] linhas, String[] colunas, int[] larguraColuna, 
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
		 	
		 	// Exibe a tabela em uma caixa de diálogo usando um painel rolável (JScrollPane).
		 	msgInfo(janelaPai, new JScrollPane(table), titulo);
	} // exibirTabela()
	
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
	 * @return <code>String</code> com o nome do arquivo a ser aberto. Se o usuário cancelar a 
	 * operação (clicar no botão "Cancelar") será retornado <code>null</code>.
	 *         
	 * @see java.awt.Component
	 * @see javax.swing.JFileChooser
	 * 
	 * @author Prof. Márlon Oliveira da Silva
	 */
	public static String dialogoAbrirArquivo(Component janelaPai, String titulo) {
	  JFileChooser dialogoAbrir = new JFileChooser();
	  
	   // Define as extensões que serão usadas pelo JFileChooser para filtrar os tipos de arquivos que serão exibidos na janela.
	  FileNameExtensionFilter extensaoDatTxt = new FileNameExtensionFilter("Arquivos DAT e TXT", "dat", "txt");
	  dialogoAbrir.setFileFilter(extensaoDatTxt);
	  
	  // Define novas extensões de arquivo e adiciona ao filtro de JFileChooser os tipos de arquivos que serão exibidos no diálogo.
	  FileNameExtensionFilter extensaoSer = new FileNameExtensionFilter("Arquivo de Serialização (ser)", "ser");
	  dialogoAbrir.addChoosableFileFilter(extensaoSer);
	     
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
} // class EntradaESaida