package com.rdr.avaliacao.questionario;

/**Representa um entrevistado, composto por um {@link Segmento} e um campus.
 * @author Ramon Giovane
 *
 */
public class Entrevistado {
	
	private Segmento segmento;
	private String campus;
	
	public Entrevistado(Segmento segmento, String campus) {
		this.segmento = segmento;
		this.campus = campus;
	}

	public Entrevistado() {	}

	public Entrevistado(String campus, String segmento) {
		this(new Segmento(segmento), campus);
	}

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
