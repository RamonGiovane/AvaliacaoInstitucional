
package com.rdr.avaliacao.ig;
import org.jfree.chart.plot.PlotOrientation;

public enum TipoRelatorio {
	POR_CURSO ("Relatório de Participantes por Curso", new String[]{"Curso", "Número de Participantes"}, PlotOrientation.HORIZONTAL), 
	POR_SEGMENTO ("Relatório de Participantes por Segmento", new String[]{"Segmento", "Número de Participantes"}, PlotOrientation.VERTICAL),
	CONCEITO_MEDIO_CURSO("Relatório de Conceito Médio por Curso", null, PlotOrientation.HORIZONTAL),
	CONCEITO_MEDIO_ASSUNTO("Relatório de Conceito Médio por Assunto", null, PlotOrientation.HORIZONTAL);

	private TipoRelatorio(String nomeRelatorio, String titulos[], PlotOrientation orientacaoGrafico) {
		this.nomeRelatório = nomeRelatorio;
		this.cabecalhos = titulos;
		this.orientacaoGrafico = orientacaoGrafico;
	}
	
	private String nomeRelatório;
	private String cabecalhos[];
	private PlotOrientation orientacaoGrafico;

	public String getNomeRelatório() {
		return nomeRelatório;
	}

	public void setNomeRelatório(String nomeRelatório) {
		this.nomeRelatório = nomeRelatório;
	}

	public String[] getCabecalhos() {
		return cabecalhos;
	}

	public void setCabecalhos(String[] cabecalhos) {
		this.cabecalhos = cabecalhos;
	}

	public PlotOrientation getOrientacaoGrafico() {
		return orientacaoGrafico;
	}

	public void setOrientacaoGrafico(PlotOrientation orientacao) {
		this.orientacaoGrafico = orientacao;
	}
	
	
	
	
	
	
	
	
}
