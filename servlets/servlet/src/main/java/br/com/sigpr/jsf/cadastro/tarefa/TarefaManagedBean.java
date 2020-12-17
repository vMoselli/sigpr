package br.com.sigpr.jsf.cadastro.tarefa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import br.com.sigpr.ejb.cadastro.colaborador.GerenciarColaborador;
import br.com.sigpr.ejb.cadastro.colaboradorprojeto.GerenciarColaboradorProjeto;
import br.com.sigpr.ejb.cadastro.funcionalidade.GerenciarFuncionalidade;
import br.com.sigpr.ejb.cadastro.projeto.GerenciarProjeto;
import br.com.sigpr.ejb.cadastro.tarefa.GerenciarTarefa;
import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.colaboradorprojeto.ColaboradorProjeto;
import br.com.sigpr.entity.funcionalidade.Funcionalidade;
import br.com.sigpr.entity.projeto.Projeto;
import br.com.sigpr.entity.status.Status;
import br.com.sigpr.entity.tarefa.Tarefa;
import br.com.sigpr.enumerations.TipoTarefa;
import br.com.sigpr.exceptions.SIGPRException;
import br.com.sigpr.util.comparators.QuantidadeTarefasComparator;

@ManagedBean(name = "tarefaMBean")
@SessionScoped
public class TarefaManagedBean implements Serializable {
	
	private static final long serialVersionUID = 2531468783714879684L;

	private static final String LISTA_TAREFA = "listaTarefa";
	private static final String SELECAO_TAREFA = "selecaoTarefa";
	private static final String ATRIBUIR_TAREFA = "atribuirTarefa";
	private static final String EDITAR_TAREFA = "editarTarefa";
	private static final String MSG_SUCESSO_INCLUSAO = "Tarefa incluída com sucesso.";
	private static final String MSG_SUCESSO_ALTERACAO = "Tarefa alterada com sucesso.";
	private static final String MSG_SUCESSO_EXCLUSAO = "Tarefa excluída com sucesso.";
	private static final String MSG_SUCESSO_ATRIBUICAO = "Tarefa atribuída com sucesso.";

	@EJB
	private GerenciarTarefa tarefaBean;
	
	@EJB
	private GerenciarFuncionalidade funcionalidadeBean;
	
	@EJB
	private GerenciarColaboradorProjeto colaboradorProjetoBean;
	
	@EJB
	private GerenciarProjeto projetoBean;
	
	@EJB
	private GerenciarColaborador colaboradorBean;
	
	private Tarefa tarefa;
	private Funcionalidade funcionalidade;
	private Projeto projeto;
	private Status status;
	private TipoTarefa tipoTarefa;
	
	private Colaborador colaborador;
	private Colaborador colaboradorSelecionado;
	
	private List<Projeto> listaProjeto;
	private List<Funcionalidade> listaFuncionalidade;
	private List<Tarefa> listaTarefa;
	private List<Colaborador> listaColaboradorPorProjeto;
	
	public List<Tarefa> getListaTarefa(){
		return listaTarefa;
	}
	
	private void consultarTarefas() throws SIGPRException{
		this.listaTarefa = tarefaBean.listar();
	}
	
	private void consultarTarefasParaAtribuicao() throws SIGPRException{
		this.listaTarefa = tarefaBean.consultarTarefaPorFuncionalidade(getFuncionalidade());
	}
	
	public String inicializaEdicao() throws SIGPRException{
		consultarProjetos();
		
		if(this.getProjeto().getId() != null && this.getProjeto().getId().longValue() > 0){
			consultarFuncionalidades();
		}
		
		return EDITAR_TAREFA;
	}
	
	public String incluir() throws SIGPRException{
		tarefa.setAtivo(true);
		tarefa.setFuncionalidade(funcionalidade);
		tarefa.setStatus(status);
		tarefa.setTipoTarefa(tipoTarefa);
		
		tarefaBean.incluir(tarefa);

		this.reset();
		consultarTarefas();
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_SUCESSO_INCLUSAO, ""));
		return LISTA_TAREFA;
	}

	public String alterar() throws SIGPRException{
		tarefa.setAtivo(true);
		tarefa.setFuncionalidade(funcionalidade);
		tarefa.setStatus(status);
		tarefa.setTipoTarefa(tipoTarefa);
		
		tarefaBean.alterar(tarefa);

		this.reset();
		consultarTarefas();
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_SUCESSO_ALTERACAO, ""));
		return LISTA_TAREFA;
	}

	public String excluir() throws SIGPRException{
		tarefa.setFuncionalidade(funcionalidade);
		tarefa.setStatus(status);
		tarefa.setTipoTarefa(tipoTarefa);
		
		tarefaBean.excluir(tarefa);

		this.reset();
		consultarTarefas();
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_SUCESSO_EXCLUSAO, ""));
		return LISTA_TAREFA;
	}
	
	public List<Projeto> getListaProjeto(){
		if(this.listaProjeto == null){
			this.listaProjeto = new ArrayList<Projeto>();
		}
		return this.listaProjeto;
	}
	
	public List<Funcionalidade> getListaFuncionalidade(){
		if(this.listaFuncionalidade == null){
			this.listaFuncionalidade = new ArrayList<Funcionalidade>();
		}
		return this.listaFuncionalidade;
	}
	
	public void definirProjeto(AjaxBehaviorEvent event) throws SIGPRException{
		consultarFuncionalidades();
	}
	
	public void definirFuncionalidade(AjaxBehaviorEvent event) throws SIGPRException{
		consultarTarefasParaAtribuicao();
	}
	
	private void consultarProjetos() throws SIGPRException{
		this.listaProjeto = projetoBean.listar();
		this.listaFuncionalidade = null;
		this.listaTarefa = null;
	}
	
	private void consultarFuncionalidades() throws SIGPRException{
		if(getProjeto().getId() != null && getProjeto().getId().longValue() > 0l){
			this.listaFuncionalidade = funcionalidadeBean.consultarFuncionalidadePorProjeto(getProjeto());
		}
		this.listaTarefa = null;
	}

	public void setTarefaSelecionada(Tarefa tarefa) throws SIGPRException{
		this.funcionalidade = tarefa.getFuncionalidade();
		this.projeto = funcionalidade.getProjeto();
		this.status = tarefa.getStatus();
		this.tarefa = tarefa;
		this.tipoTarefa = tarefa.getTipoTarefa();
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
	
	public Tarefa getTarefa(){
		if(tarefa == null){
			tarefa = new Tarefa();
		}
		return tarefa;
	}
	
	public void setTarefa(Tarefa tarefa){
		this.tarefa = tarefa;
	}
	
	private void reset(){
		this.tarefa = null;
		this.projeto = null;
		this.status = null;
		this.funcionalidade = null;
		this.tipoTarefa = null;
		this.listaTarefa = null;
	}

	public TipoTarefa getTipoTarefa() {
		return tipoTarefa;
	}

	public void setTipoTarefa(TipoTarefa tipoTarefa) {
		this.tipoTarefa = tipoTarefa;
	}
	
	public TipoTarefa[] getListaTipoTarefa(){
		return TipoTarefa.values();
	}
	
	public String cancelar() throws SIGPRException{
		this.reset();
		consultarTarefas();
		return LISTA_TAREFA;
	}
	
	public void setColaboradorLogado(long idColaborador) throws SIGPRException{
		this.reset();
		this.colaborador = colaboradorBean.findById(Long.valueOf(idColaborador));
			
		consultarTarefas();
	}
	
	public void setColaboradorLogadoAtribuicao(long idColaborador) throws SIGPRException{
		this.resetAtribuicao();
		this.colaborador = colaboradorBean.findById(Long.valueOf(idColaborador));
			
		consultarProjetos();
	}

	public List<Colaborador> getListaColaboradorPorProjeto() {
		if(listaColaboradorPorProjeto == null){
			listaColaboradorPorProjeto = new ArrayList<Colaborador>();
		}
		return listaColaboradorPorProjeto;
	}
	
	public String selecionarTarefa() throws SIGPRException{
		consultarColaboradoresPorProjeto();
		
		return ATRIBUIR_TAREFA;
	}

	private void consultarColaboradoresPorProjeto() throws SIGPRException {
		this.projeto = projetoBean.findById(this.projeto.getId());
		this.funcionalidade = funcionalidadeBean.findById(this.funcionalidade.getId());
		this.tarefa = tarefaBean.findById(this.tarefa.getId());
		
		List<ColaboradorProjeto> colaboradoresPorProjeto =
				colaboradorProjetoBean.listarPorProjeto(getProjeto());
		
		for(ColaboradorProjeto colaboradorProjeto : colaboradoresPorProjeto){
			getListaColaboradorPorProjeto().add(colaboradorProjeto.getColaborador());
		}
		
		Collections.sort(getListaColaboradorPorProjeto(), new QuantidadeTarefasComparator());
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}
	
	public String atribuir() throws SIGPRException{
		
		tarefa.setColaborador(colaboradorSelecionado);
		colaborador.getTarefas().add(tarefa);
		
		tarefaBean.atribuirTarefa(tarefa);
		
		this.resetAtribuicao();
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_SUCESSO_ATRIBUICAO, ""));
		consultarProjetos();
		return SELECAO_TAREFA;
	}

	private void resetAtribuicao() {
		this.tarefa = null;
		this.projeto = null;
		this.funcionalidade = null;
		this.listaColaboradorPorProjeto = null;
		this.listaProjeto = null;
		this.listaFuncionalidade = null;
		this.listaTarefa = null;
		this.colaboradorSelecionado = null;
	}

	public Colaborador getColaboradorSelecionado() {
		if(colaboradorSelecionado == null){
			colaboradorSelecionado = new Colaborador();
		}
		return colaboradorSelecionado;
	}

	public void setColaboradorSelecionado(Colaborador colaboradorSelecionado) {
		this.colaboradorSelecionado = colaboradorSelecionado;
	}
	
	public String cancelarAtribuicao() throws SIGPRException{
		this.resetAtribuicao();
		consultarProjetos();
		return SELECAO_TAREFA;
	}

}
