package br.com.sigpr.enumerations;

public enum TipoHistorico {
	
	INCLUSAO("Inclus�o"),
	ALTERACAO("Altera��o"),
	EXCLUSAO("Exclus�o");
	
	private TipoHistorico(String nomeTipo){
		this.nomeTipo = nomeTipo;
	}
	
	private String nomeTipo;

	public String getNomeTipo() {
		return nomeTipo;
	}

}
