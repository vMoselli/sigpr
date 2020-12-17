package br.com.sigpr.util.relatorio;

public class RelatorioArtefatosTarefa implements Comparable<RelatorioArtefatosTarefa> {

	private String nocolaborador;
	private Long nucolaborador;
	private String perfil;
	private String noprojeto;
	private String nofuncionalidade;
	private String notarefa;
	private String tipotarefa;
	private Long nuprojeto;
	private Long nufuncionalidade;
	private String prazo;
	private String noartefato;
	private String descricao;
	private String fileName;
	private String fileSize;
	private String fileType;

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
	public int compareTo(RelatorioArtefatosTarefa o) {
		if (this.nocolaborador.equals(o.nocolaborador)) {
			if(this.noprojeto.equals(o.noprojeto)){
				if(this.nofuncionalidade.equals(o.nofuncionalidade)){
					if(this.notarefa.equals(o.notarefa)){
						return this.noartefato.compareTo(o.noartefato);
					}
					else{
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
		else {
			return this.nocolaborador.compareTo(o.nocolaborador);
		}
	}

	public String getNoartefato() {
		return noartefato;
	}

	public void setNoartefato(String noartefato) {
		this.noartefato = noartefato;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

}
