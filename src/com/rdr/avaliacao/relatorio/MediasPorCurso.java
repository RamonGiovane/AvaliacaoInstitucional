package com.rdr.avaliacao.relatorio;

import com.rdr.avaliacao.questionario.Curso;

public class MediasPorCurso extends MediasDeNotas {
	private Curso curso;

	public MediasPorCurso(Curso curso) {
		
		super(curso.getDescricao());
		System.err.println(curso.toString());
		this.curso = curso;
	}
	
	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}



}
