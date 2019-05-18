package com.rdr.avaliacao;

import com.rdr.avaliacao.ig.IgAvaliacaoInstitucional;
import com.rdr.avaliacao.ig.IgBancoDeDados;

/**Classe principal do programa de Avalia��o Institucional. Nesta classe encontra-se o m�todo main.
 * Nenhuma outra classe pode instaciar um objeto desta.
 * Portanto, seu funcionamento consiste em fornecer o execut�vel do programa e intermediar as classes de
 * interface gr�fica e modelo de neg�cio com os dados.*/
public class AvaliacaoInstitucional {
	
	private AvaliacaoInstitucional() {
		//Constr�i a janela de conex�o ao banco de dados
		IgBancoDeDados.getInstance();
		
		//Constr�i a janela do menu principal
		IgAvaliacaoInstitucional.getInstance(this);

	}
	
	public static void main(String[] args) {
		new AvaliacaoInstitucional();
	}
	
	public void extrairDados() {
		
	}
}
