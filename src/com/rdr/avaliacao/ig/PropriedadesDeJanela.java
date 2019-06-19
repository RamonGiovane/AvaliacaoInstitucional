package com.rdr.avaliacao.ig;

import java.awt.Component;

/**Define as funcionalidades básicas que uma janela ou caixa de diálogo do programa deve
 * ter.
 * @author Ramon Giovane
 *
 */
public interface PropriedadesDeJanela {
	/**Exibe a janela em relação a janela que a invocou.
	 * 
	 * @param janelaPai {@link Component} que invoca esta janela.
	 */
	public void exibir(Component janelaPai);
	
	/**Define operações e o modo como a janela será fechada*/
	public void esconder();
	
	/**Define operações e o modo como a janela será fechada, liberando os recursos ao sitema operacional*/
	public void fechar();
}
