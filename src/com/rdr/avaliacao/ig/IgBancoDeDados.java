package com.rdr.avaliacao.ig;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import static com.rdr.avaliacao.ig.InterfaceConstraints.*;
import com.rdr.avaliacao.es.EntradaESaida;
import com.rdr.avaliacao.es.bd.BancoDeDados;

@SuppressWarnings("serial")
public class IgBancoDeDados extends JDialog{
	private static BancoDeDados bd;
	private static JTextField fieldNomeBD;
	private static JTextField filedUsuario;
	private static JPasswordField fieldSenha;
	private static IgBancoDeDados igBancoDeDados;
	
	private IgBancoDeDados() {
		
		construirIg();

	}
	
	public static IgBancoDeDados getInstance() {
		System.out.println(igBancoDeDados);
		if(igBancoDeDados == null) {
			igBancoDeDados = new IgBancoDeDados();
			return igBancoDeDados;
		}
		else {
			igBancoDeDados.setVisible(true);
			return igBancoDeDados;
		}
	}
	
	public static void closeInstance() {
		
	}
	
	private void construirIg() {
		System.out.println("Passing through...");
		Aparencia.definirLookAndFeel(this);
		setModal(true);
		setResizable(false);
		setBounds(new Rectangle(0, 0, 340, 255));
		getContentPane().setLayout(null);
		
		JLabel lblParaImportarOs = new JLabel("<html>Para importar os dados, \u00E9 preciso uma conex\u00E3o<br>com o Banco de Dados</html>");
		lblParaImportarOs.setBounds(10, 143, 282, 48);
		getContentPane().add(lblParaImportarOs);
		
		fieldNomeBD = new JTextField();
		fieldNomeBD.setBounds(158, 51, 134, 20);
		getContentPane().add(fieldNomeBD);
		fieldNomeBD.setColumns(10);
		
		
		JLabel lblNomeDaBase = new JLabel("Nome da Base de Dados: ");
		lblNomeDaBase.setBounds(25, 54, 123, 14);
		getContentPane().add(lblNomeDaBase);
		
		JLabel lblNomeDeUsurio = new JLabel("Nome de Usu\u00E1rio:");
		lblNomeDeUsurio.setBounds(27, 90, 92, 14);
		getContentPane().add(lblNomeDeUsurio);
		
		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setBounds(27, 125, 92, 14);
		getContentPane().add(lblSenha);
		
		filedUsuario = new JTextField();
		filedUsuario.setBounds(158, 87, 134, 20);
		getContentPane().add(filedUsuario);
		filedUsuario.setColumns(10);
		
		fieldSenha = new JPasswordField();
		fieldSenha.setBounds(158, 119, 134, 20);
		getContentPane().add(fieldSenha);
		fieldSenha.setColumns(10);
		
		JButton btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				solicitarConexaoBanco(fieldNomeBD.getText(), filedUsuario.getText(), 
						new String(fieldSenha.getPassword()));
			}

			
		});
		btnConectar.setBounds(227, 186, 89, 23);
		getContentPane().add(btnConectar);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(IgBancoDeDados.class.getResource(CAMINHO_ICON_DB)));
		label.setBounds(10, 11, 37, 32);
		getContentPane().add(label);
		setTitle(TITULO_BD);
		
		setModal(true);
		getContentPane().setBackground(COR_BACKGROUND);
		setLocationRelativeTo(IgAvaliacaoInstitucional.getInstance());
		
		fieldNomeBD.setText(NOME_BD_PADRAO);
		fieldSenha.setText(SENHA_BD_PADRAO);
		filedUsuario.setText(USUARIO_BD_PADRAO);
		
		setVisible(true);
	}
	
	private void solicitarConexaoBanco(String nomeBD, String usuarioBD, String senhaBD) {
		System.out.println(bd);
		if(bd != null) {
			int msgConfirma = EntradaESaida.msgConfirma(this, MSG_BD_CONEXAO_JA_EXISTE, 
					TITULO_BD);
			if(msgConfirma == JOptionPane.YES_OPTION)
				try {
					bd.fecharConexao();
				} catch (SQLException e) {
					EntradaESaida.msgErro(this, ERRO_DESCONECTAR_BD, TITULO_BD);
				}
			else return;
		}
		
		bd = BancoDeDados.criarConexao(nomeBD, usuarioBD, senhaBD);
		
		if(bd == null)
			EntradaESaida.msgErro(this, ERRO_CONECTAR_BD, TITULO_BD);
		else {
			EntradaESaida.msgInfo(this, MSG_SUCESSO_BD, TITULO_BD);
			setVisible(false);
			definirCredenciais(nomeBD, usuarioBD, senhaBD);
		}
	}
	
	private void definirCredenciais(String nomeBD, String usuarioBD, String senhaBD) {
		filedUsuario.setText(usuarioBD);
		fieldNomeBD.setText(nomeBD);
		fieldSenha.setText(senhaBD);
	}
}
