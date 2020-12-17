package br.com.sigpr.jsf.cadastro.status;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.sigpr.ejb.cadastro.status.GerenciarStatus;
import br.com.sigpr.entity.status.Status;
import br.com.sigpr.enumerations.TipoStatus;
import br.com.sigpr.exceptions.SIGPRException;

@ManagedBean(name = "statusMBean")
@SessionScoped
public class StatusManagedBean implements Serializable {

	private static final long serialVersionUID = -1965106495304019574L;
	
	private static final String LISTA_STATUS = "listaStatus";

	@EJB
	private GerenciarStatus statusBean;

	private Status status;

	private List<Status> listaStatus;

	@PostConstruct
	public void init() throws SIGPRException {
		consultarStatus();
	}

	public List<Status> getListaStatus() {
		return listaStatus;
	}

	public List<Status> getListaStatusPorProjeto() {
		return statusBean.listarPorTipo(TipoStatus.Projeto);
	}

	public List<Status> getListaStatusPorFuncionalidade() {
		return statusBean.listarPorTipo(TipoStatus.Funcionalidade);
	}

	public List<Status> getListaStatusPorTarefa() {
		return statusBean.listarPorTipo(TipoStatus.Tarefa);
	}

	public TipoStatus[] getListaTipoStatus() {
		return TipoStatus.values();
	}

	private void consultarStatus() throws SIGPRException {
		this.listaStatus = statusBean.listar();
	}

	public String incluir() throws SIGPRException {
		status.setAtivo(true);
		statusBean.incluir(status);

		this.reset();
		consultarStatus();
		return LISTA_STATUS;
	}

	public String alterar() throws SIGPRException {
		status.setAtivo(true);
		statusBean.alterar(status);

		this.reset();
		consultarStatus();
		return LISTA_STATUS;
	}

	public String excluir() throws SIGPRException {
		statusBean.excluir(status);

		this.reset();
		consultarStatus();
		return LISTA_STATUS;
	}

	public Status getStatus() {
		if (status == null) {
			status = new Status();
		}
		return status;
	}

	public void setStatusSelecionado(Status status) {
		this.status = status;
	}

	private void reset() {
		this.status = null;
	}
	
	public String cancelar() throws SIGPRException{
		this.reset();
		consultarStatus();
		return LISTA_STATUS;
	}

}
