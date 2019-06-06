package com.rdr.avaliacao.questionario;

public class Conceito{
	private String descricao;
	private int valor;
	
	public Conceito(int valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public Conceito() {}
	
	public int getValor() {
		return valor;
	}

	public String getDescricao() {
		return descricao;
	}
	

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return String.format("%d: %s", valor, descricao);
	}
	
}
