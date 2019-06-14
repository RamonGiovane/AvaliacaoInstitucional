
package com.rdr.avaliacao.ig;
import org.jfree.chart.plot.PlotOrientation;

public enum TipoRelatorio {
	PARTICIPANTES_POR_CURSO ("Curso", "Relatório de Participantes por Curso", PlotOrientation.HORIZONTAL), 
	PARTICIPANTES_POR_SEGMENTO ("Segmento", "Relatório de Participantes por Segmento",  PlotOrientation.VERTICAL),
	CONCEITO_MEDIO_CURSO ("Curso", "Relatório de Conceitos Médios por Curso", PlotOrientation.HORIZONTAL),
	CONCEITO_MEDIO_ASSUNTO("Assunto", "Relatório de Conceitos Médios por Assunto", PlotOrientation.HORIZONTAL);

	private TipoRelatorio(String temaRelatorio, String descricao, PlotOrientation orientacaoGrafico) {
		this.temaRelatório = temaRelatorio;
		this.orientacaoGrafico = orientacaoGrafico;
		this.descricao = descricao;
	}
	
	private String temaRelatório, descricao;
	private PlotOrientation orientacaoGrafico;

	public String getTemaRelatório() {
		return temaRelatório;
	}

	public void setTemaRelatório(String nomeRelatório) {
		this.temaRelatório = nomeRelatório;
	}

	public PlotOrientation getOrientacaoGrafico() {
		return orientacaoGrafico;
	}

	public void setOrientacaoGrafico(PlotOrientation orientacao) {
		this.orientacaoGrafico = orientacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
	
	
	
	
	
	
	
}
