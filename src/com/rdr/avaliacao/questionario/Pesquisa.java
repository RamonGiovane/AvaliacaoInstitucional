package com.rdr.avaliacao.questionario;

import com.rdr.avaliacao.es.bd.Persistencia;
import com.rdr.avaliacao.es.bd.Recuperacao;

/**Representa uma pesquisa, contendo nome (descrição), seu código associado ao banco de 
 * dados e o caminho de onde os dados da pesquisa serão importados
 * @author Ramon Giovane
 *
 */
public class Pesquisa  implements Persistencia, Recuperacao{
	private String nome;
	private int codigo;
	private String caminhoDataSet;
	
	
	
	public Pesquisa() {
	}

	public Pesquisa(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}


	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getCaminhoDataSet() {
		return caminhoDataSet;
	}

	public void setCaminhoDataSet(String caminhoDataSet) {
		this.caminhoDataSet = caminhoDataSet;
	}
	
	/**Retorna uma query SQL utilizada para inserir uma pesquisa no banco*/
	@Override
	public String insertQuery() {
		return "insert into pesquisa (descricao)";
	}

	/**Retorna o objeto necessário para utilizar a query de inserção de uma pesquisa no banco. Em outras palavras,
	 * mais especificamente,  um array de  <code>Object</code> contendo o nome da pesquisa a ser guardada*/
	@Override
	public Object[] insertObjects() {
		return new Object[]{nome};
	}

	@Override
	/**Especifica a query SQL para busca de dados no banco de dados para um objeto pergunta*/
	public String selectQuery() {
		return "select * from pesquisa where descricao = ?";
	}

	@Override
	/**Especifica o atributo nome da pesquisa como termo de busca no banco de dados para um objeto pergunta*/
	public Object[] searchKeys() {
		return new Object[] {nome};
	}

	@Override
	/**Determina se uma pesquisa é igual a outra. Considera apenas a descrição (nome) da pesquisa como 
	 * critério de comparação*/
	public boolean equals(Object obj) {
		Pesquisa pesquisa = null;
		try {
			pesquisa = (Pesquisa) obj;
		}catch (Exception e) {
			return false;
		}
		return pesquisa.nome.equals(this.nome);
	}
	
	

}
