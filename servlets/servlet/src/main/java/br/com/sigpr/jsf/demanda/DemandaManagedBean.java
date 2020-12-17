package br.com.sigpr.jsf.demanda;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;

import br.com.sigpr.ejb.cadastro.colaborador.GerenciarColaborador;
import br.com.sigpr.ejb.cadastro.funcionalidade.GerenciarFuncionalidade;
import br.com.sigpr.ejb.cadastro.projeto.GerenciarProjeto;
import br.com.sigpr.ejb.cadastro.status.GerenciarStatus;
import br.com.sigpr.ejb.cadastro.tarefa.GerenciarTarefa;
import br.com.sigpr.ejb.demanda.GerenciarDemanda;
import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.demanda.Solicitacao;
import br.com.sigpr.entity.status.Status;
import br.com.sigpr.enumerations.Prioridade;
import br.com.sigpr.enumerations.StatusSolicitacao;
import br.com.sigpr.exceptions.SIGPRException;
import br.com.sigpr.util.demanda.Demanda;
import br.com.sigpr.util.demanda.DemandaReader;

@ManagedBean(name = "demandaMBean")
@SessionScoped
public class DemandaManagedBean implements Serializable {

	private static final long serialVersionUID = -4049266164778427007L;
	
	private static final String LISTA_SOLICITACAO = "listaSolicitacao";
	private static final String NOVA_SOLICITACAO = "novaSolicitacao";
	private static final String IMPORTAR_DEMANDA = "importarDemanda";
	private static final String MSG_SUCESSO_ATENDIMENTO = "Atendimento da Solicitação efetuado com sucesso.";
	private static final String MSG_SUCESSO_SOLICITACAO = "Solicitação de Demanda criada com sucesso.";
	private static final String MSG_SUCESSO_IMPORTACAO = "Importação de Demanda efetuada com sucesso.";
	private static final String MSG_SUCESSO_CANCELAMENTO = "Cancelamento da Solicitação efetuado com sucesso.";

	@EJB
	private GerenciarColaborador colaboradorBean;
	
	@EJB
	private GerenciarDemanda demandaBean;
	
	@EJB
	private GerenciarStatus statusBean;
	
	private Solicitacao solicitacao;
	private Colaborador colaborador;
	
	private List<Solicitacao> listaSolicitacao;
	
	private ByteArrayInputStream arquivoImportado;
	
	private void consultarListaSolicitacao() throws SIGPRException{
		this.listaSolicitacao = demandaBean.listar();
	}
	
	public String incluirSolicitacao() throws SIGPRException{
		
		this.solicitacao.setStatus(StatusSolicitacao.Aguardando);
		this.solicitacao.setDataSolicitacao(new Date());
		demandaBean.incluir(solicitacao);
		
		this.reset();
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_SUCESSO_SOLICITACAO, ""));
		return NOVA_SOLICITACAO;
	}
	
	public String atenderSolicitacao() throws SIGPRException{
		
		this.solicitacao.setStatus(StatusSolicitacao.Atendida);
		this.solicitacao.setColaborador(colaborador);
		this.solicitacao.setDataAtendimento(new Date());
		
		demandaBean.alterar(solicitacao);
		
		this.reset();
		consultarListaSolicitacao();
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_SUCESSO_ATENDIMENTO, ""));
		return LISTA_SOLICITACAO;
	}
	
	public String cancelarSolicitacao() throws SIGPRException{
		
		this.solicitacao.setStatus(StatusSolicitacao.Cancelada);
		this.solicitacao.setColaborador(colaborador);
		this.solicitacao.setDataAtendimento(new Date());
		
		demandaBean.excluir(solicitacao);
		
		this.reset();
		consultarListaSolicitacao();
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_SUCESSO_CANCELAMENTO, ""));
		
		return LISTA_SOLICITACAO;
	}
	
	public String voltarLista() throws SIGPRException{
		this.reset();
		this.consultarListaSolicitacao();
		return LISTA_SOLICITACAO;
	}

	public void setColaboradorLogado(long idColaborador) throws SIGPRException{
		this.reset();
		this.colaborador = colaboradorBean.findById(Long.valueOf(idColaborador));
	}

	public void setColaboradorLogadoLista(long idColaborador) throws SIGPRException{
		this.reset();
		this.colaborador = colaboradorBean.findById(Long.valueOf(idColaborador));
		
		consultarListaSolicitacao();
	}
	
	private void reset() {
		this.listaSolicitacao = null;
		this.solicitacao = null;
	}
	
	public StatusSolicitacao[] getListaStatusSolicitacao(){
		return StatusSolicitacao.values();
	}
	
	public Prioridade[] getListaPrioridade(){
		return Prioridade.values();
	}

	public Solicitacao getSolicitacao() {
		if(solicitacao == null){
			solicitacao = new Solicitacao();
		}
		return solicitacao;
	}

	public void setSolicitacao(Solicitacao solicitacao) {
		this.solicitacao = solicitacao;
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

	public List<Solicitacao> getListaSolicitacao() {
		if(listaSolicitacao == null){
			listaSolicitacao = new ArrayList<Solicitacao>();
		}
		return listaSolicitacao;
	}

	public void setListaSolicitacao(List<Solicitacao> listaSolicitacao) {
		this.listaSolicitacao = listaSolicitacao;
	}
	
	//IMPORTAÇÃO DE DEMANDA
	public void handleFileUpload(FileUploadEvent event){
		
        arquivoImportado = new ByteArrayInputStream(event.getFile().getContents());   
        
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Arquivo enviado com sucesso. Para finalizar a operação, clique em Importar Demanda.", null));
	}
	
	public String importarDemanda(){
		
		if(arquivoImportado == null){
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Um arquivo deve ser selecionado para importação.", ""));
			return null;
		}
		
		try{
			List<Status> listaStatus = statusBean.listar();
			
			Demanda demanda = DemandaReader.importarDemanda(arquivoImportado, listaStatus);
			
			//Gravação ou Atualização dos arquivos.
			demandaBean.atualizarDemanda(demanda);
		}
		catch(SIGPRException e){
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMensagem(), ""));
			this.arquivoImportado = null;
			return null;
		}
		
		this.arquivoImportado = null;
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_SUCESSO_IMPORTACAO, ""));
		return IMPORTAR_DEMANDA;
		
	}

}
