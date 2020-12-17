package br.com.sigpr.util.relatorio;

public class RelatorioColaboradores implements Comparable<RelatorioColaboradores>{

	private Long nuunidade;
	private String nounidade;
	private String nocolaborador;
	private Long nucolaborador;
	private String telefone;
	private String logradouro;
	private Long numero;
	private String complemento;
	private String bairro;
	private String cidade;
	private String estado;
	private String cep;
	private String ramal;
	private String perfil;
	private String papel;
	private String projeto;
	private String prazo;

	public Long getNuunidade() {
		return nuunidade;
	}

	public void setNuunidade(Long nuunidade) {
		this.nuunidade = nuunidade;
	}

	public String getNounidade() {
		return nounidade;
	}

	public void setNounidade(String nounidade) {
		this.nounidade = nounidade;
	}

	public String getNocolaborador() {
		return nocolaborador;
	}

	public void setNocolaborador(String nocolaborador) {
		this.nocolaborador = nocolaborador;
	}

	public Long getNucolaborador() {
		return nucolaborador;
	}

	public void setNucolaborador(Long nucolaborador) {
		this.nucolaborador = nucolaborador;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getRamal() {
		return ramal;
	}

	public void setRamal(String ramal) {
		this.ramal = ramal;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public String getPapel() {
		return papel;
	}

	public void setPapel(String papel) {
		this.papel = papel;
	}

	public String getProjeto() {
		return projeto;
	}

	public void setProjeto(String projeto) {
		this.projeto = projeto;
	}

	public String getPrazo() {
		return prazo;
	}

	public void setPrazo(String prazo) {
		this.prazo = prazo;
	}

	@Override
	public int compareTo(RelatorioColaboradores o) {
		if(this.nounidade.equals(o.nounidade)){
			if(this.projeto.equals(o.projeto)){
				if(this.papel.equals(o.papel)){
					if(this.perfil.equals(o.perfil)){
						return this.nocolaborador.compareTo(o.nocolaborador);
					}
					else{
						return this.perfil.compareTo(o.perfil);
					}
				}
				else{
					return this.papel.compareTo(o.papel);
				}
			}
			else{
				return this.projeto.compareTo(o.projeto);
			}
		}
		else{
			return this.nounidade.compareTo(o.nounidade);
		}
	}

}
