package br.com.sigpr.jsf.cadastro.projeto;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.com.sigpr.ejb.cadastro.colaborador.GerenciarColaborador;
import br.com.sigpr.ejb.cadastro.projeto.GerenciarProjeto;
import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.projeto.Projeto;
import br.com.sigpr.entity.status.Status;
import br.com.sigpr.exceptions.SIGPRException;

@ManagedBean( name = "projetoMBean")
@SessionScoped
public class ProjetoManagedBean implements Serializable {

	private static final long serialVersionUID = -5468470516952359916L;
	
	private static final String LISTA_PROJETO = "listaProjeto";
	private static final String MSG_SUCESSO_INCLUSAO = "Projeto incluído com sucesso.";
	private static final String MSG_SUCESSO_ALTERACAO = "Projeto alterado com sucesso.";
	private static final String MSG_SUCESSO_EXCLUSAO = "Projeto excluído com sucesso.";

	@EJB
	private GerenciarProjeto projetoBean;
	
	@EJB
	private GerenciarColaborador colaboradorBean;
	
	private Projeto projeto;
	private Status status;
	
	private Colaborador colaborador;
	private List<Projeto> listaProjeto;
	
	public List<Projeto> getListaProjeto(){
		return listaProjeto;
	}
	
	public String incluir() throws SIGPRException{
		projeto.setAtivo(true);
		projeto.setStatus(status);
		projetoBean.incluir(projeto);

		this.reset();
		consultarProjetos();
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_SUCESSO_INCLUSAO, ""));
		return LISTA_PROJETO;
	}

	public String alterar() throws SIGPRException{
		projeto.setAtivo(true);
		projeto.setStatus(status);
		projetoBean.alterar(projeto);

		this.reset();
		consultarProjetos();
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_SUCESSO_ALTERACAO, ""));
		return LISTA_PROJETO;
	}

	public String excluir() throws SIGPRException{
		projeto.setStatus(status);
		projetoBean.excluir(projeto);

		this.reset();
		consultarProjetos();
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_SUCESSO_EXCLUSAO, ""));
		return LISTA_PROJETO;
	}
	
	public void setColaboradorLogado(long idColaborador) throws SIGPRException{
		this.reset();
		
		this.colaborador = colaboradorBean.findById(Long.valueOf(idColaborador));
		consultarProjetos();
	}
	
	private void consultarProjetos() throws SIGPRException{
		this.listaProjeto = projetoBean.listar();
	}

	public void setProjetoSelecionado(Projeto projeto){
		this.projeto = projeto;
		this.status = projeto.getStatus();
	}

	public Projeto getProjeto() {
		if(projeto == null){
			projeto = new Projeto();
		}
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	private void reset(){
		this.projeto = null;
		this.status = null;
	}

	public Status getStatus() {
		if(status == null){
			status = new Status();
		}
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}
	
	public String cancelar() throws SIGPRException{
		this.reset();
		
		consultarProjetos();
		return LISTA_PROJETO;
	}

}
