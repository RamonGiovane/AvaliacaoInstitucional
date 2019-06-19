
package com.rdr.avaliacao.ig;
import org.jfree.chart.plot.PlotOrientation;

/**Enumeração contendo todos os possíveis tipos de relatório e suas propriedades,
 * tema do relatório, descrição e orientação dos gráficos exibidos por esse relatório
 * 
 * @author Ramon Giovane
 *
 */
public enum TipoRelatorio {
	//Devia migrar essas propriedades para a classe abstrata Relatorio, mas não dará tempo de refatorar
	PARTICIPANTES_POR_CURSO ("Curso", "Relatório de Participantes por Curso", PlotOrientation.HORIZONTAL), 
	PARTICIPANTES_POR_SEGMENTO ("Segmento", "Relatório de Participantes por Segmento",  PlotOrientation.VERTICAL),
	CONCEITO_MEDIO_CURSO ("Curso", "Relatório de Conceitos Médios por Curso", PlotOrientation.HORIZONTAL),
	CONCEITO_MEDIO_ASSUNTO("Assunto", "Relatório de Conceitos Médios por Segmentos", PlotOrientation.HORIZONTAL);

	private TipoRelatorio(String temaRelatorio, String descricao, PlotOrientation orientacaoGrafico) {
		this.temaRelatório = temaRelatorio;
		this.orientacaoGrafico = orientacaoGrafico;
		this.descricao = descricao;
	}
	
	private String temaRelatório, descricao;
	private PlotOrientation orientacaoGrafico;

	
	/**Obtém o tema do tipo de relatório tratado*/
	public String getTemaRelatório() {
		return temaRelatório;
	}

	/**Define o tema do tipo de relatório tratado*/
	public void setTemaRelatório(String nomeRelatório) {
		this.temaRelatório = nomeRelatório;
	}

	/**Obtém a orientação dos gráfico gerados por esse tipo de relatório.
	 * @see PlotOrientation*/
	public PlotOrientation getOrientacaoGrafico() {
		return orientacaoGrafico;
	}

	/**Define a orientação dos gráfico gerados por esse tipo de relatório.
	 * @see PlotOrientation*/
	public void setOrientacaoGrafico(PlotOrientation orientacao) {
		this.orientacaoGrafico = orientacao;
	}

	/**Obtém a descrição completa do tipo de relatório tratado*/
	public String getDescricao() {
		return descricao;
	}

	/**Define a descrição completa do tipo de relatório tratado*/
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
	
	
	
	
	
	
	
}
