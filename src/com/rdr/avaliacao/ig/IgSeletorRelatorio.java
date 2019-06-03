
package com.rdr.avaliacao.ig;

import static com.rdr.avaliacao.ig.InterfaceConstraints.*;
import static com.rdr.avaliacao.ig.InterfaceConstraints.COR_BACKGROUND;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.event.ListDataListener;

import com.rdr.avaliacao.AvaliacaoInstitucional;
import com.rdr.avaliacao.es.EntradaESaida;
import com.rdr.avaliacao.es.bd.BancoDeDados;
import com.rdr.avaliacao.es.bd.DAO;
import com.rdr.avaliacao.questionario.Pesquisa;

public class IgSeletorRelatorio extends JDialog{
	private  JComboBox<String> comboNomePesquisa, comboTipoGraduacao;
	private static IgSeletorRelatorio igPesquisa;
	private static AvaliacaoInstitucional avaliacao;
	
	private TipoPesquisa tipoPesquisa;
	private JLabel lblDadosASerem;
	private JLabel lblNomeDaPesquisa;
	private final String[] TIPOS_GRADUACAO = {"Bacharelado", "Licenciatura", "Tecnólogo"};
	
	private IgSeletorRelatorio(AvaliacaoInstitucional avaliacaoInstitucional, TipoPesquisa tipoPesquisa) {
		this.tipoPesquisa = tipoPesquisa;
		avaliacao = avaliacaoInstitucional;
		construirIg();
		popularCombosBoxes();

	}

	public static IgSeletorRelatorio getInstance(AvaliacaoInstitucional avaliacaoInstitucional, TipoPesquisa tipoPesquisa) {
		if(igPesquisa == null) {
			igPesquisa = new IgSeletorRelatorio(avaliacaoInstitucional, tipoPesquisa);
			return igPesquisa;
		}
		else {
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
		if(tipoPesquisa.getNomeRelatório().equals(TipoPesquisa.CONCEITO_MEDIO_CURSO.getNomeRelatório())) {
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

	public void fechar() {
		dispose();
	}

}

