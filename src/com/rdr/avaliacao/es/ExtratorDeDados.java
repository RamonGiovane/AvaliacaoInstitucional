package com.rdr.avaliacao.es;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;

import com.rdr.avaliacao.es.bd.BancoDeDados;

public class ExtratorDeDados {
	private final String SEPARADOR_PADRAO = ";";
	private BancoDeDados bd;
	private String nomeArquivo;
	private String separador;
	private ArquivoTexto arquivo; 
	public ExtratorDeDados(BancoDeDados bd, String nomeArquivo) {
		super();
		this.bd = bd;
		this.nomeArquivo = nomeArquivo;
		this.separador = SEPARADOR_PADRAO;
		arquivo = new ArquivoTexto();
	}
	
	

	public ExtratorDeDados(BancoDeDados bd) {
		this(bd, null);
		
	}



	public BancoDeDados getBd() {
		return bd;
	}

	public void setBd(BancoDeDados bd) {
		this.bd = bd;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
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
	
	
	public void abrirArquivo() throws FileNotFoundException {
		arquivo.abrir(nomeArquivo);
	}
	
	public void extrairDados() throws IOException {
		abrirArquivo();
		
		String linha = arquivo.lerLinha();
		//Lê a primeira linha, com o cabeçalho e as perguntas
		EntradaESaida.exibirTexto(null, linha, "");
		
		separarTexto(linha);
	}
	
	private void separarTexto(String linha) {
		StringTokenizer tokenizer = new StringTokenizer(linha, "(\\d\\.)([\\*]");
		while(tokenizer.hasMoreElements())
			System.out.println(tokenizer.nextToken());
	}
	
	
	

}
