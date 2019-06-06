package com.rdr.avaliacao.questionario;

public class Questionario {
	private Resposta respostas[];
	private Entrevistado entrevistado;
	private int indice = 0;
	
	public Questionario(int numeroDeRespostas) {
		respostas = new Resposta[numeroDeRespostas];
	}

	public Questionario(Entrevistado entrevistado, int numeroDeRespostas) {
		this(numeroDeRespostas);
		this.entrevistado = entrevistado;
	}
	
	public void adicionarResposta(Resposta resposta) {
		respostas[indice++] = resposta; 
	}

	public Entrevistado getEntrevistado() {
		return entrevistado;
	}

	public void setEntrevistado(Entrevistado entrevistado) {
		this.entrevistado = entrevistado;
	}
	
	
	
	
	
	
}
