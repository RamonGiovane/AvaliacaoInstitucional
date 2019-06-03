package com.rdr.avaliacao.questionario;

import java.sql.SQLException;

import com.rdr.avaliacao.es.bd.BancoDeDados;
import com.rdr.avaliacao.es.bd.DAO;
import com.rdr.avaliacao.es.bd.Recuperacao;

public class Resposta {
	private TipoResposta resposta;
	private Pergunta pergunta;
	
	public Resposta(TipoResposta resposta) {
		this();
		this.resposta = resposta;
	}
	
	public Resposta(TipoResposta resposta, String descricaoPergunta,
			String assuntoPergunta) {
		this.resposta = resposta;
		pergunta  = new Pergunta(descricaoPergunta, assuntoPergunta);
	}

	public Resposta() {	
		pergunta = new Pergunta();
	}

	public TipoResposta getResposta() {
		return resposta;
	}

	public void setResposta(TipoResposta resposta) {
		this.resposta = resposta;
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
	
	
	
}
