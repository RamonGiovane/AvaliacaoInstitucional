package com.rdr.avaliacao.relatorio;

import com.rdr.avaliacao.questionario.Segmento;

/**Classe que estende {@link MediasDeNotas} composta de uma estrutura de dados para armazenar medias de notas de entrevistados relacionados a um segmento*/
public class MediasPorSegmento extends MediasDeNotas {
	private Segmento segmento;


	public MediasPorSegmento(Segmento segmento) {
		super(segmento.getDescricao());
		this.segmento = segmento;
	}

	public Segmento getSegmento() {
		return segmento;
	}


	public void setSegmento(Segmento segmento) {
		this.segmento = segmento;
	}



}
