package com.rdr.avaliacao.es.bd.constraints;

/**Interface com todas as chamadas das funções armazenadas no banco.
 * Todas as funções dependem da base de dados íntegra. Dispararão exceções em tempo de execcução se as condições de seus 
 * parâmteros documentados não forem respeitadas. */
public interface FuncoesSQL {
	
	/**Chamada de função SQL para inserção de perguntas no banco. Não aceita perguntas com assuntos, descricões e cósigos de 
	 * pesquisa iguais. 
	 * <br>
	 * <b>Parâmetros:</b> codigo da pesquisa (<code>int</code>), descricao da pergunta (<code>String</code>), 
	 * descricao do assunto (<code>String</code>)
	 * <br>
	 * <b>Retorna:</b> respectivamente, o código do assunto (<code>int</code>), código da pergunta (<code>int</code>), código da pesquisa (<code>int</code>), 
	 * relacionados a pergunta recém inserida.
	 */
	public static final String FUNCTION_INSERIR_PERGUNTA = "inserir_pergunta";
	
	/**Chamada de função SQL para inserção das respostas de uma pergunta associada a uma pesquisa, um assunto, uma pergunta e um entrevistado, 
	 * exclusivamente. 
	 * <br>
	 * <b>Parâmetros:</b> codigo da pesquisa (<code>int</code>), descricao da resposta (<code>String</code>), codigo do assunto (<code>int</code>),
	 * codigo da pergunta (<code>int</code>), codigo do entrevistado (<code>int</code>).
	 * <br>
	 * <b>Retorna:</b> nada (<code>void</code>).
	 */
	public static final String FUNCTION_INSERIR_RESPOSTA = "inserir_resposta";
	

	/**Chamada de função SQL para inserção de um entrevistado no banco. 
	 * <br>
	 * <b>Parâmetros:</b> codigo da pesquisa (<code>int</code>), nome do segmento (<code>String</code>), nome do campus(<code>String</code>), 
	 * nome do curso (<code>String</code>), 
	 * <br>
	 * <b>Retorna:</b> o código do entrevistado (<code>int</code>) recém inserido.
	 */
	public static final String FUNCTION_INSERIR_ENTREVISTADO = "inserir_entrevistado";
	
	/**Chamada de função SQL para inserção de um conceito no banco. Um conceito associa um valor inteiro à uma descrição.
	 * Esta função atribui à uma descrição, um valor e os adiciona na tabela <code>Conceito</code>, da seguinte forma:
	 * <br>
	 * <code>Descrição - Nota</code><br>
	 * Ótimo - 5<br>Bom - 4<br>Satisfatório - 3<br>Ruim - 2<br>Péssimo - 1<br>Qualquer outro valor (a não ser que já esteja registrado no banco) - 0.<br>
	 * <br>
	 * <b>Nota:</b> a função correspondente a {@value FuncoesSQL#FUNCTION_INSERIR_RESPOSTA} utiliza desta internamente 
	 * em seu processamento.
	 * <b>Parâmetros:</b>descrição do conceito(<code>String</code>), 
	 * nome do curso (<code>String</code>), 
	 * <br>
	 * <b>Retorna:</b> o código do conceito (<code>int</code>) recém inserido.
	 */
	public static final String FUNCTION_INSERIR_CONCEITO = "inserir_conceito";
	
	/**Chamada de função SQL utilitária, que apaga todos os dados <b>irreversivelmente</b> das tabelas associadas a um usuário do banco.
	 * <br>
	 * <b>ATENÇÃO:</b> Repetindo, os dados apagados não poderão ser recuperados. Tudo associado ao usuário do banco passado será destruído.
	 * <br>
	 * <b>Parâmetros:</b>nome de usuário proprietário das tabelas (<code>String</code>). 
	 * <br>
	 * <b>Retorna:</b> nada (<code>void</code>).
	 */
	
	public static final String FUNTION_TRUNCATE_TABLES = "truncate_tables";
}
