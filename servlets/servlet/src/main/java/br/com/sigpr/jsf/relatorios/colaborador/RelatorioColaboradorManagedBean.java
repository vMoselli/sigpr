package br.com.sigpr.jsf.relatorios.colaborador;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import br.com.sigpr.ejb.cadastro.colaborador.GerenciarColaborador;
import br.com.sigpr.ejb.cadastro.papel.GerenciarPapel;
import br.com.sigpr.ejb.cadastro.perfil.GerenciarPerfil;
import br.com.sigpr.ejb.cadastro.projeto.GerenciarProjeto;
import br.com.sigpr.ejb.cadastro.unidade.GerenciarUnidade;
import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.colaboradorprojeto.ColaboradorProjeto;
import br.com.sigpr.entity.papel.Papel;
import br.com.sigpr.entity.perfil.Perfil;
import br.com.sigpr.entity.projeto.Projeto;
import br.com.sigpr.entity.unidade.Unidade;
import br.com.sigpr.exceptions.SIGPRException;
import br.com.sigpr.util.SIGPRUtil;
import br.com.sigpr.util.relatorio.ExtDataSource;
import br.com.sigpr.util.relatorio.RelatorioColaboradores;

@ManagedBean(name = "relatorioColaboradorMBean")
@SessionScoped
public class RelatorioColaboradorManagedBean implements Serializable {
	
	private static final long serialVersionUID = 5053598456962581204L;

	private static final String RELATORIO_COLABORADORES =
		"/WEB-INF/classes/br/com/sigpr/relatorios/colaborador/Colaborador.jasper";

	private static final String MSG_DADOS_INEXISTENTES_FILTRO = "Não foram encontrados Colaboradores cadastrados para os filtros informados.";

	@EJB
	private GerenciarColaborador colaboradorBean;
	
	@EJB
	private GerenciarUnidade unidadeBean;
	
	@EJB
	private GerenciarPapel papelBean;

	@EJB
	private GerenciarPerfil perfilBean;
	
	@EJB
	private GerenciarProjeto projetoBean;
	
	private Colaborador colaborador;
	private Unidade unidade;
	private Papel papel;
	private Perfil perfil;
	private Projeto projeto;
	
	private List<Unidade> listaUnidades;
	private List<Papel> listaPapeis;
	private List<Perfil> listaPerfis;
	private List<Projeto> listaProjetos;
	
	public void setColaboradorLogado(long idColaborador) throws SIGPRException{
		this.reset();
		this.colaborador = colaboradorBean.findById(Long.valueOf(idColaborador));
		
		consultarListaPapeis();
		consultarListaPerfis();
		consultarListaProjetos();
		consultarListaUnidades();
	}
	
	public void consultarListaUnidades() throws SIGPRException{
		this.listaUnidades = unidadeBean.listar();
	}
	
	public void consultarListaPapeis() throws SIGPRException{
		this.listaPapeis = papelBean.listar();
	}
	
	public void consultarListaPerfis() throws SIGPRException{
		this.listaPerfis = perfilBean.listar();
	}
	
	public void consultarListaProjetos() throws SIGPRException{
		this.listaProjetos = projetoBean.listar();
	}
	

	private void reset() {
		colaborador = null;
		unidade = null;
		perfil = null;
		papel = null;
		projeto = null;
		
		listaUnidades = null;
		listaPapeis = null;
		listaPerfis = null;
		listaProjetos = null;
	}

	public String gerarRelatorio() throws JRException, IOException, SIGPRException{
		
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		sql.append("Select colaborador from Colaborador colaborador ");
		
		if((projeto != null && projeto.getId() != null && projeto.getId().longValue() > 0) || 
				(papel != null  && papel.getId() != null && papel.getId().longValue() > 0) || 
				(perfil != null  && perfil.getId() != null && perfil.getId().longValue() > 0) || 
				(unidade != null  && unidade.getId() != null && unidade.getId().longValue() > 0)){
			
			if((projeto != null && projeto.getId() != null && projeto.getId().longValue() > 0) || 
			(papel != null  && papel.getId() != null && papel.getId().longValue() > 0)){
				sql.append("join colaborador.projetos projetos ");
			}
			sql.append(" where ");
			int count = 1;
			if(projeto != null && projeto.getId() != null && projeto.getId().longValue() > 0){
				sql.append(" projetos.projeto = ?"+count);
				count ++;
				params.add(projeto);
			}
			
			if(papel != null  && papel.getId() != null && papel.getId().longValue() > 0){
				if(count > 1){
					sql.append(" and ");
				}
				sql.append(" projetos.papel = ?"+count);
				params.add(papel);
				count ++;
			}
			
			if(perfil != null  && perfil.getId() != null && perfil.getId().longValue() > 0){
				if(count > 1){
					sql.append(" and ");
				}
				sql.append(" colaborador.perfil = ?"+count);
				params.add(perfil);
				count ++;
			}
			
			if(unidade != null  && unidade.getId() != null && unidade.getId().longValue() > 0){
				if(count > 1){
					sql.append(" and ");
				}
				sql.append(" colaborador.unidade = ?"+count);
				params.add(unidade);
				count ++;
			}
		}
		List<Colaborador> colaboradores = null;
		try{
			colaboradores = colaboradorBean.consultar(sql.toString(), params.toArray());
		}
		catch(SIGPRException e){
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMensagem(), ""));
			return null;
		}
		
		List<RelatorioColaboradores> lista = gerarListaRelatorio(colaboradores);
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if(lista.isEmpty()){
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, MSG_DADOS_INEXISTENTES_FILTRO, ""));
		}
		else{
		
			String reportUrlReal = facesContext.getExternalContext().getRealPath(RELATORIO_COLABORADORES);
	
			// imprimir o relatório para um stream em PDF
			final JasperPrint jasperPrint = JasperFillManager.fillReport(reportUrlReal, new HashMap<String, Object>(),
					lista == null ? new JREmptyDataSource() : new ExtDataSource(lista));
			final ByteArrayOutputStream output = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, output);
	
			final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext()
			.getResponse();
	
			final byte[] pdf = output.toByteArray();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename=\"Relatorio Colaboradores.pdf\"");
			response.setContentLength(pdf.length);
			final ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(pdf, 0, pdf.length);
			ouputStream.flush();
			ouputStream.close();
	
			FacesContext.getCurrentInstance().responseComplete();
		}

		return null;
	}


	private List<RelatorioColaboradores> gerarListaRelatorio(
			List<Colaborador> colaboradores) {
		
		List<RelatorioColaboradores> lista = new ArrayList<RelatorioColaboradores>();
		
		for(Colaborador colaborador: colaboradores){
			
			for(ColaboradorProjeto colaboradorProjeto : colaborador.getProjetos()){
				
				RelatorioColaboradores colab = new RelatorioColaboradores();
				
				colab.setBairro(colaborador.getUnidade().getEndereco().getBairro());
				colab.setCep(colaborador.getUnidade().getEndereco().getCep());
				colab.setCidade(colaborador.getUnidade().getEndereco().getCidade());
				colab.setEstado(colaborador.getUnidade().getEndereco().getEstado().getNome());
				colab.setComplemento(colaborador.getUnidade().getEndereco().getComplemento());
				colab.setLogradouro(colaborador.getUnidade().getEndereco().getLogradouro());
				colab.setNumero(colaborador.getUnidade().getEndereco().getNumero());
				colab.setTelefone(colaborador.getUnidade().getTelefone());
				colab.setRamal(colaborador.getRamal());
				colab.setNocolaborador(colaborador.getNome());
				colab.setNounidade(colaborador.getUnidade().getNome());
				colab.setNuunidade(colaborador.getUnidade().getId());
				colab.setNucolaborador(colaborador.getId());
				colab.setPapel(colaboradorProjeto.getPapel().getNome());
				colab.setPerfil(colaborador.getPerfil().getNome());
				colab.setPrazo(SIGPRUtil.formatarData(colaboradorProjeto.getProjeto().getPrazo(), "dd/MM/yyyy"));
				colab.setProjeto(colaboradorProjeto.getProjeto().getNome());
				
				lista.add(colab);
			}
			
		}
		
		Collections.sort(lista);
		
		return lista;
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


	public List<Unidade> getListaUnidades() {
		return listaUnidades;
	}


	public void setListaUnidades(List<Unidade> listaUnidades) {
		this.listaUnidades = listaUnidades;
	}


	public List<Papel> getListaPapeis() {
		return listaPapeis;
	}


	public void setListaPapeis(List<Papel> listaPapeis) {
		this.listaPapeis = listaPapeis;
	}


	public List<Perfil> getListaPerfis() {
		return listaPerfis;
	}


	public void setListaPerfis(List<Perfil> listaPerfis) {
		this.listaPerfis = listaPerfis;
	}


	public List<Projeto> getListaProjetos() {
		return listaProjetos;
	}


	public void setListaProjetos(List<Projeto> listaProjetos) {
		this.listaProjetos = listaProjetos;
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

	public Papel getPapel() {
		if(papel == null){
			papel = new Papel();
		}
		return papel;
	}

	public void setPapel(Papel papel) {
		this.papel = papel;
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

	public Projeto getProjeto() {
		if(projeto == null){
			projeto = new Projeto();
		}
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

}
