package com.rdr.avaliacao.es;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import com.rdr.avaliacao.es.bd.BancoDeDados;
import com.rdr.avaliacao.es.bd.DAO;
import com.rdr.avaliacao.questionario.Aluno;
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

	public void extrairDados() throws IOException{
		extrairPerguntas();
		try {
			extrairRespostas();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void extrairPerguntas() throws IOException {

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
			System.out.println("DAO " + dao);
			try {
				dao.executarFuncao("inserir_pergunta", questao, tema);

			} catch (SQLException e) {
				System.out.println(e.getErrorCode() + " " + e.getMessage());
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

		int codigoEntrevistado;
		String linha, respostas[];

		while(true) {

			/* 	Obtendo uma linha do arquivo e capturando a exceção e saindo do loop
			 * 	se houver algum erro ou chegar ao fim do arquivo.
			 */ 
			try{
				linha = arquivo.lerLinha();
				System.out.println("Linha " +  linha);
			}catch (IOException e) {
				break;
			}

			//Dividindo a linha e strings com cada coluna
			respostas = quebrarTexto(linha);

			//Extrai e salva no banco de dados o entrevistado, obtendo o código do entrevistado salvo no banco
			codigoEntrevistado = extrairEntrevistado(respostas);
			System.out.println("Codigo Entrevistado " + codigoEntrevistado);

			//Extrai e salva no banco de dados as respostas de um entrevistado.
			extrairLinhaResposta(respostas, codigoEntrevistado);

		}

	}

	private void extrairLinhaResposta(String[] textoLinha, int codigoEntrevistado) throws SQLException {
		
		for(int i =3; i<textoLinha.length; i++) {
			dao.executarFuncao("inserir_resposta", textoLinha[i], perguntas[i-3].getDescricao(),
					perguntas[i-3].getAssunto(), Integer.valueOf(codigoEntrevistado));

		}

	}

	private int extrairEntrevistado(String[] textoLinhas) throws IOException, SQLException {
		String grau, curso;
		int codigoEntrevistado;
		Aluno aluno;

		final String STR_VAZIA = null;

		//Identificando se a ocorrência é um aluno. Se for, separa seu grau e curso
		if(Segmento.DISCENTE.equals(Segmento.parseSegmento(textoLinhas[0]))) {
			aluno = seprarGrauECurso(textoLinhas[1]);
			grau = aluno.getGrau();
			curso = aluno.getCurso();
		}
		//Senão, define o grau e curso como vazio.
		else { grau = STR_VAZIA; curso = STR_VAZIA; }

		/*
		 * Executando a função SQL para inserção (que recebe strings: segmento, campus, grau e curso) e 
		 * retorna um inteiro (código do entrevistado inserido).
		 * Todos os retornos de instrunções SQL segundo a classe DAO retornam uma matriz que contém os nomes (se houver)
		 * dos campos e os valores de retorno em forma de Object. 
		 */
		Object resultado [][] = dao.executarFuncao("inserir_entrevistado", textoLinhas[0], textoLinhas[1],  grau, curso);


		codigoEntrevistado = (int) resultado[1][0];

		return codigoEntrevistado;


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


	private Aluno seprarGrauECurso(String grauECurso){
		Aluno aluno = new Aluno();
		String grau;
		//		//int i = 0
		//		if(grauECurso.matches("^Técnico")) {
		//				System.out.println("cURSO TECNICO + 1");
		//				grau = "Técnico";
		//		}
		//		else if(grauECurso.matches("^Bacharelado")) {
		//			System.out.println("bACHARELADO + 1");
		//			grau = "Bacharelado";
		//		}
		//		else if(grauECurso.matches("^Tecnologia")) {
		//			System.out.println("tECNOLOGO + 1");
		//			grau = "Tecnólogo";
		//		}
		//		
		//		else if(grauECurso.matches("^Licenciatura")) {
		//			System.out.println("tECNOLOGO + 1");
		//			grau = "Tecnólogo";
		//		}

		String[] str = grauECurso.split("em");

		try {
			aluno.setGrau(str[0] == "Tecnologia" ? "Tecnólogo" : str[0]);
			aluno.setCurso(str[1]);
		}catch(ArrayIndexOutOfBoundsException e) {
			aluno.setCurso(grauECurso);
			aluno.setGrau(grauECurso);
		}

		return aluno;
	}


	/*Fecha o arquivo de texto (se aberto, estiver) e o abre novamente, a fim de ler o início do texto*/
	private void resetarArquivo() throws IOException {
		arquivo.fechar();
		arquivo.abrir(nomeArquivo);
	}

	public class Dados{

	}


}
