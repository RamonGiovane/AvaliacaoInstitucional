package com.rdr.avaliacao.ig;

public enum TipoRelatorio {
	POR_CURSO ("Relatório por Curso"), POR_SEGMENTO ("Relatório por Segmento"),
	CONCEITO_MEDIO_CURSO("Relatório de Conceito Médio por Curso"),
	CONCEITO_MEDIO_ASSUNTO("Relatório de Conceito Médio por Assunto");

	private TipoRelatorio(String nomeRelatorio) {
		this.nomeRelatório = nomeRelatorio;
	}
	
	private String nomeRelatório;

	public String getNomeRelatório() {
		return nomeRelatório;
	}

	public void setNomeRelatório(String nomeRelatório) {
		this.nomeRelatório = nomeRelatório;
	}
	
	
}
