package com.rdr.avaliacao.questionario;

import com.rdr.avaliacao.relatorio.DadosDeGrafico;

/**Clase para armazenar o segmento de um entrevistado, que pode ser um {@link Segmento#DISCENTE},
 * {@link Segmento#DOCENTE}, {@link Segmento#TECNICO} ou qualquer outro tipo.
 * Implementa a interface {@link DadosDeGrafico} para a geração de relatórios de participantes.
 * 
 * @author Ramon Giovane
 *
 */
public class Segmento implements DadosDeGrafico {
	public static String DISCENTE = "Discente", DOCENTE = "Docente", TECNICO =  "Técnico Administrativo";

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

	public long getQuantidadeEntrevistados() {
		return quantidadeEntrevistados;
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

