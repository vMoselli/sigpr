package br.com.sigpr.jsf.relatorios.tarefa;

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
import br.com.sigpr.ejb.cadastro.perfil.GerenciarPerfil;
import br.com.sigpr.ejb.cadastro.projeto.GerenciarProjeto;
import br.com.sigpr.ejb.cadastro.tarefa.GerenciarTarefa;
import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.funcionalidade.Funcionalidade;
import br.com.sigpr.entity.perfil.Perfil;
import br.com.sigpr.entity.projeto.Projeto;
import br.com.sigpr.entity.tarefa.Tarefa;
import br.com.sigpr.enumerations.TipoTarefa;
import br.com.sigpr.exceptions.SIGPRException;
import br.com.sigpr.util.SIGPRUtil;
import br.com.sigpr.util.relatorio.ExtDataSource;
import br.com.sigpr.util.relatorio.RelatorioTarefas;

@ManagedBean(name = "relatorioTarefaMBean")
@SessionScoped
public class RelatorioTarefaManagedBean implements Serializable {
	
	private static final long serialVersionUID = 5053598456962581204L;

	private static final String RELATORIO_TAREFA =
		"/WEB-INF/classes/br/com/sigpr/relatorios/tarefa/TarefaColaborador.jasper";

	private static final String MSG_DADOS_INEXISTENTES_FILTRO = "Não foram encontradas Tarefas cadastradas para os filtros informados.";

	@EJB
	private GerenciarColaborador colaboradorBean;
	
	@EJB
	private GerenciarPerfil perfilBean;
	
	@EJB
	private GerenciarProjeto projetoBean;
	
	@EJB
	private GerenciarFuncionalidade funcionalidadeBean;
	
	@EJB
	private GerenciarTarefa tarefaBean;
	
	private Colaborador colaborador;
	private Perfil perfil;
	private Projeto projeto;
	private Funcionalidade funcionalidade;
	private TipoTarefa tipoTarefa;
	private Date periodoInicio;
	private Date periodoFim;
	
	private List<Perfil> listaPerfis;
	private List<Projeto> listaProjetos;
	private List<Funcionalidade> listaFuncionalidades;
	private List<Colaborador> listaColaboradores;
	
	public void setColaboradorLogado(long idColaborador) throws SIGPRException{
		this.reset();
		//this.colaborador = colaboradorBean.findById(Long.valueOf(idColaborador));
		
		consultarListaPerfis();
		consultarListaProjetos();
		consultarListaFuncionalidades();
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
	
	public void consultarListaProjetos() throws SIGPRException{
		this.listaProjetos = projetoBean.listar();
	}
	
	public void consultarListaFuncionalidades() throws SIGPRException{
		this.listaFuncionalidades = funcionalidadeBean.listar();
	}
	
	public TipoTarefa[] getListaTiposTarefa(){
		return TipoTarefa.values();
	}
	

	private void reset() {
		colaborador = null;
		perfil = null;
		projeto = null;
		funcionalidade = null;
		tipoTarefa = null;
		periodoInicio = null;
		periodoFim = null;
		
		listaColaboradores = null;
		listaPerfis = null;
		listaProjetos = null;
		listaFuncionalidades = null;
	}

	public String gerarRelatorio() throws JRException, IOException, SIGPRException{
		
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		sql.append("Select tarefa from Tarefa tarefa ");
		
		if((projeto != null && projeto.getId() != null && projeto.getId().longValue() > 0) || 
				(colaborador != null  && colaborador.getId() != null && colaborador.getId().longValue() > 0) || 
				(perfil != null  && perfil.getId() != null && perfil.getId().longValue() > 0) || 
				(funcionalidade != null  && funcionalidade.getId() != null && funcionalidade.getId().longValue() > 0) || 
				(tipoTarefa != null) || (periodoInicio != null) || (periodoFim != null)){
			
			sql.append(" where ");
			int count = 1;
			if(projeto != null && projeto.getId() != null && projeto.getId().longValue() > 0){
				sql.append(" tarefa.funcionalidade.projeto = ?"+count);
				count ++;
				params.add(projeto);
			}
			
			if(colaborador != null  && colaborador.getId() != null && colaborador.getId().longValue() > 0){
				if(count > 1){
					sql.append(" and ");
				}
				sql.append(" tarefa.colaborador = ?"+count);
				params.add(colaborador);
				count ++;
			}
			
			if(perfil != null  && perfil.getId() != null && perfil.getId().longValue() > 0){
				if(count > 1){
					sql.append(" and ");
				}
				sql.append(" tarefa.colaborador.perfil = ?"+count);
				params.add(perfil);
				count ++;
			}
			
			if(funcionalidade != null  && funcionalidade.getId() != null && funcionalidade.getId().longValue() > 0){
				if(count > 1){
					sql.append(" and ");
				}
				sql.append(" tarefa.funcionalidade = ?"+count);
				params.add(funcionalidade);
				count ++;
			}
			
			if(tipoTarefa != null){
				if(count > 1){
					sql.append(" and ");
				}
				sql.append(" tarefa.tipoTarefa = ?"+count);
				params.add(tipoTarefa);
				count ++;
			}
			if(periodoInicio != null || periodoFim != null){
				if(count > 1){
					sql.append(" and ");
				}
				if(periodoInicio != null && periodoFim == null){
					sql.append(" tarefa.prazo >= ?"+count);
					params.add(periodoInicio);
					count ++;
				}
				if(periodoInicio == null && periodoFim != null){
					sql.append(" tarefa.prazo <= ?"+count);
					params.add(periodoFim);
					count ++;
				}
				if(periodoInicio != null && periodoFim != null){
					sql.append(" tarefa.prazo between ?"+count+" and ?"+(count+1));
					params.add(periodoInicio);
					params.add(periodoFim);
					count ++; count++;
				}
			}
		}
		List<Tarefa> tarefas = null;
		try{
			tarefas = tarefaBean.consultar(sql.toString(), params.toArray());
		}
		catch(SIGPRException e){
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMensagem(), ""));
			return null;
		}
		
		List<RelatorioTarefas> lista = gerarListaRelatorio(tarefas);
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if(lista.isEmpty()){
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, MSG_DADOS_INEXISTENTES_FILTRO, ""));
		}
		else{
		
			String reportUrlReal = facesContext.getExternalContext().getRealPath(RELATORIO_TAREFA);
	
			// imprimir o relatório para um stream em PDF
			final JasperPrint jasperPrint = JasperFillManager.fillReport(reportUrlReal, new HashMap<String, Object>(),
					lista == null ? new JREmptyDataSource() : new ExtDataSource(lista));
			final ByteArrayOutputStream output = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, output);
	
			final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext()
			.getResponse();
	
			final byte[] pdf = output.toByteArray();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename=\"Relatorio Tarefas.pdf\"");
			response.setContentLength(pdf.length);
			final ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(pdf, 0, pdf.length);
			ouputStream.flush();
			ouputStream.close();
	
			FacesContext.getCurrentInstance().responseComplete();
		}

		return null;
	}


	private List<RelatorioTarefas> gerarListaRelatorio(
			List<Tarefa> tarefas) {
		
		List<RelatorioTarefas> lista = new ArrayList<RelatorioTarefas>();
		
		for(Tarefa tarefa: tarefas){
			
			RelatorioTarefas taref =  new RelatorioTarefas();
			
			taref.setNocolaborador(tarefa.getColaborador() != null ? tarefa.getColaborador().getNome() : "Não Atribuído");
			taref.setNofuncionalidade(tarefa.getFuncionalidade().getNome());
			taref.setNoprojeto(tarefa.getFuncionalidade().getProjeto().getNome());
			taref.setNotarefa(tarefa.getNome());
			taref.setNucolaborador(tarefa.getColaborador() != null ? tarefa.getColaborador().getId() : 0l);
			taref.setNufuncionalidade(tarefa.getFuncionalidade().getId());
			taref.setNuprojeto(tarefa.getFuncionalidade().getProjeto().getId());
			taref.setPerfil(tarefa.getColaborador() != null ? tarefa.getColaborador().getPerfil().getNome() : "");
			taref.setPrazo(SIGPRUtil.formatarData(tarefa.getPrazo(), "dd/MM/yyyy"));
			taref.setTipotarefa(tarefa.getTipoTarefa().getNome());
			
			lista.add(taref);
		
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


	public List<Projeto> getListaProjetos() {
		return listaProjetos;
	}


	public void setListaProjetos(List<Projeto> listaProjetos) {
		this.listaProjetos = listaProjetos;
	}

	public Funcionalidade getFuncionalidade() {
		if(funcionalidade == null){
			funcionalidade = new Funcionalidade();
		}
		return funcionalidade;
	}

	public void setFuncionalidade(Funcionalidade funcionalidade) {
		this.funcionalidade = funcionalidade;
	}

	public TipoTarefa getTipoTarefa() {
		return tipoTarefa;
	}

	public void setTipoTarefa(TipoTarefa tipoTarefa) {
		this.tipoTarefa = tipoTarefa;
	}

	public List<Funcionalidade> getListaFuncionalidades() {
		return listaFuncionalidades;
	}

	public void setListaFuncionalidades(List<Funcionalidade> listaFuncionalidades) {
		this.listaFuncionalidades = listaFuncionalidades;
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

}
