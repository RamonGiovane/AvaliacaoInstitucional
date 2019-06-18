package com.rdr.avaliacao.ig.janelas;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class IgBarraDeProgresso extends JDialog{
	private JLabel lblTexto;
	private JProgressBar progressBar;
	private String texto1, texto2, texto3, titulo;
	private long valorMaximo;
	private Component janelaPai;

	/**Cria uma caixa de diálgo com uma barra de progresso e um texto descritivo abaixo.
	 * A barra recebe um valor total de processamentos, e então o método <code>incrementar</code>
	 * deve ser chamado. A barra atingirá 100% quando o valor processado for igual ao valor máximo de processamento.
	 * 
	 * @param janePai componente em relação a caixa. Aquele que a invoca. (null permitido).
	 * @param titulo da janela
	 * @param texto mensagem que a aparecerá sob a barra durante o processamento
	 * @param valorMaximo valor total a ser processado
	 */
	public IgBarraDeProgresso(Component janePai, String titulo, String texto, long valorMaximo) {
		this(janePai, titulo, texto, texto, texto, valorMaximo);
	}
	
	/**Cria uma caixa de diálgo com uma barra de progresso e um texto descritivo abaixo.
	 * A barra recebe um valor total de processamentos, e então o método <code>incrementar</code>
	 * deve ser chamado. A barra atingirá 100% quando o valor processado for igual ao valor máximo de processamento.
	 * 
	 * @param janePai componente em relação a caixa. Aquele que a invoca. (null permitido).
	 * @param titulo da janela
	 * @param texto1 mensagem que a aparecerá sob a barra durante o processamento de 0 a 40%
	 * @param texto2 mensagem que a aparecerá sob a barra durante o processamento de 41 a 80%
	 * @param texto3 mensagem que a aparecerá sob a barra durante o processamento de 81 a 100%
	 * @param valorMaximo valor total a ser processado
	 */
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

	/**Constroi a caixa de diálogo da barra*/
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
		setLocationRelativeTo(janelaPai);
		setVisible(true);
		
	}

	/**Incrementa em porcentagem o valor da barra a partir do valor processado em relação ao processamento
	 * total.<br>
	 * <b>Nota: </b> este método utiliza a Event Dispatch Thread (EDT) para atualizar e exibir a barra. A chamada deste
	 * método deve ser feito por outra thread além da que responde a EDT (thread main), senão, um congelamento de tela 
	 * será causado ou efeito esperado não será atigido
	 * @param processado valor processado do total de processamentos.
	 */
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
	
	/**Muda entre os três textos dependendo da porcentagem atual da barra*/
	private void trocarLabel(double porcentagem) {
		if(porcentagem > 40 && porcentagem < 80)
			lblTexto.setText(texto2);
		else if(porcentagem >= 80)
			lblTexto.setText(texto3);
	}

	/**Calcula quanto a barra já progrediu com base no volume total de processamentos e a quantia
	 * já processada.
	 * @param processado número de processamentos realizados até agora com relação ao total
	 * @return
	 */
	private double calcularPorcentagem(long processado) {
		return processado * 100 / valorMaximo;
	}

	public void fechar() {
		dispose();
	}




}
