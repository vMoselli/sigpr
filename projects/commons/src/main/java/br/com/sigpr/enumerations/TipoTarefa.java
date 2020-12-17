package br.com.sigpr.enumerations;

public enum TipoTarefa {

	CONSTRUCAO("Construção"),
	ALTERACAO("Alteração"),
	ESPECIFICACAO("Especificação"),
	CORRECAO("Correção"),
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
