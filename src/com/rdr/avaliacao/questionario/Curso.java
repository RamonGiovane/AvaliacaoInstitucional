package com.rdr.avaliacao.questionario;

import com.rdr.avaliacao.relatorio.DadosDeGrafico;

public class Curso implements DadosDeGrafico {
	//TODO: Refatorar esta classe, está incompleta!
	private String descricao;
	private int codigo;
	long quantidadeEntrevistados;
	
	
	public Curso(int codigo, String descricao) {
		super();
		this.descricao = descricao;
		this.codigo = codigo;
	}
	
	public Curso() {}

	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public long getQuantidadeEntrevistados() {
		return quantidadeEntrevistados;
	}
	public void setQuantidadeEntrevistados(long resultado) {
		this.quantidadeEntrevistados = resultado;
	}

	@Override
	public Number getValorLinha() {
		return quantidadeEntrevistados;
	}

	@Override
	public String getValorColuna() {
		return descricao;
	}

	@Override
	public String toString() {
		return String.format("%s", descricao);
	}
	
	
	
	

}
