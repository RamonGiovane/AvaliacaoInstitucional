package com.rdr.avaliacao.relatorio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.rdr.avaliacao.ig.TipoRelatorio;

public class RelatorioDeParticipantes extends Relatorio{
	private List<DadosDeGrafico> dados;
	
	private final String STR_PARTICIPANTES = "Participantes por ";
	
	public RelatorioDeParticipantes(TipoRelatorio tipoRelatorio){
		super(tipoRelatorio);
		dados = new ArrayList<DadosDeGrafico>();
		
	}
	public<T extends DadosDeGrafico> void adicionar(T t) {
		dados.add(t);	
	}
	
	public DadosDeGrafico obter(int posicao) {
		return dados.get(posicao);
	}
	
	@Override
	public int size() {
		return dados.size();
	}
	
	@Override
	public Object[][] asMatrix() {
		Object[][] matriz = new Object[dados.size()][2];
		for(int i = 0; i<dados.size(); i++) {
			matriz[i][0] = dados.get(i).getValorColuna();
			matriz[i][1] = dados.get(i).getValorLinha();
		}
		
		return matriz;
	}
	
	
	@Override
	public String[] getHeaders() {
		return new String[] {getTipoRelatorio().getTemaRelatório(), "Número de Participantes"};
	}
	
	@Override
	public String title() {
		return STR_RELATORIO + STR_PARTICIPANTES + getTipoRelatorio().getTemaRelatório();
	}
	public void ordenar() {
		dados.sort(new Comparator<DadosDeGrafico>() {

			@Override
			public int compare(DadosDeGrafico dado1, DadosDeGrafico dado2) {
				return dado1.getValorColuna().compareTo(dado2.getValorColuna());
			}
			
		});
	}
	
	
	
	
}
