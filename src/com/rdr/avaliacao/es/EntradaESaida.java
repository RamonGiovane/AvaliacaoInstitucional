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
	 * Exibe uma mensagem informativa em uma caixa de di�logo com o texto da barra de t�tulo definido em 
	 * titulo.
	 * 
	 * @param janelaPai objeto <code>java.awt.Component</code> que identifica a janela pai 
	 * sobre a qual a caixa de di�logo ser� exibida.
	 * @param titulo da janela a ser exibida.
	 * @param mensagem a ser exibida.
	 * 
	 * @author Prof. M�rlon Oliveira da Silva. Modificado por: Ramon Giovane
	 */
	public static void msgInfo(Component janelaPai, String mensagem, String titulo) {
		showMessageDialog(janelaPai, mensagem, titulo, INFORMATION_MESSAGE);
	}
	
	/**
	 * Exibe um componente em uma caixa de di�logo com o texto da barra de t�tulo definido em titulo.
	 * 
	 * @param janelaPai objeto <code>java.awt.Component</code> que identifica a janela pai 
	 * sobre a qual a caixa de di�logo ser� exibida.
	 * @param titulo da janela a ser exibida.
	 * @param mensagem a ser exibida.
	 * 
	 * @author Prof. M�rlon Oliveira da Silva. Modificado por: Ramon Giovane
	 */
	public static void msgInfo(Component janelaPai, Object componente, String titulo) {
		showMessageDialog(janelaPai, componente, titulo, INFORMATION_MESSAGE);
	}
	
	/**
	 * Exibe uma mensagem de erro em uma caixa de di�logo com o texto da barra de t�tulo definido em titulo.
	 *
	 * @param janelaPai objeto <code>java.awt.Component</code> que identifica a janela pai 
	 * sobre a qual a caixa de di�logo ser� exibida.
	 * @param titulo da janela a ser exibida.
	 * @param mensagem a ser exibida.
	 * 
	 * @author Prof. M�rlon Oliveira da Silva. Modificado por: Ramon Giovane
	 */
	public static void msgErro(Component janelaPai, String mensagem, String titulo) {
		showMessageDialog(janelaPai, mensagem, titulo, ERROR_MESSAGE);
	}
	
	/**
	 * L� uma string em uma caixa de di�logo com o texto da barra de t�tulo definido em titulo.
	 * Retorna a string lida ou null se o usu�rio cancelar a opera��o (clicando no bot�o Cancelar ou Fechar).
	 * 
	 * @param janelaPai objeto <code>java.awt.Component</code> que identifica a janela pai 
	 * sobre a qual a caixa de di�logo ser� exibida.
	 * @param titulo da janela a ser exibida.
	 * @param mensagem a ser exibida.
	 * 
	 * @author Prof. M�rlon Oliveira da Silva. Modificado por: Ramon Giovane
	 */
	public static String lerString(Component janelaPai, String mensagem, String titulo) {
		return showInputDialog(janelaPai, mensagem, titulo, QUESTION_MESSAGE);
	}
	
	/**
	 * L� um n�mero inteiro em uma caixa de di�logo com o texto da barra de t�tulo definido em titulo.
	 * Retorna o numero lido ou null se o usu�rio cancelar a opera��o (clicando no bot�o Cancelar ou Fechar).
	 * 
	 * @param janelaPai objeto <code>java.awt.Component</code> que identifica a janela pai 
	 * sobre a qual a caixa de di�logo ser� exibida.
	 * @param titulo da janela a ser exibida.
	 * @param mensagem a ser exibida.
	 * 
	 * @throws NumberFormatException se ocorrer um erro de convers�o do texto para n�mero.
	 * 
	 * @author Prof. M�rlon Oliveira da Silva. Modificado por: Ramon Giovane
	 */
	public static Integer lerNumeroInteiro(Component janelaPai, String mensagem, String titulo) throws NumberFormatException {
		return Integer.parseInt(lerString(janelaPai, mensagem, titulo)); 
	}
	
	/**
	 * L� um n�mero real em uma caixa de di�logo com o texto da barra de t�tulo definido em titulo.
	 * Retorna o numero lido ou null se o usu�rio cancelar a opera��o (clicando no bot�o Cancelar ou Fechar).
	 * 
	 * @param janelaPai objeto <code>java.awt.Component</code> que identifica a janela pai 
	 * sobre a qual a caixa de di�logo ser� exibida.
	 * @param titulo da janela a ser exibida.
	 * @param mensagem a ser exibida.
	 * 
	 * @throws NumberFormatException se ocorrer um erro de convers�o do texto para n�mero.
	 * 
	 * @author Prof. M�rlon Oliveira da Silva. Modificado por: Ramon Giovane
	 */
	public static Float lerNumeroReal(Component janelaPai, String mensagem, String titulo) {
		return Float.parseFloat(lerString(janelaPai, mensagem, titulo)); 
	}
	
	/**
	 * Exibe uma pergunta em uma caixa de di�logo para o usu�rio responder Sim ou N�o.
	 * 
	 * @param janelaPai objeto <code>java.awt.Component</code> que identifica a janela pai 
	 * sobre a qual a caixa de di�logo ser� exibida.
	 * @param prompt pergunta a ser exibido na caixa de di�logo;
	 * @param titulo texto a ser exibido na barra de t�tulo da caixa de di�logo.
	 * 
	 * @return os valores <code>YES_OPTION</code>, <code>NO_OPTION</code> ou <code>CLOSED_OPTION</code>, respectivamente, se o usu�rio responder
	 * Sim, N�o ou fechar a caixa de di�logo clicando no bot�o Fechar.
	 * 
	 * @see javax.swing.JOptionPane
	 * 
	 * @author Prof. M�rlon Oliveira da Silva. Modificado por: Ramon Giovane
	 */
	public static int msgConfirma(Component janelaPai, String prompt, String titulo) {
		return showConfirmDialog(janelaPai, prompt, titulo, YES_NO_OPTION);
	}
	
	/**
	 * Exibe uma tabela em uma caixa de di�logo com o conte�do dos arrays arrays <code>linhas</code> e
	 * <code>colunas</code>.
	 * 
	 * @param titulo texto a ser exibido na barra de t�tulo da caixa de di�logo;
	 * @param linhas conte�do a ser exibido nas linhas da tabela;
	 * @param colunas nomes das colunas da tabela;
	 * @param larguraColuna define o valor da largura de cada coluna da tabela. Se <code>larguraColuna</code> for <code>null</code> a largura das colunas 
	 *                 ser� calculada dividindo o valor <code>larguraTabela</code> pelo n�mero de colunas da matriz <code>linhas</code>;
	 * @param alinhamentoColuna define o tipo de alinhamento de cada coluna da tabela. Se <code>alinhamentoColuna</code> for <code>null</code> o 
	 *                  alinhamento � esquerda (<code>SwingConstants.LEFT</code>) ser� utilizado em todas as colunas da tabela. Os valores v�lidos para alinhamento, 
	 *                  definidos na interface <code>SwingConstants</code>, s�o: <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>, 
	 *                  <code>LEADING</code> ou <code>TRAILING</code>;
	 * @param larguraTabela valor, em <i>pixels</i>, correspondente a largura da �rea de exibi��o da tabela;
	 * @param alturaTabela valor, em <i>pixels</i>, correspondente a altura da �rea de exibi��o da tabela.
	 * 
	 * @see javax.swing.SwingConstants
	 * 
	 * @author Prof. M�rlon Oliveira da Silva
	 */
	public static void exibirTabela(Component janelaPai, String titulo, Object[][] linhas, String[] colunas, int[] larguraColuna, 
			int[] alinhamentoColuna, int larguraTabela, int alturaTabela) {
		
		 	// Cria o componente GUI Swing JTable para exibir a tabela.
		 	JTable table = new JTable(linhas, colunas);
		 	
		 	// Define o tamanho (largura e altura, em pixels, respectivamente) da �rea de visualiza��o (viewport) da tabela.
		 	table.setPreferredScrollableViewportSize(new Dimension(larguraTabela, alturaTabela));
		 	
		 	// Define a largura das colunas da tabela.
		 	if (larguraColuna != null)
		 		for (int coluna = 0; coluna < larguraColuna.length; coluna++) // A largura das colunas � definido de acordo com os valores do array larguraColuna.
		 			table.getColumnModel().getColumn(coluna).setPreferredWidth(larguraColuna[coluna]);
		 	else
		 		for (int coluna = 0; coluna < colunas.length; coluna++) // A largura das colunas � definido de acordo com a largura da �rea de exibi��o da tabela.
		 			table.getColumnModel().getColumn(coluna).setPreferredWidth(larguraTabela / colunas.length);
		 		
		 	// Define o alinhamento para cada coluna da tabela.
		 	if (alinhamentoColuna != null) {
			 	// Cria um objeto para "renderizar" (desenhar) todas as c�lulas de uma coluna da tabela.
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
		 	
		 	// Exibe a tabela em uma caixa de di�logo usando um painel rol�vel (JScrollPane).
		 	msgInfo(janelaPai, new JScrollPane(table), titulo);
	} // exibirTabela()
	
	/**
	 * Exibe um texto em uma caixa de di�logo com o texto da barra de t�tulo definido em titulo.
	 * 
	 * @param janelaPai objeto <code>java.awt.Component</code> que identifica a janela pai 
	 * sobre a qual a janela <code>JFileChooser</code> ser� exibida.
	 * @param texto a ser exibido.
	 * 
	 * @author Prof. M�rlon Oliveira da Silva
	 * @param titulo <code>String</code> com o nome da barra de t�tulo da caixa de di�logo.
	 */
	public static void exibirTexto(Component janelaPai, String texto, String titulo) {
		/* Cria uma �rea de texto para armazenar o texto a ser exibido. Define, respectivamente, o t�tulo, o 
		 * n�mero de linhas e colunas. */
		JTextArea textArea = new JTextArea(titulo, 10, 20);
		
		// Define a �rea de texto como n�o edit�vel.
		textArea.setEditable(false);
		
		// Define a quebra autom�tica das linha de texto.
		textArea.setLineWrap(true);
		
		// Define que a quebra autom�tica das linha de texto ocorra entre palavras.
		textArea.setWrapStyleWord(true);
		
		// Escreve o texto na �rea de texto.
		textArea.setText(texto);
		
		// Exibe a �rea de texto em uma caixa de di�logo usando um painel rol�vel (JScrollPane).
		msgInfo(janelaPai, new JScrollPane(textArea), titulo);
	}
	
	/** 
	 * Exibe uma caixa de di�logo <code>javax.swing.JFileChooser</code> para o usu�rio indicar o
	 * nome do diret�rio onde ser� gravado o arquivo e o nome do arquivo.
	 * 
	 * @param janelaPai objeto <code>java.awt.Component</code> que identifica a janela pai 
	 * sobre a qual a janela <code>JFileChooser</code> ser� exibida.
	 *   
	 * @param titulo <code>String</code> com o nome da barra de t�tulo da caixa de di�logo.
	 *        
	 * @return <code>String</code> com o nome do arquivo a ser gravado.  Se o usu�rio cancelar 
	 * a opera��o (clicar no bot�o "Cancelar") ser� retornado <code>null</code>.
	 *         
	 * @see java.awt.Component
	 * @see javax.swing.JFileChooser
	 * 
	 * @author Prof. M�rlon Oliveira da Silva
	 */
	public static String dialogoGravarArquivo(Component janelaPai, String titulo) {
	  JFileChooser dialogoGravar = new JFileChooser();
	    
	  // Indica que o usu�rio poder� selecionar apenas nomes de arquivos. 
	  dialogoGravar.setFileSelectionMode(JFileChooser.FILES_ONLY);
	
	  // Define o t�tulo do di�logo. 
	  dialogoGravar.setDialogTitle(titulo);
	
	  // Define qual � o diret�rio default de grava��o.
	  dialogoGravar.setCurrentDirectory(new File("." + File.separator + "arquivos"));
	  
	  // Exibe o di�logo.
	  int opcao = dialogoGravar.showSaveDialog(janelaPai);
	
	  // Verifica se o usu�rio cancelou a opera��o (clicou no bot�o Cancelar); se n�o, obt�m o nome do arquivo digitado ou selecionado pelo usu�rio no di�logo.
	  return (opcao == JFileChooser.CANCEL_OPTION) ? null : dialogoGravar.getSelectedFile().getPath();
	} 

	/** 
	 * Exibe uma caixa de di�logo <code>javax.swing.JFileChooser</code> para o usu�rio indicar o 
	 * nome do diret�rio e arquivo que ser� aberto. 
	 * 
	 * @param janelaPai objeto <code>java.awt.Component</code> que identifica a janela pai 
	 * sobre a qual a janela <code>JFileChooser</code> ser� exibida.
	 *  
	 * @param titulo <code>String</code> com o nome da barra de t�tulo da caixa de di�logo.
	 *        
	 * @return <code>String</code> com o nome do arquivo a ser aberto. Se o usu�rio cancelar a 
	 * opera��o (clicar no bot�o "Cancelar") ser� retornado <code>null</code>.
	 *         
	 * @see java.awt.Component
	 * @see javax.swing.JFileChooser
	 * 
	 * @author Prof. M�rlon Oliveira da Silva
	 */
	public static String dialogoAbrirArquivo(Component janelaPai, String titulo) {
	  JFileChooser dialogoAbrir = new JFileChooser();
	  
	   // Define as extens�es que ser�o usadas pelo JFileChooser para filtrar os tipos de arquivos que ser�o exibidos na janela.
	  FileNameExtensionFilter extensaoDatTxt = new FileNameExtensionFilter("Arquivos DAT e TXT", "dat", "txt");
	  dialogoAbrir.setFileFilter(extensaoDatTxt);
	  
	  // Define novas extens�es de arquivo e adiciona ao filtro de JFileChooser os tipos de arquivos que ser�o exibidos no di�logo.
	  FileNameExtensionFilter extensaoSer = new FileNameExtensionFilter("Arquivo de Serializa��o (ser)", "ser");
	  dialogoAbrir.addChoosableFileFilter(extensaoSer);
	     
	  // Define qual � o diret�rio default para abrir os arquivos.
	  dialogoAbrir.setCurrentDirectory(new File("." + File.separator + "arquivos"));
	  
	  // Indica que o usu�rio poder� selecionar apenas nomes de arquivos. 
	  dialogoAbrir.setFileSelectionMode(JFileChooser.FILES_ONLY);
	
	  // Define o t�tulo do di�logo. 
	  dialogoAbrir.setDialogTitle(titulo);
	
	  // Exibe o di�logo.
	  int opcao = dialogoAbrir.showOpenDialog(janelaPai);
	  
	  // Verifica se o usu�rio cancelou a opera��o (clicou no bot�o "Cancelar); se n�o, obt�m o nome do arquivo digitado ou selecionado pelo usu�rio no di�logo.
	  return (opcao == JFileChooser.CANCEL_OPTION) ? null : dialogoAbrir.getSelectedFile().getPath();
	} 
} // class EntradaESaida