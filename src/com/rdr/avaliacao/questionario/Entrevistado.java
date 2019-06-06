package com.rdr.avaliacao.questionario;

public class Entrevistado {
	
	private Segmento segmento;
	
	private String campus;
	
	public Entrevistado(Segmento segmento, String campus) {
		this.segmento = segmento;
		this.campus = campus;
	}

	public Entrevistado() {	}

	public Segmento getSegmento() {
		return segmento;
	}

	public void setSegmento(Segmento segmento) {
		this.segmento = segmento;
	}



	public String getCampus() {
		return campus;
	}

	public void setCampus(String campus) {
		this.campus = campus;
	}

	@Override
	public String toString() {
		return String.format("%s\nCampus: %s", segmento, campus);
	}



	

	
	
	
	
	
	
	
	
	
	
	
	

}
