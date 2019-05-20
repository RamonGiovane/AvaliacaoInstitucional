package com.rdr.avaliacao.ig;

import java.awt.Color;

public interface InterfaceConstraints {

	//Cores de interface gráfica
	public final static Color COR_BTN_MENU = new Color(10, 10, 50)/*new Color(0, 52, 204)*/, COR_BTN_MENU_HOVER = new Color(0, 71, 143),
			COR_BACKGROUND = new Color(250, 250, 250),
			COR_BACKGROUND_BOTAO = Color.BLACK;//new Color(245, 245, 245);

	//Mensagens de interface gráfica
	public final static String TITULO_PROGRAMA = "Autoavalia\u00E7\u00E3o Institucional",
			TITULO_ABRIR_ARQUIVO = "Abrir aquivo",
			MSG_ERRO_BUILD_UI = "Ocorreu um problema desconhecido durante a contrução da interface gráfica.\n "
					+ "A funcionalidade do programa não será afetada, mas os componentes visuais podem não estar  corretos.",
					ERRO_CONECTAR_BD = "Ocorreu um erro ao abrir a conexão com o banco de dados.\nVerifique "
							+ "se as credenciais da conexão estão corretas.",
					ERRO_DESCONECTAR_BD = "Ocorreu um erro ao fechar a conexão com o banco de dados.",
					TITULO_BD = "Configurar Banco de Dados",
					MSG_SUCESSO_BD = "Conexão estabelecida com sucesso!",
					MSG_BD_CONEXAO_JA_EXISTE =	"Já existe uma conexão ativa com o banco de dados."
							+ "\nSe continuar a conexão atual será encerrada.\nProsseguir mesmo assim?";

	//Caminhos de imagens
	public final static String CAMINHO_ICON_DB = "/com/rdr/avaliacao/ig/img/db_icon.png",
			CAMINHO_ICON_GRAPHIC = "/com/rdr/avaliacao/ig/img/graphic_icon.png",
			CAMINHO_IMPORT_ICON = "/com/rdr/avaliacao/ig/img/import_icon.png",
			CAMINHO_PROGRAMA_ICON = "/com/rdr/avaliacao/ig/img/favicon.png",
			ICONE_DB = "db_icon.png";
	
	//Banco de Dados
	public final static String NOME_BD_PADRAO = "spadb", USUARIO_BD_PADRAO = "spaadmin", 
			SENHA_BD_PADRAO = "SPA#Barbacena@IFSudesteMG";

	//Filtros de extensão de arquivos
	public final static String[] 
	DESCRICOES_EXTENSOES = new String[] {"Arquivo CSV - Comma Separeted Values Database (*.csv)"},
	EXTENSOES = new String[]{"csv"};

}
