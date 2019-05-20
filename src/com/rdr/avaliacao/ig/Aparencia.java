package com.rdr.avaliacao.ig;

import static com.rdr.avaliacao.es.EntradaESaida.msgInfo;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_ERRO_BUILD_UI;
import static com.rdr.avaliacao.ig.InterfaceConstraints.TITULO_PROGRAMA;

import java.awt.Component;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.UIManager;
import static com.rdr.avaliacao.ig.InterfaceConstraints.*;
public class Aparencia {
	public static void definirLookAndFeel(Component component) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		} catch (Exception e) {
			msgInfo(component, MSG_ERRO_BUILD_UI, TITULO_PROGRAMA);
		}
	}
	
	public static void definirIcone(JFrame component) {
		
		component.setIconImage(
				Toolkit.getDefaultToolkit().getImage(IgAvaliacaoInstitucional.class.getResource(CAMINHO_PROGRAMA_ICON)));
		
	}
}

