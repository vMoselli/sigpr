package br.com.sigpr.jsf.login;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import br.com.sigpr.ejb.demanda.GerenciarDemanda;
import br.com.sigpr.ejb.horas.GerenciarHoraColaborador;
import br.com.sigpr.ejb.login.ControleLogin;
import br.com.sigpr.entity.demanda.Solicitacao;
import br.com.sigpr.entity.perfil.Perfil;
import br.com.sigpr.entity.usuario.Usuario;
import br.com.sigpr.enumerations.StatusSolicitacao;
import br.com.sigpr.exceptions.LoginException;
import br.com.sigpr.exceptions.UsuarioInexistenteException;

@ManagedBean(name = "loginUsuarioMBean")
@SessionScoped
public class LoginUsuarioManagedBean implements Serializable{

	private static final String INDEX = "index";

	private static final long serialVersionUID = -5633559532094518935L;

	private static final String MSG_HORA_INICIADA = "Não é possível realizar Logoff no sistema pois o Apontamento de Horas está iniciado.";

	@EJB
	private ControleLogin controleLogin;
	
	@EJB
	private GerenciarHoraColaborador horaColaboradorBean;
	
	@EJB
	private GerenciarDemanda demandaBean;
	
	private String username;
	private String password;
	
	private Usuario usuario;
	private Perfil perfil;
	
	private boolean alertVisualizado = false;
	
	private List<Solicitacao> solicitacoesNaoAtendidas;
	
	private boolean isHoraColaboradorIniciada(){
		return horaColaboradorBean.isHoraIniciada(usuario.getColaborador());
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public Perfil getPerfil() {
		return perfil;
	}
	
	public boolean isUsuarioLogado(){
		if(usuario == null){
			return false;
		}
		else{
			return true;
		}
	}
	
	public Date getData(){
		return Calendar.getInstance().getTime();
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String login(){
		
		if(controleLogin == null){
			return INDEX;
		}
		
		try {
			usuario = controleLogin.login(getUsername(), getPassword());
			
			if(isUsuarioLogado()){
				
				perfil = controleLogin.getPerfil(usuario);
				((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).setAttribute("idColaborador", usuario.getColaborador().getId().longValue());
				
				if(isUsuarioGerenteOperacional()){
					solicitacoesNaoAtendidas = demandaBean.listarSolicitacoesPorStatus(StatusSolicitacao.Aguardando);
				}
				
				return INDEX;
			}
			
		} catch (UsuarioInexistenteException e) {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMensagem(), e.getMensagem()));
		} catch (LoginException e) {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMensagem(), e.getMensagem()));
		}
		return null;
	}
	
	public String logout(){
		
		HttpSession session = ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false));
		
		if(session != null && !session.isNew()){ 
		
			if(!isUsuarioColaborador() && isHoraColaboradorIniciada()){
				FacesContext.getCurrentInstance().addMessage(null, 
						new FacesMessage(FacesMessage.SEVERITY_ERROR, MSG_HORA_INICIADA, ""));
				return null;
			}
			this.usuario = null;
			this.perfil = null;
			this.username = null;
			this.password = null;
			this.solicitacoesNaoAtendidas = null;
		
			session.invalidate();
		}
		
		return "login";
		
	}
	
	public long getIdColaborador(){
		return usuario.getColaborador().getId().longValue();
	}
	
	public boolean isUsuarioGerenteOperacional(){
		if(perfil != null && perfil.getNome().equalsIgnoreCase("Gerente Operacional")){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isUsuarioGerenteDeProjeto(){
		if(perfil != null && perfil.getNome().equalsIgnoreCase("Gerente de Projeto")){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isUsuarioColaborador(){
		if(perfil != null && perfil.getNome().equalsIgnoreCase("Colaborador")){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isUsuarioAdministrador(){
		if(perfil != null && perfil.getNome().equalsIgnoreCase("Administrador")){
			return true;
		}
		else{
			return false;
		}
	}

	public List<Solicitacao> getSolicitacoesNaoAtendidas() {
		if(solicitacoesNaoAtendidas == null){
			solicitacoesNaoAtendidas = new ArrayList<Solicitacao>();
		}
		return solicitacoesNaoAtendidas;
	}

	public boolean isAlertVisualizado() {
		return alertVisualizado;
	}

	public void setAlertVisualizado(boolean alertVisualizado) {
		this.alertVisualizado = alertVisualizado;
	}

}
