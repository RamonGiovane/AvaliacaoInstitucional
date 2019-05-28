package com.rdr.avaliacao.questionario;

/**Enumeração que representa os tipos de segmentos dos entrevistados.
 * 
 * @author Ramon Giovane
 *
 */
public enum Segmento {
	DISCENTE("Discente"), DOCENTE("Docente"), TECNICO_ADMINISTRATIVO("Técnico Administrativo da Educação");

	private String descricao;

	private Segmento(String descricao) {
		this.descricao = descricao;


	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**Retorna um segmento que coincida com a descrição do segmento passda por parâmetro.
	 * @param descricao descrição do segmento
	 * @return um objeto enum de com.rdr.avaliacao.{@link Segmento}*/
	public static Segmento parseSegmento(String descricao) {
		for (Segmento segmento : Segmento.values()) {
			if (segmento.descricao.equalsIgnoreCase(descricao)) {
				return segmento;
			}
		}
		return null;
	}


	@Override
	public String toString() {
		return descricao;
	}


}
