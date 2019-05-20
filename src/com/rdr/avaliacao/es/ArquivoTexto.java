package com.rdr.avaliacao.es;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.Scanner;

import com.rdr.avaliacao.ig.InterfaceConstraints;
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
 * @author Prof. Márlon Oliveira da Silva
 * 
 * @version 0.1
 */
public class ArquivoTexto {
	private Scanner inputScanner; // O conteúdo do arquivo texto será lido usando um objeto Scanner.
	private FileInputStream fileInputStream; // Representa o arquivo texto como um arquivo de bytes. 
	private Formatter fileOutputFormatter; // O conteúdo do arquivo texto será escrito usando um objeto Formatter.
	 
	private final String EOF_MESSAGE ="Fim do arquivo (EOF) atingido.";
	/** 
	 * Abre um arquivo texto armazenado em disco somente para leitura. A escrita não é permitida.
	 * 
	 * @param nomeArquivo nome do arquivo a ser aberto.
	 * 
	 * @throws FileNotFoundException se o nome do arquivo não for encontrado.
	 */
	  public void abrir(String nomeArquivo) throws FileNotFoundException {
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
	   * As exceções disparadas pelo método <code>ler</code> tem o seu ponto de disparo nos 
	   * métodos <code>hasNextLine()</code> e <code>nextLine()</code> 
	   * da classe <code>java.util.Scanner</code>. Lembre que o arquivo texto é lido usando 
	   * um objeto Scanner. Estas exceções são geradas pelos métodos da classe Scanner. 
	   * 
	   * @return um <code>String</code> com o conteúdo lido do arquivo texto.
	   *
	   * @throws IOException ocorre se o arquivo estiver fechado.  
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
	  
	  public String lerLinha() throws IOException {
		  
		  try { // Lê uma linha do arquivo.
			      if (inputScanner.hasNextLine()) 
			    	  return inputScanner.nextLine();
			      else
			    	  throw new IOException(EOF_MESSAGE);
			      
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
