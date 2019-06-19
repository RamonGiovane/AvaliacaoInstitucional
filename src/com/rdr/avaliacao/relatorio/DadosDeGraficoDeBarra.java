package com.rdr.avaliacao.relatorio;

/**Representa os valores que compõem um gráfico de barra. Um valor númerico e uma descrição de cada barra*/
public interface DadosDeGraficoDeBarra  {//O nome dessa classe pode não ter ficado da melhor forma possível :|
	/**Valor de uma entrada no gráfico. Deve ser associado a uma descrição*/
	public Number valor();
	
	/**Descrição de uma entrada no gráfico. Deve ser associado a um valor*/
	public String descricao();

}
