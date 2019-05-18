package com.rdr.avaliacao.ig;

import static com.rdr.avaliacao.ig.IgConstantes.COR_LABEL_MENU;
import static com.rdr.avaliacao.ig.IgConstantes.MSG_ERRO_BUILD_UI;
import static com.rdr.avaliacao.ig.IgConstantes.TITULO_PROGRAMA;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import com.rdr.avaliacao.AvaliacaoInstitucional;
import com.rdr.avaliacao.es.EntradaESaida;

import static com.rdr.avaliacao.es.EntradaESaida.*;
import static com.rdr.avaliacao.ig.IgConstantes.*;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class IgAvaliacaoInstitucional extends JFrame{
	private static IgAvaliacaoInstitucional igAvaliacaoInstitucional;
	private AvaliacaoInstitucional avaliacaoInstitucional;
	

	private IgAvaliacaoInstitucional(AvaliacaoInstitucional avaliacaoInstitucional) {
		igAvaliacaoInstitucional = this;
		this.avaliacaoInstitucional = avaliacaoInstitucional;
		
		construirIg();
	}
	
	public static IgAvaliacaoInstitucional getInstance(AvaliacaoInstitucional avaliacaoInstitucional) {
		return igAvaliacaoInstitucional == null ? 
				new IgAvaliacaoInstitucional(avaliacaoInstitucional) : igAvaliacaoInstitucional;
	}
	
	public static IgAvaliacaoInstitucional getReference() {
		return igAvaliacaoInstitucional;
	}
	
	private void construirIg() {
		Aparencia.definirLookAndFeel(this);
		setTitle("Avalia\u00E7\u00E3o Institucional");
		getContentPane().setLayout(new BorderLayout(0, 0));
		setLocationByPlatform(true);
		JPanel panel = new JPanel();
		panel.setBackground(COR_BACKGROUND);
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel lblImportarDados = new JLabel("<html><u>Importar Dados...</u><html>");
		lblImportarDados.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblImportarDados.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		comportamentoJLabelMenu(lblImportarDados);

		lblImportarDados.setToolTipText("Abrir planilha de dados a serem analisados");
		lblImportarDados.setForeground(COR_LABEL_MENU);
		lblImportarDados.setBounds(239, 145, 113, 14);
		
		panel.add(lblImportarDados);

		JLabel lblComear = new JLabel("Come\u00E7ar");
		lblComear.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblComear.setBounds(163, 116, 113, 17);
		panel.add(lblComear);

		JLabel lblFinalizar = new JLabel("Gerar Relat\u00F3rio");
		lblFinalizar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFinalizar.setBounds(163, 211, 113, 14);
		panel.add(lblFinalizar);

		JLabel lblGerarRelatrios = new JLabel("<html><u>Participantes por Curso...</u></html>");
		lblGerarRelatrios.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblGerarRelatrios.setForeground(COR_LABEL_MENU);
		lblGerarRelatrios.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblGerarRelatrios.setBounds(239, 241, 154, 14);
		panel.add(lblGerarRelatrios);
		
		JLabel lblparticipantesPorSegmento = new JLabel("<html><u>Participantes por Segmento...</u></html>");
		lblparticipantesPorSegmento.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblparticipantesPorSegmento.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblparticipantesPorSegmento.setForeground(COR_LABEL_MENU);
		lblparticipantesPorSegmento.setBounds(239, 278, 179, 14);
		panel.add(lblparticipantesPorSegmento);
		
		JLabel lblconceitoMdioPor = new JLabel("<html><u>Conceito M\u00E9dio por Curso...</u></html>");
		lblconceitoMdioPor.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblconceitoMdioPor.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblconceitoMdioPor.setBounds(239, 315, 179, 14);
		lblconceitoMdioPor.setForeground(COR_LABEL_MENU);
		panel.add(lblconceitoMdioPor);
		
		JLabel lblconceitoMdioPor_1 = new JLabel("<html><u>Conceito M\u00E9dio por Assunto...</u></html>");
		lblconceitoMdioPor_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblconceitoMdioPor_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblconceitoMdioPor_1.setBounds(239, 355, 179, 14);
		lblconceitoMdioPor_1.setForeground(COR_LABEL_MENU);
		panel.add(lblconceitoMdioPor_1);
		
		JLabel lblConfigurar = new JLabel("Configurar");
		lblConfigurar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblConfigurar.setBounds(163, 398, 113, 17);
		panel.add(lblConfigurar);
		
		JLabel lblConfigurar_1 = new JLabel("<html><u>Op\u00E7\u00F5es do Banco de Dados...</u></html>");
		lblConfigurar_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblConfigurar_1.setToolTipText("Abrir planilha de dados a serem analisados");
		lblConfigurar_1.setForeground(COR_LABEL_MENU);
		lblConfigurar_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		//Adicionando eventos de mouse à interface
		comportamentoJLabelMenu(lblConfigurar_1);
		comportamentoJLabelMenu(lblconceitoMdioPor);
		comportamentoJLabelMenu(lblconceitoMdioPor_1);
		comportamentoJLabelMenu(lblparticipantesPorSegmento);
		comportamentoJLabelMenu(lblGerarRelatrios);
		
		lblConfigurar_1.setBounds(239, 427, 179, 14);
		panel.add(lblConfigurar_1);
		
		JLabel label_1 = new JLabel("");
		label_1.setIcon(new ImageIcon(IgAvaliacaoInstitucional.class.getResource(CAMINHO_ICON_DB)));
		label_1.setBounds(127, 383, 32, 32);
		panel.add(label_1);
		
		JLabel label_4 = new JLabel("");
		label_4.setIcon(new ImageIcon(IgAvaliacaoInstitucional.class.getResource(CAMINHO_ICON_GRAPHIC)));
		label_4.setBounds(127, 198, 32, 32);
		panel.add(label_4);
		
		JLabel label_5 = new JLabel("");
		label_5.setIcon(new ImageIcon(IgAvaliacaoInstitucional.class.getResource(CAMINHO_IMPORT_ICON)));
		label_5.setBounds(127, 104, 32, 32);
		panel.add(label_5);
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
		getContentPane().setBackground(COR_BACKGROUND);
		setVisible(true);
		
	}



	private void comportamentoJLabelMenu(JLabel label) {

		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				label.setForeground(COR_LABEL_MENU_HOVER);
			}	
			@Override
			public void mouseExited(MouseEvent e) {
				label.setForeground(COR_LABEL_MENU);
			}

		});
	}
}
