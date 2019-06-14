
package com.rdr.avaliacao.ig;

import static com.rdr.avaliacao.ig.InterfaceConstraints.*;
import static com.rdr.avaliacao.ig.InterfaceConstraints.COR_BACKGROUND;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_ERRO_GERAR_RELATORIO;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_NAO_HA_PESQUISAS;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_RELATORIO_SEM_CONEXAO;
import static com.rdr.avaliacao.ig.InterfaceConstraints.TITULO_PROGRAMA;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import com.rdr.avaliacao.AvaliacaoInstitucional;
import com.rdr.avaliacao.es.EntradaESaida;
import com.rdr.avaliacao.questionario.Pesquisa;

public class IgSeletorRelatorio extends JDialog implements PropriedadesDeJanela{
	private  JComboBox<String> comboNomePesquisa, comboTipoGraduacao;
	private static IgSeletorRelatorio igPesquisa;
	private static AvaliacaoInstitucional avaliacao;

	private TipoRelatorio tipoRelatorio;
	private JLabel lblDadosASerem;
	private JLabel lblNomeDaPesquisa;
	private final String[] TIPOS_GRADUACAO = {"Bacharelado", "Licenciatura", "Técnicos e Tecnólogos"};

	private IgSeletorRelatorio(AvaliacaoInstitucional avaliacaoInstitucional, TipoRelatorio tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
		avaliacao = avaliacaoInstitucional;
		construirIg();
		popularCombosBoxes();

	}

	public static IgSeletorRelatorio getInstance(AvaliacaoInstitucional avaliacaoInstitucional, TipoRelatorio tipoPesquisa) {
		if(igPesquisa == null) {
			igPesquisa = new IgSeletorRelatorio(avaliacaoInstitucional, tipoPesquisa);
			return igPesquisa;
		}
		else {
			igPesquisa.resetar(tipoPesquisa);
			return igPesquisa;
		}
	}

	/**Define o título da janela de acordo com o tipo de relatório ativo*/
	private void definirTituloJanela() {
		setTitle(tipoRelatorio.getDescricao());
	}
	
	/**Realiza alterações necesárias na janela e exibe na tela a caixa de diálogo de seleção de relatórios.*/
	@Override
	public void exibir(Component janelaPai) {
		
		//Checa se há o mínimo de requisitos necessários para gerar relatório 
		if(!checarDadosDisponiveis()) return;
		
		//Alterando o título da janela
		definirTituloJanela();
		
		//Define a pesquisa ativa como item de pesquisa selecionado
		definirPesquisaAtiva();
		
		//Exibe de fato
		setVisible(true);
	}

	/**Define a pesquisa selecionada como a ativa a partir deste momento.*/
	private void definirPesquisaAtiva() {
		comboNomePesquisa.setSelectedItem(avaliacao.getPesquisaAtiva().getNome());
	}

	/**Checa se existe uma conexão com o  banco de dados e há pesquisas disponíveis para gerar relatórios.
	 * Exibe mensagens de informação ao usuário quanto a esses quesitos.
	 * @return false se não houver dados disponíveis, true se há.
	 */
	private boolean checarDadosDisponiveis() {

		if(!avaliacao.checarConexaoBancoDeDados()) {
			EntradaESaida.msgInfo(this, MSG_RELATORIO_SEM_CONEXAO, InterfaceConstraints.TITULO_PROGRAMA);
			return false;
		}

		if(avaliacao.numeroPesquisas() == 0) {
			EntradaESaida.msgInfo(this, MSG_NAO_HA_PESQUISAS,InterfaceConstraints.TITULO_PROGRAMA);
			return false;
		}
		
		return true;
	}

	private void resetar(TipoRelatorio tipoPesquisa) {
		this.tipoRelatorio = tipoPesquisa;
		popularCombosBoxes();
		comboNomePesquisa.setSelectedItem(avaliacao.getPesquisaAtiva());
	}
	

	private void construirIg() {
		Aparencia.definirLookAndFeel(this);
		setModal(true);
		setResizable(false);
		setBounds(new Rectangle(0, 0, 340, 255));
		getContentPane().setLayout(null);

		comboNomePesquisa = new JComboBox<String>();
		comboNomePesquisa.setFont(new Font("Tahoma", Font.PLAIN, 11));
		comboNomePesquisa.setBounds(128, 55, 193, 20);
		getContentPane().add(comboNomePesquisa);
		setSize(438, 165);

		lblNomeDaPesquisa = new JLabel("Nome da Pesquisa:");
		lblNomeDaPesquisa.setBounds(25, 60, 123, 14);
		getContentPane().add(lblNomeDaPesquisa);



		JButton btnConectar = new JButton("OK");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				IgAvaliacaoInstitucional.mudarCursor(Cursor.WAIT_CURSOR);
				abrirRelatorio();
				avaliacao.setPesquisaAtiva(comboNomePesquisa.getSelectedItem().toString());
			}

		});
		btnConectar.setBounds(331, 89, 89, 23);
		getContentPane().add(btnConectar);

		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(IgSeletorRelatorio.class.getResource(CAMINHO_ICON_GRAPHIC)));
		label.setBounds(10, 11, 37, 32);
		getContentPane().add(label);
		

		setModal(true);
		getContentPane().setBackground(COR_BACKGROUND);
		setLocationRelativeTo(IgAvaliacaoInstitucional.getInstance());

		lblDadosASerem = new JLabel("Tipo de Graduação:");
		lblDadosASerem.setBounds(25, 95, 123, 14);
		getContentPane().add(lblDadosASerem);

		comboTipoGraduacao = new JComboBox<String>();
		comboTipoGraduacao.setFont(new Font("Tahoma", Font.PLAIN, 11));
		comboTipoGraduacao.setBounds(128, 90, 193, 20);
		comboTipoGraduacao.setModel(new DefaultComboBoxModel<String>(TIPOS_GRADUACAO));
		getContentPane().add(comboTipoGraduacao);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				esconder();
			}

		});


	}


	private void popularCombosBoxes() {
		popularComboBoxNomePesquisa();
		if(tipoRelatorio.getTemaRelatório().equals(TipoRelatorio.CONCEITO_MEDIO_CURSO.getTemaRelatório())) {
			comboTipoGraduacao.setVisible(true);
			lblDadosASerem.setVisible(true);



		}
		else {
			comboTipoGraduacao.setVisible(false);
			lblDadosASerem.setVisible(false);


		}
	}

	private void popularComboBoxNomePesquisa() {
		String[] pesquisas = avaliacao.listarPesquisas();
		comboNomePesquisa.setModel(new DefaultComboBoxModel<String>(pesquisas));


	}

	public void esconder() {

		setVisible(false);
	}
	

	private void abrirRelatorio() {
		//Esconde a janela de seleção de relatório
		esconder();

		try {
			Pesquisa pesquisa = avaliacao.obterPesquisa(comboNomePesquisa.getSelectedItem().toString());
			
			//Se o relatório for sobre o conceito médio, é preciso chamar a versão sobrecarregada do método que abre IgRelatorio.
			if(tipoRelatorio.equals(TipoRelatorio.CONCEITO_MEDIO_CURSO))
				IgRelatorio.getInstance(tipoRelatorio, pesquisa,
						comboTipoGraduacao.getSelectedItem().toString()).exibir(this);
			else
				IgRelatorio.getInstance(tipoRelatorio, pesquisa).exibir(this);
		}catch (SQLException e) {
			EntradaESaida.msgErro(this, MSG_ERRO_GERAR_RELATORIO + MSG_DETALHES_ERRO, 
			TITULO_PROGRAMA);
			
			//TODO: Debug only
			e.printStackTrace();
			
			return;
		}catch (NullPointerException e) {
			EntradaESaida.msgInfo(this, MSG_ERRO_NENHUM_RELATORIO_GERADO, TITULO_PROGRAMA);
			
			//TODO: Debug only
			e.printStackTrace();
			
			return;
			
		} catch (Exception e) {
			EntradaESaida.msgErro(this, MSG_ERRO_GERAR_RELATORIO, TITULO_PROGRAMA);
			
			//TODO: Debug only
			e.printStackTrace();
			return;
			
		}finally{
			IgAvaliacaoInstitucional.mudarCursor(Cursor.DEFAULT_CURSOR);
		}

 


	}

	public void fechar() {
		dispose();
	}

}

