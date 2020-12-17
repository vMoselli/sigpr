package br.com.sigpr.enumerations;

public enum TipoTarefa {

	CONSTRUCAO("Constru��o"),
	ALTERACAO("Altera��o"),
	ESPECIFICACAO("Especifica��o"),
	CORRECAO("Corre��o"),
	TESTE("Teste");
	
	
	private String nome;
	
	private TipoTarefa(String nome){
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
