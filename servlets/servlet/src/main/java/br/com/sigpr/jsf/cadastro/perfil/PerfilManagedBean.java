package br.com.sigpr.jsf.cadastro.perfil;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.sigpr.ejb.cadastro.perfil.GerenciarPerfil;
import br.com.sigpr.entity.perfil.Perfil;
import br.com.sigpr.exceptions.SIGPRException;

@ManagedBean(name="perfilMBean")
@SessionScoped
public class PerfilManagedBean implements Serializable{

	private static final long serialVersionUID = -1965106495304019574L;
	
	private static final String LISTA_PERFIL = "listaPerfil";

	@EJB
	private GerenciarPerfil cadastroPerfilBean;

	private Perfil perfil;

	private List<Perfil> listaPerfil;

	@PostConstruct
	public void init() throws SIGPRException{
		consultarPerfis();
	}

	public List<Perfil> getListaPerfil(){
		return listaPerfil;
	}
	
	private void consultarPerfis() throws SIGPRException{
		this.listaPerfil = cadastroPerfilBean.listar();
	}

	public String incluir() throws SIGPRException{
		cadastroPerfilBean.incluir(perfil);

		this.reset();
		consultarPerfis();
		return LISTA_PERFIL;
	}

	public String alterar() throws SIGPRException{
		cadastroPerfilBean.alterar(perfil);

		this.reset();
		consultarPerfis();
		return LISTA_PERFIL;
	}

	public String excluir() throws SIGPRException{
		cadastroPerfilBean.excluir(perfil);

		this.reset();
		consultarPerfis();
		return LISTA_PERFIL;
	}

	public Perfil getPerfil() {
		if(perfil == null){
			perfil = new Perfil();
		}
		return perfil;
	}

	public void setPerfilSelecionado(Perfil perfil) {
		this.perfil = perfil;
	}

	private void reset(){
		this.perfil = null;
	}
	
	public String cancelar() throws SIGPRException{
		this.reset();
		consultarPerfis();
		return LISTA_PERFIL;
	}

}
