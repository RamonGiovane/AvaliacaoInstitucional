package com.rdr.avaliacao.es.bd;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

/**Classe que realiza o trabalho de execução de querys SQL ao Banco de Dados.
 * Trabalha em conjunto com a classe {@link BancoDeDados}.
 * 
 * @author Ramon Giovane
 *
 */
public class DAO {
	private static BancoDeDados bd;
	private final String ERRO_PREPARAR_QUERY = "Não foi possível preparar a query SQL.",
			ERRO_PREPARAR_FUNCAO = "Não foi possível preparar a stored procedure.",
			STR_ABRE_CALL = " { call ", STR_FECHA_CALL = " } ", STR_ABRE_PARENTESES = "( ", 
			STR_FECHA_PARENTESES = ")", STR_VALUES = " values", STR_INTERROGACAO = "?",
			STR_VIRGULA = ", ", STR_INSERT_INTO = "insert into " ;
	
	
	/**Instancia um objeto da classe, recebendo um objeto {@link BancoDeDados} contendo uma conexão ativa
	 * @see BancoDeDados
	 * */
	public DAO(BancoDeDados bd) {
		DAO.bd = bd;
	}
	
	/**Obtém o objeto de conexão ao banco relacionado à classe.
	 * 
	 * @return um objeto {@link BancoDeDados}
	 */
	public BancoDeDados getBd() {
		return bd;
	}
	/**Define o objeto de conexão ao banco relacionado à classe.
	 * 
	 * @param bd um objeto {@link BancoDeDados} com uma conexão ativa
	 */
	public void setBd(BancoDeDados bd) {
		DAO.bd = bd;
	}
	
	/**Prepara a chamada para a execução de uma instrução SQL no banco.
	 * 
	 * @param query instrução SQL
	 * @return um {@link PreparedStatement} com a query pronta para ser executada
	 * @throws SQLException se ocorrer um erro na preparação ou a querry estiver mal formada
	 */
	private PreparedStatement prepareStatement(String query) throws SQLException {
		return bd.getConnection().prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
	}
	
	/**Prepara a chamada para a execução de uma função armazenada no banco.
	 * 
	 * @param query instrução SQL
	 * @return um {@link PreparedStatement} com a query pronta para ser executada
	 * @throws SQLException se ocorrer um erro na preparação ou a querry estiver mal formada
	 */
	private CallableStatement prepareCall(String query) throws SQLException {
		return bd.getConnection().prepareCall(query,  ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
	}
	
	/** Insere objetos quaisquer de tipos quaisquer (<code>Object</code>) ao banco de dados.<br><br>
	 * <i>A versão sobrecarregada deste metódo pode ser levemente mais eficiente que esta.</i><br>
	 * 
	 * <br>
	 * <b>Nota:</b> este método é <b>seguro</b> contra injeções de SQL.
	 *  
	 * <br>
	 * <b>Nota:</b> o uso deste método é recomendado para a inserção de objetos <code>String</code>,
	 *  <code>Integer</code>, <code>Float</code>, etc...
	 *  O uso para objetos não <code>String</code> ou que não pertencem às classes empacotadoras de tipo é desencorajado.
	 *  Apenas objetos <code>String </code> podem ser passados como <code>null</code>.
	 * 
	 * <br>
	 * 
	 * @param nomeTabela nome da tabela do banco de dados onde será feita a inserção
	 * @param nomeDasColunas nomes das colunas que corresponde aos <code>objetos</code> a serem inseridos. 
	 * Note que o números de objetos e de nomes de colunas deve ser o mesmo.
	 * @param objetos lista de objetos a serem inseridos na instrução SQL.
	 * @throws SQLException caso ocorra algum erro na execução da instrução ou de conexão ao banco.
	 * 
	 * @author Ramon Giovane
	 * @see Persistencia
	 * @see DAO#inserir(Persistencia, Object...)
	 */	
	public void inserir(String nomeTabela, String[]nomeDasColunas, Object...objetos) throws SQLException {
		PreparedStatement ps;
		StringBuilder insertQuery = new 
				StringBuilder(STR_INSERT_INTO).append(nomeTabela).
				append(adicionarNomesColunasQuery(nomeDasColunas)).append(queryFormatSymbols(objetos.length, true));
		
		try {
			ps = prepareStatement(insertQuery.toString());
		} catch (SQLException e) { 
			throw new SQLException(ERRO_PREPARAR_QUERY);
		}
		
		try{
			inserirObjetosPreparedStatement(ps, objetos);
			ps.execute();
		
		}catch (SQLException e) {
			throw e;
		
		}finally {
			ps.close();
		}
		
	}
	
	/** Insere objetos quaisquer de tipos quaisquer (<code>Object</code>) ao banco de dados.<br><br>
	 * <i>Este metódo pode ser levemente mais eficiente que sua versão sobrecarregada.</i><br>
	
	 * <br>
	 * <b>Nota:</b> este método é <b>seguro</b> contra injeções de SQL. 
	 * <br>
	 * <b>Nota:</b> o uso deste método é recomendado para a inserção de objetos <code>String</code>,
	 *  <code>Integer</code>, <code>Float</code>, etc...
	 *  
	 *  <br>
	 *  
	 *  O uso para objetos não <code>String</code> ou que não pertencem às classes empacotadoras de tipo é desencorajado.
	 *  Apenas objetos <code>String </code> podem ser passados como <code>null</code>.
	 * 
	 * <br>
	 * 
	 * @param <T> tipo que deve que deve implementar a interface <code>com.rdr.avaliacao.es.bd.Persistencia</code>.
	 * @param t objeto que implementa o método da interface supracitada, <code>insertQuery</code> retornando uma 
	 * query SQL com a sintaxe de inserção ao banco.
	 * Para usos sem a implementação do método <code>insertQuery</code>, usar a versão sobrecarregada de 
	 * <code>inserir</code>.
	 *
	 * @param objetos lista de objetos a serem inseridos na instrução SQL.
	 * @throws SQLException caso ocorra algum erro na execução da instrução ou de conexão ao banco.
	 * 
	 * @author Ramon Giovane
	 * @see Persistencia
	 * @see DAO#inserir(String, Object...)
	 */
	public <T extends Persistencia> void inserir(T t) throws SQLException { //WARNING! CHANGE DOC!!!
		PreparedStatement ps;
		Object[] objetos = t.insertObjects();
		try {
			ps = bd.getConnection().prepareStatement(t.insertQuery() + queryFormatSymbols(objetos.length, true));
		} catch (SQLException e1) {
			throw new SQLException(ERRO_PREPARAR_QUERY);
		}
		try{
			for(int indice = 0; indice<objetos.length; indice++)
				ps.setObject(indice+1, objetos[indice]);
			ps.execute();
		}catch (SQLException e) {
			throw e;
		}finally {
			ps.close();
		}		
		
	}
	
	/**Executa uma chamada de função ao banco de dados JDBC.
	 * 
	 * <br><br>
	 * <b>Nota:</b> este método é <b>seguro</b> contra injeções de SQL. 
	 * <br>
	 * <b>Nota:</b> o uso deste método é recomendado com parâmetros de objetos <code>String</code>,
	 *  <code>Integer</code>, <code>Float</code>, etc...
	 *  
	 *  <br>
	 *  
	 *  O uso para objetos não <code>String</code> ou que não pertencem às classes empacotadoras de tipo é desencorajado.
	 *  Apenas objetos <code>String </code> podem ser passados como <code>null</code>.
	 * 
	 * <br>
	 * @param nomeFuncao nome da função armazenada no banco de dados.
	 * @param parametros lista de objetos que compõe os parâmetros necessários na chamada da função.
	 * @return uma matriz de objetos contendo os nomes das colunas correspondentes no banco e objetos
	 * recuperados no retorno da função.
	 * 
	 * @throws SQLException caso ocorra algum erro na execução da instrução ou de conexão ao banco.
	 */
	public Object[][] executarFuncao(String nomeFuncao, Object... parametros) throws SQLException {
		/*
		 * A chamada de uma função SQL deve respeitar a seguinte sintaxe:
		 * 		 { call function( ?, ?, ? ) } "
		 * Onde call é o comando de chamada; function o nome da função; 
		 * e ? representa o número de parâmetros.
		 */
		StringBuilder call = new StringBuilder(STR_ABRE_CALL).
				append(nomeFuncao).append(queryFormatSymbols(parametros.length, false)).append(STR_FECHA_CALL);
		
		//Nota: CallableStatement é um PreparedStatement (herança)
		CallableStatement statement;
		
		try {
			statement = prepareCall(call.toString());
		} catch (SQLException e) {
			throw new SQLException(ERRO_PREPARAR_FUNCAO);
		}
		
		try {
			inserirObjetosPreparedStatement(statement, parametros);
			
			return resultSetAsMatrix(statement.executeQuery());
			
		}catch(SQLException e) {
			throw e;
		}finally {
			statement.close();
		}
	}
	
	
	/**Executa uma instrução SQL qualquer. Não captura retornos.<br>
	 * <b>Nota:</b> este método <b>NÃO</b> é seguro contra injeções SQL. Assegure-se de
	 * usuá-lo da forma correta. Recomenda-se o uso apenas para funções armazenadas sem parâmetro ou
	 * em ambiente controlado, onde não há interferência de agentes externos.
	 * @param query instrução SQL a ser executada
	 * @throws SQLException se ocorrer algo erro de conexão ou a query estiver mal formada.
	 */
	public void executar(String query) throws SQLException{
		CallableStatement statement = prepareCall(new StringBuilder().
				append(STR_ABRE_CALL).append(query).append(STR_FECHA_CALL).toString());
		try{
			statement.execute();
		}finally {
			statement.close();
		}
		
	}
	
	/**Realiza uma consulta no banco de dados, retornando uma matriz de objetos obtidos na consulta.
	 * 
	 * <br><br>
	 * <b>Nota:</b> este método é <b>seguro</b> contra injeções de SQL. 
	 * <br>
	 * @param t objeto de uma classe que implementa {@link Recuperacao}.
	 * @author Ramon Giovane
	 * @return uma matriz de objetos contendo os nomes das colunas correspondentes no banco e objetos recuperados na consulta.
	 * @throws SQLException caso ocorra algum erro na execução da instrução ou de conexão ao banco.
	 */
	public <T extends Recuperacao> Object[][] consultar(T t) throws SQLException {
		PreparedStatement ps = prepareStatement(t.selectQuery());
		Object chavesPrimarias[] = t.searchKeys();
		try{
			if(chavesPrimarias != null)
				inserirObjetosPreparedStatement(ps, chavesPrimarias);
			return resultSetAsMatrix(ps.executeQuery());
		}catch (Exception e) {
			throw e;
		}finally {
			ps.close();
		}
	}
	
	/**Realiza uma consulta no banco de dados, retornando uma matriz de objetos obtidos na consulta.
	 * 
	 * <br><br>
	 * <b>Nota:</b> este método é <b>seguro</b> contra injeções de SQL. 
	 * <br>
	 * @param query instrução SQL a ser executada
	 * @param parametros lista de parâmetros passados na instrução
	 * @author Ramon Giovane
	 * @return uma matriz de objetos contendo os nomes das colunas correspondentes no banco e objetos recuperados na consulta.
	 * @throws SQLException caso ocorra algum erro na execução da instrução ou de conexão ao banco.*/
	 
	public Object[][] consultar(String query, Object... parametros) throws SQLException {
		PreparedStatement ps = prepareStatement(query);
		try{
			if(parametros != null)
				inserirObjetosPreparedStatement(ps, parametros);
			return resultSetAsMatrix(ps.executeQuery());
		}catch (Exception e) {
			throw e;
		}finally {
			ps.close();
		}
	}

	
	/**Retorna a quantidade de registros em um ResultSet*/
	private int resultSetLength(ResultSet resultSet) throws SQLException {
		int size = 0;
		resultSet.beforeFirst();
		while(resultSet.next()) {
			size++;
		}
		resultSet.beforeFirst();
		return size;
	}
	
	/**A partir de um objeto <code>ResultSet</code> retorna uma matriz com os objetos obtidos em uma consulta SQL.
	 * 
	 * @param resultSet conjunto de dados obtidos numa consulta SQL
	 * @return uma matriz de objetos contendo as linhas e colunas correspondentes recuperados na consulta SQL.
	 * @throws SQLException
	 */
	private Object[][] resultSetAsMatrix(ResultSet resultSet) throws SQLException {
		int length = resultSetLength(resultSet);

		ResultSetMetaData metaData = resultSet.getMetaData();
		int numeroColunas = metaData.getColumnCount();
		Object resultados[][] =  new Object[length][numeroColunas];
		
		//for(int i = 0; i<length; i++) {
		int linhas =0;
		while(resultSet.next()){
			for(int colunas = 0; colunas < numeroColunas; colunas++)
				resultados[linhas][colunas] = resultSet.getObject(colunas+1);
			linhas++;
		
		}
		
		return resultados;
			
	}
	/**Recebe um número de parâmetros e gera uma String para ser usado numa query SQL.
	 * Exemplo: numeroDeParametros = 2, adicionarValues = true
	 * 			retorno "values (?, ?)"
	 * 			
	 * 			numeroDeParametros = 4, adiconarValues = false
	 * 			retorno "(?, ?, ?, ?)"
	 */
	private String queryFormatSymbols(int numeroDeParametros, boolean adicionarValues) {
		StringBuilder str = new StringBuilder();
		str.append(adicionarValues ? STR_VALUES : " ").append(STR_ABRE_PARENTESES);
		for(int i =0; i<numeroDeParametros; i++) {
			str.append(STR_INTERROGACAO);
			if(i+1 != numeroDeParametros)
				str.append(STR_VIRGULA);
		}
		str.append(STR_FECHA_PARENTESES);
		
		return str.toString();
	}
	
	/**Adiciona os nomes das colunas a serem usadas na instrução SQL.*/
	private Object adicionarNomesColunasQuery(String[] nomeDasColunas) {
		StringBuilder str = new StringBuilder(STR_ABRE_PARENTESES);
		for(int i =0; i<nomeDasColunas.length; i++) {
			str.append(nomeDasColunas[i]);
			str.append((i+1 != nomeDasColunas.length ? STR_VIRGULA : STR_FECHA_PARENTESES));
		}

		return str.toString();
	}
	
	private void inserirObjetosPreparedStatement(PreparedStatement ps, Object...objetos) throws SQLException {
		for(int indice = 0; indice<objetos.length; indice++)
			if(objetos[indice] == null)
				ps.setNull(indice+1, Types.VARCHAR);
			else ps.setObject(indice+1, objetos[indice]);
	}
	
	

}
