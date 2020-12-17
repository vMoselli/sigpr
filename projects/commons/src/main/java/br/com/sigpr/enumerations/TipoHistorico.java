package br.com.sigpr.enumerations;

public enum TipoHistorico {
	
	INCLUSAO("Inclusão"),
	ALTERACAO("Alteração"),
	EXCLUSAO("Exclusão");
	
	private TipoHistorico(String nomeTipo){
		this.nomeTipo = nomeTipo;
	}
	
	private String nomeTipo;

	public String getNomeTipo() {
		return nomeTipo;
	}

}
