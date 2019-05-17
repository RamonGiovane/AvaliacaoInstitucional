package com.rdr.avaliacao.ig;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import javax.swing.JSeparator;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class IgAvaliacaoInstitucional extends JFrame{
	public IgAvaliacaoInstitucional() {
		setTitle("Avalia\u00E7\u00E3o Institucional");
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblImportarDados = new JLabel("Importar Dados...");
		lblImportarDados.setBounds(222, 140, 113, 14);
		panel.add(lblImportarDados);
		
		JLabel lblAbrirDadosPreviamente = new JLabel("Abrir Dados Previamente Importados...");
		lblAbrirDadosPreviamente.setBounds(222, 165, 196, 14);
		panel.add(lblAbrirDadosPreviamente);
		
		JLabel lblComear = new JLabel("Come\u00E7ar...");
		lblComear.setBounds(146, 114, 113, 14);
		panel.add(lblComear);
		
		JLabel lblSalvarDadosImportados = new JLabel("Salvar Dados Importados...");
		lblSalvarDadosImportados.setBounds(222, 234, 154, 14);
		panel.add(lblSalvarDadosImportados);
		
		JLabel lblSalvarDados = new JLabel("Terminar Depois...");
		lblSalvarDados.setBounds(146, 208, 113, 14);
		panel.add(lblSalvarDados);
		
		JLabel lblFinalizar = new JLabel("Finalizar...");
		lblFinalizar.setBounds(146, 268, 113, 14);
		panel.add(lblFinalizar);
		
		JLabel lblGerarRelatrios = new JLabel("Gerar Relat\u00F3rios...");
		lblGerarRelatrios.setBounds(222, 294, 154, 14);
		panel.add(lblGerarRelatrios);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnArquivo = new JMenu("Arquivo");
		menuBar.add(mnArquivo);
		
		JMenuItem mntmNovaImportaoDe = new JMenuItem("Importar Novos Dados...");
		mntmNovaImportaoDe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnArquivo.add(mntmNovaImportaoDe);
		
		JMenuItem mntmAbrir = new JMenuItem("Abrir...");
		mntmAbrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnArquivo.add(mntmAbrir);
		
		JMenuItem mntmSalvarComo = new JMenuItem("Salvar como...");
		mntmSalvarComo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnArquivo.add(mntmSalvarComo);
		
		JSeparator separator = new JSeparator();
		mnArquivo.add(separator);
		
		JMenuItem mntmFecharPrograma = new JMenuItem("Fechar Programa");
		mnArquivo.add(mntmFecharPrograma);
		
		JMenu mnRelatrio = new JMenu("Relat\u00F3rio");
		menuBar.add(mnRelatrio);
		
		JMenu mnAjuda = new JMenu("Ajuda");
		menuBar.add(mnAjuda);
	}
}
