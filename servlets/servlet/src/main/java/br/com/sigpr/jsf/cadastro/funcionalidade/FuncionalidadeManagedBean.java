package br.com.sigpr.jsf.cadastro.funcionalidade;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.com.sigpr.ejb.cadastro.colaborador.GerenciarColaborador;
import br.com.sigpr.ejb.cadastro.funcionalidade.GerenciarFuncionalidade;
import br.com.sigpr.ejb.cadastro.projeto.GerenciarProjeto;
import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.funcionalidade.Funcionalidade;
import br.com.sigpr.entity.projeto.Projeto;
import br.com.sigpr.entity.status.Status;
import br.com.sigpr.exceptions.SIGPRException;

@ManagedBean( name = "funcionalidadeMBean")
@SessionScoped
public class FuncionalidadeManagedBean implements Serializable {
	
	private static final long serialVersionUID = 2531468783714879684L;

	private static final String LISTA_FUNCIONALIDADE = "listaFuncionalidade";
	private static final String EDITAR_FUNCIONALIDADE = "editarFuncionalidade";

	private static final String MSG_SUCESSO_INCLUSAO = "Funcionalidade incluída com sucesso.";
	private static final String MSG_SUCESSO_ALTERACAO = "Funcionalidade alterada com sucesso.";
	private static final String MSG_SUCESSO_EXCLUSAO = "Funcionalidade excluída com sucesso.";

	@EJB
	private GerenciarFuncionalidade funcionalidadeBean;
	
	@EJB
	private GerenciarColaborador colaboradorBean;
	
	@EJB
	private GerenciarProjeto projetoBean;
	
	private Funcionalidade funcionalidade;
	private Projeto projeto;
	private Status status;
	
	private Colaborador colaborador;
	private List<Projeto> listaProjeto;
	private List<Funcionalidade> listaFuncionalidade;
	
	public List<Funcionalidade> getListaFuncionalidade(){
		return listaFuncionalidade;
	}
	
	private void consultarFuncionalidades() throws SIGPRException{
		this.listaFuncionalidade = funcionalidadeBean.listar();
	}
	
	private void consultarProjetos() throws SIGPRException{
		this.listaProjeto = projetoBean.listar();
	}
	
	public String inicializaEdicao() throws SIGPRException{
		consultarProjetos();
		
		return EDITAR_FUNCIONALIDADE;
	}
	
	public String incluir() throws SIGPRException{
		funcionalidade.setAtivo(true);
		funcionalidade.setProjeto(projeto);
		funcionalidade.setStatus(status);
		
		funcionalidadeBean.incluir(funcionalidade);

		this.reset();
		consultarFuncionalidades();
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_SUCESSO_INCLUSAO, ""));
		return LISTA_FUNCIONALIDADE;
	}

	public String alterar() throws SIGPRException{
		funcionalidade.setAtivo(true);
		funcionalidade.setProjeto(projeto);
		funcionalidade.setStatus(status);
		
		funcionalidadeBean.alterar(funcionalidade);

		this.reset();
		consultarFuncionalidades();
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_SUCESSO_ALTERACAO, ""));
		return LISTA_FUNCIONALIDADE;
	}

	public String excluir() throws SIGPRException{
		funcionalidade.setProjeto(projeto);
		funcionalidade.setStatus(status);
		
		funcionalidadeBean.excluir(funcionalidade);

		this.reset();
		consultarFuncionalidades();
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_SUCESSO_EXCLUSAO, ""));
		return LISTA_FUNCIONALIDADE;
	}

	public void setFuncionalidadeSelecionada(Funcionalidade funcionalidade){
		this.projeto = funcionalidade.getProjeto();
		this.status = funcionalidade.getStatus();
		this.funcionalidade = funcionalidade;
	}
	
	public void setColaboradorLogado(long idColaborador) throws SIGPRException{
		this.reset();
		
		this.colaborador = colaboradorBean.findById(Long.valueOf(idColaborador));
		consultarFuncionalidades();
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

	public Status getStatus() {
		if(status == null){
			status = new Status();
		}
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public Funcionalidade getFuncionalidade(){
		if(funcionalidade == null){
			funcionalidade = new Funcionalidade();
		}
		return funcionalidade;
	}
	
	public void setFuncionalidade(Funcionalidade funcionalidade){
		this.funcionalidade = funcionalidade;
	}
	
	private void reset(){
		this.projeto = null;
		this.status = null;
		this.funcionalidade = null;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}
	
	public String cancelar() throws SIGPRException{
		this.reset();
		consultarFuncionalidades();
		
		return LISTA_FUNCIONALIDADE;
	}

	public List<Projeto> getListaProjeto() {
		return listaProjeto;
	}

	public void setListaProjeto(List<Projeto> listaProjeto) {
		this.listaProjeto = listaProjeto;
	}

}
