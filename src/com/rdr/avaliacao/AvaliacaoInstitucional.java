package com.rdr.avaliacao;

import java.awt.Component;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.rdr.avaliacao.es.EntradaESaida;
import com.rdr.avaliacao.es.ExtratorDeDados;
import com.rdr.avaliacao.es.bd.BancoDeDados;
import com.rdr.avaliacao.es.bd.DAO;
import com.rdr.avaliacao.es.bd.Recuperacao;
import com.rdr.avaliacao.ig.IgAvaliacaoInstitucional;
import com.rdr.avaliacao.ig.InterfaceConstraints;
import com.rdr.avaliacao.ig.TipoRelatorio;
import com.rdr.avaliacao.questionario.Curso;
import com.rdr.avaliacao.questionario.Pergunta;
import com.rdr.avaliacao.questionario.Pesquisa;
import com.rdr.avaliacao.questionario.Resposta;
import com.rdr.avaliacao.relatorio.DataSet;

/**Classe principal do programa de Avaliação Institucional. Nesta classe encontra-se o método main.
 * Nenhuma outra classe pode instaciar um objeto desta.
 * Portanto, seu funcionamento consiste em fornecer o meio executável do programa, um método estático para finalizá-lo
 * e intermediar as classes de interface gráfica e modelo de negócio com os dados, fornecendo os serviços necessários.*/
public class AvaliacaoInstitucional {
	private static BancoDeDados bd;
	
	//TODO: Este objeto DAO deve ser comum para todas as classes que fazem acesso ao banco de dados
	//TODO: Fazer método que obtém este objeto
	private static DAO dao;
	
	private ExtratorDeDados extrator;

	/**Lista de todas as pesquisas armazenadas no banco. Deve ser feita uma consulta e recuperar
	 * todas as pesquisas existentes no início. Se uma pesquisa for adicionada, deve ser adicionado 
	 * também nesta lista.
	 */
	private List<Pesquisa> pesquisasList;
	
	private AvaliacaoInstitucional() {
		pesquisasList = new ArrayList<Pesquisa>();
		
		try {
		
		//Constrói a janela do menu principal
		IgAvaliacaoInstitucional.getInstance(this);
		
		//Salva a referência do banco de dados
		bd = BancoDeDados.getBancoDeDados();
		dao = new DAO(bd); //TODO: Transformar DAO em um subclasse de BancoDeDados
		
		
		//Procura por pesquisas no banco
		obterPesquisasBanco();
		
		}catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	//Este método só deve ser executado se houver conexão com o banco
	private void obterPesquisasBanco() throws SQLException {
		Pesquisa pesquisa;
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
		
		for(int i=0; i<pesquisas.length; i++){
			pesquisa = new Pesquisa();
			pesquisa.setCodigo((int)pesquisas[i][0]);
			pesquisa.setNome(pesquisas[i][1].toString());
			
			pesquisasList.add(pesquisa);
		}
				
		
	}

	public static void main(String[] args) {
		new AvaliacaoInstitucional();
	}
	
//	/**Método que pode apenas ser chamado pelas classes que possuem referência esta.*/
//	public void importarDados(String nomeArquivo) {
//		if(extrator == null) extrator = new ExtratorDeDados(bd, nomeArquivo);
//		extrator.setNomeArquivo(nomeArquivo);
//		try {
//		
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	/**Fecha todas as janelas que foram abertas pelo programa e o encerra.
	 * @author Ramon Giovane**/
	public static void fecharPrograma() {
		try {
			System.out.println("Closing everything...");
			DAO dao = new DAO(bd);
			
			//TODO: RESETANDO O BANCO. APAGAR DEPOIS!!
			//dao.executarFuncao("truncate_tables", "spaadmin");
			//dao.inserir("pesquisa", new String[] {"codigo", "descricao"}, 1, "aaa");
			
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
		Object resultadoBruto[][] = dao.consultar(pesquisa);
		
		pesquisa.setCodigo((int)resultadoBruto[0][0]);
		
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
	
	public boolean checarConexao() {
		return BancoDeDados.isConectado();
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
	
	public static DataSet gerarDataSetParticipantesCurso(Pesquisa pesquisa, TipoRelatorio tipoRelatorio) throws SQLException {
		ExtratorDeDados extrator = new ExtratorDeDados(bd, pesquisa);

		switch(tipoRelatorio){
		case POR_CURSO:
			 return extrator.gerarDataSetParticipantesCurso(pesquisa);
			
		case POR_SEGMENTO:
			return extrator.gerarDataSetParticipantesSegmento(pesquisa);

			
		default:
			return null;
		}
		
	}
	
	public static DataSet gerarDataSetParticipantesSegmento(Pesquisa pesquisa) throws SQLException {
		ExtratorDeDados extrator = new ExtratorDeDados(bd, pesquisa);
		DataSet dataSet = extrator.gerarDataSetParticipantesSegmento(pesquisa);
		dataSet.ordenarPorValor();
		return dataSet;
	}
	
	public Pesquisa obterPesquisa(String nomePesquisa) {
		return pesquisasList.get(pesquisasList.indexOf(new Pesquisa(nomePesquisa)));
	}
	
	public void importarDados(Component janelaPai, Pesquisa pesquisa) throws FileNotFoundException, IOException {
		ExtratorDeDados.extrairDados(janelaPai, bd, pesquisa);
	}
}
