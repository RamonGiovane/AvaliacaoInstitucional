package com.rdr.avaliacao.ig.janelas;

import static com.rdr.avaliacao.ig.InterfaceConstraints.CAMINHO_ICON_DB;
import static com.rdr.avaliacao.ig.InterfaceConstraints.CAMINHO_ICON_GRAPHIC;
import static com.rdr.avaliacao.ig.InterfaceConstraints.CAMINHO_IMPORT_ICON;
import static com.rdr.avaliacao.ig.InterfaceConstraints.COR_BACKGROUND;
import static com.rdr.avaliacao.ig.InterfaceConstraints.COR_BTN_MENU;
import static com.rdr.avaliacao.ig.InterfaceConstraints.COR_BTN_MENU_HOVER;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_ERRO_IMPORTAR_SEM_CONEXAO;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_SOBRE;
import static com.rdr.avaliacao.ig.InterfaceConstraints.TITULO_IMPORTAR_DADOS;
import static com.rdr.avaliacao.ig.InterfaceConstraints.TITULO_PROGRAMA;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import com.rdr.avaliacao.AvaliacaoInstitucional;
import com.rdr.avaliacao.es.EntradaESaida;
import com.rdr.avaliacao.ig.LookAndFeel;
import com.rdr.avaliacao.ig.TipoRelatorio;

/**Classe de interface gráfica que representa a janela principal da aplicação, por onde o usuário pode 
 * acessar todos os outros componentes
 * @author Ramon Giovane
 *
 */
public class IgAvaliacaoInstitucional extends JFrame{
	private static IgAvaliacaoInstitucional igAvaliacaoInstitucional;
	private static IgBancoDeDados igBancoDeDados;
	
	private AvaliacaoInstitucional avaliacaoInstitucional;

	
	
	private IgAvaliacaoInstitucional(AvaliacaoInstitucional avaliacaoInstitucional) {
		setResizable(false);

		
		LookAndFeel.definirIcone(this);
		igAvaliacaoInstitucional = this;
		this.avaliacaoInstitucional = avaliacaoInstitucional;

		construirIg();
		
		//Constrói a janela de conexão ao banco de dados
		igBancoDeDados = IgBancoDeDados.getInstance();
		igBancoDeDados.exibir(this);
	}

	/**Obtém uma instância da classe de interface gráfica principal do programa
	 * 
	 * @param avaliacaoInstitucional
	 * @return
	 */
	public static IgAvaliacaoInstitucional getInstance(AvaliacaoInstitucional avaliacaoInstitucional) {
		if(igAvaliacaoInstitucional == null)
			igAvaliacaoInstitucional = 	new IgAvaliacaoInstitucional(avaliacaoInstitucional);
		return igAvaliacaoInstitucional;
	}


	public static IgAvaliacaoInstitucional getInstance() {
		return igAvaliacaoInstitucional;
	}

	private void construirIg() {
		
		LookAndFeel.definirLookAndFeel(this);
	
		
		setTitle("Avalia\u00E7\u00E3o Institucional");
		getContentPane().setLayout(new BorderLayout(0, 0));
		setLocationByPlatform(true);
		JPanel panel = new JPanel();
		panel.setBackground(COR_BACKGROUND);
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JButton lblImportarDados = new JButton("Importar Nova Pesquisa...");
		lblImportarDados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				abrirIgNovaPesquisa();
			}

		});
		lblImportarDados.setBackground(Color.WHITE);
		lblImportarDados.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblImportarDados.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		comportamentoJLabelMenu(lblImportarDados);

		lblImportarDados.setToolTipText("Abrir planilha de dados a serem analisados");
		lblImportarDados.setForeground(COR_BTN_MENU);
		lblImportarDados.setBounds(275, 192, 217, 24);

		panel.add(lblImportarDados);

		JLabel lblComear = new JLabel("Come\u00E7ar");
		lblComear.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblComear.setBounds(199, 164, 113, 17);
		panel.add(lblComear);

		JLabel lblFinalizar = new JLabel("Gerar Relat\u00F3rio");
		lblFinalizar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFinalizar.setBounds(199, 259, 113, 14);
		panel.add(lblFinalizar);

		JButton lblGerarRelatrios = new JButton("Participantes por Curso...");
		lblGerarRelatrios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				relatorio(TipoRelatorio.PARTICIPANTES_POR_CURSO);
			}
		});
		lblGerarRelatrios.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblGerarRelatrios.setForeground(COR_BTN_MENU);
		lblGerarRelatrios.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblGerarRelatrios.setBounds(275, 289, 217, 24);
		panel.add(lblGerarRelatrios);

		JButton lblparticipantesPorSegmento = new JButton("Participantes por Segmento...");
		lblparticipantesPorSegmento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				relatorio(TipoRelatorio.PARTICIPANTES_POR_SEGMENTO);
			}

		});
		lblparticipantesPorSegmento.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblparticipantesPorSegmento.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblparticipantesPorSegmento.setForeground(COR_BTN_MENU);
		lblparticipantesPorSegmento.setBounds(275, 326, 217, 24);
		panel.add(lblparticipantesPorSegmento);

		JButton lblconceitoMdioPor = new JButton("Conceito M\u00E9dio por Curso...");
		lblconceitoMdioPor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				relatorio(TipoRelatorio.CONCEITO_MEDIO_CURSO);
			}
		});
		lblconceitoMdioPor.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblconceitoMdioPor.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblconceitoMdioPor.setBounds(275, 363, 217, 24);
		lblconceitoMdioPor.setForeground(COR_BTN_MENU);
		panel.add(lblconceitoMdioPor);

		JButton lblconceitoMdioPor_1 = new JButton("Conceito Médio por Segmento...");
		lblconceitoMdioPor_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				relatorio(TipoRelatorio.CONCEITO_MEDIO_ASSUNTO);
			}
		});
		lblconceitoMdioPor_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblconceitoMdioPor_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblconceitoMdioPor_1.setBounds(275, 403, 217, 24);
		lblconceitoMdioPor_1.setForeground(COR_BTN_MENU);
		panel.add(lblconceitoMdioPor_1);

		JLabel lblConfigurar = new JLabel("Configurar");
		lblConfigurar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblConfigurar.setBounds(199, 446, 113, 17);
		panel.add(lblConfigurar);

		JButton lblConfigurar_1 = new JButton("Conex\u00E3o com Banco de Dados...");
		lblConfigurar_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				abrirIgBancoDeDados();
			}
		});
		lblConfigurar_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblConfigurar_1.setToolTipText("Abrir planilha de dados a serem analisados");
		lblConfigurar_1.setForeground(COR_BTN_MENU);
		lblConfigurar_1.setFont(new Font("Tahoma", Font.PLAIN, 12));

		//Adicionando eventos de mouse � interface
		comportamentoJLabelMenu(lblConfigurar_1);
		comportamentoJLabelMenu(lblconceitoMdioPor);
		comportamentoJLabelMenu(lblconceitoMdioPor_1);
		comportamentoJLabelMenu(lblparticipantesPorSegmento);
		comportamentoJLabelMenu(lblGerarRelatrios);

		lblConfigurar_1.setBounds(275, 475, 217, 24);
		panel.add(lblConfigurar_1);

		JLabel label_1 = new JLabel("");
		label_1.setIcon(new ImageIcon(IgAvaliacaoInstitucional.class.getResource(CAMINHO_ICON_DB)));
		label_1.setBounds(163, 431, 32, 32);
		panel.add(label_1);

		JLabel label_4 = new JLabel("");
		label_4.setIcon(new ImageIcon(IgAvaliacaoInstitucional.class.getResource(CAMINHO_ICON_GRAPHIC)));
		label_4.setBounds(163, 246, 32, 32);
		panel.add(label_4);

		JLabel label_5 = new JLabel("");
		label_5.setIcon(new ImageIcon(IgAvaliacaoInstitucional.class.getResource(CAMINHO_IMPORT_ICON)));
		label_5.setBounds(163, 152, 32, 32);
		panel.add(label_5);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnArquivo = new JMenu("Arquivo");
		mnArquivo.setMnemonic(KeyEvent.VK_A);
		menuBar.add(mnArquivo);

		JMenuItem mntmNovaImportaoDe = new JMenuItem("Importar Novos Pesquisa...");
		mntmNovaImportaoDe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				abrirIgNovaPesquisa();
			}
		});
		mntmNovaImportaoDe.setMnemonic(KeyEvent.VK_I);
		mntmNovaImportaoDe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
		mnArquivo.add(mntmNovaImportaoDe);
		
		JSeparator separator_1 = new JSeparator();
		mnArquivo.add(separator_1);

		JMenuItem mntmAbrir = new JMenuItem("Conectar com Banco de Dados...");
		mntmAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirIgBancoDeDados();
			}
		});
		mntmAbrir.setMnemonic(KeyEvent.VK_C);
		mntmAbrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK));
		mnArquivo.add(mntmAbrir);

		JSeparator separator = new JSeparator();
		mnArquivo.add(separator);

		JMenuItem mntmFecharPrograma = new JMenuItem("Fechar Programa");
		mntmFecharPrograma.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AvaliacaoInstitucional.fecharPrograma();
			}
		});
		mntmFecharPrograma.setMnemonic(KeyEvent.VK_F);
		mnArquivo.add(mntmFecharPrograma);

		JMenu mnRelatrio = new JMenu("Relat\u00F3rio");
		mnRelatrio.setMnemonic(KeyEvent.VK_R);
		menuBar.add(mnRelatrio);
		
		JMenuItem mntmParticipantesPorCurso = new JMenuItem("Participantes por Curso...");
		mntmParticipantesPorCurso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				relatorio(TipoRelatorio.PARTICIPANTES_POR_CURSO);
			}
		});
		mntmParticipantesPorCurso.setMnemonic(KeyEvent.VK_P);
		mntmParticipantesPorCurso.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mntmParticipantesPorCurso.setActionCommand("Participantes por Curso...");
		mnRelatrio.add(mntmParticipantesPorCurso);
		
		JMenuItem mntmParticipantesPorSegmento = new JMenuItem("Participantes por Segmento...");
		mntmParticipantesPorSegmento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				relatorio(TipoRelatorio.PARTICIPANTES_POR_SEGMENTO);
			}
		});
		mntmParticipantesPorSegmento.setMnemonic(KeyEvent.VK_S);
		mntmParticipantesPorSegmento.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnRelatrio.add(mntmParticipantesPorSegmento);
		
		JMenuItem mntmConceitoMdioPor = new JMenuItem("Conceito M\u00E9dio Por Curso...");
		mntmConceitoMdioPor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				relatorio(TipoRelatorio.CONCEITO_MEDIO_CURSO);
			}
		});
		mntmConceitoMdioPor.setMnemonic(KeyEvent.VK_C);
		mntmConceitoMdioPor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnRelatrio.add(mntmConceitoMdioPor);
		
		JMenuItem mntmConceitoMdioPor_1 = new JMenuItem("Conceito M\u00E9dio por Assuto...");
		mntmConceitoMdioPor_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				relatorio(TipoRelatorio.CONCEITO_MEDIO_ASSUNTO);
			}
		});
		mntmConceitoMdioPor_1.setMnemonic(KeyEvent.VK_A);
		mntmConceitoMdioPor_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnRelatrio.add(mntmConceitoMdioPor_1);

		JMenu mnAjuda = new JMenu("Ajuda");
		mnAjuda.setMnemonic(KeyEvent.VK_U);
		menuBar.add(mnAjuda);
		
		JMenuItem mntmSobre = new JMenuItem("Sobre");
		mntmSobre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sobre();
			}

		});
		mnAjuda.add(mntmSobre);

		setSize(700, 694);
		getContentPane().setBackground(COR_BACKGROUND);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				
				dispose();
				AvaliacaoInstitucional.fecharPrograma();
			}
		});
		setVisible(true);

	}


	private void abrirIgBancoDeDados() {
		igBancoDeDados.exibir(IgAvaliacaoInstitucional.this);
	}

	/**Transforma o cursor em um formato difrente.
	 * 
	 * @param tipoCursor código de formato do cursor estabelcido pelas constantes da classe {@link Cursor}.
	 */
	public static void mudarCursor(int tipoCursor) {
		igAvaliacaoInstitucional.setCursor(Cursor.getPredefinedCursor(tipoCursor));
	}


	private void comportamentoJLabelMenu(JButton label) {
		label.setOpaque(true);
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				label.setForeground(COR_BTN_MENU_HOVER);
			}	
			@Override
			public void mouseExited(MouseEvent e) {
				label.setForeground(COR_BTN_MENU);
			}

		});
	}
	
	/**Minimiza a janela principal do programa*/
	public static void desativarInterface() {
		igAvaliacaoInstitucional.setEnabled(false);
	}
	
	/**Maximiza e foca a janela principal do programa. Se já estiver ativada, não faz nada.*/
	public static void ativarInterface() {
		igAvaliacaoInstitucional.setEnabled(true);
		igAvaliacaoInstitucional.requestFocus();
	}
	private void relatorio(TipoRelatorio tipoRelatorio) {
		IgSeletorRelatorio.getInstance(tipoRelatorio).exibir(this);
		
	}
	
	private void importarDados() {
		if(avaliacaoInstitucional.checarConexaoBancoDeDados())
			IgNovaPesquisa.getInstance(avaliacaoInstitucional).exibir(this);
		else
			EntradaESaida.msgInfo(this, MSG_ERRO_IMPORTAR_SEM_CONEXAO, TITULO_IMPORTAR_DADOS);
	}
	
	private void abrirIgNovaPesquisa() {
		try{
			importarDados();
		}catch (NullPointerException e) {
			//System.out.println("Abrir arquivo cancelado.");
		}
	}
	
	private void sobre() {
		EntradaESaida.msgInfo(this, MSG_SOBRE, TITULO_PROGRAMA);
		
	}
}
