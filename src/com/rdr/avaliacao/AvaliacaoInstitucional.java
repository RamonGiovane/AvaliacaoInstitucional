package com.rdr.avaliacao;

import com.rdr.avaliacao.ig.IgAvaliacaoInstitucional;
import com.rdr.avaliacao.ig.IgBancoDeDados;

/**Classe principal do programa de Avaliação Institucional. Nesta classe encontra-se o método main.
 * Nenhuma outra classe pode instaciar um objeto desta.
 * Portanto, seu funcionamento consiste em fornecer o executável do programa e intermediar as classes de
 * interface gráfica e modelo de negócio com os dados.*/
public class AvaliacaoInstitucional {
	
	private AvaliacaoInstitucional() {
		//Constrói a janela de conexão ao banco de dados
		IgBancoDeDados.getInstance();
		
		//Constrói a janela do menu principal
		IgAvaliacaoInstitucional.getInstance(this);

	}
	
	public static void main(String[] args) {
		new AvaliacaoInstitucional();
	}
	
	public void extrairDados() {
		
	}
}
