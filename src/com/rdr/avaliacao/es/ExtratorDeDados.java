package com.rdr.avaliacao.es;

import static com.rdr.avaliacao.es.bd.constraints.FuncoesSQL.FUNCTION_INSERIR_ENTREVISTADO;
import static com.rdr.avaliacao.es.bd.constraints.FuncoesSQL.FUNCTION_INSERIR_PERGUNTA;
import static com.rdr.avaliacao.es.bd.constraints.FuncoesSQL.FUNCTION_INSERIR_RESPOSTA;
import static com.rdr.avaliacao.ig.InterfaceConstraints.*;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_BARRA_DE_PROGRESSO_2;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_BARRA_DE_PROGRESSO_3;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_PERGUNTA_VAZIA;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_PESQUISA_IMPORTADA;

import java.awt.Component;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.rdr.avaliacao.AvaliacaoInstitucional;
import com.rdr.avaliacao.es.bd.DAO;
import com.rdr.avaliacao.es.bd.Recuperacao;
import com.rdr.avaliacao.ig.InterfaceConstraints;
import com.rdr.avaliacao.ig.TipoRelatorio;
import com.rdr.avaliacao.ig.janelas.IgAvaliacaoInstitucional;
import com.rdr.avaliacao.ig.janelas.IgBarraDeProgresso;
import com.rdr.avaliacao.questionario.Assunto;
import com.rdr.avaliacao.questionario.Curso;
import com.rdr.avaliacao.questionario.Pesquisa;
import com.rdr.avaliacao.questionario.Segmento;
import com.rdr.avaliacao.relatorio.MediasPorCurso;
import com.rdr.avaliacao.relatorio.MediasPorSegmento;
import com.rdr.avaliacao.relatorio.RelatorioDeMedias;
import com.rdr.avaliacao.relatorio.RelatorioDeParticipantes;
/**
 * Esta classe realiza o intermédio entre os dispositivos de armazenamento e as classes e objetos.
 * É responsável por todo o trabalho pesado: ler arquivos de texto, fazer requisões e chamadas
 * SQL ao banco por meio da classe {@link DAO}, salvar dados brutos em objetos e preparar objetos
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
	private String separador;
	private ArquivoTexto arquivo; 
	private DAO dao;

	private IndicePergunta indicesPerguntas[];

	private Pesquisa pesquisa;

	private IgBarraDeProgresso barraDeProgresso;

	private Component janelaPai;

	private long numeroDeLinhas;

	private static String STR_CURSOS_TECNICOS = "Cursos Técnicos", STR_TECNICO = "Técnico";

	private AvaliacaoInstitucional app; 



	public ExtratorDeDados() {

		separador = SEPARADOR_PADRAO;

		arquivo = new ArquivoTexto();

		app = AvaliacaoInstitucional.getInstance();

		janelaPai = IgAvaliacaoInstitucional.getInstance();
	}

	public ExtratorDeDados(String separadorArquivo) {

		separador = separadorArquivo;

		arquivo = new ArquivoTexto();

		app = AvaliacaoInstitucional.getInstance();

		janelaPai = IgAvaliacaoInstitucional.getInstance();
	}

	public DAO getDao() {
		return dao;
	}


	public void setDao(DAO dao) {
		this.dao = dao;
	}

	public String getSeparador() {
		return separador;
	}

	public void setSeparador(String separador) {
		this.separador = separador;
	}


	private String[] quebrarTexto(String texto) throws IOException {

		return texto.split(separador); 

	}

	private void fecharArquivo() throws IOException {
		arquivo.fechar();
	}

	public void extrairDados(Pesquisa pesquisa) throws IOException, SQLException {

		this.pesquisa = pesquisa;

		abrirArquivo();
		numeroDeLinhas = contarLinhas();

		//	try {
		extrairPerguntas();
		
		extrairRespostas();
		//		} catch (SQLException e) {
		//			e.printStackTrace();
		//		}

		fecharArquivo();

	}

	/**Cria a barra de progresso e esconde a janela principal*/
	private void iniciarBarraDeProgresso() throws IOException {
		//Esconde a janela principal quando a barra é criada
		IgAvaliacaoInstitucional.desativarInterface();

		//Cria a barra de progresso, passando as mensagens que passarão durante a importação
		barraDeProgresso = new IgBarraDeProgresso(janelaPai, 
				InterfaceConstraints.TITULO_PROGRAMA, MSG_BARRA_DE_PROGRESSO_1, 
				MSG_BARRA_DE_PROGRESSO_2 , MSG_BARRA_DE_PROGRESSO_3 , numeroDeLinhas);

	}

	/**Fecha a barra de progresso, reativa a janela principal, se não estiver ativada, e exibe uma mensagem 
	 * de sucesso na importação.
	 */
	private void terminarExtracao(int linhasIgnoradas, int totalDeLinhas) {
		barraDeProgresso.fechar();

		//Se nenhuma foi ignorada
		if(linhasIgnoradas == 0) {
			EntradaESaida.msgInfo(janelaPai, MSG_PESQUISA_IMPORTADA, 
					InterfaceConstraints.TITULO_PROGRAMA);
		}
		//Se todas as linhas foram ignoradas
		else if(linhasIgnoradas == totalDeLinhas) {
			EntradaESaida.msgErro(janelaPai, "ERRO: A importação foi mal sucedida porque as respostas do entrevistados não condizem com o cabeçalho de perguntas.",
					InterfaceConstraints.TITULO_PROGRAMA);
			app.apagarPesquisa(pesquisa);
		}

		//Se algumas linhas foram ignoradas
		else {
			EntradaESaida.msgAlerta(janelaPai, String.format("A pesquisa for importada.\nNo entanto %s linha(s) ignorada(s) durante o processo porque não "
					+ "condiziam com o cabeçalho de perguntas.", linhasIgnoradas), InterfaceConstraints.TITULO_PROGRAMA);
		}

		IgAvaliacaoInstitucional.ativarInterface();
	}

	private void extrairPerguntas() throws IOException {

		//Obtém o texto do cabeçalho
		String texto = obterCabecalho();
		
		//Cancela a importação se estiver vazio
		if(texto == null) {
			cancelarImportacao(MSG_ARQUIVO_VAZIO);
			throw new IOException();
		}
		
		String strPerguntas[] =  quebrarTexto(texto); //!!! must check this

		indicesPerguntas = new IndicePergunta[strPerguntas.length-3];

		//Ignora as colunas de identificacao do entrevistado
		int indice = 3;
		String tema, questao;
		Object[][] retorno;
		for(; indice<strPerguntas.length; indice++) {
			if(strPerguntas[indice].isEmpty()) {
				cancelarImportacao(MSG_PERGUNTA_VAZIA);
				throw new IOException(MSG_PERGUNTA_VAZIA);
			}
			strPerguntas[indice] = strPerguntas[indice].replace(" - ", ".");
			strPerguntas[indice] = strPerguntas[indice].replaceAll("\\d{1,}[.]", "").trim();
			try{
				tema = strPerguntas[indice].substring(0,
						strPerguntas[indice].indexOf('[')).trim();

				questao = strPerguntas[indice].substring(strPerguntas[indice].indexOf('[')+1,
						strPerguntas[indice].indexOf(']')).trim();
			}catch(StringIndexOutOfBoundsException e) {
				/*
				 * Se ocorrer esta exceção no momento de extrair o tema, significa que não há tema, apenas uma pergunta.
				 * Ou um tema sem pergunta. Em outras palavras, eles deverão ser o mesmo.
				 */
				tema = strPerguntas[indice];
				questao = strPerguntas[indice];
			}
			try {
				retorno = dao.executarFuncao(FUNCTION_INSERIR_PERGUNTA, pesquisa.getCodigo(), questao, tema);

				indicesPerguntas[indice-3] = new IndicePergunta((int)retorno[0][0], (int) retorno[0][1]);

			} catch (SQLException e) {
				cancelarImportacao(InterfaceConstraints.MSG_PERGUNTA_REPETIDA);
				throw new IOException(InterfaceConstraints.MSG_PERGUNTA_REPETIDA);
			}
		}
		System.out.println(indicesPerguntas.length);
	}

	/**Exibe uma mensagem de erro e cancela a importação dos dados. Tenta apagar pesquisa criada*/
	private void cancelarImportacao(String mensagemDeErro) {
		EntradaESaida.msgErro(janelaPai, mensagemDeErro, InterfaceConstraints.TITULO_IMPORTAR_DADOS);
		app.apagarPesquisa(pesquisa);
		try {
			apagarPesquisaBanco();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	private void apagarPesquisaBanco() throws SQLException {
		dao.consultar("delete from pesquisa cascade where codigo = ?", pesquisa.getCodigo());
	}

	private int contarLinhas() throws IOException {
		int i = 0;
		for(; arquivo.lerLinha() != null; i++);
		resetarArquivo();
		return i;
	}

	private void extrairRespostas() throws IOException, SQLException {

		/*É preciso criar uma thread aqui para processar as respostas, pois ao incrementar a barra de progresso,
		 * a EDT que é a mesma que a thread main travará, esperando que a caixa de diálogo com a barra seja
		 * fechada, travando o programa na chamada do método barraDeProgresso.incrementar().
		 */
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				int codigoEntrevistado;
				String linha, respostas[];
				int contador = 0;

				//Contador de quantas linhas tiveram dados inconsistentes e as respostas não foram armazenadas
				int linhasIgnoradas = 0;

				try {

					iniciarBarraDeProgresso();

					do {

						//Obtendo uma linha do arquivo
						linha = arquivo.lerLinha();

						barraDeProgresso.incrementar(contador++);

						//Se a linha for nula, significa que a importação terminou 
						if(linha == null ) continue; 

						//Se estiver vazia, incrementa o número de linhas ignoradas e pula os processamentos seguintes
						if(linha.isEmpty()) { linhasIgnoradas++; continue; }

						//Dividindo a linha e strings com cada coluna
						respostas = quebrarTexto(linha);

						//Extrai e salva no banco de dados o entrevistado, obtendo o código do entrevistado salvo no banco
						try{ codigoEntrevistado = extrairEntrevistado(respostas);
						}catch (Exception e) {
							//Se acontecer algum erro ao extrair o entrevistado, pula a leitura de respostas
							linhasIgnoradas++;
							continue;
						}

						//Extrai e salva no banco de dados as respostas de um entrevistado.
						try{ 
							extrairLinhaResposta(linha, codigoEntrevistado);
						
						}catch (IllegalArgumentException e) {
							linhasIgnoradas++;
							continue;
						}


					}while(linha != null);
				} catch (IOException | SQLException e) {
					Thread.currentThread().interrupt();
					e.printStackTrace();
				}
				terminarExtracao(linhasIgnoradas, contador);

			}
		});

		thread.start();
		if(thread.isInterrupted()) throw new SQLException();
	}



	
	private void extrairLinhaResposta(String textoLinha, int codigoEntrevistado) throws SQLException, IllegalArgumentException {
		int i = 0, numeroPerguntas = indicesPerguntas.length+3;
		
		StringTokenizer tokenizer = new StringTokenizer(textoLinha, separador);
		
		int numeroToknes = tokenizer.countTokens();
		
		//Ignorando as identificacoes do entrevistado
		for(int x = 0; x<3; x++)
			tokenizer.nextElement();

		//Se o número de perguntas não for igual ao número de tokens, ignora a linha disparando uma exceção
		if(numeroToknes != numeroPerguntas)
			throw new IllegalArgumentException("Respostas não condizem com o cabeçalho");

		String resposta;
		while (tokenizer.hasMoreElements()) {
			
			resposta = tokenizer.nextElement().toString();
			
			dao.executarFuncao(FUNCTION_INSERIR_RESPOSTA, pesquisa.getCodigo(), resposta, indicesPerguntas[i].getIndiceAssunto(),
					indicesPerguntas[i++].getIndicePergunta(), codigoEntrevistado);
			System.out.println(resposta);
			
		}


	}


	private int extrairEntrevistado(String[] textoLinhas) throws IOException, SQLException {

		int codigoEntrevistado;
		String segmento = textoLinhas[0];
		String campus = textoLinhas[1]; 

		if(segmento.isEmpty() || campus.isEmpty()) throw new IllegalArgumentException("Campo vazio detectado");

		/*
		 * Executando a função SQL para inserção (que recebe o codigo da pesquisa, segmento, campus e curso) e 
		 * retorna um inteiro (código do entrevistado inserido).
		 * Todos os retornos de instrunções SQL segundo a classe DAO retornam uma matriz que contém os nomes (se houver)
		 * dos campos e os valores de retorno em forma de Object. 
		 * 
		 * Se o  campo segmento for discente, o campo curso não é salvo (passa-se null).
		 */

		Object resultado [][] = dao.executarFuncao(FUNCTION_INSERIR_ENTREVISTADO, pesquisa.getCodigo(), segmento,
				campus, segmento.equals(Segmento.DISCENTE) ? textoLinhas[2] : null);


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

	private void abrirArquivo() throws NullPointerException, IOException{
		arquivo.abrir(pesquisa.getCaminhoDataSet());
	}



	public RelatorioDeParticipantes gerarDataSetParticipantesCurso(TipoRelatorio tipoRelatorio) throws SQLException{
		RelatorioDeParticipantes dataSet = new RelatorioDeParticipantes(tipoRelatorio);

		pesquisa = app.getPesquisaAtiva();

		//Consulta 1: Seleciona todos os cursos de todos os segmentos
		List<Curso> cursos = obterCursos(null);

		Object[][] resultado = new Object[0][0];

		for(Curso curso : cursos) {

			//Consulta 2: obtém o número de entrevistados de todos cursos
			resultado = dao.consultar("select count(codcurso) from entrevistado where codcurso = ? and codpesquisa = ?", curso.getCodigo(), pesquisa.getCodigo());


			//Adiciona a quantidade entrevistados a partir do resultado da consulta
			curso.setQuantidadeEntrevistados((long)resultado[0][0]);

			//Formata o nome do curso se possível
			formatarNomeCursoTecnico(curso);

			if(curso.getQuantidadeEntrevistados() > 0) {
				//Adiciona um curso no dataSet
				dataSet.adicionar(curso);
			}
		}


		return dataSet;
	}

	public RelatorioDeParticipantes gerarDataSetParticipantesSegmento(TipoRelatorio tipoRelatorio) throws SQLException{
		RelatorioDeParticipantes dataSet = new RelatorioDeParticipantes(tipoRelatorio);

		pesquisa = app.getPesquisaAtiva();

		//Consulta 1: Seleciona todos os segmentos
		List<Segmento> objetos = obterSegmentos();

		Object[][] resultado = new Object[0][0];

		for(Segmento segmento : objetos) {

			//Consulta 2: obtém o número de entrevistados de todos cursos
			resultado = dao.consultar("select count(codsegmento) from entrevistado where codsegmento = ? and codpesquisa = ?",
					segmento.getCodigo(), pesquisa.getCodigo());

			segmento.setQuantidadeEntrevistados((long)resultado[0][0]);

			if(segmento.getQuantidadeEntrevistados() > 0) {
				//Adiciona um segmento no dataSet
				dataSet.adicionar(segmento);
			}
		}


		return dataSet;
	}

	private List<Segmento> obterSegmentos() throws SQLException {
		List<Segmento> segmentosList = new ArrayList<Segmento>();

		Object[][] segmentos = dao.consultar("select codigo, descricao from segmento");

		for(int i = 0; i<segmentos.length; i++) {
			segmentosList.add(new Segmento((int)segmentos[i][0], segmentos[i][1].toString()));

		}
		return segmentosList;
	}

	/**Retorna todos os assuntos de uma pesquisa específica em uma lista de Cursos*/
	private List<Curso> obterCursos( String tipoGraduacao) throws SQLException {
		List<Curso> cursosList = new ArrayList<Curso>();
		boolean cursosTecnologia = false;
		String termoDeBuscaSecundario = "";

		//Preparando a query básica
		StringBuilder strBuilder = 	new StringBuilder("select codigo, descricao from curso");

		//Se for passado um tipo de graduação, adiciona este filtro na query
		if(tipoGraduacao != null) {
			if(tipoGraduacao.equals("Técnicos e Tecnólogos")) {
				cursosTecnologia = true;
				tipoGraduacao =  "Tecnologia";
				termoDeBuscaSecundario = "T_cnico";
			}

			strBuilder.append(" where descricao ilike '%")
			.append(tipoGraduacao).append("%'");

			if(cursosTecnologia)
				strBuilder.append(" or descricao ilike '%").append(termoDeBuscaSecundario).append("%'");
		}


		//Realiza a consulta
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


	/**Tenta simplificar as descrições de um assunto.
	 * <b>Importante:</b> Notável queda de performance ao utilizar essa tentativa de simplificação. Não deve ser usado
	 * na extração dos dados do arquivo texto.
	 * @param assunto descrição do assunto a ser formatado
	 * @return a descrição formatada. Se não for possível formatá-la, retorna o conteúdo original.
	 */
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
	public RelatorioDeMedias gerarRelatorioDeMediasPorCurso(String tipoGraduacao, TipoRelatorio tipoRelatorio) throws SQLException {

		pesquisa = app.getPesquisaAtiva();

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

			int i = 0;
			System.out.println(assuntosList.size());
			for(Assunto assunto : assuntosList) {

				//Obtendo a média de nota de cada tema avaliado por entrevistados de um curso
				resultado = dao.consultar("select cast(avg(conceito.valor) as decimal) from conceito " + 
						"inner join resposta on (conceito.codigo = resposta.codconceito) " + 
						"inner join entrevistado on (entrevistado.codigo = resposta.codentrevistado) " + 
						"where entrevistado.codcurso = ? and resposta.codassunto = ? and resposta.codpesquisa = ?", 
						curso.getCodigo(), assunto.getCodigo(), pesquisa.getCodigo());
				System.err.printf("\n%s: %s", i++, assunto.getCodigo());
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



	public RelatorioDeMedias gerarRelatorioDeMediasPorSegmento(TipoRelatorio tipoRelatorio) throws SQLException {

		pesquisa = app.getPesquisaAtiva();

		double media;
		Object[][] resultado;

		//Obtendo todos os assuntos avaliados na pesquisa
		List<Assunto> assuntosList = obterAssuntos(pesquisa);

		//Obtendo uma lista de todos os cursos pertencentes ao tipo de graduação escolhido
		List<Segmento> segmentosList = obterSegmentos();



		//Cria uma lista para armazenar a média de notas de cada assunto de cada curso
		RelatorioDeMedias listaDeMedias = new RelatorioDeMedias(tipoRelatorio);


		MediasPorSegmento notas;

		for(Segmento curso : segmentosList) {

			notas = new MediasPorSegmento(curso);

			int i = 0;
			System.out.println(assuntosList.size());
			for(Assunto assunto : assuntosList) {

				//Obtendo a média de nota de cada tema avaliado por entrevistados de um curso
				resultado = dao.consultar("select cast(avg(conceito.valor) as decimal) from conceito " + 
						"inner join resposta on (conceito.codigo = resposta.codconceito) " + 
						"inner join entrevistado on (entrevistado.codigo = resposta.codentrevistado) " + 
						"where entrevistado.codsegmento = ? and resposta.codassunto = ? and resposta.codpesquisa = ?", 
						curso.getCodigo(), assunto.getCodigo(), pesquisa.getCodigo());
				System.err.printf("\n%s: %s", i++, assunto.getCodigo());
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

	public static void formatarNomeCursoTecnico(Curso curso) {
		if(curso.getDescricao().equals(STR_TECNICO))
			curso.setDescricao(STR_CURSOS_TECNICOS);
	}


	/**Retora em um {@link List} todas as pesquisas existentes no banco de dados.
	 * 
	 * @return uma lista de objetos {@link Pesquisa}.
	 * @throws SQLException se ocorrer um erro de conexão ou no momento da consulta
	 */
	public List<Pesquisa> obterPesquisas() throws SQLException {

		List<Pesquisa> pesquisasList = new ArrayList<Pesquisa>();

		/*
		 * Seleciona as pesquisas implementando os métodos da classe recuperação, para uma forma
		 * de consulta paralela ao padrão da classe Pesquisa
		 */
		Object[][] pesquisas = dao.consultar(new Recuperacao() {
			@Override
			public String selectQuery() {
				return "select * from pesquisa";
			}

			@Override
			public Object[] searchKeys() {
				return null;
			}
		});


		//Copiando o resultado da consulta dentro da lista
		for(int i=0; i<pesquisas.length; i++){
			pesquisa = new Pesquisa();
			pesquisa.setCodigo((int)pesquisas[i][0]);
			pesquisa.setNome(pesquisas[i][1].toString());

			pesquisasList.add(pesquisa);
		}


		return pesquisasList;
	}

}
