package br.com.sigpr.jsf.relatorios.projeto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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
import br.com.sigpr.ejb.cadastro.projeto.GerenciarProjeto;
import br.com.sigpr.ejb.cadastro.status.GerenciarStatus;
import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.projeto.Projeto;
import br.com.sigpr.entity.status.Status;
import br.com.sigpr.enumerations.TipoStatus;
import br.com.sigpr.exceptions.SIGPRException;
import br.com.sigpr.util.SIGPRUtil;
import br.com.sigpr.util.relatorio.ExtDataSource;
import br.com.sigpr.util.relatorio.RelatorioProjetosStatus;

@ManagedBean(name = "relatorioProjetoStatusMBean")
@SessionScoped
public class RelatorioProjetoStatusManagedBean implements Serializable {
	
	private static final long serialVersionUID = 5053598456962581204L;

	private static final String RELATORIO_PROJETO_STATUS =
		"/WEB-INF/classes/br/com/sigpr/relatorios/projeto/ProjetoStatus.jasper";

	private static final String MSG_DADOS_INEXISTENTES_FILTRO = "Não foram encontrados Projetos cadastrados para os filtros informados.";

	@EJB
	private GerenciarColaborador colaboradorBean;
	
	@EJB
	private GerenciarProjeto projetoBean;
	
	@EJB
	private GerenciarStatus statusBean;
	
	private Colaborador colaborador;
	private Date periodoInicio;
	private Date periodoFim;
	private Status status;
	
	private List<Status> listaStatus;
	
	public void setColaboradorLogado(long idColaborador) throws SIGPRException{
		this.reset();
		this.colaborador = colaboradorBean.findById(Long.valueOf(idColaborador));
		
		consultarListaStatus();
	}
	
	public void consultarListaStatus() throws SIGPRException{
		this.listaStatus = statusBean.listarPorTipo(TipoStatus.Projeto);
	}
	
	private void reset() {
		colaborador = null;
		periodoInicio = null;
		periodoFim = null;
		status = null;
		
		listaStatus = null;
	}

	public String gerarRelatorio() throws JRException, IOException, SIGPRException{
		
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		sql.append("Select projeto from Projeto projeto ");
		
		if((status != null  && status.getId() != null && status.getId().longValue() > 0) || 
				(periodoInicio != null) || 
				(periodoFim != null)){
			
			sql.append(" where ");
			int count = 1;
			
			if(status != null  && status.getId() != null && status.getId().longValue() > 0){
				sql.append(" projeto.status = ?"+count);
				params.add(status);
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
		
		List<RelatorioProjetosStatus> lista = gerarListaRelatorio(projetos);
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if(lista.isEmpty()){
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, MSG_DADOS_INEXISTENTES_FILTRO, ""));
		}
		else{
		
			String reportUrlReal = facesContext.getExternalContext().getRealPath(RELATORIO_PROJETO_STATUS);
	
			// imprimir o relatório para um stream em PDF
			final JasperPrint jasperPrint = JasperFillManager.fillReport(reportUrlReal, new HashMap<String, Object>(),
					lista == null ? new JREmptyDataSource() : new ExtDataSource(lista));
			final ByteArrayOutputStream output = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, output);
	
			final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext()
			.getResponse();
	
			final byte[] pdf = output.toByteArray();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename=\"Relatorio Projeto Por Status.pdf\"");
			response.setContentLength(pdf.length);
			final ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(pdf, 0, pdf.length);
			ouputStream.flush();
			ouputStream.close();
	
			FacesContext.getCurrentInstance().responseComplete();
		}

		return null;
	}


	private List<RelatorioProjetosStatus> gerarListaRelatorio(
			List<Projeto> projetos) {
		
		List<RelatorioProjetosStatus> lista = new ArrayList<RelatorioProjetosStatus>();
		
		for(Projeto projeto: projetos){
			
			RelatorioProjetosStatus proj =  new RelatorioProjetosStatus();
			
			proj.setNoprojeto(projeto.getNome());
			proj.setNostatus(projeto.getStatus().getNome());
			proj.setNuprojeto(projeto.getId());
			proj.setNustatus(projeto.getStatus().getId());
			proj.setPrazo(SIGPRUtil.formatarData(projeto.getPrazo(), "dd/MM/yyyy"));
			
			lista.add(proj);
		
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

	public Status getStatus() {
		if(status == null){
			status = new Status();
		}
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<Status> getListaStatus() {
		return listaStatus;
	}

	public void setListaStatus(List<Status> listaStatus) {
		this.listaStatus = listaStatus;
	}

}
