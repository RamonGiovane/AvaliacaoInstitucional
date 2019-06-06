package com.rdr.avaliacao.es;

import java.awt.Component;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.StringTokenizer;

import com.rdr.avaliacao.es.bd.BancoDeDados;
import com.rdr.avaliacao.es.bd.DAO;
import com.rdr.avaliacao.es.bd.Recuperacao;
import com.rdr.avaliacao.ig.IgBarraDeProgresso;
import com.rdr.avaliacao.ig.InterfaceConstraints;
import com.rdr.avaliacao.questionario.Aluno;
import com.rdr.avaliacao.questionario.Curso;
import com.rdr.avaliacao.questionario.Entrevistado;
import com.rdr.avaliacao.questionario.Pesquisa;
import com.rdr.avaliacao.questionario.Questionario;
import com.rdr.avaliacao.questionario.Resposta;
import com.rdr.avaliacao.relatorio.DadosDeGrafico;
import com.rdr.avaliacao.relatorio.DataSet;

public class ExtratorDeDados {
	private final String SEPARADOR_PADRAO = ";";
	private BancoDeDados bd;
	private String separador;
	private ArquivoTexto arquivo; 
	private final String TEMA_INDEFINIDO = "Geral";
	private DAO dao;

	private IndicePergunta indicesPerguntas[];

	private Pesquisa pesquisa;

	private IgBarraDeProgresso barraDeProgresso;

	private Component janelaPai;

	private ExtratorDeDados(Component janelaPai, BancoDeDados bd, Pesquisa pesquisa) {
		super();
		this.bd = bd;
		dao = new DAO(bd);

		this.separador = SEPARADOR_PADRAO;
		arquivo = new ArquivoTexto();
		this.janelaPai = janelaPai;
		this.pesquisa = pesquisa;
	}
	
	public ExtratorDeDados(BancoDeDados bd, Pesquisa pesquisa) {
		dao = new DAO(bd);
		this.separador = SEPARADOR_PADRAO;
		arquivo = new ArquivoTexto();
		this.pesquisa = pesquisa;
	}


	public BancoDeDados getBd() {
		return bd;
	}

	public void setBd(BancoDeDados bd) {
		this.bd = bd;
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

	private void fecharArquivo() throws IOException {
		arquivo.fechar();
	}

	private void extrairDados() throws IOException {
		System.out.println(pesquisa.getCaminhoDataSet());
		abrirArquivo();

		try {
			extrairPerguntas();
			extrairRespostas();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		fecharArquivo();

	}

	private void iniciarBarraDeProgresso() {
		barraDeProgresso = new IgBarraDeProgresso(janelaPai, 
				InterfaceConstraints.TITULO_PROGRAMA, 
				"Lendo respostas...", indicesPerguntas.length);
	}

	private void terminarBarraDeProgresso() {
		barraDeProgresso.fechar();
	}

	//TODO: Verificar o nome da pesquisa no banco de dados. Salvar se não existe, sinalizar se já existe
	public static void extrairDados(Component janelaPai, BancoDeDados banco, Pesquisa pesquisa) throws IOException{
		Calendar inicio = Calendar.getInstance();

		new ExtratorDeDados(janelaPai, banco, pesquisa).extrairDados();

		Calendar fim = Calendar.getInstance();

		EntradaESaida.msgInfo(null, String.format("Time %d", 
				fim.getTimeInMillis() - inicio.getTimeInMillis()), "Opa");
	}

	private void extrairPerguntas() throws IOException {

		String strPerguntas[] =  quebrarTexto(obterCabecalho()); //!!! must check this

		indicesPerguntas = new IndicePergunta[strPerguntas.length-3];

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
				System.out.println(tema + " " + questao);
				System.out.println(dao);
				retorno = dao.executarFuncao("inserir_pergunta", pesquisa.getCodigo(), questao, tema);

				indicesPerguntas[indice-3] = new IndicePergunta((int)retorno[0][0], (int) retorno[0][1]);

			} catch (SQLException e) {
				System.out.println(e.getErrorCode() + " " + e.getMessage());
			}
		}

	}

	private void extrairRespostas() throws IOException, SQLException {

		int codigoEntrevistado;
		String linha, respostas[];
		int contador = 0;

		iniciarBarraDeProgresso();

		do {

			/* 	Obtendo uma linha do arquivo e capturando a exceção e saindo do loop
			 * 	se houver algum erro ou chegar ao fim do arquivo.
			 */
			linha = arquivo.lerLinha();
			if(linha == null) break;


			barraDeProgresso.incrementar(contador++);

			//Dividindo a linha e strings com cada coluna
			respostas = quebrarTexto(linha);

			//Extrai e salva no banco de dados o entrevistado, obtendo o código do entrevistado salvo no banco
			codigoEntrevistado = extrairEntrevistado(respostas);

			//Extrai e salva no banco de dados as respostas de um entrevistado.
			extrairLinhaResposta(linha, codigoEntrevistado);


		}while(linha != null);

		terminarBarraDeProgresso();
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
			dao.executarFuncao("inserir_resposta", pesquisa.getCodigo(), word.trim(), indicesPerguntas[i].getIndiceAssunto(),
					indicesPerguntas[i].getIndicePergunta(), codigoEntrevistado);

			textoLinha = textoLinha.substring(textoLinha.indexOf(";") + 1) + " ";


			try {
				word = textoLinha.substring(0,textoLinha.indexOf(";"));
				System.out.printf("[%s]", word);

			}catch (Exception e) {
				dao.executarFuncao("inserir_resposta", pesquisa.getCodigo(), textoLinha.trim(), indicesPerguntas[i].getIndiceAssunto(),
						indicesPerguntas[i].getIndicePergunta(), codigoEntrevistado);
				break;
			}



			i++;
		}

	}


	//TODO: QUEBRAR O TEXTO NA FUNÇÃO ANTERIOR, PASSAR APENAS OS TRES PARAMETROS PARA ESSA! EVITA COPIAR A LINHA INTEIRA.
	private int extrairEntrevistado(String[] textoLinhas) throws IOException, SQLException {

		int codigoEntrevistado;



		/*
		 * Executando a função SQL para inserção (que recebe strings: segmento, campus, grau e curso) e 
		 * retorna um inteiro (código do entrevistado inserido).
		 * Todos os retornos de instrunções SQL segundo a classe DAO retornam uma matriz que contém os nomes (se houver)
		 * dos campos e os valores de retorno em forma de Object. 
		 */
		Object resultado [][] = dao.executarFuncao("inserir_entrevistado", pesquisa.getCodigo(), textoLinhas[0], textoLinhas[1],  textoLinhas[2]);


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
		abrirArquivo();
	}

	private void abrirArquivo() throws FileNotFoundException{
		arquivo.abrir(pesquisa.getCaminhoDataSet());
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

//	public <R extends Recuperacao>  DataSet consultarBancoDeDados(R recuperacao) throws SQLException {
//		DataSet dataSet = new DataSet();
//		Object[][] dados = dao.consultar(recuperacao);
//		
//		for(int i = 0; i<dados.length; i++) {
//			dataSet.adicionar((Number)dados[i][0], dados[i][1].toString());
//		}
//		
//		return dataSet;
//	}
//	
	
//	public Curso[] consultarBancoDeDados(Pesquisa pesquisa) throws SQLException{
//		Curso cursos[];
//		Object[][] resultado = new Object[0][0], objetos = dao.consultar(new Recuperacao() {
//			
//			@Override
//			public String selectQuery() {
//
//				return "select codigo, descricao from curso";
//			}
//
//			@Override
//			public Object[] searchKeys() {
//				return null;
//			}
//		});
//		cursos = new Curso[objetos.length];
//		int i = 0;
//		for(; i<objetos.length; i++) {
//			cursos[i] = new Curso((int)objetos[i][0], objetos[i][1].toString());
//
//
//			resultado = dao.consultar("select count(codcurso) from entrevistado where codcurso = ?", cursos[i].getCodigo());
//			
//			cursos[i].setQuantidadeEntrevistados((long)resultado[0][0]);
//		}
//
//		
//		return cursos;
//	}

	public DataSet consultarBancoDeDados(Pesquisa pesquisa) throws SQLException{
		DataSet dataSet = new DataSet();
		
		//Consulta 1: Seleciona todos os cursos
		Object[][] objetos = dao.consultar("select codigo, descricao from curso");
		
		Curso curso;
		int i = 0;
		Object[][] resultado = new Object[0][0];
		
		for(; i<objetos.length; i++) {
			curso = new Curso((int)objetos[i][0], objetos[i][1].toString());
			
			//Consulta 2: obtém o número de entrevistados de todos cursos
			resultado = dao.consultar("select count(codcurso) from entrevistado where codcurso = ?", curso.getCodigo());
			
			curso.setQuantidadeEntrevistados((long)resultado[0][0]);
			
			//Adiciona um curso no dataSet
			dataSet.adicionar(curso);
		}

		
		return dataSet;
	}

}
