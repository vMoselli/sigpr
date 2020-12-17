package br.com.sigpr.jsf.cadastro.papel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.sigpr.ejb.cadastro.papel.GerenciarPapel;
import br.com.sigpr.entity.papel.Papel;
import br.com.sigpr.exceptions.SIGPRException;

@ManagedBean(name = "papelMBean")
@SessionScoped
public class PapelManagedBean implements Serializable {

	private static final long serialVersionUID = 807198136482550297L;
	
	private static final String LISTA_PAPEL = "listaPapel";

	@EJB
	private GerenciarPapel papelBean;

	private Papel papel;
	private List<Papel> listaPapel;

	@PostConstruct
	public void init() throws SIGPRException {
		consultarPapeis();
	}

	private void consultarPapeis() throws SIGPRException {
		this.listaPapel = papelBean.listar();
	}

	public String incluir() throws SIGPRException {
		papel.setAtivo(true);
		papelBean.incluir(papel);

		this.reset();
		consultarPapeis();
		return LISTA_PAPEL;
	}

	public String alterar() throws SIGPRException {
		papel.setAtivo(true);
		papelBean.alterar(papel);

		this.reset();
		consultarPapeis();
		return LISTA_PAPEL;
	}

	public void excluir() throws SIGPRException {

		papelBean.excluir(papel);

		this.reset();
		consultarPapeis();

	}

	private void reset() {
		this.papel = null;
		this.listaPapel = null;
	}

	public Papel getPapel() {
		if (papel == null) {
			papel = new Papel();
		}
		return papel;
	}

	public void setPapel(Papel papel) {
		this.papel = papel;
	}

	public List<Papel> getListaPapel() {
		if (listaPapel == null) {
			listaPapel = new ArrayList<Papel>();
		}
		return listaPapel;
	}

	public void setListaPapel(List<Papel> listaPapel) {
		this.listaPapel = listaPapel;
	}

	public String cancelar() throws SIGPRException {
		this.reset();
		consultarPapeis();
		return LISTA_PAPEL;
	}
}
