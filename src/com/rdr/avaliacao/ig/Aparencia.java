package com.rdr.avaliacao.ig;

import static com.rdr.avaliacao.es.EntradaESaida.msgInfo;
import static com.rdr.avaliacao.ig.IgConstantes.MSG_ERRO_BUILD_UI;
import static com.rdr.avaliacao.ig.IgConstantes.TITULO_PROGRAMA;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.UIManager;

public class Aparencia {
	public static void definirLookAndFeel(Component component) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		} catch (Exception e) {
			msgInfo(component, MSG_ERRO_BUILD_UI, TITULO_PROGRAMA);
		}
	}
}

