package com.rdr.avaliacao.relatorio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DataSet {
	private List<DadosDeGrafico> dados;
	
	public DataSet(){
		dados = new ArrayList<DadosDeGrafico>();
		
	}
	public<T extends DadosDeGrafico> void adicionar(T t) {
		dados.add(t);	
	}
	
	public DadosDeGrafico ober(int posicao) {
		return dados.get(posicao);
	}
	
	public int tamanho() {
		return dados.size();
	}
	
	public Object[][] asMatrix() {
		Object[][] matriz = new Object[dados.size()][2];
		for(int i = 0; i<dados.size(); i++) {
			matriz[i][0] = dados.get(i).getValorColuna();
			matriz[i][1] = dados.get(i).getValorLinha();
		}
		
		return matriz;
	}
	
	public void ordenarPorDescricao( ) {
		dados.sort(new Comparator<DadosDeGrafico>() {

			@Override
			public int compare(DadosDeGrafico dado1, DadosDeGrafico dado2) {
				return dado1.getValorColuna().compareTo(dado2.getValorColuna());
			}
			
		});
	}
	
	public void ordenarPorValor( ) {
		dados.sort(new Comparator<DadosDeGrafico>() {

			@Override
			public int compare(DadosDeGrafico dado1, DadosDeGrafico dado2) {
				try {
					return Long.compare((long)dado1.getValorLinha(), (long)dado2.getValorLinha());
				}catch (NumberFormatException e) {
					return Double.compare((double)dado1.getValorLinha(), (double)dado2.getValorLinha());
					
				}
			}
			
		});
	}
}
