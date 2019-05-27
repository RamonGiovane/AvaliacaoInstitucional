package com.rdr.avaliacao.es;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

import com.rdr.avaliacao.es.bd.BancoDeDados;
import com.rdr.avaliacao.es.bd.DAO;
import com.rdr.avaliacao.questionario.Entrevistado;
import com.rdr.avaliacao.questionario.Pergunta;
import com.rdr.avaliacao.questionario.Segmento;

public class ExtratorDeDados {
	private final String SEPARADOR_PADRAO = ";";
	private BancoDeDados bd;
	private String nomeArquivo;
	private String separador;
	private ArquivoTexto arquivo; 
	private Pergunta[] perguntas;
	private final String TEMA_INDEFINIDO = "Geral";
	private DAO dao;
	
	public ExtratorDeDados(BancoDeDados bd, String nomeArquivo) {
		super();
		this.bd = bd;
		dao = new DAO(bd);
		this.nomeArquivo = nomeArquivo;
		this.separador = SEPARADOR_PADRAO;
		arquivo = new ArquivoTexto();
	}
	
	

	public ExtratorDeDados(BancoDeDados bd) {
		this(bd, null);
		
	}



	public BancoDeDados getBd() {
		return bd;
	}

	public void setBd(BancoDeDados bd) {
		this.bd = bd;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getSeparador() {
		return separador;
	}

	public void setSeparador(String separador) {
		this.separador = separador;
	}
	
	
	public boolean checarConexaoBD() {
		return (bd != null);
	}
	
	
	public Object extrairDados(ResultSet resultSet, String nomeColuna) throws SQLException {
		return resultSet.getObject(nomeColuna);
	}
	
	private String[] quebrarTexto(String texto) throws IOException {
		System.out.println("TEXTO " +  texto.split(";").length);
		return texto.split(";"); 
		
	}
	
	public void extrairDados() throws IOException {
		
		String strPerguntas[] =  quebrarTexto(obterCabecalho()); //!!! must check this
		perguntas =  new Pergunta[strPerguntas.length-3];
	
		//Ignora as colunas de identificacao do entrevistado
		int indice = 3;
		String tema, questao;
		Pergunta pergunta;
		for(; indice<strPerguntas.length; indice++) {
			strPerguntas[indice] = strPerguntas[indice].replace(" - ", ".");
			strPerguntas[indice] = strPerguntas[indice].replaceAll("\\d{1,}[.]", "").trim();
			try{
				tema = strPerguntas[indice].substring(0,
						strPerguntas[indice].indexOf('[')).trim();
				
				questao = strPerguntas[indice].substring(strPerguntas[indice].indexOf('[')+1,
						strPerguntas[indice].indexOf(']')).trim();
			}catch(StringIndexOutOfBoundsException e) {
				tema = TEMA_INDEFINIDO;
				questao = strPerguntas[indice];
			}
			System.out.println(tema + ": " + questao);
			pergunta =  new Pergunta(questao, tema);
			System.out.println(pergunta);
			
			try {
				dao.executarFuncao("inserir_pergunta", questao, tema);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			perguntas[indice-3] = pergunta;
		}
		
		try {
			extrairRespostas();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("DEU RUIM");
		}
		
	}
	
	
	private void extrairRespostas() throws IOException, SQLException {
		String linha = arquivo.lerLinha();
		String respostas[] = quebrarTexto(linha);
		
		
		Object resultado [][] = dao.executarFuncao("inserir_entrevistado", respostas[0], respostas[1], respostas[2]);
		System.out.println( resultado[0][0]);
		//int codigoEntrevistado = (int) resultado[1][0];
		//System.out.println(codigoEntrevistado);
	/*	for(int i =3; i<respostas.length; i++) {
			dao.executarFuncao("inserir_resposta", respostas[i], 
					perguntas[i].getAssunto(), perguntas[i].getDescricao());
			
		}*/
		
	}
	
	
	/**Obtém a primeira linha do arquivo de texto a ser extraído.
	 * 
	 * @throws IOException se ocorrer um erro de E/S, como por exemplo se o arquivo não existir.
	 * 
	 * @returns uma <code>String</code> contendo o conteúdo da primeira linha do arquivo.
	 * 
	 * @author Ramon Giovane
	 * 
	 * */
	public String obterCabecalho() throws IOException {
		resetarArquivo();
		return arquivo.lerLinha();
	}
	

	
	/*Fecha o arquivo de texto (se aberto, estiver) e o abre novamente, a fim de ler o início do texto*/
	private void resetarArquivo() throws IOException {
		arquivo.fechar();
		arquivo.abrir(nomeArquivo);
	}
	
	public class Dados{
		
	}
	

}
