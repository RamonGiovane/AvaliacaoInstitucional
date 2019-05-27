package com.rdr.avaliacao.questionario;

public enum Segmento {
		DISCENTE("Discente"), DOCENTE("Docente"), TECNICO_ADMINISTRATIVO("Técnico Administrativo da Educação");
		
		private String descricao;
		
		private Segmento(String descricao) {
			this.descricao = descricao;
		
		
	}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}

		@Override
		public String toString() {
			return descricao;
		}
		
		
}
