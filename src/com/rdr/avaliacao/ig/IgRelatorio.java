package com.rdr.avaliacao.ig;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.border.BevelBorder;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.TitledBorder;

import com.rdr.avaliacao.es.EntradaESaida;

import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IgRelatorio extends JDialog {
	private ButtonGroup buttonGroup =  new ButtonGroup();
	private JRadioButton radioBtnTabela;
	private JRadioButton radioBtinGrafico;
	private JPanel panelDados;
	//private JPanel panel_1;
	private JPanel panelTabela;
	
	public IgRelatorio() {
		setResizable(false);
		setTitle("Relat\u00F3rio de Autoavalia\u00E7\u00E3o Institucional");
		getContentPane().setLayout(null);
		setSize(715, 596);
		panelDados = new JPanel();
		panelDados.setBorder(new TitledBorder(null, "Dados", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelDados.setBounds(10, 55, 689, 414);
		
		panelDados.setLayout(new BorderLayout(0, 0));
		
		//panel_1 = new JPanel();
		
		
		
		JPanel panelModoExibicao = new JPanel();
		panelModoExibicao.setBorder(new TitledBorder(null, "Modo de Exibi\u00E7\u00E3o", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelModoExibicao.setBounds(10, 481, 689, 75);
		getContentPane().add(panelModoExibicao);
		panelModoExibicao.setLayout(null);
		
		radioBtnTabela = new JRadioButton("Tabela");
		radioBtnTabela.setBounds(165, 19, 109, 23);
		panelModoExibicao.add(radioBtnTabela);
		
		radioBtinGrafico = new JRadioButton("Gr\u00E1fico");
		radioBtinGrafico.setBounds(364, 19, 109, 23);
		panelModoExibicao.add(radioBtinGrafico);
		
		JButton btnGerarPdf = new JButton("Gerar PDF");
		btnGerarPdf.setEnabled(false);
		btnGerarPdf.setBounds(560, 35, 89, 23);
		panelModoExibicao.add(btnGerarPdf);
		
		buttonGroup.add(radioBtinGrafico);
		buttonGroup.add(radioBtnTabela);
		
		definirComportamentoRadioButtons();
		
		JLabel lblNomeDaPesquisa = new JLabel("Nome da Pesquisa:");
		lblNomeDaPesquisa.setBounds(12, 21, 126, 14);
		getContentPane().add(lblNomeDaPesquisa);
		
		JComboBox comboNomePesquisa = new JComboBox();
		comboNomePesquisa.setBounds(110, 18, 225, 20);
		getContentPane().add(comboNomePesquisa);
		

			
		
		panelTabela = new JPanel();
		definirComportamentoRadioButtons();
		getContentPane().add(panelDados);
		panelDados.add(panelTabela, BorderLayout.CENTER);
		panelDados.add(panelTabela);	
		
		
		setVisible(true);
	}

	private void definirComportamentoRadioButtons() {
		radioBtnTabela.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				System.out.println("Hey");
				exibirTabela();
			}
		});
		
	
	}

	private void exibirTabela() {
		String[] titulos = {"Teste", "Teste", "Teste"};
		Object[][] dados = {{"1", 2, 4},
				{"2", "2", "4"},
				{"ewd", 2, 1}};
		
		
		panelTabela.setLayout(new BorderLayout());
		JTable tabela = EntradaESaida.gerarTabela(dados, titulos, null, null, 50, 500);
		panelTabela.add(new JScrollPane(tabela), BorderLayout.CENTER);
		panelTabela.add(tabela);
		panelDados.add(panelTabela);
		tabela.setVisible(true);
		
		panelTabela.revalidate();
		panelTabela.repaint();
		panelTabela.setVisible(true);
		panelDados.setVisible(true);
		
		
		
	}
	
}
