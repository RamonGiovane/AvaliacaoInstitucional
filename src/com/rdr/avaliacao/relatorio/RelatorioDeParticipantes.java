package com.rdr.avaliacao.relatorio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.rdr.avaliacao.ig.TipoRelatorio;

/**Classe que encapsula um conjuto de dados que implementam  {@link DadosDeGraficoDeBarra} e implementa operações para gerar relatórios sobre esses dados*/
public class RelatorioDeParticipantes extends Relatorio{
	private List<DadosDeGraficoDeBarra> dados;
	
	private final String STR_PARTICIPANTES = "Participantes por ";
	
	public RelatorioDeParticipantes(TipoRelatorio tipoRelatorio){
		super(tipoRelatorio);
		dados = new ArrayList<DadosDeGraficoDeBarra>();
		
	}
	public<T extends DadosDeGraficoDeBarra> void adicionar(T t) {
		dados.add(t);	
	}
	
	public DadosDeGraficoDeBarra obter(int posicao) {
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
			matriz[i][0] = dados.get(i).descricao();
			matriz[i][1] = dados.get(i).valor();
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
		dados.sort(new Comparator<DadosDeGraficoDeBarra>() {

			@Override
			public int compare(DadosDeGraficoDeBarra dado1, DadosDeGraficoDeBarra dado2) {
				return dado1.descricao().compareTo(dado2.descricao());
			}
			
		});
	}
	
	
	
	
}
