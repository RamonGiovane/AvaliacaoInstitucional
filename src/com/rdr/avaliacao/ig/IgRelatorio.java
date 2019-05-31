package com.rdr.avaliacao.ig;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.border.BevelBorder;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JComboBox;

public class IgRelatorio extends JDialog {
	protected ButtonGroup buttonGroup =  new ButtonGroup();
	
	public IgRelatorio() {
		setResizable(false);
		setTitle("Relat\u00F3rio de Autoavalia\u00E7\u00E3o Institucional");
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Dados", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(142, 55, 557, 414);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Modo de Exibi\u00E7\u00E3o", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 481, 689, 75);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Tabela");
		rdbtnNewRadioButton.setBounds(165, 19, 109, 23);
		panel_1.add(rdbtnNewRadioButton);
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Gr\u00E1fico");
		rdbtnNewRadioButton_1.setBounds(364, 19, 109, 23);
		panel_1.add(rdbtnNewRadioButton_1);
		
		JButton btnGerarPdf = new JButton("Gerar PDF");
		btnGerarPdf.setEnabled(false);
		btnGerarPdf.setBounds(560, 35, 89, 23);
		panel_1.add(btnGerarPdf);
		
		buttonGroup.add(rdbtnNewRadioButton_1);
		buttonGroup.add(rdbtnNewRadioButton);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "JPanel title", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(4, 55, 134, 414);
		getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		JList list = new JList();
		list.setBounds(7, 16, 118, 387);
		panel_2.add(list);
		
		JLabel lblNomeDaPesquisa = new JLabel("Nome da Pesquisa:");
		lblNomeDaPesquisa.setBounds(12, 21, 126, 14);
		getContentPane().add(lblNomeDaPesquisa);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(110, 18, 225, 20);
		getContentPane().add(comboBox);
	}
}
