package br.com.sigpr.util.relatorio;

public class RelatorioProjetos implements Comparable<RelatorioProjetos> {

	private String nocolaborador;
	private Long nucolaborador;
	private String perfil;
	private String papel;
	private String noprojeto;
	private Long nuprojeto;
	private String prazo;
	private Boolean isvencido;

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

	public String getNoprojeto() {
		return noprojeto;
	}

	public void setNoprojeto(String noprojeto) {
		this.noprojeto = noprojeto;
	}

	public Long getNuprojeto() {
		return nuprojeto;
	}

	public void setNuprojeto(Long nuprojeto) {
		this.nuprojeto = nuprojeto;
	}

	public String getPrazo() {
		return prazo;
	}

	public void setPrazo(String prazo) {
		this.prazo = prazo;
	}

	public Boolean getIsvencido() {
		return isvencido;
	}

	public void setIsvencido(Boolean isvencido) {
		this.isvencido = isvencido;
	}

	@Override
	public int compareTo(RelatorioProjetos o) {
		if (this.nocolaborador.equals(o.nocolaborador)) {
			return this.noprojeto.compareTo(o.noprojeto);
		} else {
			return this.nocolaborador.compareTo(o.nocolaborador);
		}
	}

}
