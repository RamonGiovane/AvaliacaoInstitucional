
package com.rdr.avaliacao.ig;
import org.jfree.chart.plot.PlotOrientation;

public enum TipoRelatorio {
	POR_CURSO ("Curso", PlotOrientation.HORIZONTAL), 
	POR_SEGMENTO ("Segmento", PlotOrientation.VERTICAL),
	CONCEITO_MEDIO_CURSO("Conceito Médio por Curso", PlotOrientation.HORIZONTAL),
	CONCEITO_MEDIO_ASSUNTO("Conceito Médio por Assunto", PlotOrientation.HORIZONTAL);

	private TipoRelatorio(String nomeRelatorio, PlotOrientation orientacaoGrafico) {
		this.nomeRelatório = nomeRelatorio;
		this.orientacaoGrafico = orientacaoGrafico;
	}
	
	private String nomeRelatório;
	private PlotOrientation orientacaoGrafico;

	public String getNomeRelatório() {
		return nomeRelatório;
	}

	public void setNomeRelatório(String nomeRelatório) {
		this.nomeRelatório = nomeRelatório;
	}

	public PlotOrientation getOrientacaoGrafico() {
		return orientacaoGrafico;
	}

	public void setOrientacaoGrafico(PlotOrientation orientacao) {
		this.orientacaoGrafico = orientacao;
	}
	
	
	
	
	
	
	
	
}
