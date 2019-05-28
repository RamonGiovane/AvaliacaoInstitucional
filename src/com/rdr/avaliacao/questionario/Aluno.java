package com.rdr.avaliacao.questionario;

public class Aluno extends Entrevistado {
	private String curso;
	private String grau;

	public Aluno(Segmento segmento, String campus, String curso, String grau) {
		super(segmento, campus);
		this.curso = curso;
		this.grau = grau;
	}

	public Aluno() {
		super();
	}

	public Aluno(Segmento segmento, String campus) {
		super(segmento, campus);
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
