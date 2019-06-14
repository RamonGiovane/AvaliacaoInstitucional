package com.rdr.avaliacao.relatorio;

import com.rdr.avaliacao.questionario.Curso;

public class MediasPorCurso extends MediasDeNotas {
	private Curso curso;

	private static final String SEPARADOR_NOME_CURSO = "em";
	
	public MediasPorCurso(Curso curso) {
		
		super(quebrarNomeCurso(curso.getDescricao()));
		System.err.println(curso.toString());
		this.curso = curso;
	}
	
	public Curso getCurso() {
		return curso;
	}
	
	private static String quebrarNomeCurso(String nomeCurso) {
		try{
			return nomeCurso.split(SEPARADOR_NOME_CURSO)[1].trim();
		}catch (Exception e) {
			return nomeCurso;
		}
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}



}
