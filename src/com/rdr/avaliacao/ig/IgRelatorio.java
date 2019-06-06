package com.rdr.avaliacao.ig;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.border.BevelBorder;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Component;

import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.plot.PlotOrientation;

import com.rdr.avaliacao.AvaliacaoInstitucional;
import com.rdr.avaliacao.es.EntradaESaida;
import com.rdr.avaliacao.questionario.Pesquisa;
import com.rdr.avaliacao.relatorio.DadosDeGrafico;
import com.rdr.avaliacao.relatorio.DataSet;

import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class IgRelatorio <T extends DadosDeGrafico> extends JDialog implements PropriedadesDeJanela {
	private ButtonGroup buttonGroup =  new ButtonGroup();
	private JRadioButton radioBtnTabela;
	private JRadioButton radioBtinGrafico;
	private JPanel panelDados;
	private static JPanel panelTabela;
	
	private static TipoRelatorio tipoPesquisa;
	private static Pesquisa pesquisa;
	private String tipoGraduacao;
	
	private static IgRelatorio igRelatorio;
	
	private DataSet dadosRelatorio;
	private static String[] titulosRelatorio;
	
 	private static String[] TITULOS_PARTICIPANTES = new String[]{"Curso", "Número de Participantes"};
	private IgRelatorio() {
		//construirIg();
	}
	
	private void definriParametrosRelatorio(TipoRelatorio tipoPesquisa, Pesquisa pesquisa, String tipoGraduacao) {
		System.out.println("def param - pesqu " + pesquisa );
		this.tipoPesquisa = tipoPesquisa;
		IgRelatorio.pesquisa = pesquisa;
		this.tipoGraduacao = tipoGraduacao;
	
	}


	private static IgRelatorio instanciar(TipoRelatorio tipoPesquisa, Pesquisa pesquisa, String tipoGraduacao) {
		if(igRelatorio == null) {
			igRelatorio = new IgRelatorio();
			
		}
		igRelatorio.definriParametrosRelatorio(tipoPesquisa, pesquisa, tipoGraduacao);
		igRelatorio.construirIg();//TODO: Ta errado isso aqui, quebra totalmente o proposito de apenas 1 objeto
		return igRelatorio;
	}
	
	public static IgRelatorio getInstance(TipoRelatorio tipoPesquisa, Pesquisa pesquisa) {
		return instanciar(tipoPesquisa, pesquisa, null);
	}
	
	public static IgRelatorio getInstance(TipoRelatorio tipoPesquisa, Pesquisa pesquisa, String tipoGraduacao) {
		return instanciar(tipoPesquisa, pesquisa, tipoGraduacao);
		
	}
	
	
	private void construirIg() {
		setResizable(false);
		setTitle("Relat\u00F3rio de Autoavalia\u00E7\u00E3o Institucional");
		getContentPane().setLayout(null);
		setSize(715, 583);
		panelDados = new JPanel();
		panelDados.setBorder(new TitledBorder(null, "Dados", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelDados.setBounds(10, 11, 689, 458);
		
		panelDados.setLayout(new BorderLayout(0, 0));
		
		JPanel panelModoExibicao = new JPanel();
		panelModoExibicao.setBorder(new TitledBorder(null, "Modo de Exibi\u00E7\u00E3o", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelModoExibicao.setBounds(10, 481, 689, 64);
		getContentPane().add(panelModoExibicao);
		panelModoExibicao.setLayout(null);
		
		radioBtnTabela = new JRadioButton("Tabela");
		radioBtnTabela.setSelected(true);
		radioBtnTabela.setBounds(165, 19, 109, 23);
		panelModoExibicao.add(radioBtnTabela);
		
		radioBtinGrafico = new JRadioButton("Gr\u00E1fico");
		radioBtinGrafico.setBounds(364, 19, 109, 23);
		panelModoExibicao.add(radioBtinGrafico);
		
		JButton btnGerarPdf = new JButton("Gerar PDF");
		btnGerarPdf.setEnabled(false);
		btnGerarPdf.setBounds(560, 30, 89, 23);
		panelModoExibicao.add(btnGerarPdf);
	
		
		buttonGroup.add(radioBtinGrafico);
		buttonGroup.add(radioBtnTabela);
		
		definirComportamentoRadioButtons();
		
		panelTabela = new JPanel();
		panelTabela.setLayout(new BorderLayout());
		definirComportamentoRadioButtons();
		getContentPane().add(panelDados);
		panelDados.add(panelTabela, BorderLayout.CENTER);
		panelDados.add(panelTabela);	
		
		gerarDadosRelatorio();
		exibirTabela();
	}
	
	
	private void definirComportamentoRadioButtons() {
		radioBtnTabela.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				System.out.println("Hey");
				exibirTabela();
			}
		});
		
		radioBtinGrafico.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				exibirGrafico();
				
			}

		});
		
	
	}
	
	private void gerarDadosRelatorio() {
		try{
			switch(tipoPesquisa){
			case POR_SEGMENTO:
				dadosRelatorio = AvaliacaoInstitucional.gerarRelatorioPorParticipantes(pesquisa);
				titulosRelatorio = TITULOS_PARTICIPANTES;
				break;
				
			default:
				System.out.println("DEFFED :" + tipoPesquisa);
				break;
		}
		
		}catch (SQLException e) {
			System.err.println(e);
		}
		
	}
	

	private void exibirGrafico() {
		panelTabela.removeAll();
		panelTabela.add(EntradaESaida.gerarGraficoBarra3D(dadosRelatorio, "Título", "Curso", "Número de Part",
						PlotOrientation.HORIZONTAL, 200, 400), BorderLayout.CENTER);
		panelDados.add(panelTabela);
		panelTabela.revalidate();
		panelTabela.repaint();
		
		panelTabela.setVisible(true);
		panelDados.setVisible(true);
		
	}
	private void exibirTabela() {
		panelTabela.removeAll();
		JTable tabela = EntradaESaida.gerarTabela(dadosRelatorio.asMatrix(), titulosRelatorio, null, null, 50, 500);
		panelTabela.add(new JScrollPane(tabela), BorderLayout.CENTER);
		panelTabela.add(tabela.getTableHeader(), BorderLayout.NORTH);
		panelTabela.add(tabela);
		panelDados.add(panelTabela);
		tabela.setVisible(true);
		
		panelTabela.revalidate();
		panelTabela.repaint();
		panelTabela.setVisible(true);
		panelDados.setVisible(true);
		
		
		
	}

	@Override
	public void exibir(Component janelaPai) {
		setLocationRelativeTo(janelaPai);
		setVisible(true);
	}

	@Override
	public void esconder() {
		setVisible(false);
	}

	@Override
	public void fechar() {
		dispose();
	}

	
	
}
