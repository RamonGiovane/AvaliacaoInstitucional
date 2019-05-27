package com.rdr.avaliacao.questionario;

public enum TipoResposta{
	OTIMO(5, "Ótimo"), BOM(4, "Bom"), SATISFATORIO (3, "Satisfatório"), 
	RUIM(2, "Ruim"), PESSIMO(1, "Péssimo"), INEXISTENTE(0, "Inexistente"), NAO_CONHECO(0, "Não conheço");
	
	private int valor;
	private String descricao;
	
	private TipoResposta(int valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public int getValor() {
		return valor;
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public String toString() {
		return String.format("%d", descricao);
	}
	
}
