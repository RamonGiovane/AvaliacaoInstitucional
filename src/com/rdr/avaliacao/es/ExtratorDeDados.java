package com.rdr.avaliacao.es;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
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
	private final String TEMA_INDEFINIDO = "Geral";
	private DAO dao;

	private IndicePergunta indices[];

 
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

	private String[] quebrarTexto(String texto) throws IOException {

		return texto.split(";"); 

	}

	public void extrairDados() throws IOException{
		Calendar inicio = Calendar.getInstance();
		extrairPerguntas();
		try {
			extrairRespostas();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		arquivo.fechar();

		Calendar fim = Calendar.getInstance();

		EntradaESaida.msgInfo(null, String.format("Time %d", 
				fim.getTimeInMillis() - inicio.getTimeInMillis()), "Opa");
	}

	private void extrairPerguntas() throws IOException {

		String strPerguntas[] =  quebrarTexto(obterCabecalho()); //!!! must check this

		indices = new IndicePergunta[strPerguntas.length-3];

		//Ignora as colunas de identificacao do entrevistado
		int indice = 3;
		String tema, questao;
		Object[][] retorno;
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
   
 



			try {
				retorno = dao.executarFuncao("inserir_pergunta", questao, tema);

				indices[indice-3] = new IndicePergunta((int)retorno[0][0], (int) retorno[0][1]);

			} catch (SQLException e) {
				System.out.println(e.getErrorCode() + " " + e.getMessage());
			}
		}

	}

	private void extrairRespostas() throws IOException, SQLException {

		int codigoEntrevistado;
		String linha, respostas[];

		do {

			/* 	Obtendo uma linha do arquivo e capturando a exceção e saindo do loop
			 * 	se houver algum erro ou chegar ao fim do arquivo.
			 */
			linha = arquivo.lerLinha();
			if(linha == null) break;
	


			//Dividindo a linha e strings com cada coluna
			respostas = quebrarTexto(linha);

			//Extrai e salva no banco de dados o entrevistado, obtendo o código do entrevistado salvo no banco
			codigoEntrevistado = extrairEntrevistado(respostas);

			//Extrai e salva no banco de dados as respostas de um entrevistado.
			extrairLinhaResposta(linha, codigoEntrevistado);

		}while(linha != null);

	}

	private void extrairLinhaResposta(String textoLinha, int codigoEntrevistado) throws SQLException {
		int i = 0;
		
		String word = null;
		for(int x = 0; x<3; x++) {
			textoLinha = textoLinha.substring(textoLinha.indexOf(";") + 1) + " ";
			word = textoLinha.substring(0,textoLinha.indexOf(";"));
			System.out.printf("[%s]", word);
		}
																			 
											
										
								  

		while(true){
			dao.executarFuncao("inserir_resposta", word.trim(), indices[i].getIndiceAssunto(),
					indices[i].getIndicePergunta(), codigoEntrevistado);

			textoLinha = textoLinha.substring(textoLinha.indexOf(";") + 1) + " ";
			
			try {
				word = textoLinha.substring(0,textoLinha.indexOf(";"));
				System.out.printf("[%s]", word);
			}catch (Exception e) {
				dao.executarFuncao("inserir_resposta", textoLinha.trim(), indices[i].getIndiceAssunto(),
						indices[i].getIndicePergunta(), codigoEntrevistado);
				break;
			}

  

			i++;
		}
		
	}
	
	
//TO DO: QUEBRAR O TEXTO NA FUNÇÃO ANTERIOR, PASSAR APENAS OS TRES PARAMETROS PARA ESSA! EVITA COPIAR A LINHA INTEIRA.
	private int extrairEntrevistado(String[] textoLinhas) throws IOException, SQLException {

		int codigoEntrevistado;
	
		

		/*
		 * Executando a função SQL para inserção (que recebe strings: segmento, campus, grau e curso) e 
		 * retorna um inteiro (código do entrevistado inserido).
		 * Todos os retornos de instrunções SQL segundo a classe DAO retornam uma matriz que contém os nomes (se houver)
		 * dos campos e os valores de retorno em forma de Object. 
		 */
			Object resultado [][] = dao.executarFuncao("inserir_entrevistado", textoLinhas[0], textoLinhas[1],  textoLinhas[2]);


		codigoEntrevistado = (int) resultado[0][0];

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


	private void seprarGrauECurso(Aluno aluno, String grauECurso){
		StringTokenizer stringTokenizer = new StringTokenizer(grauECurso, "em");

		String str;

		if(stringTokenizer.hasMoreElements()) {
			str = stringTokenizer.nextToken();
			aluno.setGrau(str == "Tecnologia" ? "Tecnólogo" : str);

			aluno.setCurso(stringTokenizer.nextToken());
		}

		else {
			aluno.setCurso(grauECurso);
			aluno.setGrau(grauECurso);
		}
	}


	/*Fecha o arquivo de texto (se aberto, estiver) e o abre novamente, a fim de ler o início do texto*/
	private void resetarArquivo() throws IOException {
		arquivo.fechar();
		arquivo.abrir(nomeArquivo);
	}


	private class IndicePergunta{
		private int indiceAssunto, indicePergunta;

		public IndicePergunta(int indiceAssunto, int indicePergunta) {
			this.indiceAssunto = indiceAssunto;
			this.indicePergunta = indicePergunta;
		}

		public IndicePergunta() {
			super();
		}

		public int getIndiceAssunto() {
			return indiceAssunto;
		}

		public void setIndiceAssunto(int indiceAssunto) {
			this.indiceAssunto = indiceAssunto;
		}

		public int getIndicePergunta() {
			return indicePergunta;
		}

		public void setIndicePergunta(int indicePergunta) {
			this.indicePergunta = indicePergunta;
		}




	}


}
