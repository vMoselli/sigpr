package br.com.sigpr.jsf.cadastro.unidade;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.sigpr.ejb.cadastro.unidade.GerenciarUnidade;
import br.com.sigpr.entity.endereco.Endereco;
import br.com.sigpr.entity.unidade.Unidade;
import br.com.sigpr.enumerations.Estado;
import br.com.sigpr.exceptions.SIGPRException;

@ManagedBean(name="unidadeMBean")
@SessionScoped
public class UnidadeManagedBean implements Serializable{
	
	private static final long serialVersionUID = -5958829514586157779L;

	private static final String LISTA_UNIDADE = "listaUnidade";

	@EJB
	private GerenciarUnidade unidadeBean;
	
	private Unidade unidade;
	private Endereco endereco;
	
	private List<Unidade> listaUnidade;
	
	@PostConstruct
	public void init() throws SIGPRException{
		consultarUnidades();
	}
	
	public List<Unidade> getListaUnidade(){
		return listaUnidade;
	}
	
	private void consultarUnidades() throws SIGPRException{
		this.listaUnidade = unidadeBean.listar();
	}
	
	public String incluir() throws SIGPRException{
		unidade.setAtivo(true);
		unidade.setEndereco(endereco);
		unidadeBean.incluir(unidade);
		
		this.reset();
		consultarUnidades();
		return LISTA_UNIDADE;
	}
	
	public String alterar() throws SIGPRException{
		unidade.setAtivo(true);
		unidade.setEndereco(endereco);
		unidadeBean.alterar(unidade);
		
		this.reset();
		consultarUnidades();
		return LISTA_UNIDADE;
	}
	
	public String excluir() throws SIGPRException{
		unidade.setEndereco(endereco);
		unidadeBean.excluir(unidade);
		
		this.reset();
		consultarUnidades();
		return LISTA_UNIDADE;
	}
	
	public Estado[] getListaEstados(){
		return Estado.values();
	}
	
	public void setUnidadeSelecionada(Unidade unidade){
		this.unidade = unidade;
		this.endereco = unidade.getEndereco();
	}
	
	private void reset(){
		this.unidade = null;
		this.endereco = null;
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

	public Endereco getEndereco() {
		if(endereco == null){
			endereco = new Endereco();
		}
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
	public String cancelar() throws SIGPRException{
		this.reset();
		consultarUnidades();
		return LISTA_UNIDADE;
	}
}
