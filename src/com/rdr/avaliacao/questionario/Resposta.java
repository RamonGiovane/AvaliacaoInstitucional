package com.rdr.avaliacao.questionario;

import java.sql.SQLException;

import com.rdr.avaliacao.es.bd.BancoDeDados;
import com.rdr.avaliacao.es.bd.DAO;
import com.rdr.avaliacao.es.bd.Recuperacao;

public class Resposta {
	private Conceito conceito;
	private Pergunta pergunta;
	
	public Resposta(int nota, String descricaoConceito,  String descricaoPergunta,
			String assuntoPergunta) {
		
		conceito = new Conceito(nota, descricaoConceito);
		pergunta  = new Pergunta(descricaoPergunta, assuntoPergunta);
	}
	
	public Resposta(int nota, String descricaoPergunta,
			String assuntoPergunta) {
		
		conceito = new Conceito(nota);
		pergunta  = new Pergunta(descricaoPergunta, assuntoPergunta);
	}

	public Resposta() {	
		pergunta = new Pergunta();
	}

	public Conceito getConceito() {
		return conceito;
	}

	public void setConceito(Conceito conceito) {
		this.conceito = conceito;
	}
	
	public void adicionarPergunta(String descricaoPergunta, String assuntoPergunta) {
		pergunta.setAssunto(assuntoPergunta);
		pergunta.setDescricao(descricaoPergunta);
		
	}
	
	public void adicionarPergunta(int numeroPergunta) throws SQLException {
		DAO dao =  new DAO(BancoDeDados.getBancoDeDados());
		Object[][] resultado = dao.consultar(new Recuperacao() {
			
			@Override
			public String selectQuery() {
				return "select  from pergunta where codigo = ? ";
			}
			
			@Override
			public Object[] searchKeys() {
				return new Object[] {new Integer(numeroPergunta)};
			}
		});
		pergunta.setAssunto(resultado[1][0].toString());
	}
	
	public String obterDescricaoPergunta() {
		return pergunta.getDescricao();
	}
	
	public String obterAssuntoPergunta() {
		return pergunta.getAssunto();
	}
	
	
}
