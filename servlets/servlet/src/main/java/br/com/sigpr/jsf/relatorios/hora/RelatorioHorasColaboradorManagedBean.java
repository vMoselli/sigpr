package br.com.sigpr.jsf.relatorios.hora;

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
import br.com.sigpr.ejb.cadastro.perfil.GerenciarPerfil;
import br.com.sigpr.ejb.horas.GerenciarHoraColaborador;
import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.horas.HoraColaborador;
import br.com.sigpr.entity.perfil.Perfil;
import br.com.sigpr.exceptions.SIGPRException;
import br.com.sigpr.util.SIGPRUtil;
import br.com.sigpr.util.relatorio.ExtDataSource;
import br.com.sigpr.util.relatorio.RelatorioHorasColaborador;

@ManagedBean(name = "relatorioHorasColaboradorMBean")
@SessionScoped
public class RelatorioHorasColaboradorManagedBean implements Serializable {
	
	private static final long serialVersionUID = 5053598456962581204L;

	private static final String RELATORIO_HORA_COLABORADOR =
		"/WEB-INF/classes/br/com/sigpr/relatorios/hora/HoraColaborador.jasper";

	private static final String MSG_DADOS_INEXISTENTES_FILTRO = "Não foram encontradas Horas apontadas para os filtros informados.";

	@EJB
	private GerenciarColaborador colaboradorBean;
	
	@EJB
	private GerenciarPerfil perfilBean;
	
	@EJB
	private GerenciarHoraColaborador horaColaboradorBean;
	
	private Colaborador colaborador;
	private Perfil perfil;
	private Date periodoInicio;
	private Date periodoFim;
	
	private List<Perfil> listaPerfis;
	private List<Colaborador> listaColaboradores;
	
	public void setColaboradorLogado(long idColaborador) throws SIGPRException{
		this.reset();
		//this.colaborador = colaboradorBean.findById(Long.valueOf(idColaborador));
		
		consultarListaPerfis();
		consultarListaColaboradores();
	}
	
	public void consultarListaColaboradores() throws SIGPRException{
		this.listaColaboradores = colaboradorBean.consultar("Select colaborador from Colaborador colaborador" +
				" where colaborador.perfil.nome <> ?1", 
				"Administrador");
	}
	
	public void consultarListaPerfis() throws SIGPRException{
		this.listaPerfis = perfilBean.listar();
	}
	

	private void reset() {
		colaborador = null;
		perfil = null;
		periodoInicio = null;
		periodoFim = null;
		
		listaColaboradores = null;
		listaPerfis = null;
	}

	public String gerarRelatorio() throws JRException, IOException, SIGPRException{
		
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		sql.append("Select hora from HoraColaborador hora ");
		
		if((colaborador != null  && colaborador.getId() != null && colaborador.getId().longValue() > 0) || 
				(perfil != null  && perfil.getId() != null && perfil.getId().longValue() > 0) || 
				(periodoInicio != null) || (periodoFim != null)){
			
			sql.append(" where ");
			int count = 1;
			if(colaborador != null  && colaborador.getId() != null && colaborador.getId().longValue() > 0){
				if(count > 1){
					sql.append(" and ");
				}
				sql.append(" hora.colaborador = ?"+count);
				params.add(colaborador);
				count ++;
			}
			
			if(perfil != null  && perfil.getId() != null && perfil.getId().longValue() > 0){
				if(count > 1){
					sql.append(" and ");
				}
				sql.append(" hora.colaborador.perfil = ?"+count);
				params.add(perfil);
				count ++;
			}
			
			if(periodoInicio != null || periodoFim != null){
				if(count > 1){
					sql.append(" and ");
				}
				if(periodoInicio != null && periodoFim == null){
					sql.append(" hora.dataReferencia >= ?"+count);
					params.add(periodoInicio);
					count ++;
				}
				if(periodoInicio == null && periodoFim != null){
					sql.append(" hora.dataReferencia <= ?"+count);
					params.add(periodoFim);
					count ++;
				}
				if(periodoInicio != null && periodoFim != null){
					sql.append(" hora.dataReferencia between ?"+count+" and ?"+(count+1));
					params.add(periodoInicio);
					params.add(periodoFim);
					count ++; count++;
				}
			}
		}
		
		sql.append(" order by hora.dataReferencia asc");
		List<HoraColaborador> horas = null;
		try{
			horas = horaColaboradorBean.consultar(sql.toString(), params.toArray());
		}
		catch(SIGPRException e){
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMensagem(), ""));
			return null;
		}
		
		List<RelatorioHorasColaborador> lista = gerarListaRelatorio(horas);
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if(lista.isEmpty()){
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, MSG_DADOS_INEXISTENTES_FILTRO, ""));
		}
		else{
		
			String reportUrlReal = facesContext.getExternalContext().getRealPath(RELATORIO_HORA_COLABORADOR);
	
			// imprimir o relatório para um stream em PDF
			final JasperPrint jasperPrint = JasperFillManager.fillReport(reportUrlReal, new HashMap<String, Object>(),
					lista == null ? new JREmptyDataSource() : new ExtDataSource(lista));
			final ByteArrayOutputStream output = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, output);
	
			final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext()
			.getResponse();
	
			final byte[] pdf = output.toByteArray();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename=\"Relatorio Horas Colaborador.pdf\"");
			response.setContentLength(pdf.length);
			final ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(pdf, 0, pdf.length);
			ouputStream.flush();
			ouputStream.close();
	
			FacesContext.getCurrentInstance().responseComplete();
		}

		return null;
	}


	private List<RelatorioHorasColaborador> gerarListaRelatorio(
			List<HoraColaborador> horas) {
		
		List<RelatorioHorasColaborador> lista = new ArrayList<RelatorioHorasColaborador>();
		
		for(HoraColaborador hora: horas){
			
			RelatorioHorasColaborador hr =  new RelatorioHorasColaborador();
			
			hr.setNocolaborador(hora.getColaborador().getNome());
			hr.setNucolaborador(hora.getColaborador().getId());
			hr.setPerfil(hora.getColaborador().getPerfil().getNome());
			hr.setDataReferencia(SIGPRUtil.formatarData(hora.getDataReferencia(), "dd/MM/yyyy"));
			hr.setInicio(SIGPRUtil.formatarData(hora.getInicio(), "HH:mm"));
			hr.setFim(hora.getFim() != null ? SIGPRUtil.formatarData( hora.getFim(), "HH:mm") : "");
			hr.setTotal(hora.getFim() != null ? hora.getTotalHoras() : "" );
			
			lista.add(hr);
		
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

	public List<Perfil> getListaPerfis() {
		return listaPerfis;
	}


	public void setListaPerfis(List<Perfil> listaPerfis) {
		this.listaPerfis = listaPerfis;
	}

	public List<Colaborador> getListaColaboradores() {
		return listaColaboradores;
	}

	public void setListaColaboradores(List<Colaborador> listaColaboradores) {
		this.listaColaboradores = listaColaboradores;
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

}
