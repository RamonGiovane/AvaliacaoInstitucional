package com.rdr.avaliacao.ig;

import javax.swing.JDialog;
import javax.swing.JProgressBar;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class IgBarraDeProgresso extends JDialog{
	private JLabel lblTexto;
	private JProgressBar progressBar;
	private String texto1, texto2, texto3, titulo;
	private long valorMaximo;
	private Component janelaPai;

	public IgBarraDeProgresso(Component janePai, String titulo, String texto, long valorMaximo) {
		this(janePai, titulo, texto, texto, texto, valorMaximo);
	}
	
	public IgBarraDeProgresso(Component janelaPai, String titulo, String texto1,String texto2, String texto3,
			long valorMaximo) {
		this.texto1 = texto1;
		this.texto2 = texto2;
		this.texto3 = texto3;
		
		this.titulo = titulo;
		this.valorMaximo = valorMaximo;
		this.janelaPai = janelaPai;


		construirIg();

	}

	private void construirIg() {
		getContentPane().setLayout(null);

		progressBar = new JProgressBar(0, 100);
		progressBar.setBounds(10, 33, 347, 22);
		getContentPane().add(progressBar);

		lblTexto = new JLabel();
		lblTexto.setHorizontalAlignment(SwingConstants.CENTER);
		lblTexto.setFont(new Font("Tahoma", Font.PLAIN, 12));
		setTitle(titulo);
		lblTexto.setText(texto1);
		lblTexto.setBounds(20, 66, 337, 14);
		getContentPane().add(lblTexto);
		setLocationRelativeTo(janelaPai);
		setSize(386, 127);
		progressBar.setMaximum(100);
		setResizable(false);
		//setUndecorated(true);

		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//do nothing;
			}
		});
		
		progressBar.setIndeterminate(false);
		setLocationRelativeTo(getParent().getParent());
		setVisible(true);
		
	}

	public void incrementar(long processado) {

		
		double porcentagem = calcularPorcentagem(processado);

		trocarLabel(porcentagem);
		
		setModal(true);
		setVisible(true);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
			
				progressBar.setString((int)porcentagem + " %");
				progressBar.setValue((int)porcentagem);
				progressBar.setStringPainted(true);
				
				
			}
		});
		
		
	}
	
	private void trocarLabel(double porcentagem) {
		if(porcentagem > 40 && porcentagem < 80)
			lblTexto.setText(texto2);
		else if(porcentagem >= 80)
			lblTexto.setText(texto3);
	}

	private double calcularPorcentagem(long processado) {
		return processado * 100 / valorMaximo;
	}

	public void fechar() {
		dispose();
	}




}
