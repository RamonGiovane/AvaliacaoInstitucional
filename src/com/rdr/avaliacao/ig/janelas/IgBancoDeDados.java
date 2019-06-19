package com.rdr.avaliacao.ig.janelas;

import static com.rdr.avaliacao.ig.InterfaceConstraints.CAMINHO_ICON_DB;
import static com.rdr.avaliacao.ig.InterfaceConstraints.COR_BACKGROUND;
import static com.rdr.avaliacao.ig.InterfaceConstraints.ERRO_CONECTAR_BD;
import static com.rdr.avaliacao.ig.InterfaceConstraints.ERRO_DESCONECTAR_BD;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_BD_CONEXAO_JA_EXISTE;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_JANELA_BANCO;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_SUCESSO_BD;
import static com.rdr.avaliacao.ig.InterfaceConstraints.NOME_BD_PADRAO;
import static com.rdr.avaliacao.ig.InterfaceConstraints.SENHA_BD_PADRAO;
import static com.rdr.avaliacao.ig.InterfaceConstraints.TITULO_BD;
import static com.rdr.avaliacao.ig.InterfaceConstraints.USUARIO_BD_PADRAO;

import java.awt.Component;
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

import com.rdr.avaliacao.AvaliacaoInstitucional;
import com.rdr.avaliacao.es.EntradaESaida;
import com.rdr.avaliacao.ig.LookAndFeel;
import com.rdr.avaliacao.ig.PropriedadesDeJanela;

/**Tela de interface gráfica para que o usuário possa inserir os dados de e realizar  uma conexão com o banco de dados
 * 
 * @author Ramon Giovane
 *
 */
public class IgBancoDeDados extends JDialog implements PropriedadesDeJanela{
	private static JTextField fieldNomeBD;
	private static JTextField filedUsuario;
	private static JPasswordField fieldSenha;
	private static IgBancoDeDados igBancoDeDados;
	private static AvaliacaoInstitucional app;

	private IgBancoDeDados() {
		app = AvaliacaoInstitucional.getInstance();
		igBancoDeDados = this;
		construirIg();

	}

	/**Obtém uma instância única da janela*/
	public static IgBancoDeDados getInstance() {
		if(igBancoDeDados == null) 
			new IgBancoDeDados();

		return igBancoDeDados;
	}


	private void construirIg() {
		LookAndFeel.definirLookAndFeel(this);
		setModal(true);
		setResizable(false);
		setBounds(new Rectangle(0, 0, 340, 255));
		getContentPane().setLayout(null);

		JLabel lblParaImportarOs = new JLabel(MSG_JANELA_BANCO);
		lblParaImportarOs.setBounds(10, 143, 282, 48);
		getContentPane().add(lblParaImportarOs);

		fieldNomeBD = new JTextField();
		fieldNomeBD.setBounds(158, 51, 140, 20);
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
		filedUsuario.setBounds(158, 87, 140, 20);
		getContentPane().add(filedUsuario);
		filedUsuario.setColumns(10);

		fieldSenha = new JPasswordField();
		fieldSenha.setBounds(158, 119, 140, 20);
		getContentPane().add(fieldSenha);
		fieldSenha.setColumns(10);

		JButton btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				solicitarConexaoBanco(fieldNomeBD.getText(), filedUsuario.getText(), 
						new String(fieldSenha.getPassword()));
			}


		});
		btnConectar.setBounds(227, 190, 89, 23);
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

		LookAndFeel.definirBotaoPrincipal(this, btnConectar);
	}


	@Override
	/**Exibe a janela em relação àquela que invocou esta
	 * 
	 * @param janelaPai componente AWT que realizou a invocação desta
	 */
	public void exibir(Component janelaPai) {
		setLocationRelativeTo(janelaPai);
		setVisible(true);

	}

	@Override
	/**Esconde a janela mas <b>não</b> a fecha*/
	public void esconder() {
		setVisible(false);

	}

	@Override
	/**Libera os recursos da janela e fecha de fato*/
	public void fechar() {
		igBancoDeDados.dispose();
	}

	private void solicitarConexaoBanco(String nomeBD, String usuarioBD, String senhaBD) {
		if(app.checarConexaoBancoDeDados()) {
			//Se uma conexão com o banco já existe, confirma ao usuário se ele quer desconectar da atual.
			int msgConfirma = EntradaESaida.msgConfirma(this, MSG_BD_CONEXAO_JA_EXISTE, 
					TITULO_BD);
			if(msgConfirma == JOptionPane.YES_OPTION)
				try {
					app.desconectarBancoDeDados();
				} catch (SQLException e) {
					EntradaESaida.msgErro(this, ERRO_DESCONECTAR_BD, TITULO_BD);
				}
			else return;
		}

		try {
			app.conectarBancoDados(nomeBD, usuarioBD, senhaBD);
			EntradaESaida.msgInfo(this, MSG_SUCESSO_BD, TITULO_BD);
			definirCredenciais(nomeBD, usuarioBD, senhaBD);
			igBancoDeDados.esconder();

		} catch (SQLException e) {
			EntradaESaida.msgErro(this, ERRO_CONECTAR_BD, TITULO_BD);
		}

	}

	private void definirCredenciais(String nomeBD, String usuarioBD, String senhaBD) {
		filedUsuario.setText(usuarioBD);
		fieldNomeBD.setText(nomeBD);
		fieldSenha.setText(senhaBD);
	}
}
