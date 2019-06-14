package com.rdr.avaliacao.es;

import java.awt.Component;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.rdr.avaliacao.es.bd.BancoDeDados;
import com.rdr.avaliacao.es.bd.DAO;
import com.rdr.avaliacao.ig.IgAvaliacaoInstitucional;
import com.rdr.avaliacao.ig.IgBarraDeProgresso;
import com.rdr.avaliacao.ig.InterfaceConstraints;
import com.rdr.avaliacao.ig.TipoRelatorio;
import com.rdr.avaliacao.questionario.Assunto;
import com.rdr.avaliacao.questionario.Curso;
import com.rdr.avaliacao.questionario.Pesquisa;
import com.rdr.avaliacao.relatorio.MediasPorCurso;
import com.rdr.avaliacao.relatorio.RelatorioDeMedias;
import com.rdr.avaliacao.relatorio.RelatorioDeParticipantes;

/**
 * Esta classe realiza o intermédio entre os dispositivos de armazenamento e as classes e objetos.
 * É responsável por todo o trabalho pesado: ler arquivos de texto, fazer requisões e chamadas
 * SQL ao banco por meio da classe {@link DAO}, salvar dados brutos em objetos e prepara objetos
 * em conjunto para serem utilizados como relatório.<br>
 * Dá também uma resposta visual ao usuário, utilizando uma barra de progresso quando solicitada
 * uma extração de dados de um arquivo texto para o banco de dados.
 * 
 * @author Ramon Giovane
 * 
 * @see DAO
 * @see ArquivoTexto
 * @see IgBarraDeProgresso
 *
 */
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
	private long numeroDeLinhas;


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

	public ExtratorDeDados(BancoDeDados bd, Pesquisa pesquisa, String separador) {
		dao = new DAO(bd);
		this.separador = separador;
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
		abrirArquivo();
		numeroDeLinhas = contarLinhas();

		try {
			extrairPerguntas();
			extrairRespostas();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		fecharArquivo();

	}

	private void iniciarBarraDeProgresso() throws IOException {
		IgAvaliacaoInstitucional.desativarInterface();
		barraDeProgresso = new IgBarraDeProgresso(janelaPai, 
				InterfaceConstraints.TITULO_PROGRAMA, 
				"Lendo pesquisa...", "Isto pode levar alguns minutos...", "Quase lá...", numeroDeLinhas);
		
	}

	private void terminarBarraDeProgresso() {
		barraDeProgresso.fechar();
		IgAvaliacaoInstitucional.ativarInterface();
		EntradaESaida.msgInfo(janelaPai, "Pesquisa importada com sucesso!", 
				InterfaceConstraints.TITULO_PROGRAMA);
	}

	//TODO: Verificar o nome da pesquisa no banco de dados. Salvar se não existe, sinalizar se já existe
	public static void extrairDados(Component janelaPai, BancoDeDados banco, Pesquisa pesquisa) throws IOException{
		Calendar inicio = Calendar.getInstance();

		new ExtratorDeDados(janelaPai, banco, pesquisa).extrairDados();

		Calendar fim = Calendar.getInstance();

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
				/*
				 * Se ocorrer uma exceção no momento de extrair o tema, significa que não há tema, apenas uma pergunta.
				 * Ou um tema sem pergunta. Em outras palavras, eles deverão ser o mesmo.
				 */
				tema = strPerguntas[indice];
				questao = strPerguntas[indice];
			}

			try {
				retorno = dao.executarFuncao("inserir_pergunta", pesquisa.getCodigo(), questao, tema);

				indicesPerguntas[indice-3] = new IndicePergunta((int)retorno[0][0], (int) retorno[0][1]);

			} catch (SQLException e) {
				System.out.println(e.getErrorCode() + " " + e.getMessage());
			}
		}

	}

	private int contarLinhas() throws IOException {
		int i = 0;
		for(; arquivo.lerLinha() != null; i++);
		resetarArquivo();
		return i;
	}
	
	private void extrairRespostas() throws IOException, SQLException {

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				int codigoEntrevistado;
				String linha, respostas[];
				int contador = 0;
				
				try {
					
					iniciarBarraDeProgresso();
				
					do {

						/* 	Obtendo uma linha do arquivo, capturando a exceção e saindo do loop
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
				} catch (IOException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				terminarBarraDeProgresso();
				
			}
		});
		thread.start();
	}



	private void extrairLinhaResposta(String textoLinha, int codigoEntrevistado) throws SQLException {
		int i = 0;

		String word = null;
		for(int x = 0; x<3; x++) {
			textoLinha = textoLinha.substring(textoLinha.indexOf(";") + 1) + " ";
			word = textoLinha.substring(0,textoLinha.indexOf(";"));
		}


		while(true){
			dao.executarFuncao("inserir_resposta", pesquisa.getCodigo(), word.trim(), indicesPerguntas[i].getIndiceAssunto(),
					indicesPerguntas[i].getIndicePergunta(), codigoEntrevistado);

			textoLinha = textoLinha.substring(textoLinha.indexOf(";") + 1) + " ";


			try {
				word = textoLinha.substring(0,textoLinha.indexOf(";"));

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
	private String obterCabecalho() throws IOException {
		resetarArquivo();
		return arquivo.lerLinha();
	}


	/**Fecha o arquivo de texto (se aberto, estiver) e o abre novamente, a fim de ler o início do texto*/
	private void resetarArquivo() throws IOException {
		arquivo.fechar();
		abrirArquivo();
	}

	private void abrirArquivo() throws FileNotFoundException{
		arquivo.abrir(pesquisa.getCaminhoDataSet());
	}



	public RelatorioDeParticipantes gerarDataSetParticipantesCurso(Pesquisa pesquisa, TipoRelatorio tipoRelatorio) throws SQLException{
		RelatorioDeParticipantes dataSet = new RelatorioDeParticipantes(tipoRelatorio);

		//Consulta 1: Seleciona todos os cursos
		Object[][] objetos = dao.consultar("select codigo, descricao from curso");

		Curso curso;
		int i = 0;
		Object[][] resultado = new Object[0][0];

		for(; i<objetos.length; i++) {
			curso = new Curso((int)objetos[i][0], objetos[i][1].toString());

			//Consulta 2: obtém o número de entrevistados de todos cursos
			resultado = dao.consultar("select count(codcurso) from entrevistado where codcurso = ? and codpesquisa = ?", curso.getCodigo(), pesquisa.getCodigo());

			curso.setQuantidadeEntrevistados((long)resultado[0][0]);

			//Adiciona um curso no dataSet
			dataSet.adicionar(curso);
		}


		return dataSet;
	}

	public RelatorioDeParticipantes gerarDataSetParticipantesSegmento(Pesquisa pesquisa, TipoRelatorio tipoRelatorio) throws SQLException{
		RelatorioDeParticipantes dataSet = new RelatorioDeParticipantes(tipoRelatorio);

		//Consulta 1: Seleciona todos os cursos
		Object[][] objetos = dao.consultar("select codigo, descricao from segmento");

		Curso curso; //TODO: Trocar pra segmento
		int i = 0;
		Object[][] resultado = new Object[0][0];

		for(; i<objetos.length; i++) {
			curso = new Curso((int)objetos[i][0], objetos[i][1].toString());

			//Consulta 2: obtém o número de entrevistados de todos cursos
			resultado = dao.consultar("select count(codsegmento) from entrevistado where codsegmento = ? and codpesquisa = ?",
					curso.getCodigo(), pesquisa.getCodigo());

			curso.setQuantidadeEntrevistados((long)resultado[0][0]);

			//Adiciona um curso no dataSet
			dataSet.adicionar(curso);
		}


		return dataSet;
	}

	/**Retorna todos os assuntos de uma pesquisa específica em uma lista de Cursos*/
	private List<Curso> obterCursos(String tipoGraduacao) throws SQLException {
		List<Curso> cursosList = new ArrayList<Curso>();
		boolean cursosTecnologia = false;
		String termoDeBuscaSecundario = "";
		
		if(tipoGraduacao.equals("Técnicos e Tecnólogos")) {
			cursosTecnologia = true;
			tipoGraduacao =  "Tecnologia";
			termoDeBuscaSecundario = "T_cnico";
		}
		
		StringBuilder strBuilder =
				new StringBuilder("select codigo, descricao from curso ").append("where descricao ilike '%")
				.append(tipoGraduacao).append("%'");
		
		if(cursosTecnologia)
			strBuilder.append(" or descricao ilike '%").append(termoDeBuscaSecundario).append("%'");

		System.out.println(strBuilder.toString());
		Object[][] cursos = dao.consultar(strBuilder.toString());

		for(int i = 0; i<cursos.length; i++) {
			cursosList.add(new Curso((int)cursos[i][0], cursos[i][1].toString()));

		}

		return cursosList;
	}

	/**Retorna todos os assuntos de uma pesquisa específica em uma lista de Assuntos*/
	private List<Assunto> obterAssuntos(Pesquisa pesquisa) throws SQLException {
		List<Assunto> assuntosList = new ArrayList<Assunto>();


		Object[][] assuntos = 
				dao.consultar("select distinct assunto.codigo, assunto.descricao from assunto inner join assunto_pergunta on (assunto_pergunta.codassunto = assunto.codigo)" + 
						"where assunto_pergunta.codpesquisa = ?", pesquisa.getCodigo());

		for(int i = 0; i<assuntos.length; i++) {
			assuntosList.add(new Assunto((int)assuntos[i][0], 
					formataraDescricaoAssunto(assuntos[i][1].toString())));

		}

		return assuntosList;
	}

	
	private String formataraDescricaoAssunto(String assunto) {
		String novoAssunto;
		try{
			novoAssunto = assunto.split("Das condições \\w{2,3} ")[1].trim();
		}catch (Exception e) {
			try {
				novoAssunto = assunto.split("Em linhas gerais, como você avalia")[1].trim();
			}catch (Exception e2) {
				return assunto;
			}
		}
		

		novoAssunto = Character.toUpperCase(novoAssunto.charAt(0)) + novoAssunto.substring(1); 
		return novoAssunto;
	}
	public RelatorioDeMedias gerarDataSetConceitoMedioAssunto(Pesquisa pesquisa, String tipoGraduacao, TipoRelatorio tipoRelatorio) throws SQLException {

		
		double media;
		Object[][] resultado;

		//Obtendo todos os assuntos avaliados na pesquisa
		List<Assunto> assuntosList = obterAssuntos(pesquisa);

		//Obtendo uma lista de todos os cursos pertencentes ao tipo de graduação escolhido
		List<Curso> cursosList = obterCursos(tipoGraduacao);



		//Cria uma lista para armazenar a média de notas de cada assunto de cada curso
		RelatorioDeMedias listaDeMedias = new RelatorioDeMedias(tipoRelatorio);


		MediasPorCurso notas;

		for(Curso curso : cursosList) {

			notas = new MediasPorCurso(curso);


			for(Assunto assunto : assuntosList) {

				//Obtendo a média de nota de cada tema avaliado por entrevistados de um curso
				resultado = dao.consultar("select cast(avg(conceito.valor) as decimal) from conceito " + 
						"inner join resposta on (conceito.codigo = resposta.codconceito) " + 
						"inner join entrevistado on (entrevistado.codigo = resposta.codentrevistado) " + 
						"where entrevistado.codcurso = ? and resposta.codassunto = ? and resposta.codpesquisa = ?", 
						curso.getCodigo(), assunto.getCodigo(), pesquisa.getCodigo());
				
				if(resultado[0][0] != null) {
					media = ((BigDecimal)resultado[0][0]).doubleValue();

					//Adicionando a media associada a um assunto
					notas.adicionar(assunto, arredondarMedia(media));
				}

			}

			if(notas.tamanho() > 0) {
				//Adicionando as médias de um curso numa lista
				listaDeMedias.adicionar(notas);
			}

		}
		
		if(listaDeMedias.size() == 0)
			throw new NullPointerException("Nenhum dado de relatório encontrado com os parâmetros passados.");

		return listaDeMedias;
	}


	private double arredondarMedia(double media) {
		return (double) Math.round(media * 10) /10;
	}

	private class IndicePergunta{
		private int indiceAssunto, indicePergunta;

		public IndicePergunta(int indiceAssunto, int indicePergunta) {
			this.indiceAssunto = indiceAssunto;
			this.indicePergunta = indicePergunta;
		}

		public int getIndiceAssunto() {
			return indiceAssunto;
		}


		public int getIndicePergunta() {
			return indicePergunta;
		}

	}
}
