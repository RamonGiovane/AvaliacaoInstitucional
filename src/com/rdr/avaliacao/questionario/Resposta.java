package com.rdr.avaliacao.questionario;

public class Resposta {
	private Conceito conceito;
	private Pergunta pergunta;
	
	public Resposta(int nota, String descricaoConceito,  String descricaoPergunta,
			String assuntoPergunta) {
		
		conceito = new Conceito(nota, descricaoConceito);
		pergunta  = new Pergunta(descricaoPergunta, assuntoPergunta);
	}
	
	public Resposta(int nota, String descricaoPergunta,
			String assuntoPergunta) {
		
		conceito = new Conceito(nota);
		pergunta  = new Pergunta(descricaoPergunta, assuntoPergunta);
	}

	public Resposta() {	
		pergunta = new Pergunta();
	}

	public Conceito getConceito() {
		return conceito;
	}

	public void setConceito(Conceito conceito) {
		this.conceito = conceito;
	}
	
	public void adicionarPergunta(String descricaoPergunta, String assuntoPergunta) {
		pergunta.setAssunto(assuntoPergunta);
		pergunta.setDescricao(descricaoPergunta);
		
	}

	public String obterDescricaoPergunta() {
		return pergunta.getDescricao();
	}
	
	public String obterAssuntoPergunta() {
		return pergunta.getAssunto();
	}
	
	
}
