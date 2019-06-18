package com.rdr.avaliacao.ig;

import java.awt.Color;

public interface InterfaceConstraints {

	//Cores de interface gráfica
	public final static Color COR_BTN_MENU = new Color(10, 10, 50)/*new Color(0, 52, 204)*/, COR_BTN_MENU_HOVER = new Color(0, 71, 143),
			COR_BACKGROUND = new Color(250, 250, 250),
			COR_BACKGROUND_BOTAO = Color.BLACK;//new Color(245, 245, 245);

	/**Mensagem de interface gráfica*/
	public final static String TITULO_PROGRAMA = "Autoavalia\u00E7\u00E3o Institucional",

			TITULO_ABRIR_ARQUIVO = "Abrir aquivo",
			TITULO_IMPORTAR_DADOS = "Importar Novos Dados...",
			TITULO_SALVAR_PDF = "Salvar como PDF",
			TEXTO_IMPORTAR_DADOS = "<html>Defina uma breve descrição para sua pesquisa que a diferencie de outras. Em seguida, escolha o caminho dos dados a serem importados.</html>",
			MSG_ERRO_BUILD_UI = "Ocorreu um problema desconhecido durante a contrução da interface gráfica.\n "
					+ "A funcionalidade do programa não será afetada, mas os componentes visuais podem não estar  corretos.",
			ERRO_CONECTAR_BD = "Ocorreu um erro ao abrir a conexão com o banco de dados.\nVerifique "
							+ "se as credenciais da conexão estão corretas ou se a base de dados existe.",
			ERRO_DESCONECTAR_BD = "Ocorreu um erro ao fechar a conexão com o banco de dados.",
			TITULO_BD = "Configurar Banco de Dados",
			MSG_SUCESSO_BD = "Conexão estabelecida com sucesso!",
			MSG_BD_CONEXAO_JA_EXISTE =	"Já existe uma conexão ativa com o banco de dados."
									+ "\nSe continuar a conexão atual será encerrada.\nProsseguir mesmo assim?",
			MSG_ARQUIVO_NAO_ENCONTRADO = "Não foi possível localizar o arquivo: ",
			MSG_NOME_PESQUISA_VAZIO = "Você deve fornecer um nome para a pesquisa.",
			MSG_CAMINHO_DADOS_VAZIO = "Você deve escolher um arquivo por onde os dados serão importados.",
			MSG_PESQUISA_JA_EXISTE =  "Já existe uma pesquisa com este nome!",
			MSG_RELATORIO_SEM_CONEXAO = "Para gerar um relatório primeiro é preciso uma conexão com o banco de dados.",
			MSG_NAO_HA_PESQUISAS = "Não há pesquisas cadastradas. Importe os dados de\numa nova pesquisa para gerar um relatório.",
	 		MSG_ERRO_GERAR_RELATORIO = "Ocorreu um erro ao gerar os relatórios.\nContate o suporte.",
	 		MSG_DETALHES_ERRO  = "\nTalvez haja um problema na conexão com o banco.",
	 		MSG_ERRO_NENHUM_RELATORIO_GERADO = "Não há dados suficientes para gerar relatórios com os parâmteros passados",
	 		MSG_ERRO_IMPORTAR_SEM_CONEXAO = "É necessária uma conexão com banco de dados para\nimportar  dados de uma pesquisa",
			MSG_PESQUISA_IMPORTADA = "Pesquisa importada com sucesso!",
			MSG_BARRA_DE_PROGRESSO_1 = "Lendo pesquisa...",
			MSG_BARRA_DE_PROGRESSO_2 = "Isto pode levar alguns minutos...",
			MSG_BARRA_DE_PROGRESSO_3 =	"Quase lá...",
			MSG_JANELA_BANCO = "<html>Para usar as funcionalidades do programa,<br> \u00E9 preciso uma conex\u00E3o com o"
					+ " Banco de Dados</html>",
			MSG_SOBRE = "© 2019 Avaliação Institucional\nDesenvolvido por Ramon Giovane.\n"
					+ "Aluno do curso Tecnologia em Sistemas para Internet.\nhttps://github.com/RamonGiovane/",
			MSG_ARQUIVO_VAZIO = "O arquivo passado está vazio ou corrompido.";
	
	

	/**Caminho de imagem usada na interface gráfica**/
	public final static String CAMINHO_ICON_DB = "/com/rdr/avaliacao/ig/img/db_icon.png",
			CAMINHO_ICON_GRAPHIC = "/com/rdr/avaliacao/ig/img/graphic_icon.png",
			CAMINHO_IMPORT_ICON = "/com/rdr/avaliacao/ig/img/import_icon.png",
			CAMINHO_PROGRAMA_ICON = "/com/rdr/avaliacao/ig/img/favicon.png",
			ICONE_DB = "db_icon.png";

	//Banco de Dados
	public final static String NOME_BD_PADRAO = "spadb", USUARIO_BD_PADRAO = "spaadmin", 
			SENHA_BD_PADRAO = "SPA#Barbacena@IFSudesteMG";


	/**Filtros de extensão de arquivos*/
	public final static String[] 
			DESCRICOES_EXTENSOES_CSV = new String[] {"Arquivo CSV - Comma Separeted Values Database (*.csv)"},
			DESCRICOES_EXTENSOES_PDF = new String[] {"Arquivo PDF (*.pdf)"},
			EXTENSOES_CSV = new String[]{"csv"}, EXTENSOES_PDF = new String[] {"pdf"};
	
	/**Ponto final, separador de nomes de arquivos e extensões*/
	public static final String PONTO = ".";

	public static final String MSG_CHECAR_CABECALHO = "Por favor, verifique os dados da pesquisa e certifique\nque estejam de acordo com o padrão correto.";

	public static final String MSG_PERGUNTA_VAZIA = "ERRO: Coluna vazia detectada no cabeçalho do arquivo fornecido.\n" + MSG_CHECAR_CABECALHO;

	public static final String MSG_PERGUNTA_REPETIDA =  "ERRO: Informações repetidas foram detectadas no cabeçalho do arquivo fornecido.\n" + MSG_CHECAR_CABECALHO;;

}
