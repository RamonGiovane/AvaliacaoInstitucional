package com.rdr.avaliacao.es.bd;

public interface Persistencia {
	/**Especifica o formato da  query SQL utilizada 
	 * para guardar os dados do objeto que implementa este método no banco de dados JDBC.
	 * 
	 * @return uma <code>String</code> contendo uma query SQL.
	 * 
	 * @author Ramon Giovane
	 * @see DAO#inserir(Persistencia)
	 */
	public abstract String insertQuery();
	
	
	/**Especifica quais objetos serão inseridos no banco de dos JDBC, durante uma operação de 
	 * inserção.
	 * 
	 * @return um array de objetos (<code>Object</code>) com os atributos a serem inseridos.
	 * 
	 * @see DAO#inserir(Persistencia)
	 * 
	 * @author Ramon Giovane
	 */
	 public abstract Object[] insertObjects();
	
	
	 public abstract String selectQuery();
	
}
