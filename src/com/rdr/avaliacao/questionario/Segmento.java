package com.rdr.avaliacao.questionario;

import com.rdr.avaliacao.relatorio.DadosDeGrafico;

/**Enumeração que representa os tipos de segmentos dos entrevistados.
 * 
 * @author Ramon Giovane
 *
 */
public class Segmento implements DadosDeGrafico {
	public static String DISCENTE = "Discente";

	private int codigo;
	private String descricao;

	private long quantidadeEntrevistados;

	public Segmento(int codigo, String descricao) {
		super();
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public Segmento(String descricao) {
		this.descricao = descricao;
	}

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
	
	@Override
	public String toString() {
		return descricao;
	}

	public void setQuantidadeEntrevistados(long quantidadeEntrevistados) {
		this.quantidadeEntrevistados = quantidadeEntrevistados;
	}

	public void setQuatidadeEntrevistados(long quatidadeEntrevistados) {
		this.quantidadeEntrevistados = quatidadeEntrevistados;
	}

	@Override
	public Number getValorLinha() {
		return quantidadeEntrevistados;
	}

	@Override
	public String getValorColuna() {
		return descricao;
	}
	
	
	
}

