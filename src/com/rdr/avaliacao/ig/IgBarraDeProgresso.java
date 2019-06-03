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
	private String texto, titulo;
	private long valorMaximo;
	private Component janelaPai;
	
	public IgBarraDeProgresso(Component janelaPai, String titulo, String texto, long valorMaximo) {
		this.texto = texto;
		this.titulo = titulo;
		this.valorMaximo = valorMaximo;
		this.janelaPai = janelaPai;
		
	/*	SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				construirIg();
			}
		});*/
		
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
		lblTexto.setText(texto);
		lblTexto.setBounds(20, 66, 337, 14);
		getContentPane().add(lblTexto);
		setLocationRelativeTo(janelaPai);
		setSize(386, 150);
		setResizable(false);
		//setUndecorated(true);
		
		setModal(true);
		
		//setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//do nothing;
			}
		});
		
		
	}
	
	public void incrementar(long processado) {
		double porcentagem = (double)processado / valorMaximo * 100;
		System.err.println("METODO INCREMENTAR - START");
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				System.err.println("METODO RUN");
				
				progressBar.setValue((int)porcentagem);
				setVisible(true);
			}
		});
		
		System.err.println("METODO INCREMENTAR - DEPOIS DO RUN");
	}
	
	public void fechar() {
		dispose();
	}
	
	
	public void setTexto(String texto) {
		this.texto = texto;
		lblTexto.setText(texto);
		revalidate();
		repaint();
	}
	
	
}
