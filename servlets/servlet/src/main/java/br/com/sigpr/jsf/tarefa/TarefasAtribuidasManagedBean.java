package br.com.sigpr.jsf.tarefa;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.sigpr.ejb.cadastro.artefato.GerenciarArtefato;
import br.com.sigpr.ejb.cadastro.colaborador.GerenciarColaborador;
import br.com.sigpr.ejb.cadastro.status.GerenciarStatus;
import br.com.sigpr.ejb.horas.GerenciarHoraColaborador;
import br.com.sigpr.ejb.horas.GerenciarHoraTarefa;
import br.com.sigpr.ejb.tarefa.GerenciarFinalizacaoTarefa;
import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.horas.HoraTarefa;
import br.com.sigpr.entity.status.Status;
import br.com.sigpr.entity.tarefa.Arquivo;
import br.com.sigpr.entity.tarefa.Artefato;
import br.com.sigpr.entity.tarefa.Tarefa;
import br.com.sigpr.enumerations.TipoStatus;
import br.com.sigpr.exceptions.SIGPRException;

@ManagedBean(name = "tarefasAtribuidasMBean")
@SessionScoped
public class TarefasAtribuidasManagedBean implements Serializable {

	private static final long serialVersionUID = 3479251632372505891L;

	private static final String TAREFAS_ATRIBUIDAS = "tarefasAtribuidas";
	private static final String VISUALIZAR_TAREFA = "visualizarTarefa";
	private static final String FINALIZAR_TAREFA = "finalizarTarefa";
	private static final String VISUALIZAR_ARTEFATOS = "visualizarArtefatos";
	private static final String EDITAR_ARTEFATO_FINALIZACAO_TAREFA = "editarArtefatoFinalizacaoTarefa";

	private static final String MSG_SUCESSO_APONTAMENTO = "Apontamento realizado com sucesso.";
	private static final String MSG_SUCESSO_FINALIZACAO = "Tarefa finalizada com sucesso.";
	private static final String MSG_HORA_NAO_INICIADA = "Não é possível realizar a operação. Favor apontar o início de sua Hora no Apontamento de Horas.";

	@EJB
	private GerenciarColaborador colaboradorBean;

	@EJB
	private GerenciarStatus statusBean;

	@EJB
	private GerenciarHoraTarefa horaTarefaBean;

	@EJB
	private GerenciarHoraColaborador horaColaboradorBean;

	@EJB
	private GerenciarFinalizacaoTarefa finalizacaoTarefaBean;

	@EJB
	private GerenciarArtefato artefatoBean;

	private Colaborador colaborador;
	private Tarefa tarefa;
	private Artefato artefato;
	private Arquivo arquivo;

	private StreamedContent fileDownload;

	private List<Tarefa> listaTarefa;
	private List<Status> listaStatus;
	private List<HoraTarefa> listaHoraTarefa;
	private List<Arquivo> listaArquivos;
	private List<Artefato> listaArtefatos;

	public List<Tarefa> getListaTarefa() {
		if(listaTarefa == null){
			listaTarefa = new ArrayList<Tarefa>();
		}
		return listaTarefa;
	}

	public void setListaTarefa(List<Tarefa> listaTarefa) {
		this.listaTarefa = listaTarefa;
	}

	public List<Status> getListaStatus() {
		if(listaStatus == null){
			listaStatus = new ArrayList<Status>();
		}
		return listaStatus;
	}

	public void setListaStatus(List<Status> listaStatus) {
		this.listaStatus = listaStatus;
	}

	public Colaborador getColaborador() {
		if(colaborador == null){
			colaborador = new Colaborador();
		}
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
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

	public void setColaboradorLogado(long idColaborador) throws SIGPRException{
		this.reset();
		this.colaborador = colaboradorBean.findById(Long.valueOf(idColaborador));

		consultarTarefas();
	}

	private boolean isHoraColaboradorIniciada(){
		return horaColaboradorBean.isHoraIniciada(colaborador);
	}

	public String iniciarTarefa(){
		if(isHoraColaboradorIniciada()){
			horaTarefaBean.apontar(tarefa, colaborador);

			this.reset();
			consultarTarefas();
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_SUCESSO_APONTAMENTO, ""));
			return TAREFAS_ATRIBUIDAS;
		}
		else{
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, MSG_HORA_NAO_INICIADA, ""));
			return null;
		}
	}

	public String pausarTarefa(){
		if(isHoraColaboradorIniciada()){
			horaTarefaBean.apontar(tarefa, colaborador);

			this.reset();	
			consultarTarefas();
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_SUCESSO_APONTAMENTO, ""));
			return TAREFAS_ATRIBUIDAS;
		}else{
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, MSG_HORA_NAO_INICIADA, ""));
			return null;
		}
	}

	public String finalizarTarefa(){
		if(isHoraColaboradorIniciada()){
			return FINALIZAR_TAREFA;
		}else{
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, MSG_HORA_NAO_INICIADA, ""));
			return null;
		}
	}

	public String confirmarFinalizacaoTarefa(){
		tarefa.setStatus(statusBean.findByNameAndTipoStatus("Finalizada", TipoStatus.Tarefa));
		finalizacaoTarefaBean.finalizarTarefa(tarefa, colaborador);

		this.reset();
		consultarTarefas();
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_SUCESSO_FINALIZACAO, ""));
		return TAREFAS_ATRIBUIDAS;
	}

	public boolean tarefaIniciada(Tarefa tarefa){
		return horaTarefaBean.isTarefaIniciada(tarefa, colaborador);
	}

	private void consultarTarefas() {
		List<Status> listaStatus = null;
		if(isColaboradorGerente()){
			listaStatus = statusBean.listarTarefas();
		}
		else{
			listaStatus = statusBean.listarTarefasPorColaborador(colaborador);
		}

		this.listaStatus = null;

		if(listaStatus != null){
			for(Status status: listaStatus){
				if(!getListaStatus().contains(status)){
					getListaStatus().add(status);
				}
			}
		}
	}

	public String visualizarTarefa(){

		consultarHorasTarefa();
		consultarListaArtefatos();

		return VISUALIZAR_TAREFA;
	}

	public String visualizarArtefatos(){
		return VISUALIZAR_ARTEFATOS;
	}

	private void consultarHorasTarefa() {
		if(isColaboradorGerente()){
			this.listaHoraTarefa = horaTarefaBean.listarHoras(tarefa);
		}
		else{
			this.listaHoraTarefa = horaTarefaBean.listarHoras(tarefa, colaborador);
		}
	}

	public boolean isColaboradorGerente(){
		if(colaborador.getPerfil().getNome().toUpperCase().contains("GERENTE".toUpperCase())){
			return true;
		}
		else{
			return false;
		}
	}

	public String voltarListaTarefas(){
		this.reset();
		consultarTarefas();

		return TAREFAS_ATRIBUIDAS;
	}

	public String voltarVisualizarTarefa(){
		return VISUALIZAR_TAREFA;
	}

	private void reset() {
		this.listaStatus = null;
		this.listaTarefa = null;
		this.listaHoraTarefa = null;
		this.listaArquivos = null;
		this.listaArtefatos = null;
		this.tarefa = null;
		this.artefato = null;
		this.arquivo = null;
		this.fileDownload = null;
	}

	public List<HoraTarefa> getListaHoraTarefa() {
		if(listaHoraTarefa == null){
			listaHoraTarefa = new ArrayList<HoraTarefa>();
		}
		return listaHoraTarefa;
	}

	public void setListaHoraTarefa(List<HoraTarefa> listaHoraTarefa) {
		this.listaHoraTarefa = listaHoraTarefa;
	}

	public List<Artefato> getListaArtefatos() {
		if(listaArtefatos == null){
			listaArtefatos = new ArrayList<Artefato>();
		}
		return listaArtefatos;
	}

	public void setListaArtefatos(List<Artefato> listaArtefatos) {
		this.listaArtefatos = listaArtefatos;
	}

	public StreamedContent getFileDownload() {
		return fileDownload;
	}

	public void setFileDownload(StreamedContent fileDownload){
		this.fileDownload = fileDownload;
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

	public List<Arquivo> getListaArquivos() {
		if(listaArquivos == null){
			listaArquivos = new ArrayList<Arquivo>();
		}
		return listaArquivos;
	}

	public void setListaArquivos(List<Arquivo> listaArquivos) {
		this.listaArquivos = listaArquivos;
	}

	public String incluirArtefato() throws SIGPRException{
		artefato.setAtivo(true);
		artefato.setTarefa(tarefa);
		artefato.setArquivos(getListaArquivos());

		artefatoBean.incluir(artefato);
		this.resetArtefato();
		consultarListaArtefatos();
		return FINALIZAR_TAREFA;
	}

	private void resetArtefato() {
		this.arquivo = null;
		this.artefato = null;
		this.listaArquivos = null;
	}

	public String alterarArtefato() throws SIGPRException{
		artefato.setAtivo(true);
		artefato.setTarefa(tarefa);
		artefato.setArquivos(getListaArquivos());

		artefatoBean.alterar(artefato);
		this.resetArtefato();
		consultarListaArtefatos();
		return FINALIZAR_TAREFA;
	}

	public void excluirArtefato() throws SIGPRException{
		artefatoBean.excluir(artefato);
		consultarListaArtefatos();
	}

	public String incluirArtefatoFinalizacaoTarefa() throws SIGPRException{
		this.resetArtefato();
		return EDITAR_ARTEFATO_FINALIZACAO_TAREFA;
	}

	private void consultarListaArtefatos(){
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

}
