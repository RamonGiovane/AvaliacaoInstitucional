
package com.rdr.avaliacao.ig;

import static com.rdr.avaliacao.ig.InterfaceConstraints.*;
import static com.rdr.avaliacao.ig.InterfaceConstraints.COR_BACKGROUND;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_ERRO_GERAR_RELATORIO;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_NAO_HA_PESQUISAS;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_RELATORIO_SEM_CONEXAO;
import static com.rdr.avaliacao.ig.InterfaceConstraints.TITULO_PROGRAMA;

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

public class IgSeletorRelatorio extends JDialog{
	private  JComboBox<String> comboNomePesquisa, comboTipoGraduacao;
	private static IgSeletorRelatorio igPesquisa;
	private static AvaliacaoInstitucional avaliacao;

	private TipoRelatorio tipoPesquisa;
	private JLabel lblDadosASerem;
	private JLabel lblNomeDaPesquisa;
	private final String[] TIPOS_GRADUACAO = {"Bacharelado", "Licenciatura", "Tecnólogo"};

	private IgSeletorRelatorio(AvaliacaoInstitucional avaliacaoInstitucional, TipoRelatorio tipoPesquisa) {
		this.tipoPesquisa = tipoPesquisa;
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

	public void exibir() {
		if(!avaliacao.checarConexao()) {
			EntradaESaida.msgInfo(this, MSG_RELATORIO_SEM_CONEXAO, InterfaceConstraints.TITULO_PROGRAMA);
			return;
		}

		if(avaliacao.numeroPesquisas() == 0)
			EntradaESaida.msgInfo(this, MSG_NAO_HA_PESQUISAS,InterfaceConstraints.TITULO_PROGRAMA);
		setVisible(true);
	}

	private void resetar(TipoRelatorio tipoPesquisa) {
		this.tipoPesquisa = tipoPesquisa;
		popularCombosBoxes();
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
				abrirRelatorio();
			}

		});
		btnConectar.setBounds(331, 89, 89, 23);
		getContentPane().add(btnConectar);

		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(IgSeletorRelatorio.class.getResource(CAMINHO_ICON_GRAPHIC)));
		label.setBounds(10, 11, 37, 32);
		getContentPane().add(label);
		setTitle(tipoPesquisa.getNomeRelatório());

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
		if(tipoPesquisa.getNomeRelatório().equals(TipoRelatorio.CONCEITO_MEDIO_CURSO.getNomeRelatório())) {
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
			System.out.println("PESQ " + pesquisa);
			
			//Se o relatório for sobre o conceito médio, é preciso chamar a versão sobrecarregada do método que abre IgRelatorio.
			if(tipoPesquisa.equals(TipoRelatorio.CONCEITO_MEDIO_CURSO))
				IgRelatorio.getInstance(tipoPesquisa, pesquisa,
						comboTipoGraduacao.getSelectedItem().toString()).exibir(this);
			else
				IgRelatorio.getInstance(tipoPesquisa, pesquisa).exibir(this);
		}catch (SQLException e) {
			EntradaESaida.msgErro(this, MSG_ERRO_GERAR_RELATORIO + MSG_DETALHES_ERRO, 
			TITULO_PROGRAMA);

		} catch (Exception e) {
			EntradaESaida.msgErro(this, MSG_ERRO_GERAR_RELATORIO, TITULO_PROGRAMA);
			
			//Debug only
			e.printStackTrace();
			return;
		}

 


	}

	public void fechar() {
		dispose();
	}

}

