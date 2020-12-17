package br.com.sigpr.util.relatorio;

public class RelatorioHorasTarefa implements Comparable<RelatorioHorasTarefa> {

	private String noprojeto;
	private String nofuncionalidade;
	private String notarefa;
	private String prazo;
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

	public String getNoprojeto() {
		return noprojeto;
	}

	public void setNoprojeto(String noprojeto) {
		this.noprojeto = noprojeto;
	}

	public String getNofuncionalidade() {
		return nofuncionalidade;
	}

	public void setNofuncionalidade(String nofuncionalidade) {
		this.nofuncionalidade = nofuncionalidade;
	}

	public String getNotarefa() {
		return notarefa;
	}

	public void setNotarefa(String notarefa) {
		this.notarefa = notarefa;
	}

	public String getPrazo() {
		return prazo;
	}

	public void setPrazo(String prazo) {
		this.prazo = prazo;
	}

	@Override
	public int compareTo(RelatorioHorasTarefa o) {
		if(this.noprojeto.equals(o.noprojeto)){
			if(this.nofuncionalidade.equals(o.nofuncionalidade)){
				if(this.notarefa.equals(o.notarefa)){
					return this.nocolaborador.compareTo(o.nocolaborador);
				}else{
					return this.notarefa.compareTo(o.notarefa);
				}
			}
			else{
				return this.nofuncionalidade.compareTo(o.nofuncionalidade);
			}
		}
		else{
			return this.noprojeto.compareTo(o.noprojeto);
		}
	}
	
}
