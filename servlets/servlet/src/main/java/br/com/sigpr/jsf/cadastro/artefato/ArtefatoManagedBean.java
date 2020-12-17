package br.com.sigpr.jsf.cadastro.artefato;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.sigpr.ejb.cadastro.artefato.GerenciarArtefato;
import br.com.sigpr.ejb.cadastro.colaborador.GerenciarColaborador;
import br.com.sigpr.ejb.cadastro.funcionalidade.GerenciarFuncionalidade;
import br.com.sigpr.ejb.cadastro.projeto.GerenciarProjeto;
import br.com.sigpr.ejb.cadastro.tarefa.GerenciarTarefa;
import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.funcionalidade.Funcionalidade;
import br.com.sigpr.entity.projeto.Projeto;
import br.com.sigpr.entity.tarefa.Arquivo;
import br.com.sigpr.entity.tarefa.Artefato;
import br.com.sigpr.entity.tarefa.Tarefa;
import br.com.sigpr.exceptions.SIGPRException;

@ManagedBean(name = "artefatoMBean")
@SessionScoped
public class ArtefatoManagedBean implements Serializable {
	
	private static final long serialVersionUID = 1416333277874818883L;
	
	private static final String LISTA_ARTEFATO = "listaArtefato";
	
	@EJB
	private GerenciarProjeto projetoBean;
	
	@EJB
	private GerenciarFuncionalidade funcionalidadeBean;
	
	@EJB
	private GerenciarTarefa tarefaBean;
	
	@EJB
	private GerenciarArtefato artefatoBean;
	
	@EJB
	private GerenciarColaborador colaboradorBean;
	
	private Artefato artefato;
	private Tarefa tarefa;
	private Projeto projeto;
	private Funcionalidade funcionalidade;
	private Arquivo arquivo;
	
	private List<Arquivo> listaArquivos;
	private List<Artefato> listaArtefatos;
	
	private StreamedContent fileDownload;
	
	private Colaborador colaborador;
	
	private List<Projeto> listaProjeto;
	private List<Funcionalidade> listaFuncionalidade;
	private List<Tarefa> listaTarefa;
	
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
	
	private void consultarTarefas() throws SIGPRException{
		this.listaTarefa = tarefaBean.consultarTarefaPorFuncionalidade(getFuncionalidade());
	}
	
	public void definirProjeto(AjaxBehaviorEvent event) throws SIGPRException{
		consultarFuncionalidades();
	}
	
	public void definirFuncionalidade(AjaxBehaviorEvent event) throws SIGPRException{
		consultarTarefas();
	}
	
	public void definirTarefa(AjaxBehaviorEvent event) throws SIGPRException{
		this.tarefa = tarefaBean.findById(tarefa.getId());
		consultarListaArtefatos();
	}
	
	public String incluir() throws SIGPRException{
		artefato.setAtivo(true);
		artefato.setTarefa(tarefa);
		artefato.setArquivos(getListaArquivos());
		
		artefatoBean.incluir(artefato);
		
		consultarListaArtefatos();
		
		this.reset();
		return LISTA_ARTEFATO;
	}
	
	public String alterar() throws SIGPRException{
		artefato.setAtivo(true);
		artefato.setTarefa(tarefa);
		artefato.setArquivos(getListaArquivos());
		
		artefatoBean.alterar(artefato);
		consultarListaArtefatos();
		
		this.reset();
		return LISTA_ARTEFATO;
	}
	
	public void excluir() throws SIGPRException{
		
		artefatoBean.excluir(artefato);
		consultarListaArtefatos();
		
		this.reset();
	}
	
	public void consultarListaArtefatos(){
		this.listaArtefatos = artefatoBean.consultarArtefatosPorTarefa(tarefa);
	}

	public void handleFileUpload(FileUploadEvent event){
		
		Arquivo arquivo = new Arquivo();
		arquivo.setArquivo(event.getFile().getContents());
		arquivo.setFileName(event.getFile().getFileName().substring(0, event.getFile().getFileName().lastIndexOf('.')));
		arquivo.setFileType(event.getFile().getFileName().substring(event.getFile().getFileName().lastIndexOf('.')));
		arquivo.setMimeType(event.getFile().getContentType());
		arquivo.setFileSize(Long.valueOf(event.getFile().getSize()));
		arquivo.setArtefato(getArtefato());
		
		getListaArquivos().add(arquivo);
		
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Arquivo enviado com sucesso.", "Arquivo enviado com sucesso."));
	}
	
	public void handleFileDownload(Arquivo arquivo){
		this.fileDownload = new DefaultStreamedContent(
				new ByteArrayInputStream(arquivo.getArquivo()), 
				arquivo.getMimeType(),
				arquivo.getFileName()+arquivo.getFileType());
	}
	
	public void excluirArquivo(){
		if(this.arquivo != null){
			if(arquivo.getId() != null && arquivo.getId().longValue() > 0l){
				artefatoBean.excluirArquivo(arquivo);
			}
			listaArquivos.remove(arquivo);
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Arquivo excluído com sucesso.", "Arquivo excluído com sucesso."));
		}
	}
	
	public void setColaboradorLogado(long idColaborador) throws SIGPRException{
		this.reset();
		
		this.colaborador = colaboradorBean.findById(Long.valueOf(idColaborador));
		
		consultarProjetos();
	}
	
	private void reset() {
		this.artefato = null;
		this.arquivo = null;
		this.listaArquivos = null;
	}
	
	public void setArquivoSelecionado(Arquivo arquivo){
		this.arquivo = arquivo;
	}
	
	public void setArtefatoSelecionado(Artefato artefato) throws SIGPRException{
		this.artefato = artefato;
		this.listaArquivos = artefato.getArquivos();
	}
	
	public Artefato getArtefato() {
		if(artefato == null){
			artefato = new Artefato();
		}
		return artefato;
	}

	public void setArtefato(Artefato artefato) {
		this.artefato = artefato;
	}

	public Tarefa getTarefa() {
		if(tarefa == null){
			tarefa = new Tarefa();
		}
		return tarefa;
	}

	public void setTarefa(Tarefa tarefa) {
		this.tarefa = tarefa;
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

	public List<Arquivo> getListaArquivos() {
		if(listaArquivos == null){
			listaArquivos = new ArrayList<Arquivo>();
		}
		return listaArquivos;
	}

	public void setListaArquivos(List<Arquivo> listaArquivos) {
		this.listaArquivos = listaArquivos;
	}

	public List<Artefato> getListaArtefatos() {
		if(listaArtefatos == null){
			listaArtefatos = new ArrayList<Artefato>();
		}
		return listaArtefatos;
	}

	public Funcionalidade getFuncionalidade() {
		if(funcionalidade == null){
			funcionalidade = new Funcionalidade();
		}
		return funcionalidade;
	}

	public void setFuncionalidade(Funcionalidade funcionalidade) {
		this.funcionalidade = funcionalidade;
	}

	public StreamedContent getFileDownload() {
		return fileDownload;
	}
	
	public void setFileDownload(StreamedContent fileDownload){
		this.fileDownload = fileDownload;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}
	
	public List<Projeto> getListaProjeto(){
		if(listaProjeto == null){
			listaProjeto = new ArrayList<Projeto>();
		}
		return listaProjeto;
	}
	
	public List<Funcionalidade> getListaFuncionalidade(){
		if(listaFuncionalidade == null){
			listaFuncionalidade = new ArrayList<Funcionalidade>();
		}
		return listaFuncionalidade;
	}
	
	public List<Tarefa> getListaTarefa(){
		if(listaTarefa == null){
			listaTarefa = new ArrayList<Tarefa>();
		}
		return listaTarefa;
	}

}
