package com.rdr.avaliacao.ig;

import static com.rdr.avaliacao.es.EntradaESaida.msgInfo;
import static com.rdr.avaliacao.ig.InterfaceConstraints.CAMINHO_PROGRAMA_ICON;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_ERRO_BUILD_UI;
import static com.rdr.avaliacao.ig.InterfaceConstraints.TITULO_PROGRAMA;

import java.awt.Component;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.rdr.avaliacao.ig.janelas.IgAvaliacaoInstitucional;
public class LookAndFeel {
	
	private static boolean msgErroLookAndFeelExibida = false;
	
	/**Define o Look and Feel de um component AWT para usar o estilo padrão do sistema operacional.
	 * Exibe uma mensagem de erro(apenas uma vez) e utiliza o padrão do pacote Swing se não for possível.
	 * @param component componente a ser definido o estilo
	 */
	public static void definirLookAndFeel(Component component) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		} catch (Exception e) {
			if(!msgErroLookAndFeelExibida)
				msgInfo(component, MSG_ERRO_BUILD_UI, TITULO_PROGRAMA);
			msgErroLookAndFeelExibida = true;
		}
	}

	/**
	 * Define o ícone do programa a uma janela {@link JFrame}.
	 * 
	 * @param janela {@link JFrame} a receber o ícone.
	 */
	public static void definirIcone(JFrame janela) {

		janela.setIconImage(
				Toolkit.getDefaultToolkit().getImage(IgAvaliacaoInstitucional.class.getResource(CAMINHO_PROGRAMA_ICON)));

	}

	/**Define um botão para ser acionado em uma tela quando o usuário pressionar ENTER.
	 * 
	 * @param component componente AWT que contém o botão
	 * @param button botão a receber a propriedade
	 */
	public static void definirBotaoPrincipal(Component component, JButton button) {
		JRootPane rootPane = SwingUtilities.getRootPane(component); 
		rootPane.setDefaultButton(button);
	}
}

