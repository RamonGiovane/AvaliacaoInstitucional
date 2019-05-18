package com.rdr.avaliacao.ig;

import java.awt.Color;
import java.awt.SystemColor;
import java.io.File;

import javax.swing.UIManager;

public interface IgConstantes {

	public final static Color COR_LABEL_MENU = new Color(0, 102, 204), COR_LABEL_MENU_HOVER = new Color(0, 71, 143),
			COR_BACKGROUND = new Color(245, 250, 250);

	public final static String TITULO_PROGRAMA = "Autoavalia\u00E7\u00E3o Institucional",
			MSG_ERRO_BUILD_UI = "Ocorreu um problema desconhecido durante a contrução da interface gráfica.\n "
					+ "A funcionalidade do programa não será afetada, mas os componentes visuais podem não estar  corretos.",
					ERRO_CONECTAR_BD = "Ocorreu um erro ao abrir a conexão com o banco de dados.\nVerifique "
							+ "se as credenciais da conexão estão corretas.",
					ERRO_DESCONECTAR_BD = "Ocorreu um erro ao fechar a conexão com o banco de dados.",
					TITULO_BD = "Configurar Banco de Dados",
					MSG_SUCESSO_BD = "Conexão estabelecida com sucesso!",
					MSG_BD_CONEXAO_JA_EXISTE =	"Já existe uma conexão ativa com o banco de dados."
							+ "\nSe continuar a conexão atual será encerrada.\nProsseguir mesmo assim?";

	public final static String CAMINHO_ICON_DB = "/com/rdr/avaliacao/ig/img/db_icon.png",
			CAMINHO_ICON_GRAPHIC = "/com/rdr/avaliacao/ig/img/graphic_icon.png",
			CAMINHO_IMPORT_ICON = "/com/rdr/avaliacao/ig/img/import_icon.png";

	public final static String ICONE_DB = "db_icon.png";

}
