package br.com.sigpr.util.relatorio;

public class RelatorioFuncionalidades implements Comparable<RelatorioFuncionalidades> {

	private String noprojeto;
	private Long nuprojeto;
	private String nofuncionalidade;
	private Long nufuncionalidade;
	private String prazo;

	public String getNofuncionalidade() {
		return nofuncionalidade;
	}

	public void setNofuncionalidade(String nofuncionalidade) {
		this.nofuncionalidade = nofuncionalidade;
	}

	public Long getNufuncionalidade() {
		return nufuncionalidade;
	}

	public void setNufuncionalidade(Long nufuncionalidade) {
		this.nufuncionalidade = nufuncionalidade;
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

	@Override
	public int compareTo(RelatorioFuncionalidades o) {
		if(this.noprojeto.equals(o.noprojeto)){
			return this.nofuncionalidade.compareTo(o.nofuncionalidade);
		}
		else{
			return this.noprojeto.compareTo(o.noprojeto);
		}
	}

}
