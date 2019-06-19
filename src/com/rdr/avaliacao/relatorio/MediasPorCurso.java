package com.rdr.avaliacao.relatorio;

import com.rdr.avaliacao.es.ExtratorDeDados;
import com.rdr.avaliacao.questionario.Curso;

/**Classe que estende {@link MediasDeNotas} composta de uma estrutura de dados para armazenar medias de notas de 
 * entrevistados relacionados a um curso*/
public class MediasPorCurso extends MediasDeNotas {
	private Curso curso;

	private static final String SEPARADOR_NOME_CURSO = " em "; 

	public MediasPorCurso(Curso curso) {
		super(formatarNomeCurso(curso));
		this.curso = curso;
	}

	public Curso getCurso() {
		return curso;
	}
	/**Formata o nome do curso passado. Retorna o conteúdo formatado.
	 * @param curso que possui nome a ser formatado. <br>
	 * Por exemplo: se for passado um curso com a descrição "Técnico", converte em: "Cursos Técnicos". Se houver junto a modalidade do curso,
	 *  a retira também. Por exemplo: "Bacharelado em Agronomia" torna-se "Agronomia".
	 *  
	 * @return descrição formatada. Se não for possível formatar, o conteúdo original será mantido.
	 */
	private static String formatarNomeCurso(Curso curso) {
			
			ExtratorDeDados.formatarNomeCursoTecnico(curso);
		
		try{
			curso.setDescricao(curso.getDescricao().split(SEPARADOR_NOME_CURSO)[1].trim());
		}catch (Exception e) {	}
		
		return curso.getDescricao();
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}



}
