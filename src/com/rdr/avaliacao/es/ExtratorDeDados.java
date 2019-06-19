package com.rdr.avaliacao.es;

import static com.rdr.avaliacao.es.bd.constraints.FuncoesSQL.FUNCTION_INSERIR_ENTREVISTADO;
import static com.rdr.avaliacao.es.bd.constraints.FuncoesSQL.FUNCTION_INSERIR_PERGUNTA;
import static com.rdr.avaliacao.es.bd.constraints.FuncoesSQL.FUNCTION_INSERIR_RESPOSTA;
import static com.rdr.avaliacao.ig.InterfaceConstraints.*;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_BARRA_DE_PROGRESSO_2;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_BARRA_DE_PROGRESSO_3;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_PERGUNTA_VAZIA;
import static com.rdr.avaliacao.ig.InterfaceConstraints.MSG_PESQUISA_IMPORTADA_COM_SUCESSO;

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



	/**Cria um objeto extrator de dados com as configurações padrões.*/
	public ExtratorDeDados() {

		separador = SEPARADOR_PADRAO;

		arquivo = new ArquivoTexto();

		app = AvaliacaoInstitucional.getInstance();

		janelaPai = IgAvaliacaoInstitucional.getInstance();
	}

	/**Cria um objeto extrator de dados com um separador de colunas de arquivo diferente para os arquivos lidos.*/
	public ExtratorDeDados(String separadorDeColunasDeArquivo) {

		separador = separadorDeColunasDeArquivo;

		arquivo = new ArquivoTexto();

		app = AvaliacaoInstitucional.getInstance();

		janelaPai = IgAvaliacaoInstitucional.getInstance();
	}

	/**Obtém o objeto {@link DAO} que tem acesso ao banco de dados associado à classe.
	 * 
	 * @return um objeto {@link DAO}.
	 */
	public DAO getDao() {
		return dao;
	}


	/**Define o objeto que terá acesso ao banco de dados e será usado por esta classe
	 * 
	 * @param dao objeto {@link DAO} com uma conexão ativa ao banco, associado à classe
	 */
	public void setDao(DAO dao) {
		this.dao = dao;
	}

	/**Define o seprador de colunas dos arquivos de texto associado à  classe*/
	public String getSeparador() {
		return separador;
	}

	/**Obtém o seprador de colunas dos arquivos de texto associado à classe*/
	public void setSeparador(String separador) {
		this.separador = separador;
	}


	/**Quebra um texto de acordo com o separador de colunas associado à classe, retornando as partes divididas.
	 * 
	 * @param texto <code>String</code> com o texto na íntegra a ser quebrado
	 * @return um array de <code>String</code> contendo os elementos obtidos na quebra do texto
	 * @throws IOException se ocorrer um erro na quebra, como por exemplo, o texto não possui ocorrências do delimitador
	 * fornecido.
	 */
	private String[] quebrarTexto(String texto) throws IOException {

		return texto.split(separador); 

	}

	/**Fecha o objeto {@link ArquivoTexto} associado à classe. Deve ser chamado após qualquer uso 
	 * de entrada e saída através do objeto.
	 * 
	 * @throws IOException se o arquivo já estiver fechado.
	 */
	private void fecharArquivo() throws IOException {
		arquivo.fechar();
	}

	/**Extrai os dados de uma pesquisa (do seu respectivo arquivo de texto) e os salva no banco de dados.
	 * 
	 * @param pesquisa objeto {@link Pesquisa} contendo entre seus atributo um caminho para um arquivo válido.
	 * @throws IOException se ocorrer um erro na extração dos dados do arquivo.
	 * @throws SQLException se ocorrer um erro na importação dos dados um banco
	 */
	public void extrairDados(Pesquisa pesquisa) throws IOException, SQLException {

		//Associa o objeto pesquisa da classe à pesquisa passada por parâmetro
		this.pesquisa = pesquisa;

		abrirArquivo();
		
		//Conta e gurada o número de linhas total do arquivo
		numeroDeLinhas = contarLinhas();

		//Extrai e salva as perguntas (o cabeçalho)
		extrairPerguntas();
		
		//Extrai e salva as perguntas e entrevistados (as linhas subsequentes)
		extrairRespostas();
		
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
			EntradaESaida.msgInfo(janelaPai, MSG_PESQUISA_IMPORTADA_COM_SUCESSO, TITULO_PROGRAMA);
		}
		//Se todas as linhas foram ignoradas
		else if(linhasIgnoradas == totalDeLinhas) {
			EntradaESaida.msgErro(janelaPai, MSG_IMPORTACAO_MAL_SUCEDIDA, TITULO_PROGRAMA);
			app.apagarPesquisa(pesquisa);
		}

		//Se algumas linhas foram ignoradas
		else {
			EntradaESaida.msgAlerta(janelaPai, String.format(MSG_PESQUISA_IMPORTADA_PARCIALMENTE + linhasIgnoradas + 
					MSG_LINHAS_IGNORADAS, linhasIgnoradas), InterfaceConstraints.TITULO_PROGRAMA);
		}

		//Reativando a interface depois do processamento
		IgAvaliacaoInstitucional.ativarInterface();
	}

	/**
	 * Extrai o cabeçalho do arquivo texto que contém as perguntas de uma pesquisa (que está associada à classe) e as salvas no banco de dados.
	 *  
	 * @throws IOException se ocorrer um erro na extração do arquivo
	 */
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
		for(; indice<strPerguntas.length; indice++) {
			if(strPerguntas[indice].isEmpty()) {
				cancelarImportacao(MSG_PERGUNTA_VAZIA);
				throw new IOException(MSG_PERGUNTA_VAZIA);
			}
		
			
			//Retira os números e símbolos do inicio dos assuntos e perguntas
			strPerguntas[indice] = retirarSimbolosInicioPergunta(strPerguntas[indice]);
		
			
			//Tentando separar o tema da pergunta(a.k.a questao)
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
			
			//Insere de fato a pergunta
			inserirPergunta(tema, questao, indice);
		}
	}

	/**Insere uma pergunta no banco de dados.
	 * 
	 * @param tema da pergunta (valor entre colchetes) se não houver, será considerado o mesmo valor que a questao (a.k.a) pergunta
	 * @param questao pergunta a ser inserida sobre o tema especificado anteriormente
	 * @param indicePerguntaAtual indice da classe de índices {@link IndicePergunta} da qual será retirada uma pergunta e um tema
	 * @throws IOException se ocorrer um erro na inserção no banco
	 */
	private void inserirPergunta(String tema, String questao, int indicePerguntaAtual) throws IOException {
		try {
			Object[][] retorno = dao.executarFuncao(FUNCTION_INSERIR_PERGUNTA, pesquisa.getCodigo(), questao, tema);

			indicesPerguntas[indicePerguntaAtual-3] = new IndicePergunta((int)retorno[0][0], (int) retorno[0][1]);

		} catch (SQLException e) {
			cancelarImportacao(InterfaceConstraints.MSG_PERGUNTA_REPETIDA);
			throw new IOException(InterfaceConstraints.MSG_PERGUNTA_REPETIDA);
		}
	}

	/**Tenta retirar os números no texto de uma pergunta
	 * 
	 * @param string a ser formatada
	 * @return a string formatada.
	 */
	private String retirarSimbolosInicioPergunta(String string) {
		string = string.replace(" - ", ".");
		string = string.replaceAll("\\d{1,}[.]", "").trim();
		return string;
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


	/**Apaga uma pesquisa e do banco e tudo associado à ela
	 * 
	 * @throws SQLException se a pesquisa não existir ou acontecer um erro
	 */
	private void apagarPesquisaBanco() throws SQLException {
		dao.consultar("delete from pesquisa cascade where codigo = ?", pesquisa.getCodigo());
	}

	/**Contas as linhas de um arquivo de texto. Isto é necessário para se ter noção de quantos processamentos é preciso
	 * realizar para completar o todo o trabalho. Útil para usar com a classe {@link IgBarraDeProgresso}.
	 * @return o número de linhas do 
	 * @throws IOException
	 */
	private int contarLinhas() throws IOException {
		int i = 0;
		for(; arquivo.lerLinha() != null; i++);
		System.out.println("lINHAS " + i);
		resetarArquivo();
		return i;
	}

	/**Extrai do arquivo as respostas (conteúdo após o cabeçalho).
	 * 
	 * @throws IOException se ocorrer algum erro na extração do arquivo
	 * @throws SQLException se ocorrer algum erro na importação ao banco
	 */
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

					//Constrói e exibe a barra de progresso vazia
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



	/**Recebe uma linha contendo todas as respostas de um entrevistado. Ignora os dados do mesmo, já processados
	 * e salva no banco as respostas com os conceitos dados por ele. Se a quantidade de respostas for diferente da quantidade
	 * de perguntas passadas, dispara uma exceção sinalizando que nada desta linha foi salvo e ela deve ser ignorada.
	 * 
	 * @param textoLinha conteúdo da linha, com os dados do entrevistado e as respostas em si.
	 * @param codigoEntrevistado código do entrevistado a qual pertence as respostas
	 * @throws SQLException se um erro ocorrer na inserção ao banco
	 * @throws IllegalArgumentException se as respostas dadas não condizem com as perguntas da pesquisa.
	 */
	private void extrairLinhaResposta(String textoLinha, int codigoEntrevistado) throws SQLException, IllegalArgumentException {
		int i = 0, numeroPerguntas = indicesPerguntas.length+3;
		
		StringTokenizer tokenizer = new StringTokenizer(textoLinha, separador);
		
		int numeroToknes = tokenizer.countTokens();
		
		//Ignorando as identificacoes do entrevistado
		for(int x = 0; x<3; x++)
			tokenizer.nextElement();

		//Se o número de perguntas não for igual ao número de tokens, ignora a linha disparando uma exceção
		if(numeroToknes != numeroPerguntas)
			throw new IllegalArgumentException(MSG_EXCECAO_RESPOSTAS_NAO_CONDIZEM);

		String resposta;
		while (tokenizer.hasMoreElements()) {
			
			resposta = tokenizer.nextElement().toString();
			
			dao.executarFuncao(FUNCTION_INSERIR_RESPOSTA, pesquisa.getCodigo(), resposta, indicesPerguntas[i].getIndiceAssunto(),
					indicesPerguntas[i++].getIndicePergunta(), codigoEntrevistado);
			
		}
	}


	/** Recebe um array com dados do entrevistado como <code>String</code>. Valida os campos e os insere no banco.
	 * 
	 * @param textoLinhas dados do entrevistado (segmento, campus e curso) em um array de <code>String</code>.
	 * @return o código do entrevistado recém inserido
	 * @throws SQLException se um erro ocorrer na inserção ao banco
	 * @throws IllegalArgumentException se as respostas dadas não condizem com as perguntas da pesquisa.
	 */
	private int extrairEntrevistado(String[] textoLinhas) throws IOException, SQLException {

		int codigoEntrevistado;
		String segmento = textoLinhas[0];
		String campus = textoLinhas[1]; 

		if(segmento.isEmpty() || campus.isEmpty()) throw new IllegalArgumentException(MSG_EXCECAO_CAMPO_VAZIO);

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

	/**Abre um arquivo de texto para leitura dos dados, utilizando o objeto da classe {@link ArquivoTexto} associado à esta*/
	private void abrirArquivo() throws NullPointerException, IOException{
		arquivo.abrir(pesquisa.getCaminhoDataSet());
	}



	/**
	 * Importa do banco de dados um relatório de participantes por curso, encapsulados
	 * em um objeto do tipo {@link RelatorioDeParticipantes}.
	 * 
	 * @param tipoRelatorio enumeração que contém informações do tipo do relatório a ser gerado
	 * @return
	 * @throws SQLException se ocorrer um erro relacionado ao banco de dados
	 */
	public RelatorioDeParticipantes gerarRelatorioParticipantesCurso(TipoRelatorio tipoRelatorio) throws SQLException{
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


	/**
	 * Importa do banco de dados um relatório de participantes por segmento, encapsulados
	 * em um objeto do tipo {@link RelatorioDeParticipantes}.
	 * 
	 * @param tipoRelatorio enumeração que contém informações do tipo do relatório a ser gerado
	 * @return um objeto do tipo {@link RelatorioDeParticipantes}
	 * @throws SQLException se ocorrer um erro relacionado ao banco de dados
	 */
	public RelatorioDeParticipantes gerarRelatorioParticipantesSegmento(TipoRelatorio tipoRelatorio) throws SQLException{
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

	/**Retorna em um {@link List}  de {@link Segmento} todos os segmentos armazenados no banco de dados
	 * 
	 * @return uma lista de segmentos
	 * @throws SQLException
	 */
	private List<Segmento> obterSegmentos() throws SQLException {
		List<Segmento> segmentosList = new ArrayList<Segmento>();

		Object[][] segmentos = dao.consultar("select codigo, descricao from segmento");

		for(int i = 0; i<segmentos.length; i++) {
			segmentosList.add(new Segmento((int)segmentos[i][0], segmentos[i][1].toString()));

		}
		return segmentosList;
	}

	/**Retorna em um {@link List}  de {@link Curso} todos os cursos armazenados no banco de dados.
	 * A pesquisa pode ser filtrada pelo tipo (modalidade) de curso.
	 * 
	 * @param tipoGraduacao uma <code>String</code> com a modalidade dos cursos a serem obtidos. Exemplo: 'bacharelado'
	 * <br>(<code>null</code> pode ser passado para ignorar este filtro).
	 * @return uma lista de cursos
	 * @throws SQLException se ocorrer um erro relacionado ao banco de dados
	 */
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

	/**Retorna em um {@link List}  de {@link Assunto}, todos os assuntos de uma pesquisa específica armazenados no banco de dados
	 * @param pesquisa contendo o código da pesquisa associada aos assuntos a serem obtidos.
	 * @return uma lista de segmentos
	 * @throws SQLException
	 */
	private List<Assunto> obterAssuntos(Pesquisa pesquisa) throws SQLException {
		List<Assunto> assuntosList = new ArrayList<Assunto>();


		Object[][] assuntos = 
				dao.consultar("select distinct assunto.codigo, assunto.descricao from assunto inner join assunto_pergunta on (assunto_pergunta.codassunto = assunto.codigo)" + 
						"where assunto_pergunta.codpesquisa = ?", pesquisa.getCodigo());

		for(int i = 0; i<assuntos.length; i++) {
			assuntosList.add(new Assunto((int)assuntos[i][0], 
					formatarDescricaoAssunto(assuntos[i][1].toString())));


		}

		return assuntosList;
	}


	/**Tenta simplificar as descrições de um assunto.
	 * <b>Importante:</b> Notável queda de performance ao utilizar essa tentativa de simplificação. Não deve ser usado
	 * na extração dos dados do arquivo texto.
	 * @param assunto descrição do assunto a ser formatado
	 * @return a descrição formatada. Se não for possível formatá-la, retorna o conteúdo original.
	 */
	private String formatarDescricaoAssunto(String assunto) {
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
	
	

	/**
	 * Importa do banco de dados um relatório de médias de notas por um tipo de curso, encapsulados
	 * em um objeto do tipo {@link RelatorioDeMedias}.
	 * 
	 * @param tipoGraduacao descrição em formato de <code>String</code> da modalidade dos cursos que serão obtidos no relatório
	 * @param tipoRelatorio enumeração que contém informações do tipo do relatório a ser gerado
	 * @return um objeto do {@link RelatorioDeMedias}.
	 * @throws SQLException se ocorrer um erro relacionado ao banco de dados
	 */
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
			throw new NullPointerException(MSG_EXCECAO_NENHUM_DADO_ENCONTRADO);

		return listaDeMedias;
	}



	/**
	 * Importa do banco de dados um relatório de médias de notas por segmento, encapsulados
	 * em um objeto do tipo {@link RelatorioDeMedias}.
	 * 
	 * @param tipoRelatorio enumeração que contém informações do tipo do relatório a ser gerado
	 * @return um objeto do {@link RelatorioDeMedias}.
	 * @throws SQLException se ocorrer um erro relacionado ao banco de dados
	 */
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

			
			for(Assunto assunto : assuntosList) {

				//Obtendo a média de nota de cada tema avaliado por entrevistados de um curso
				resultado = dao.consultar("select cast(avg(conceito.valor) as decimal) from conceito " + 
						"inner join resposta on (conceito.codigo = resposta.codconceito) " + 
						"inner join entrevistado on (entrevistado.codigo = resposta.codentrevistado) " + 
						"where entrevistado.codsegmento = ? and resposta.codassunto = ? and resposta.codpesquisa = ?", 
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


	/**Arredonda uma média (<code>double</code>) para um valor com uma casa decimal*/
	private double arredondarMedia(double media) {
		return (double) Math.round(media * 10) /10;
	}

	/**Recebe um objeto curso e tenta formatar sua descrição associada se for um curso técnico.
	 * 
	 * @param curso objeto {@link Curso} com uma descrição. Se não for possível, a descrição do curso passada permance inalterada
	 */
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
	
	/**Classe que contém o índices dos assuntos (a.k.a temas) e perguntas (a.k.a questoes ou subtemas)
	 * inseridos no banco durante na extração dos dados.
	 * @author RamonGiovane
	 *
	 */
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
