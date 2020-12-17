package br.com.sigpr.enumerations;

public enum Prioridade{
	
	BAIXA("Baixa"),
	MEDIA("Média"),
	ALTA("Alta");

	private String label;

	private Prioridade(String label){
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}
}
