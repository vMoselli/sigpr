package br.com.sigpr.util.relatorio;

public class RelatorioProjetosStatus implements Comparable<RelatorioProjetosStatus> {

	private String noprojeto;
	private Long nuprojeto;
	private String prazo;
	private Long nustatus;
	private String nostatus;

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

	@Override
	public int compareTo(RelatorioProjetosStatus o) {
		if(this.nostatus.equals(o.nostatus)){
			return this.noprojeto.compareTo(o.noprojeto);
		}else{
			return this.nostatus.compareTo(o.nostatus);
		}
	}

	public Long getNustatus() {
		return nustatus;
	}

	public void setNustatus(Long nustatus) {
		this.nustatus = nustatus;
	}

	public String getNostatus() {
		return nostatus;
	}

	public void setNostatus(String nostatus) {
		this.nostatus = nostatus;
	}

}
