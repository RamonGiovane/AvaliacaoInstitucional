package com.rdr.avaliacao.es;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class EntradaESaida {
	/**
	 * Exibe uma mensagem informativa em uma caixa de diálogo com o texto da barra de título definido em 
	 * titulo.
	 */
	public static void msgInfo(String mensagem, String titulo) {
		showMessageDialog(null, mensagem, titulo, INFORMATION_MESSAGE);
	}
	
	/**
	 * Exibe um componente em uma caixa de diálogo com o texto da barra de título definido em titulo.
	 */
	public static void msgInfo(Object componente, String titulo) {
		showMessageDialog(null, componente, titulo, INFORMATION_MESSAGE);
	}
	
	/**
	 * Exibe uma mensagem de erro em uma caixa de diálogo com o texto da barra de título definido em titulo.
	 */
	public static void msgErro(String mensagem, String titulo) {
		showMessageDialog(null, mensagem, titulo, ERROR_MESSAGE);
	}
	
	/**
	 * Lê uma string em uma caixa de diálogo com o texto da barra de título definido em titulo.
	 * Retorna a string lida ou null se o usuário cancelar a operação (clicando no botão Cancelar ou Fechar).
	 */
	public static String lerString(String mensagem, String titulo) {
		return showInputDialog(null, mensagem, titulo, QUESTION_MESSAGE);
	}
	
	/**
	 * Lê um número inteiro em uma caixa de diálogo com o texto da barra de título definido em titulo.
	 * Retorna o numero lido ou null se o usuário cancelar a operação (clicando no botão Cancelar ou Fechar).
	 */
	public static Integer lerNumeroInteiro(String mensagem, String titulo) throws NumberFormatException {
		return Integer.parseInt(lerString(mensagem, titulo)); 
	}
	
	/**
	 * Lê um número real em uma caixa de diálogo com o texto da barra de título definido em titulo.
	 * Retorna o numero lido ou null se o usuário cancelar a operação (clicando no botão Cancelar ou Fechar).
	 */
	public static Float lerNumeroReal(String mensagem, String titulo) {
		return Float.parseFloat(lerString(mensagem, titulo)); 
	}
	
	/**
	 * Exibe uma pergunta em uma caixa de diálogo para o usuário responder Sim ou Não.
	 * 
	 * @param prompt pergunta a ser exibido na caixa de diálogo;
	 * @param titulo texto a ser exibido na barra de título da caixa de diálogo.
	 * 
	 * @return os valores <code>YES_OPTION</code>, <code>NO_OPTION</code> ou <code>CLOSED_OPTION</code>, respectivamente, se o usuário responder
	 * Sim, Não ou fechar a caixa de diálogo clicando no botão Fechar.
	 * 
	 * @see javax.swing.JOptionPane
	 */
	public static int msgConfirma(String prompt, String titulo) {
		return showConfirmDialog(null, prompt, titulo, YES_NO_OPTION);
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
	 */
	public static void exibirTabela(String titulo, Object[][] linhas, String[] colunas, int[] larguraColuna, 
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
		 	msgInfo(new JScrollPane(table), titulo);
	} // exibirTabela()
	
	/**
	 * Exibe um texto em uma caixa de diálogo com o texto da barra de título definido em titulo.
	 */
	public static void exibirTexto(String texto, String titulo) {
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
		msgInfo(new JScrollPane(textArea), titulo);
	}
} // class EntradaESaida