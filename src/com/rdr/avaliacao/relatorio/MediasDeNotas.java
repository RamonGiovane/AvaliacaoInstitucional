package com.rdr.avaliacao.relatorio;

import java.util.HashMap;
import java.util.Map;

import com.rdr.avaliacao.questionario.Assunto;

public abstract class MediasDeNotas{
	private Map<Assunto, Double> mediasPorAssunto;
	
	/**Descrição da média corresponde a uma identificação daquela média, por exemplo: 
	 * nome do cuso ou nome do segmento entrevistado*/
	private String descricao;
	
	public MediasDeNotas(String descricao) {
		this.descricao = descricao;
		mediasPorAssunto = new HashMap<Assunto, Double>();
		
	}

	public void adicionar(Assunto assunto, double notaMedia) {
		mediasPorAssunto.put(assunto, notaMedia);
	}

	public double obterNotaMedia(Assunto assunto) {
		return mediasPorAssunto.get(assunto);
	}

	
	public double obterNota(Assunto assunto) {
		return mediasPorAssunto.get(assunto);
	}
	
	public int tamanho() {
		return mediasPorAssunto.size();
	}


	/**Retorna um array de {@link Assunto}, com todos os temas avaliados na pesquisa por esse curso**/
	public Assunto[] obterAssuntos() {
		return mediasPorAssunto.keySet().toArray(new Assunto[0]);
	}

	/**Retorna a média de notas dos assuntos do curso*/
	public double obterMediaGeral() {
		double soma = 0;
		for (Map.Entry<Assunto, Double> entry : mediasPorAssunto.entrySet()) {
			soma += entry.getValue();
		}
		
		return soma / (double) mediasPorAssunto.size();
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
}
