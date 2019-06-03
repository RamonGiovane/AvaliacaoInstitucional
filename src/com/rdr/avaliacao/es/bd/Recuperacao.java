package com.rdr.avaliacao.es.bd;

public interface Recuperacao {
	/**Especifica o formato da  query SQL utilizada 
	 * para recuperar os dados de um objeto de uma classe que implementa este método no banco de dados JDBC.
	 * <br>
	 * @return uma <code>String</code> contendo uma query SQL.
	 * 
	 * @author Ramon Giovane
	 * @see DAO#consultar(Recuperacao)
	 * @see Persistencia
	 */
	public abstract String selectQuery();
	
	/**Especifica quais objetos serão utilizados como chave de busca
	 * para recuperar os dados de um objeto de uma classe que implementa este método no banco de dados JDBC.
	 * Se não houver nenhuma chave de busca, este método deve retornar <code>null</code>. 
	 * <br>
	 * Quando utilizado com o método {@link DAO#consultar(Recuperacao)}, a consulta SQL utilizará os termos de busca
	 * especificados por este método para retornar todos os dados em array de <code>Object</code>.<br>

	 * @return um array de obejtos (<code>Object</code>) contendo o resultado da consulta SQL.
	 * 
	 * @author Ramon Giovane
	 * @see DAO#consultar(Recuperacao)
	 * @see Persistencia
	 */
	public abstract Object[] searchKeys();
}
