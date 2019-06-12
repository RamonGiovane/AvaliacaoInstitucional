package com.rdr.avaliacao.questionario;

public class Aluno extends Entrevistado {
	private String curso;
	private String grau;

	public Aluno(String campus, String curso) {
		super(Segmento.DISCENTE, campus);
		this.curso = curso;
	}

	public Aluno() {
		super();
	}



	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}
	
	public String getGrau() {
		return grau;
	}

	public void setGrau(String grau) {
		this.grau = grau;
	}

	@Override
	public String toString() {
		return String.format("%s\nCurso:%s em %s", super.toString(), grau, curso);
	}
	
	
}
