package br.com.sigpr.enumerations;

public enum Estado {
	
	AC("AC","Acre"),
	AL("AL","Alagoas"),
	AP("AP","Amap�"),
	AM("AM","Amazonas"),
	BA("BA","Bahia"),
	CE("CE","Cer�"),
	DF("DF","Distrito Federal"),
	ES("ES","Esp�rito Santo"),
	GO("GO","Goi�s"),
	MA("MA","Maranh�o"),
	MT("MT","Mato Grosso"),
	MS("MS","Mato Grosso do Sul"),
	MG("MG","Minas Gerais"),
	PA("PA","Par�"),
	PB("PB","Para�ba"),
	PR("PR","Paran�"),
	PE("PE","Pernambuco"),
	PI("PI","Piau�"),
	RJ("RJ","Rio de Janeiro"),
	RN("RN","Rio Grande do Norte"),
	RS("RS","Rio Grande do Sul"),
	RO("RO","Rond�nia"),
	RR("RR","Roraima"),
	SC("SC","Santa Catarina"),
	SP("SP","S�o Paulo"),
	SE("SE","Sergipe"),
	TO("TO","Tocantins");
	
	private String sigla;
	private String nome;
	
	private Estado(String sigla, String nome){
		this.sigla = sigla;
		this.nome = nome;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
