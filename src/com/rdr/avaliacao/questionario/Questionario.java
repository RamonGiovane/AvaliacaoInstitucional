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
	
	public void adicionarResposta(TipoResposta tipoResposta) {
		Resposta resposta = new Resposta(tipoResposta);
		respostas[indice++] = resposta; 
		
		
	}
	
	
	
	
}
