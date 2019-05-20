package com.rdr.avaliacao.es.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** 
 * Usa o padrão de projeto (design pattern) Singleton para obter uma conexão única com o banco 
 * de dados. Esta classe possui operações para permitir as operações CRUD no banco de dados.
 * 
 *   O acrônimo CRUD corresponde as seguintes instruções SQL:
 *   
 *   Create = SQL INSERT 
 *   Retrieve =  SQL SELECT
 *   Update = SQL UPDATE
 *   Delete = SQL DELETE
 */
public class BancoDeDados {
	private Connection connection;
	private Statement statement;
	private String nomeBD, usuarioBD, senhaBD;
	private final String DRIVER_BD = "jdbc:postgresql:";
	private static BancoDeDados bd;

	private static boolean conectado;

	private BancoDeDados(String nomeBD, String usuarioBD, String senhaBD) throws SQLException {
		this.nomeBD = nomeBD;
		this.usuarioBD = usuarioBD;
		this.senhaBD = senhaBD;

		/* Obtém uma conexão com o banco de dados bd identificado por nomeBD e que possui 
		 * usário e senha de acesso definidos por usuarioBD e senhaBD.
		 *
		 * A partir do JDBC 4.0 o comando abaixo não é mais necessário porque a classe 
		 * DriverManager se encarregada de carregá-lo automaticamente. 
		 * 
		 * // Driver JDBC do PostgreSQL.
		 * String driverJDBC = "org.postgresql.Driver";
		 * 
		 * // Carrega a classe do driver JDBC para permitir conexão com o BD. 
		 * 	Class.forName(driverJDBC);   
		 */
		connection = DriverManager.getConnection(DRIVER_BD + nomeBD, usuarioBD, senhaBD);

		// Obtém um objeto Statement para enviar consultas SQL ao banco de dados.
		statement = connection.createStatement(); 
	} // construtor privado

	/** Obtém uma conexão única com o banco de dados. Se não for possível estabelecer uma conexão
	 * com o banco de dados retorna null.
	 */
	public static BancoDeDados criarConexao(String nomeBD, String usuarioBD, String senhaBD) {
		if (bd == null) {
			try { 
				bd = new BancoDeDados(nomeBD, usuarioBD, senhaBD);
			} catch (SQLException e) {
				System.out.println(nomeBD + usuarioBD + senhaBD);
				System.out.println(e);
				conectado = false;
				return null;
			}
		}
		conectado = true;
		System.out.println("BD = " + bd);
		return bd;
	} // criarConexao()

	/** Fecha a conexão com o banco de dados. */  
	public void fecharConexao() throws SQLException { 
		if (statement != null) statement.close();
		if (connection != null) connection.close();
		bd = null;
	} 

	public Connection getConnection() {
		return connection;
	}

	public String getNomeBD() {
		return nomeBD;
	}

	public void setNomeBD(String nomeBD) {
		this.nomeBD = nomeBD;
	}

	public String getUsuarioBD() {
		return usuarioBD;
	}

	public void setUsuarioBD(String usuarioBD) {
		this.usuarioBD = usuarioBD;
	}

	public String getSenhaBD() {
		return senhaBD;
	}

	public void setSenhaBD(String senhaBD) {
		this.senhaBD = senhaBD;
	}

	public String getDriverBD() {
		return DRIVER_BD;
	}

	public static boolean isConectado() {
		return conectado;
	}

	public static BancoDeDados getBancoDeDados() {
		return bd;
	}
} // class BancoDeDados 