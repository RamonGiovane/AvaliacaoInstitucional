package com.rdr.avaliacao.questionario;

public class Pergunta{
	private String descricao;
	private String assunto;
	
	
	public Pergunta(String descricao, String assunto) {
		this.descricao = descricao;
		this.assunto = assunto;
	}

	public Pergunta() {	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	@Override
	public String toString() {
		return String.format("[%s]: %s", assunto, descricao);
	}
	/*
	@Override
	public int compare(Pergunta pergunta1, Pergunta pergunta2) {
		int comparacao = Integer.compare(pergunta1.resposta.getValor(),
				pergunta2.resposta.getValor());
		if(comparacao == 0)
			comparacao = pergunta1.resposta.getDescricao().compareTo(
					pergunta2.resposta.getDescricao());
		return comparacao;
	}*/


	
	
	
	
	
	
	
	
	
}
