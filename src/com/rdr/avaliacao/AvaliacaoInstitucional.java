package com.rdr.avaliacao;

import java.io.IOException;
import java.sql.SQLException;

import com.rdr.avaliacao.es.EntradaESaida;
import com.rdr.avaliacao.es.ExtratorDeDados;
import com.rdr.avaliacao.es.bd.BancoDeDados;
import com.rdr.avaliacao.ig.IgAvaliacaoInstitucional;
import com.rdr.avaliacao.ig.InterfaceConstraints;

/**Classe principal do programa de Avaliação Institucional. Nesta classe encontra-se o método main.
 * Nenhuma outra classe pode instaciar um objeto desta.
 * Portanto, seu funcionamento consiste em fornecer o executável do programa e intermediar as classes de
 * interface gráfica e modelo de negócio com os dados.*/
public class AvaliacaoInstitucional {
	private ExtratorDeDados extrator;
	private static BancoDeDados bd;
	private AvaliacaoInstitucional() {
		
		try {
		
		//Constrói a janela do menu principal
		IgAvaliacaoInstitucional.getInstance(this);
		
		//Salva a referência do banco de dados
		bd = BancoDeDados.getBancoDeDados();
		
		
		}catch(Exception e) {
			
		}finally {
			try {
				bd.fecharConexao();
			}catch (NullPointerException e) {
				System.out.println("Banco de Dados não estava conectado.");
			} catch (SQLException e1) {
				EntradaESaida.msgErro(null, InterfaceConstraints.ERRO_DESCONECTAR_BD,
						InterfaceConstraints.TITULO_PROGRAMA);
			}
		}

	}
	
	public static void main(String[] args) {
		new AvaliacaoInstitucional();
	}
	
	public void importarDados(String nomeArquivo) {
		if(extrator == null) extrator = new ExtratorDeDados(bd, nomeArquivo);
		extrator.setNomeArquivo(nomeArquivo);
		try {
			extrator.extrairDados();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
