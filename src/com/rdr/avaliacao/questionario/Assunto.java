package com.rdr.avaliacao.questionario;

/**Classe que representa um tema ou assunto de uma pergunta de uma pesquisa
 * 
 * @author Ramon Giovane
 *
 */
public class Assunto implements Comparable<Assunto> {
	private int codigo;
	private String descricao;
	
	public Assunto() {	}

	public Assunto(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return String.format("%s", descricao);
	}

	@Override
	public int compareTo(Assunto assunto) {
		try{
			return -Integer.compare(assunto.codigo, codigo);
		}catch (Exception e) {
			return assunto.getDescricao().compareTo(descricao);
		}
	}
	
	
	
}
