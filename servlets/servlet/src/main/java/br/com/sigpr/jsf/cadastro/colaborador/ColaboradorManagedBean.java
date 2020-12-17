package br.com.sigpr.jsf.cadastro.colaborador;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.sigpr.ejb.cadastro.colaborador.GerenciarColaborador;
import br.com.sigpr.ejb.cadastro.colaboradorprojeto.GerenciarColaboradorProjeto;
import br.com.sigpr.ejb.cadastro.papel.GerenciarPapel;
import br.com.sigpr.ejb.cadastro.projeto.GerenciarProjeto;
import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.colaboradorprojeto.ColaboradorProjeto;
import br.com.sigpr.entity.papel.Papel;
import br.com.sigpr.entity.perfil.Perfil;
import br.com.sigpr.entity.projeto.Projeto;
import br.com.sigpr.entity.unidade.Unidade;
import br.com.sigpr.entity.usuario.Usuario;
import br.com.sigpr.exceptions.LoginExistenteException;
import br.com.sigpr.exceptions.SIGPRException;

@ManagedBean( name = "colaboradorMBean")
@SessionScoped
public class ColaboradorManagedBean implements Serializable {

	private static final long serialVersionUID = -5468470516952359916L;
	
	private static final String SELECAO_COLABORADOR = "selecaoColaborador";
	private static final String LISTA_COLABORADOR = "listaColaborador";
	private static final String EDITAR_DADOS_COLABORADOR = "editarDados";
	private static final String HOME = "index";
	private static final String ASSOCIAR_PROJETO = "associarProjeto";

	private static final String MSG_DADOS_ALTERADOS_COM_SUCESSO = "Dados alterados com sucesso.";
	private static final String MSG_UNIDADE_ASSOCIADA = "Unidade associada com sucesso.";
	private static final String MSG_PROJETO_ASSOCIADO = "Projeto associado com sucesso.";
	private static final String MSG_PROJETO_DESASSOCIADO = "Projeto desassociado com sucesso.";

	@EJB
	private GerenciarColaborador colaboradorBean;
	
	@EJB
	private GerenciarColaboradorProjeto colaboradorProjetoBean;
	
	@EJB
	private GerenciarProjeto projetoBean;
	
	@EJB
	private GerenciarPapel papelBean;

	private Usuario usuario;
	private Colaborador colaborador;
	private Unidade unidade;
	private Perfil perfil;
	
	private String confirmacao;
	
	private boolean passwordChanged = false;
	
	private ColaboradorProjeto projetoAssociado;
	private Projeto projeto;
	private Papel papel;
	
	private List<ColaboradorProjeto> listaProjetosAssociados;
	private List<Projeto> listaProjetosNaoAssociados;
	
	private List<Colaborador> listaColaborador;
	
	private StreamedContent image;
	
	@PostConstruct
	public void init() throws SIGPRException{
		consultarColaboradores();
	}
	
	public List<Colaborador> getListaColaborador(){
		return listaColaborador;
	}
	
	private void consultarColaboradores() throws SIGPRException{
		this.listaColaborador = colaboradorBean.listar();
	}

	public String incluir() throws SIGPRException{
		colaborador.setAtivo(true);
		colaborador.setUsuario(usuario);
		colaborador.setPerfil(perfil);
		colaborador.setUnidade(unidade);
		
		try{
			colaboradorBean.incluir(colaborador);
		}
		catch (LoginExistenteException e) {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMensagem(), e.getMensagem()));
			return null;
		}

		this.reset();
		consultarColaboradores();
		return LISTA_COLABORADOR;
	}

	public String alterar() throws SIGPRException{
		colaborador.setAtivo(true);
		colaborador.setUsuario(usuario);
		colaborador.setPerfil(perfil);
		colaborador.setUnidade(unidade);
		
		try{
			colaboradorBean.alterar(colaborador);
		}
		catch (LoginExistenteException e) {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMensagem(), e.getMensagem()));
			return null;
		}

		this.reset();
		consultarColaboradores();
		return LISTA_COLABORADOR;
	}

	public String excluir() throws SIGPRException{
		colaborador.setUsuario(usuario);
		colaborador.setPerfil(perfil);
		colaborador.setUnidade(unidade);
		colaboradorBean.excluir(colaborador);

		this.reset();
		consultarColaboradores();
		return LISTA_COLABORADOR;
	}
	
	public String associarUnidade() throws SIGPRException{
		
		colaborador.setUnidade(unidade);
		colaboradorBean.alterar(colaborador);
		
		this.reset();
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_UNIDADE_ASSOCIADA, ""));
		consultarColaboradores();
		return SELECAO_COLABORADOR;
	}
	
	public List<ColaboradorProjeto> getListaProjetosAssociados(){
		if(listaProjetosAssociados == null){
			listaProjetosAssociados = colaboradorProjetoBean.listarPorColaborador(colaborador);
		}
		return listaProjetosAssociados;
	}
	
	public List<Projeto> getListaProjetosNaoAssociados(){
		if(listaProjetosNaoAssociados == null){
			listaProjetosNaoAssociados = projetoBean.listarProjetosNaoAssociados(colaborador);
		}
		return listaProjetosNaoAssociados;
	}
	
	public List<Papel> getListaPapel() throws SIGPRException{
		return papelBean.listar();
	}
	
	public void associar() throws SIGPRException{
		ColaboradorProjeto colabProj = new ColaboradorProjeto();
		colabProj.setAtivo(true);
		colabProj.setColaborador(colaborador);
		colabProj.setPapel(papel);
		colabProj.setProjeto(projeto);
		
		colaboradorProjetoBean.incluir(colabProj);
		
		this.limparCamposAssociacao();
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_PROJETO_ASSOCIADO, ""));
		consultarColaboradores();
	}
	
	public void atualizarAssociacao() throws SIGPRException{
		
		projetoAssociado.setAtivo(true);
		colaboradorProjetoBean.alterar(projetoAssociado);
		
		this.limparCamposAssociacao();
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_PROJETO_ASSOCIADO, ""));
		consultarColaboradores();
	}
	
	public void excluirAssociacao() throws SIGPRException{
		
		projetoAssociado.setAtivo(false);
		colaboradorProjetoBean.excluir(projetoAssociado);
		
		this.limparCamposAssociacao();
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_PROJETO_DESASSOCIADO, ""));
		consultarColaboradores();
	}
	
	public void validateSamePassword(FacesContext context, UIComponent component, Object value) {
		String confirmPassword = (String)value;
		if (isPasswordChanged() && !confirmPassword.equals(usuario.getSenha())){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Senha e a Confirmação não coincidem!", "A Senha e a Confirmação não coincidem!");
			throw new ValidatorException(message);
		}
	}
	
	public void changePassword(ValueChangeEvent event){  
        passwordChanged = true; 
    }  

	public void setColaboradorSelecionado(Colaborador colaborador){
		this.colaborador = colaborador;
		this.usuario = colaborador.getUsuario();
		this.perfil = colaborador.getPerfil();
		this.unidade = colaborador.getUnidade();
		if(colaborador.getImagem() != null){
			this.image = new DefaultStreamedContent(new ByteArrayInputStream(colaborador.getImagem()), "image/jpg");
		}
	}

	private void reset(){
		this.usuario = null;
		this.unidade = null;
		this.perfil = null;
		this.colaborador = null;
		this.confirmacao = "";
		this.passwordChanged = false;
		this.listaProjetosAssociados = null;
		this.listaProjetosNaoAssociados = null;
		this.papel = null;
		this.projeto = null;
	}
	
	public void limparCamposAssociacao(){
		this.listaProjetosAssociados = null;
		this.listaProjetosNaoAssociados = null;
		this.papel = null;
		this.projeto = null;
		this.projetoAssociado = null;
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

	public Unidade getUnidade() {
		if(unidade == null){
			unidade = new Unidade();
		}
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	public Perfil getPerfil() {
		if(perfil == null){
			perfil = new Perfil();
		}
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Usuario getUsuario() {
		if(usuario == null){
			usuario = new Usuario();
		}
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getConfirmacao() {
		return confirmacao;
	}

	public void setConfirmacao(String confirmacao) {
		this.confirmacao = confirmacao;
	}

	public boolean isPasswordChanged() {
		return passwordChanged;
	}

	public void setPasswordChanged(boolean passwordChanged) {
		this.passwordChanged = passwordChanged;
	}

	public ColaboradorProjeto getProjetoAssociado() {
		if(projetoAssociado == null){
			projetoAssociado = new ColaboradorProjeto();
		}
		return projetoAssociado;
	}

	public void setProjetoAssociado(ColaboradorProjeto projetoAssociado) {
		this.projetoAssociado = projetoAssociado;
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

	public Papel getPapel() {
		if(papel == null){
			papel = new Papel();
		}
		return papel;
	}

	public void setPapel(Papel papel) {
		this.papel = papel;
	}
	
	public String cancelar() throws SIGPRException{
		this.reset();
		consultarColaboradores();
		return LISTA_COLABORADOR;
	}
	
	public void handleFileUpload(FileUploadEvent event){
		
        image = new DefaultStreamedContent(new ByteArrayInputStream(event.getFile().getContents()), event.getFile().getContentType());   
		getColaborador().setImagem(event.getFile().getContents());
		
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Arquivo enviado com sucesso.", null));
	}

	public StreamedContent getImage() {
		return image;
	}

	public void setImage(StreamedContent image) {
		this.image = image;
	}
	
	public void setColaboradorLogado(long idColaborador) throws SIGPRException{
		this.colaborador = colaboradorBean.findById(Long.valueOf(idColaborador));
		this.usuario = colaborador.getUsuario();
		this.perfil = colaborador.getPerfil();
		this.unidade = colaborador.getUnidade();
		if(colaborador.getImagem() != null){
			this.image = new DefaultStreamedContent(new ByteArrayInputStream(colaborador.getImagem()), "image/jpg");
		}
	}

	public String alterarEdicaoDados() throws SIGPRException{
		colaborador.setAtivo(true);
		colaborador.setUsuario(usuario);
		colaborador.setPerfil(perfil);
		colaborador.setUnidade(unidade);
		
		try{
			colaboradorBean.alterar(colaborador);
		}
		catch (LoginExistenteException e) {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMensagem(), e.getMensagem()));
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_DADOS_ALTERADOS_COM_SUCESSO, ""));
		return EDITAR_DADOS_COLABORADOR;
	}
	
	public String cancelarEdicaoDados() throws SIGPRException{
		this.reset();
		return HOME;
	}
	
	public String cancelarAssociacao() throws SIGPRException{
		this.limparCamposAssociacao();
		return ASSOCIAR_PROJETO;
	}
	
	public String cancelarAssociacaoUnidade(){
		this.unidade = null;
		return SELECAO_COLABORADOR;
	}
	
}
