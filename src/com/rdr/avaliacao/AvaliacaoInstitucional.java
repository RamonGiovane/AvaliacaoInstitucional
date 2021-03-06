package com.rdr.avaliacao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.rdr.avaliacao.es.EntradaESaida;
import com.rdr.avaliacao.es.ExtratorDeDados;
import com.rdr.avaliacao.es.bd.BancoDeDados;
import com.rdr.avaliacao.es.bd.DAO;
import com.rdr.avaliacao.ig.InterfaceConstraints;
import com.rdr.avaliacao.ig.TipoRelatorio;
import com.rdr.avaliacao.ig.janelas.IgAvaliacaoInstitucional;
import com.rdr.avaliacao.questionario.Pesquisa;
import com.rdr.avaliacao.relatorio.Relatorio;

/**Classe principal do programa de Avaliação Institucional. Nesta classe encontra-se o método main.
 * Nenhuma outra classe pode instaciar um objeto desta.
 * Portanto, seu funcionamento consiste em fornecer o meio executável do programa, um método estático para finalizá-lo
 * e intermediar as classes de interface gráfica e modelo de negócio com os dados, fornecendo os serviços necessários.*/
public class AvaliacaoInstitucional {
	private static BancoDeDados bd;
	
	private static DAO dao;
	
	/**Autoreferência para que todas as classes invocadas por essa possam ter acesso a seus métodos*/
	private static AvaliacaoInstitucional app;
	
	/**Objeto da classe responsável por extrair dados do CSV e salvar/consultar o banco*/
	private ExtratorDeDados extrator;
	
	private static Pesquisa pesquisaAtiva;

	/**Lista de todas as pesquisas armazenadas no banco. Deve ser feita uma consulta e recuperar
	 * todas as pesquisas existentes no início. Se uma pesquisa for adicionada, deve ser adicionado 
	 * também nesta lista.
	 */
	private List<Pesquisa> pesquisasList;
	
	private AvaliacaoInstitucional() {
		app = this;
		
		pesquisasList = new ArrayList<Pesquisa>();
		
		extrator  = new  ExtratorDeDados();
		
		try {
		
		//Constrói a janela do menu principal
		IgAvaliacaoInstitucional.getInstance(this);
	
		
		}catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	public static AvaliacaoInstitucional getInstance() {
		return app;
	}
	
	/**Recupera do banco todas as pesquisas existentes e salva na memória
	 * 
	 * @throws SQLException se ocorerr um erro na consulta
	 */
	private void obterPesquisasBanco() throws SQLException {
		
		
		pesquisasList = extrator.obterPesquisas();
		
		if(pesquisasList.size() == 0) return;
	
		
		//Define a pesquisa ativa
		pesquisaAtiva = pesquisasList.get(pesquisasList.size()-1);
		
	}

	public static void main(String[] args) {
		new AvaliacaoInstitucional();
	}


	/**Fecha todas as janelas que foram abertas pelo programa e o encerra.
	 * @author Ramon Giovane**/
	public static void fecharPrograma() {
		try {
			//TODO: 
			System.out.println("Closing everything...");

			bd.fecharConexao();
		}catch (NullPointerException e) {
			System.out.println("Banco de Dados não estava conectado.");
		} catch (SQLException e1) {
			EntradaESaida.msgErro(null, InterfaceConstraints.ERRO_DESCONECTAR_BD,
					InterfaceConstraints.TITULO_PROGRAMA);
		}
		
		System.exit(0);
	}
	
	/**Adiciona uma pesquisa na base de dados e na lista de pesquisas em memória.
	 * Em caso de sucesso, o objeto pesquisa passado como parâmetro terá o atributo código
	 * modificado, com o código da pesquisa inserida no banco.
	 * @param pesquisa objeto com o nome da pesquisa a ser inserido.
	 * @throws SQLException se uma pesquisa com o nome especificado já existir.
	 * 
	 */
	public void adicionarPesquisa(Pesquisa pesquisa) throws SQLException {
		dao.inserir(pesquisa);
		
		//Realiza uma consulta de uma pesquisa utilizando a implementação de Recuperação da classe pesquisa
		Object resultadoBruto[][] = dao.consultar(pesquisa);
		
		pesquisa.setCodigo((int)resultadoBruto[0][0]);
		
		pesquisaAtiva = pesquisa;
		
		pesquisasList.add(pesquisa);
		
	}
	
	/**Consulta uma pesquisa na lista de pesquisas em memória.
	 * 
	 * @param pesquisa recebe a pesquisa a ser pesquisado.
	 * 
	 * @return true se uma pesquisa com o mesmo nome existe, falso caso contrário.
	 */
	public boolean pesquisaExiste(Pesquisa pesquisa) throws SQLException {
		return pesquisasList.contains(pesquisa);
		
	}
	
	/**Retorna o número de pesquisas guardas em memória*/
	public int numeroPesquisas() {
		return pesquisasList.size();
	}
	
	
	/**Obtém todas as pesquisas em memória, presentes na lista de pesquisas.
	 * 
	 * @return um <code>ArrayList</code> de objetos <code>Pesquisa</code>.
	 */
	public String[] listarPesquisas() {
		String[] strPesquisas = new String[pesquisasList.size()];
		
		for(int i =0; i<pesquisasList.size(); i++) {
			strPesquisas[i] = pesquisasList.get(i).getNome();
		}
		
		return strPesquisas;
	}
	
	/** Gera uma relatório de acordo com o tipo de pesquisa solicitado dentro da pesquisa atual selecionada, salvando os dados
	 * em uma classe que implementa o super tipo abstrato {@link Relatorio}.
	 * 
	 * @param tipoRelatorio tipo de relatório a ser gerado de acordo com a <code>enumeração</code> {@link TipoRelatorio}.
	 * @param tipoGraduacao filtro de pesquisa representado pela descrição de um tipo de graduação de entrevistados
	 * (uma referência <code>null</code> é permitida). 
	 * 
	 * <br><br>
	 * <b>Nota</b>: Os argumentos passados precisam coincidir com o tipo de relatório informado.
	 *  Consulte a <code>enumeração</code> {@link TipoRelatorio} para as possíveis combinações.
	 *
	 * @return um objeto de uma classe que implementa a classe abstrata {@link Relatorio}.
	 * @throws SQLException quando ocorre algum problema de comunicação com o banco de dados, onde os dados 
	 * 		   estão persistidos.
	 * @throws NullPointerException quando nenhum resultado é encontrado com os parâmetros de pesquisa passados.
	 * 
	 * @see Relatorio
	 * @see TipoRelatorio
	 * @see AvaliacaoInstitucional#setPesquisaAtiva(String)
	 */
	public Relatorio gerarRelatorio(TipoRelatorio tipoRelatorio, String tipoGraduacao) 
			throws SQLException, NullPointerException {
		switch(tipoRelatorio){
		case PARTICIPANTES_POR_CURSO:
			 return extrator.gerarRelatorioParticipantesCurso(tipoRelatorio);
			
		case PARTICIPANTES_POR_SEGMENTO:
			return extrator.gerarRelatorioParticipantesSegmento(tipoRelatorio);

		case CONCEITO_MEDIO_CURSO:
			return extrator.gerarRelatorioDeMediasPorCurso(tipoGraduacao, tipoRelatorio);
		
		case CONCEITO_MEDIO_ASSUNTO:
			return extrator.gerarRelatorioDeMediasPorSegmento(tipoRelatorio);
		
		default:
			throw new NullPointerException();
		}
		
	}
	
	/**Obtém uma pesquisa da lista de pesquisas na memória*
	 * 
	 * @param nomePesquisa nome da pesquisa solicitada
	 * @return
	 * @throws IllegalArgumentException se a pesquisa passada não existe na memória
	 */
	public Pesquisa obterPesquisa(String nomePesquisa) {
		try{
			return pesquisasList.get(pesquisasList.indexOf(new Pesquisa(nomePesquisa)));
		}catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException();
		}
	}
	
	/**Realiza a importação dos dados do CSV de uma pesquisa especificada e os salva no banco de dados.
	 * 
	 * @param pesquisa objeto {@link Pesquisa} que deve conter o código, descrição e local onde o arquivo CSV se encontra.
	 * @throws FileNotFoundException se o arquivo não for encontrado
	 * @throws IOException se ocorrer algum erro na leitura
	 * @throws SQLException se ocorrer algum erro na inserção dos dados no banco
	 */
	public void importarDados(Pesquisa pesquisa) throws FileNotFoundException, IOException, SQLException {
		extrator.extrairDados(pesquisa);
	}
	
	/**Inicia uma conexão com o banco de dados, preparando os serviços necessários para a manipulação de dados
	 * Para isso, utiliza o nome, o usuário e a senha de uma base de dados já existente.
	 * 
	 * @param nomeBD nome da base de dados
	 * @param usuarioBD nome de usuário, proprietário da base.
	 * @param senhaBD senha do usuário.
	 * @throws SQLException se todas as tentativas de conexão falharem ou ocorrer um erro.
	 */
	public void conectarBancoDados(String nomeBD, String usuarioBD, String senhaBD) throws SQLException {
		if(bd == null)
			bd = BancoDeDados.criarConexao(nomeBD, usuarioBD, senhaBD);
		else {
			desconectarBancoDeDados();
			bd = BancoDeDados.criarConexao(nomeBD, usuarioBD, senhaBD);
			
		}
		if(bd == null) throw new SQLException();
		
		//Instancia o DAO
		dao = new DAO(bd);
		
		extrator.setDao(dao);
		
		//Se a conexão foi bem sucedida, obtém então as pesquisas armazenadas na base de dado
		obterPesquisasBanco();
		
		
			
	}
	
	/**Define qual é a pesquisa ativa na aplicação. Isso é importante para evitar que usuário tenha sempre  
	 * que utilizar o combo box para escolher a pesquisa desejada quando gerar um relatório.
	 * Se a pesquisa informada não existir na lista de pesquisa na memória, não faz nada.
	 * @param nomePesquisa uma <code>String</code> com o nome da pesquisa a ser usada. 
	 */
	public void setPesquisaAtiva(String nomePesquisa) {
		Pesquisa pesquisa = obterPesquisa(nomePesquisa);
		if(pesquisasList.contains(pesquisa))
			pesquisaAtiva = pesquisa;
	}
	
	/**
	 * Retorna a pesquisa ativa na aplicação  no momento.
	 * @return o objeto {@link Pesquisa} atual.
	 */
	public Pesquisa getPesquisaAtiva() {
		return pesquisaAtiva;
	}
	
	/**Verifica se o banco de dados tem uma conexão ativa.
	 * 
	 * @return <code>true</code> se fim, <code>false</code> do contrário
	 */
	public boolean checarConexaoBancoDeDados() {
		if(bd == null) return false;
		return BancoDeDados.isConectado();
	}
	public void desconectarBancoDeDados() throws SQLException {
		bd.fecharConexao();
	}

	/**Apaga uma pesquisa da lista de pesquisas na memória
	 * 
	 * @param pesquisa pesquisa a ser removida
	 */
	public void apagarPesquisa(Pesquisa pesquisa) {
		pesquisasList.remove(pesquisa);
	}
	
}
