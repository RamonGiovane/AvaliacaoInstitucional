package com.rdr.avaliacao.relatorio;

import com.rdr.avaliacao.ig.TipoRelatorio;

public abstract class Relatorio {
	
	private TipoRelatorio tipoRelatorio;
	
	protected final String STR_RELATORIO = "Relatório de ";
	
	public Relatorio(TipoRelatorio tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	/**Transforma os dados de uma relatório em uma matriz de objetos, tornando 
	 * propícia a população de tabelas.
	 * @return uma matriz de <code>Object</code>.
	 */
	public abstract Object[][] asMatrix();
	
	/**Retorna a quantidade de dados armazenados no relatório. */
	public abstract int size();
	
	/**Retorna um array de Strings contendo os cabeaçlhos dos dados representados em uma matriz*/
	public abstract String[] getHeaders();
	
	/**Retorna um título associado ao relatório*/
	public abstract String title();

	
	public TipoRelatorio getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(TipoRelatorio tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}
	
	
	
	
	
}
