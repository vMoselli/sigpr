package br.com.sigpr.jsf.relatorios.funcionalidade;

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
import br.com.sigpr.ejb.cadastro.funcionalidade.GerenciarFuncionalidade;
import br.com.sigpr.ejb.cadastro.projeto.GerenciarProjeto;
import br.com.sigpr.ejb.cadastro.status.GerenciarStatus;
import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.funcionalidade.Funcionalidade;
import br.com.sigpr.entity.projeto.Projeto;
import br.com.sigpr.entity.status.Status;
import br.com.sigpr.enumerations.TipoStatus;
import br.com.sigpr.exceptions.SIGPRException;
import br.com.sigpr.util.SIGPRUtil;
import br.com.sigpr.util.relatorio.ExtDataSource;
import br.com.sigpr.util.relatorio.RelatorioFuncionalidadesStatus;

@ManagedBean(name = "relatorioFuncionalidadeStatusMBean")
@SessionScoped
public class RelatorioFuncionalidadeStatusManagedBean implements Serializable {
	
	private static final long serialVersionUID = 5053598456962581204L;

	private static final String RELATORIO_FUNCIONALIDADE_STATUS =
		"/WEB-INF/classes/br/com/sigpr/relatorios/funcionalidade/FuncionalidadeStatus.jasper";

	private static final String MSG_DADOS_INEXISTENTES_FILTRO = "Não foram encontradas Funcionalidades cadastradas para os filtros informados.";

	@EJB
	private GerenciarColaborador colaboradorBean;
	
	@EJB
	private GerenciarProjeto projetoBean;
	
	@EJB
	private GerenciarFuncionalidade funcionalidadeBean;
	
	@EJB
	private GerenciarStatus statusBean;
	
	private Colaborador colaborador;
	private Projeto projeto;
	private Date periodoInicio;
	private Date periodoFim;
	private Status status;
	
	private List<Projeto> listaProjetos;
	private List<Status> listaStatus;
	
	public void setColaboradorLogado(long idColaborador) throws SIGPRException{
		this.reset();
		this.colaborador = colaboradorBean.findById(Long.valueOf(idColaborador));
		
		consultarListaProjetos();
		consultarListaStatus();
	}
	
	public void consultarListaProjetos() throws SIGPRException{
		this.listaProjetos = projetoBean.listar();
	}
	
	public void consultarListaStatus() throws SIGPRException{
		this.listaStatus = statusBean.listarPorTipo(TipoStatus.Funcionalidade);
	}
	
	private void reset() {
		colaborador = null;
		projeto = null;
		periodoInicio = null;
		periodoFim = null;
		status = null;
		
		listaProjetos = null;
		listaStatus = null;
	}

	public String gerarRelatorio() throws JRException, IOException, SIGPRException{
		
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		sql.append("Select funcionalidade from Funcionalidade funcionalidade ");
		
		if((projeto != null && projeto.getId() != null && projeto.getId().longValue() > 0) || 
				(periodoInicio != null) || 
				(periodoFim != null) ||
				(status != null  && status.getId() != null && status.getId().longValue() > 0)){
			
			sql.append(" where ");
			int count = 1;
			if(projeto != null && projeto.getId() != null && projeto.getId().longValue() > 0){
				sql.append(" funcionalidade.projeto = ?"+count);
				count ++;
				params.add(projeto);
			}
			
			if(status != null  && status.getId() != null && status.getId().longValue() > 0){
				if(count > 1){
					sql.append(" and ");
				}
				sql.append(" funcionalidade.status = ?"+count);
				params.add(status);
				count ++;
			}
			if(periodoInicio != null || periodoFim != null){
				if(count > 1){
					sql.append(" and ");
				}
				if(periodoInicio != null && periodoFim == null){
					sql.append(" funcionalidade.prazo >= ?"+count);
					params.add(periodoInicio);
					count ++;
				}
				if(periodoInicio == null && periodoFim != null){
					sql.append(" funcionalidade.prazo <= ?"+count);
					params.add(periodoFim);
					count ++;
				}
				if(periodoInicio != null && periodoFim != null){
					sql.append("funcionalidade.prazo between ?"+count+" and ?"+(count+1));
					params.add(periodoInicio);
					params.add(periodoFim);
					count ++; count++;
				}
			}
		}
		List<Funcionalidade> funcionalidades = null;
		try{
			funcionalidades = funcionalidadeBean.consultar(sql.toString(), params.toArray());
		}
		catch(SIGPRException e){
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMensagem(), ""));
			return null;
		}
		
		List<RelatorioFuncionalidadesStatus> lista = gerarListaRelatorio(funcionalidades);
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if(lista.isEmpty()){
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, MSG_DADOS_INEXISTENTES_FILTRO, ""));
		}
		else{
		
			String reportUrlReal = facesContext.getExternalContext().getRealPath(RELATORIO_FUNCIONALIDADE_STATUS);
	
			// imprimir o relatório para um stream em PDF
			final JasperPrint jasperPrint = JasperFillManager.fillReport(reportUrlReal, new HashMap<String, Object>(),
					lista == null ? new JREmptyDataSource() : new ExtDataSource(lista));
			final ByteArrayOutputStream output = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, output);
	
			final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext()
			.getResponse();
	
			final byte[] pdf = output.toByteArray();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename=\"Relatorio Funcionalidades Por Status.pdf\"");
			response.setContentLength(pdf.length);
			final ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(pdf, 0, pdf.length);
			ouputStream.flush();
			ouputStream.close();
	
			FacesContext.getCurrentInstance().responseComplete();
		}

		return null;
	}


	private List<RelatorioFuncionalidadesStatus> gerarListaRelatorio(
			List<Funcionalidade> funcionalidades) {
		
		List<RelatorioFuncionalidadesStatus> lista = new ArrayList<RelatorioFuncionalidadesStatus>();
		
		for(Funcionalidade funcionalidade: funcionalidades){
			
			RelatorioFuncionalidadesStatus func =  new RelatorioFuncionalidadesStatus();
			
			func.setNofuncionalidade(funcionalidade.getNome());
			func.setNoprojeto(funcionalidade.getProjeto().getNome());
			func.setNostatus(funcionalidade.getStatus().getNome());
			func.setNufuncionalidade(funcionalidade.getId());
			func.setNuprojeto(funcionalidade.getProjeto().getId());
			func.setNustatus(funcionalidade.getStatus().getId());
			func.setPrazo(SIGPRUtil.formatarData(funcionalidade.getPrazo(), "dd/MM/yyyy"));
			
			lista.add(func);
		
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

	public List<Projeto> getListaProjetos() {
		return listaProjetos;
	}


	public void setListaProjetos(List<Projeto> listaProjetos) {
		this.listaProjetos = listaProjetos;
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
