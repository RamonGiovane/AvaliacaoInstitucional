package com.rdr.avaliacao.relatorio;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.rdr.avaliacao.questionario.Assunto;

/**Classe abstrata composta de uma estrutura de dados para armazenar medias de notas*/
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
		Assunto[] assunto = mediasPorAssunto.keySet().toArray(new Assunto[0]);
		Arrays.sort(assunto);
		return assunto;
	}

	/**Retorna a média de notas dos assuntos do curso*/
	public int obterMediaGeral() {
		double soma = 0;
		for (Map.Entry<Assunto, Double> entry : mediasPorAssunto.entrySet()) {
			soma += entry.getValue();
		}
		double media = soma/mediasPorAssunto.size();
		return (int)  Math.round(media);
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
