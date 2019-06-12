package com.rdr.avaliacao.questionario;

import java.util.ArrayList;
import java.util.List;

public class Entrevista {
	private  List<Resposta> respostas;
	private Entrevistado entrevistado;
	

	public Entrevista(Entrevistado entrevistado) {
		respostas =  new ArrayList<Resposta>();
	}

	public void adicionarResposta(int nota, String descricaoPergunta, String assuntoPergunta) {

		respostas.add(new Resposta(nota, descricaoPergunta, assuntoPergunta));
	}

	public void adicionarResposta(int nota, String descricaoConceito, String descricaoPergunta, String assuntoPergunta) {

		respostas.add(new Resposta(nota, descricaoConceito, descricaoPergunta, assuntoPergunta));
	}

	public Entrevistado getEntrevistado() {
		return entrevistado;
	}

	public void setEntrevistado(Entrevistado entrevistado) {
		this.entrevistado = entrevistado;
	}






}
