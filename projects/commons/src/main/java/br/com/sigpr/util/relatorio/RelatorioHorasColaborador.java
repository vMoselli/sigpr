package br.com.sigpr.util.relatorio;

public class RelatorioHorasColaborador implements Comparable<RelatorioHorasColaborador> {

	private String nocolaborador;
	private Long nucolaborador;
	private String perfil;
	private String dataReferencia;
	private String inicio;
	private String fim;
	private String total;

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


	@Override
	public int compareTo(RelatorioHorasColaborador o) {
		return this.nocolaborador.compareTo(o.nocolaborador);
	}

	public String getDataReferencia() {
		return dataReferencia;
	}

	public void setDataReferencia(String dataReferencia) {
		this.dataReferencia = dataReferencia;
	}

	public String getInicio() {
		return inicio;
	}

	public void setInicio(String inicio) {
		this.inicio = inicio;
	}

	public String getFim() {
		return fim;
	}

	public void setFim(String fim) {
		this.fim = fim;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

}
