package com.rdr.avaliacao.questionario;

public class Entrevistado {
	
	private Segmento segmento;
	private String curso;
	private String campus;

	public Entrevistado(Segmento segmento, String curso, String campus) {
		this.segmento = segmento;
		this.curso = curso;
		this.campus = campus;
	}

	public Entrevistado() {	}

	public Segmento getSegmento() {
		return segmento;
	}

	public void setSegmento(Segmento segmento) {
		this.segmento = segmento;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getCampus() {
		return campus;
	}

	public void setCampus(String campus) {
		this.campus = campus;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder(segmento.toString());
		str.append(" - ").append(campus);
		
		if(segmento == Segmento.DISCENTE)
			str.append(" - ").append(curso);
		
		return str.toString();
	}

	
	
	
	
	
	
	
	
	
	
	
	

}
