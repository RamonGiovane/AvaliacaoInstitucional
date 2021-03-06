package com.rdr.avaliacao.ig.janelas;

import static com.rdr.avaliacao.es.EntradaESaida.dialogoAbrirArquivo;
import static com.rdr.avaliacao.es.EntradaESaida.msgErro;
import static com.rdr.avaliacao.ig.InterfaceConstraints.CAMINHO_IMPORT_ICON;
import static com.rdr.avaliacao.ig.InterfaceConstraints.COR_BACKGROUND;
import static com.rdr.avaliacao.ig.InterfaceConstraints.DESCRICOES_EXTENSOES_CSV;
import static com.rdr.avaliacao.ig.InterfaceConstraints.EXTENSOES_CSV;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_ARQUIVO_NAO_ENCONTRADO;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_CAMINHO_DADOS_VAZIO;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_NOME_PESQUISA_VAZIO;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_PESQUISA_JA_EXISTE;
import static com.rdr.avaliacao.ig.InterfaceConstraints.TEXTO_IMPORTAR_DADOS;
import static com.rdr.avaliacao.ig.InterfaceConstraints.TITULO_ABRIR_ARQUIVO;
import static com.rdr.avaliacao.ig.InterfaceConstraints.TITULO_IMPORTAR_DADOS;

import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.rdr.avaliacao.AvaliacaoInstitucional;
import com.rdr.avaliacao.es.EntradaESaida;
import com.rdr.avaliacao.ig.LookAndFeel;
import com.rdr.avaliacao.ig.PropriedadesDeJanela;
import com.rdr.avaliacao.questionario.Pesquisa;

/**Tela de interface gráfica para que possibilita o usuário possa inserir os dados de uma nova pesquisa
 * e começar a imporação dos dados dela
 * 
 * @author Ramon Giovane
 *
 */
public class IgNovaPesquisa extends JDialog implements PropriedadesDeJanela{
	private  JTextField fieldNomePesquisa, fieldCaminhoDados;
	private static IgNovaPesquisa igPesquisa;
	private static AvaliacaoInstitucional avaliacaoInstitucional;
	
	private IgNovaPesquisa(AvaliacaoInstitucional avaliacao) {
		avaliacaoInstitucional = avaliacao;
		construirIg();

	}

	public static IgNovaPesquisa getInstance(AvaliacaoInstitucional avaliacaoInstitucional) {
		System.out.println(igPesquisa);
		if(igPesquisa == null) {
			igPesquisa = new IgNovaPesquisa(avaliacaoInstitucional);
			return igPesquisa;
		}
		else {
			return igPesquisa;
		}
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



	private void construirIg() {
		LookAndFeel.definirLookAndFeel(this);
		setModal(true);
		setResizable(false);
		setBounds(new Rectangle(0, 0, 340, 255));
		getContentPane().setLayout(null);

		JLabel lblParaImportarOs = new JLabel(TEXTO_IMPORTAR_DADOS);
		lblParaImportarOs.setBounds(10, 129, 330, 48);
		getContentPane().add(lblParaImportarOs);

		fieldNomePesquisa = new JTextField();
		fieldNomePesquisa.setFont(new Font("Tahoma", Font.PLAIN, 11));
		fieldNomePesquisa.setBounds(128, 51, 212, 20);
		getContentPane().add(fieldNomePesquisa);
		fieldNomePesquisa.setColumns(10);
		setSize(470, 250);

		JLabel lblNomeDaPesquisa = new JLabel("Nome da Pesquisa:");
		lblNomeDaPesquisa.setBounds(25, 54, 123, 14);
		getContentPane().add(lblNomeDaPesquisa);

		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				comecarImportacao();
			}


		});
		btnOK.setBounds(350, 184, 89, 23);
		getContentPane().add(btnOK);

		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(IgNovaPesquisa.class.getResource(CAMINHO_IMPORT_ICON)));
		label.setBounds(10, 11, 37, 32);
		getContentPane().add(label);
		setTitle(TITULO_IMPORTAR_DADOS);

		setModal(true);
		getContentPane().setBackground(COR_BACKGROUND);
		setLocationRelativeTo(IgAvaliacaoInstitucional.getInstance());

		JLabel lblDadosASerem = new JLabel("Importar de:");
		lblDadosASerem.setBounds(25, 85, 123, 14);
		getContentPane().add(lblDadosASerem);

		fieldCaminhoDados = new JTextField();
		fieldCaminhoDados.setFont(new Font("Tahoma", Font.PLAIN, 10));

		fieldCaminhoDados.setColumns(10);
		fieldCaminhoDados.setBounds(128, 82, 212, 20);
		getContentPane().add(fieldCaminhoDados);

		JButton btnSelecionar = new JButton("Selecionar...");
		btnSelecionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selecionarArquivo();
			}
		});
		btnSelecionar.setBounds(350, 82, 93, 23);
		getContentPane().add(btnSelecionar);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				esconder();
			}

		});
		
		LookAndFeel.definirBotaoPrincipal(this, btnOK);

	}

	/**Extrai dos campos de texto o nome da pesquisa e o caminho dos dados para a  importação.
	 * Verifica se os dados estão íntegros, adiciona uma pesquisa no banco e começa a importação
	 * dos dados da fonte fornecida.
	 * Exibe mensagens de erro em caso de erro.
	 * */
	private void comecarImportacao() {
		String nomePesquisa = fieldNomePesquisa.getText(),
				caminhoDados = fieldCaminhoDados.getText();
		
		
		
		//Verifica se os campos estão vazios
		if(!verificarCamposVazios(nomePesquisa, caminhoDados)) return;
		
		//Verifica se o arquivo existe
		if(!EntradaESaida.verificarSeArquivoExiste(caminhoDados)) {
			msgErro(this, MSG_ARQUIVO_NAO_ENCONTRADO + caminhoDados, TITULO_ABRIR_ARQUIVO);
			return;
		}
		
		
		Pesquisa pesquisa = new Pesquisa(nomePesquisa);
		pesquisa.setCaminhoDataSet(caminhoDados);
		
		//Verifia se a pesquisa informada já existe
		try {
			if(avaliacaoInstitucional.pesquisaExiste(pesquisa)) {
				msgErro(this, MSG_PESQUISA_JA_EXISTE, TITULO_IMPORTAR_DADOS);
				return;
			}
			
			//Adiciona a pesquisa no banco de dados e na lista de pesquisas na memória
			avaliacaoInstitucional.adicionarPesquisa(pesquisa);
			
			
		} catch (SQLException e1) {
			System.out.println("Problema quando verificando pesquisa");
			e1.printStackTrace();
		}
		
		//Começa a importação de dados.
		try {
			avaliacaoInstitucional.importarDados(pesquisa);
			esconder();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			msgErro(this, MSG_ARQUIVO_NAO_ENCONTRADO + caminhoDados, TITULO_ABRIR_ARQUIVO);
		
		} catch (IOException e) {
			e.printStackTrace();

		} catch (SQLException e) {
			msgErro(this, "Ocorreu um erro durante a importação dos dados da pesquisa. Dados corrompidos ou inconsistentes.", TITULO_IMPORTAR_DADOS);
		}

	}
	private boolean verificarCamposVazios(String nomePesquisa, String caminhoDados) {

		//Verifica se o campo de nome da pesquisa está vazio
		if(nomePesquisa.isEmpty()) {
			msgErro(this, MSG_NOME_PESQUISA_VAZIO, TITULO_IMPORTAR_DADOS);
			return false;
		}
		
		//Verifica se o campo de caminho dos dados está vazio
		if(caminhoDados.isEmpty()) {
			msgErro(this, MSG_CAMINHO_DADOS_VAZIO, TITULO_IMPORTAR_DADOS);
			return false;
		}
		
		return true;
	}

	private void selecionarArquivo() {
		String nomeArquivo = dialogoAbrirArquivo(this, TITULO_ABRIR_ARQUIVO, DESCRICOES_EXTENSOES_CSV, EXTENSOES_CSV);
		fieldCaminhoDados.setText(nomeArquivo);

	}

	public void esconder() {
		fieldNomePesquisa.setText(null);
		fieldCaminhoDados.setText(null);
		setVisible(false);
	}

	public void fechar() {
		dispose();
	}

	

}
