package br.com.sigpr.jsf.relatorios.projeto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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
import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.colaboradorprojeto.ColaboradorProjeto;
import br.com.sigpr.entity.papel.Papel;
import br.com.sigpr.entity.perfil.Perfil;
import br.com.sigpr.entity.projeto.Projeto;
import br.com.sigpr.exceptions.SIGPRException;
import br.com.sigpr.util.SIGPRUtil;
import br.com.sigpr.util.relatorio.ExtDataSource;
import br.com.sigpr.util.relatorio.RelatorioProjetos;

@ManagedBean(name = "relatorioProjetoMBean")
@SessionScoped
public class RelatorioProjetoManagedBean implements Serializable {
	
	private static final long serialVersionUID = 5053598456962581204L;

	private static final String RELATORIO_PROJETO =
		"/WEB-INF/classes/br/com/sigpr/relatorios/projeto/ProjetoColaborador.jasper";

	private static final String MSG_DADOS_INEXISTENTES_FILTRO = "Não foram encontrados Projeto cadastrados para os filtros informados.";

	@EJB
	private GerenciarColaborador colaboradorBean;
	
	@EJB
	private GerenciarProjeto projetoBean;
	
	@EJB
	private GerenciarPapel papelBean;

	@EJB
	private GerenciarPerfil perfilBean;
	
	private Colaborador colaborador;
	private Papel papel;
	private Perfil perfil;
	private Projeto projeto;
	private Date periodoInicio;
	private Date periodoFim;
	
	private List<Papel> listaPapeis;
	private List<Perfil> listaPerfis;
	private List<Projeto> listaProjetos;
	private List<Colaborador> listaColaboradores;
	
	public void setColaboradorLogado(long idColaborador) throws SIGPRException{
		this.reset();
		//this.colaboradorLogado = colaboradorBean.findById(Long.valueOf(idColaborador));
		
		consultarListaPapeis();
		consultarListaPerfis();
		consultarListaProjetos();
		consultarListaColaboradores();
	}
	
	public void consultarListaColaboradores() throws SIGPRException{
		this.listaColaboradores = colaboradorBean.consultar("Select colaborador from Colaborador colaborador" +
				" where colaborador.perfil.nome <> ?1", 
				"Administrador");
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
		perfil = null;
		papel = null;
		projeto = null;
		periodoInicio = null;
		periodoFim = null;
		
		listaColaboradores = null;
		listaPapeis = null;
		listaPerfis = null;
		listaProjetos = null;
	}

	public String gerarRelatorio() throws JRException, IOException, SIGPRException{
		
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		sql.append("Select projeto from Projeto projeto ");
		
		if((projeto != null && projeto.getId() != null && projeto.getId().longValue() > 0) || 
				(papel != null  && papel.getId() != null && papel.getId().longValue() > 0) || 
				(perfil != null  && perfil.getId() != null && perfil.getId().longValue() > 0) || 
				(colaborador != null  && colaborador.getId() != null && colaborador.getId().longValue() > 0) ||
				(periodoInicio != null) || (periodoFim != null)){
			
			if((colaborador != null && colaborador.getId() != null && colaborador.getId().longValue() > 0) || 
					(papel != null  && papel.getId() != null && papel.getId().longValue() > 0) || 
					(perfil != null  && perfil.getId() != null && perfil.getId().longValue() > 0)){
				sql.append("join projeto.colaboradores colaboradores ");
			}
			sql.append(" where ");
			int count = 1;
			if(projeto != null && projeto.getId() != null && projeto.getId().longValue() > 0){
				sql.append(" projeto = ?"+count);
				count ++;
				params.add(projeto);
			}
			
			if(papel != null  && papel.getId() != null && papel.getId().longValue() > 0){
				if(count > 1){
					sql.append(" and ");
				}
				sql.append(" colaboradores.papel = ?"+count);
				params.add(papel);
				count ++;
			}
			
			if(perfil != null  && perfil.getId() != null && perfil.getId().longValue() > 0){
				if(count > 1){
					sql.append(" and ");
				}
				sql.append(" colaboradores.colaborador.perfil = ?"+count);
				params.add(perfil);
				count ++;
			}
			
			if(colaborador != null  && colaborador.getId() != null && colaborador.getId().longValue() > 0){
				if(count > 1){
					sql.append(" and ");
				}
				sql.append(" colaboradores.colaborador = ?"+count);
				params.add(colaborador);
				count ++;
			}
			if(periodoInicio != null || periodoFim != null){
				if(count > 1){
					sql.append(" and ");
				}
				if(periodoInicio != null && periodoFim == null){
					sql.append(" projeto.prazo >= ?"+count);
					params.add(periodoInicio);
					count ++;
				}
				if(periodoInicio == null && periodoFim != null){
					sql.append(" projeto.prazo <= ?"+count);
					params.add(periodoFim);
					count ++;
				}
				if(periodoInicio != null && periodoFim != null){
					sql.append(" projeto.prazo between ?"+count+" and ?"+(count+1));
					params.add(periodoInicio);
					params.add(periodoFim);
					count ++; count++;
				}
			}
		}
		List<Projeto> projetos = null;
		try{
			projetos = projetoBean.consultar(sql.toString(), params.toArray());
		}
		catch(SIGPRException e){
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMensagem(), ""));
			return null;
		}
		
		List<RelatorioProjetos> lista = gerarListaRelatorio(projetos);
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if(lista.isEmpty()){
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, MSG_DADOS_INEXISTENTES_FILTRO, ""));
		}
		else{
		
			String reportUrlReal = facesContext.getExternalContext().getRealPath(RELATORIO_PROJETO);
	
			// imprimir o relatório para um stream em PDF
			final JasperPrint jasperPrint = JasperFillManager.fillReport(reportUrlReal, new HashMap<String, Object>(),
					lista == null ? new JREmptyDataSource() : new ExtDataSource(lista));
			final ByteArrayOutputStream output = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, output);
	
			final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext()
			.getResponse();
	
			final byte[] pdf = output.toByteArray();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename=\"Relatorio Projetos.pdf\"");
			response.setContentLength(pdf.length);
			final ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(pdf, 0, pdf.length);
			ouputStream.flush();
			ouputStream.close();
	
			FacesContext.getCurrentInstance().responseComplete();
		}

		return null;
	}


	private List<RelatorioProjetos> gerarListaRelatorio(
			List<Projeto> projetos) {
		
		List<RelatorioProjetos> lista = new ArrayList<RelatorioProjetos>();
		
		for(Projeto projeto: projetos){
			
			for(ColaboradorProjeto colaboradorProjeto : projeto.getColaboradores()){
				
				RelatorioProjetos proj = new RelatorioProjetos();
				
				proj.setNocolaborador(colaboradorProjeto.getColaborador().getNome());
				proj.setNucolaborador(colaboradorProjeto.getColaborador().getId());
				proj.setPapel(colaboradorProjeto.getPapel().getNome());
				proj.setPerfil(colaboradorProjeto.getColaborador().getPerfil().getNome());
				proj.setPrazo(SIGPRUtil.formatarData(projeto.getPrazo(), "dd/MM/yyyy"));
				proj.setNoprojeto(projeto.getNome());
				proj.setNuprojeto(projeto.getId());
				
				if(projeto.getPrazo().before(Calendar.getInstance().getTime())){
					proj.setIsvencido(Boolean.TRUE);
				}
				else{
					proj.setIsvencido(Boolean.FALSE);
				}
				
				lista.add(proj);
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

	public Date getPeriodoInicio() {
		return periodoInicio;
	}

	public void setPeriodoInicio(Date periodoInicio) {
		this.periodoInicio = periodoInicio;
	}

	public Date getPeriodoFim() {
		return periodoFim;
	}

	public void setPeriodoFim(Date periodoFim) {
		this.periodoFim = periodoFim;
	}

	public List<Colaborador> getListaColaboradores() {
		return listaColaboradores;
	}

	public void setListaColaboradores(List<Colaborador> listaColaboradores) {
		this.listaColaboradores = listaColaboradores;
	}

}
