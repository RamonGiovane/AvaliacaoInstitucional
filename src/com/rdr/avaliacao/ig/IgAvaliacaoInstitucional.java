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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.rdr.avaliacao.es.EntradaESaida;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static com.rdr.avaliacao.ig.IgConstantes.*;
import java.awt.Cursor;
import java.awt.Font;
public class IgAvaliacaoInstitucional extends JFrame{
	private IgAvaliacaoInstitucional igAvaliacaoInstitucional;
	public IgAvaliacaoInstitucional() {
		igAvaliacaoInstitucional = this;
		setLookAndFeel();
		setTitle("Avalia\u00E7\u00E3o Institucional");
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel lblImportarDados = new JLabel("<html><u>Importar Dados...</u><html>");
		lblImportarDados.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblImportarDados.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		comportamentoJLabelMenu(lblImportarDados);

		lblImportarDados.setToolTipText("Abrir planilha de dados a serem analisados");
		lblImportarDados.setForeground(Color.BLACK);
		lblImportarDados.setBounds(239, 145, 113, 14);
		
		panel.add(lblImportarDados);

		JLabel lblComear = new JLabel("Come\u00E7ar...");
		lblComear.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblComear.setBounds(163, 116, 113, 17);
		panel.add(lblComear);

		JLabel lblFinalizar = new JLabel("Finalizar...");
		lblFinalizar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFinalizar.setBounds(163, 211, 113, 14);
		panel.add(lblFinalizar);

		JLabel lblGerarRelatrios = new JLabel("<html><u>Gerar Relat\u00F3rios...</u></html>");
		lblGerarRelatrios.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblGerarRelatrios.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblGerarRelatrios.setBounds(239, 241, 154, 14);
		panel.add(lblGerarRelatrios);
		
		JLabel label = new JLabel("<html><u>Gerar Relat\u00F3rios...</u></html>");
		label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		label.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label.setBounds(239, 278, 154, 14);
		panel.add(label);
		
		JLabel label_2 = new JLabel("<html><u>Gerar Relat\u00F3rios...</u></html>");
		label_2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label_2.setBounds(239, 315, 154, 14);
		panel.add(label_2);
		
		JLabel label_3 = new JLabel("<html><u>Gerar Relat\u00F3rios...</u></html>");
		label_3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label_3.setBounds(239, 355, 154, 14);
		panel.add(label_3);
		
		JLabel lblConfigurar = new JLabel("Configurar...");
		lblConfigurar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblConfigurar.setBounds(163, 398, 113, 17);
		panel.add(lblConfigurar);
		
		JLabel lblConfigurar_1 = new JLabel("<html><u>Op\u00E7\u00F5es do Banco de Dados...</u></html>");
		lblConfigurar_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblConfigurar_1.setToolTipText("Abrir planilha de dados a serem analisados");
		lblConfigurar_1.setForeground(Color.BLACK);
		lblConfigurar_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		comportamentoJLabelMenu(lblConfigurar_1);
		comportamentoJLabelMenu(label_2);
		comportamentoJLabelMenu(label_3);
		comportamentoJLabelMenu(label);
		comportamentoJLabelMenu(lblGerarRelatrios);
		lblConfigurar_1.setBounds(239, 427, 179, 14);
		panel.add(lblConfigurar_1);
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
		
		setSize(600, 600);
		
		setVisible(true);
	}

	private void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		} catch (Exception e) {
			EntradaESaida.msgInfo(igAvaliacaoInstitucional, MSG_ERRO_BUILD_UI, NOME_PROGRAMA);
		}
	}

	private void comportamentoJLabelMenu(JLabel label) {

		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				label.setForeground(COR_LABEL_MENU);
			}	
			@Override
			public void mouseExited(MouseEvent e) {
				label.setForeground(Color.BLACK);
			}

		});
	}

	//private void definirCor()
}
