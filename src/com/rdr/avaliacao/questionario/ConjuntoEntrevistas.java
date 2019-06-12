package com.rdr.avaliacao.questionario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConjuntoEntrevistas {
	
	private Map<Integer, Entrevista> conjunto;
	private List<String> listaCursos;
	
	public ConjuntoEntrevistas() {
		conjunto = new HashMap<Integer, Entrevista>();
	}
	
	//código do entrevistado(aluno); descricao curso; pergunta; assunto; conceito; 
	public void inserir(int codigoEntrevistado, String descricaoCurso, String campus, String descricaoPergunta,
			String descricaoAssunto, int notaConceito) {
		
		if(!conjunto.containsKey(codigoEntrevistado)) {
			conjunto.put(codigoEntrevistado, new Entrevista(new Aluno(campus, descricaoCurso)));
		}	
		
		Entrevista entrevista = conjunto.get(codigoEntrevistado);
		entrevista.adicionarResposta(notaConceito, descricaoPergunta, descricaoAssunto);
		
	}
	
	public void inserir(int codigoEntrevistado, String campus, String descricaoPergunta,
			String descricaoAssunto, int notaConceito, String segmento) {
		
		if(!conjunto.containsKey(codigoEntrevistado)) {
			conjunto.put(codigoEntrevistado, new Entrevista(new Entrevistado(campus, segmento)));
		}	
		
		Entrevista entrevista = conjunto.get(codigoEntrevistado);
		entrevista.adicionarResposta(notaConceito, descricaoPergunta, descricaoAssunto);
		
	}
	
	
}
