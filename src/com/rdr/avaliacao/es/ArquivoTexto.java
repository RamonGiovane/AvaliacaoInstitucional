package com.rdr.avaliacao.es;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.Scanner;
/**
 * Fornece vários métodos para manipular um arquivo texto em disco.
 * <p>
 * Como é um arquivo texto somente uma operação pode ser realizada por vez: escrita ou leitura.
 * </p>
 * <p> 
 * Se for leitura o arquivo deve ser aberto usando o método <code>abrir</code> e após a leitura 
 * deve ser fechado usando o método <code>fechar</code>. 
 * </p>
 * <p>
 * Se for escrita o arquivo deve ser criado usando o método <code>criar</code> e após a escrita deve ser fechado usando 
 * o método <code>fechar</code>.
 * </p>
 *   
 * @author Prof. Márlon Oliveira da Silva<br>
 * 		   Ramon Giovane
 * 
 * @version 0.2
 */
public class ArquivoTexto {
	private Scanner inputScanner; // O conteúdo do arquivo texto será lido usando um objeto Scanner.
	private FileInputStream fileInputStream; // Representa o arquivo texto como um arquivo de bytes. 
	private Formatter fileOutputFormatter; // O conteúdo do arquivo texto será escrito usando um objeto Formatter.
	private BufferedReader reader;
	private final String UTF_8 = "UTF-8";
	 
	/** 
	 * Abre um arquivo texto armazenado em disco somente para leitura. A escrita não é permitida.
	 * 
	 * @param nomeArquivo nome do arquivo a ser aberto.
	 * 
	 * @throws NullPointerException se o nome do arquivo for nulo.
	 * @throws IOException 
	 */
	  public void abrir(String nomeArquivo) throws NullPointerException, IOException {
		 //Cria um objeto de reder bufferizado.
		  try {
			  //Tenta abrir o arquivo como UTF-8
			  reader = new BufferedReader(new InputStreamReader(new FileInputStream(nomeArquivo), UTF_8));
		} catch (UnsupportedEncodingException e) {
			//Se não conseguir, tenta abri-lo de outra forma. 
			reader = new BufferedReader(new FileReader(nomeArquivo));
		}
		
		  // Abre um arquivo de bytes para realizar a entrada de dados. 
		  fileInputStream = new FileInputStream(nomeArquivo);
		        
		  // O arquivo será lido como um arquivo de texto puro usando   a classe java.util.Scanner. 
		  inputScanner = new Scanner(fileInputStream);
	  } 

	  /** 
	   * Cria um arquivo texto em disco. Se o arquivo já existe o seu conteúdo será apagado. O arquivo criado é
	   * só para escrita. A leitura não é peermitida.
	   * 
	   * @param nomeArquivo nome do arquivo a ser criado.
	   * 
	   * @throws FileNotFoundException se o nome do arquivo não for encontrado.
	   */
	  public void criar(String nomeArquivo) throws FileNotFoundException {
		  // Cria um arquivo texto em disco.
		  fileOutputFormatter = new Formatter(nomeArquivo);
	  } 

	  /** 
	   * Escreve no arquivo texto o conteúdo do objeto <code>String</code> armazenado no
       * parâmetro conteudo. 
	   * 
	   * @param conteudo conteúdo a ser escrito no arquivo texto.
	   * 
	   * @throws IOException disparada se o arquivo estiver fechado.
	   */
	  public void escrever(String conteudo) throws IOException {
		  try {  // Escreve o conteúdo no arquivo texto.
			       fileOutputFormatter.format("%s\n", conteudo);
		  } catch (FormatterClosedException e) {
			  throw new IOException();
		}
	  } 
	  
	  /** 
	   * Lê o conteúdo completo do arquivo texto.
	   * 
	   * @return uma <code>String</code> com o conteúdo lido do arquivo texto.
	   *
	   * @throws IOException ocorre se o arquivo estiver fechado.  
	   * 
	   * @author Prof. Márlon Oliveira da Silva
	   */
	  public String ler() throws IOException {
		  StringBuilder conteudo = new StringBuilder();
		  
		  try { // Lê o conteúdo completo do arquivo.
			      while (inputScanner.hasNextLine()) 
			    	  conteudo.append(inputScanner.nextLine()).append("\n");
		  
			      return conteudo.toString();
		  } catch (Exception e) {
			  throw new IOException();
		}
	  }
	  
	  /** 
	   * Lê o conteúdo completo de uma linha do arquivo texto.
	   * 
	   * @return uma <code>String</code> com o conteúdo lido do arquivo texto.
	   *
	   * @throws IOException ocorre se o arquivo estiver fechado ou se não há mais linhas para ler
	   * 
	   * @author Ramon Giovane
	   * */  
	   
	  public String lerLinha() throws IOException {

		 
		 try { 
			 return reader.readLine();
			      
		  } catch (Exception e) {
			  throw new IOException();
		}
	  }
	  
	
	  
	  /**
	   * Fecha os arquivos que foram criados para manipulação do arquivo texto.
	   * 
	   * @throws IOException se ocorrer algum erro de E/S ao tentar fechar o arquivo.
	   */
	  public void fechar() throws IOException {
		  if (fileInputStream != null) fileInputStream.close();
		  if (inputScanner != null) inputScanner.close();
		  if (fileOutputFormatter != null) fileOutputFormatter.close();
	  } 
} // class ArquivoTexto
