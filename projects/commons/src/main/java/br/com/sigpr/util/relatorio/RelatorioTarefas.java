package br.com.sigpr.util.relatorio;

public class RelatorioTarefas implements Comparable<RelatorioTarefas> {

	private String nocolaborador;
	private Long nucolaborador;
	private String perfil;
	private String noprojeto;
	private Long nuprojeto;
	private String nofuncionalidade;
	private Long nufuncionalidade;
	private String notarefa;
	private String tipotarefa;
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

	public String getNotarefa() {
		return notarefa;
	}

	public void setNotarefa(String notarefa) {
		this.notarefa = notarefa;
	}

	public String getTipotarefa() {
		return tipotarefa;
	}

	public void setTipotarefa(String tipotarefa) {
		this.tipotarefa = tipotarefa;
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

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
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
	public int compareTo(RelatorioTarefas o) {
		if (this.nocolaborador.equals(o.nocolaborador)) {
			if(this.noprojeto.equals(o.noprojeto)){
				if(this.nofuncionalidade.equals(o.nofuncionalidade)){
					return this.notarefa.compareTo(o.notarefa);
				}
				else{
					return this.nofuncionalidade.compareTo(o.nofuncionalidade);
				}
			}
			else{
				return this.noprojeto.compareTo(o.noprojeto);
			}
		} 
		else {
			return this.nocolaborador.compareTo(o.nocolaborador);
		}
	}

}
