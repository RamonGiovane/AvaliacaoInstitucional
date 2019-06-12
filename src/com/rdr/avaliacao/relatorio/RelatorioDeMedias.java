package com.rdr.avaliacao.relatorio;

import java.util.ArrayList;
import java.util.List;

import com.rdr.avaliacao.ig.TipoRelatorio;
import com.rdr.avaliacao.questionario.Assunto;

public class RelatorioDeMedias extends Relatorio{
	private List<MediasDeNotas> listaDeMedias;
	private String cabecalhos[];
	private TipoRelatorio tipoRelatorio;
	
	public RelatorioDeMedias(TipoRelatorio tipoRelatorio) {
		super(tipoRelatorio);
		listaDeMedias = new ArrayList<MediasDeNotas>();
	}
	
	public void adicionar(MediasDeNotas medias) {
		listaDeMedias.add(medias);
	}

	@Override
	public Object[][] asMatrix() {
		
		System.out.println(listaDeMedias.get(0).obterAssuntos().length);
		System.out.println(listaDeMedias.size());
		Assunto[] assuntos = listaDeMedias.get(0).obterAssuntos();
		Object[][] matriz = new Object[listaDeMedias.get(0).obterAssuntos().length+1][listaDeMedias.size()+1];
		
		cabecalhos = new String[listaDeMedias.size()+1];
		
		for(int i = 0; i<assuntos.length; i++) {
			matriz[i][0] = assuntos[i];
			for(int c = 0; c < listaDeMedias.size(); c++) {
				matriz[i][c+1] = listaDeMedias.get(c).obterNota(assuntos[i]);
			}
		}
		
		matriz[assuntos.length][0] = "Conceito Médio Geral: ";
		for(int i = 0; i<listaDeMedias.size(); i++)
			matriz[assuntos.length][i+1] = listaDeMedias.get(i).obterMediaGeral(); 
		
			
		return matriz;
	}
	
	@Override
	public String title() {
		return "Relatório de " + getTipoRelatorio();
	}

	public String[] getHeaders() {
		int tamanho = size();
		String[] cabecalhos = new String[tamanho+1];
		cabecalhos[0] =  "Assuntos Avaliados";
		for(int i=0; i<tamanho; i++) {
			cabecalhos[i+1] = listaDeMedias.get(i).getDescricao();
		}
		
		return cabecalhos;
	}

	@Override
	public int size() {
		return listaDeMedias.size();
	}

	public TipoRelatorio getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(TipoRelatorio tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	
}
