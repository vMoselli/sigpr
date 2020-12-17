package br.com.sigpr.jsf.relatorios.artefato;

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
import br.com.sigpr.ejb.cadastro.artefato.GerenciarArtefato;
import br.com.sigpr.ejb.cadastro.colaborador.GerenciarColaborador;
import br.com.sigpr.ejb.cadastro.funcionalidade.GerenciarFuncionalidade;
import br.com.sigpr.ejb.cadastro.perfil.GerenciarPerfil;
import br.com.sigpr.ejb.cadastro.projeto.GerenciarProjeto;
import br.com.sigpr.ejb.cadastro.tarefa.GerenciarTarefa;
import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.funcionalidade.Funcionalidade;
import br.com.sigpr.entity.perfil.Perfil;
import br.com.sigpr.entity.projeto.Projeto;
import br.com.sigpr.entity.tarefa.Arquivo;
import br.com.sigpr.entity.tarefa.Artefato;
import br.com.sigpr.entity.tarefa.Tarefa;
import br.com.sigpr.enumerations.TipoTarefa;
import br.com.sigpr.exceptions.SIGPRException;
import br.com.sigpr.util.SIGPRUtil;
import br.com.sigpr.util.relatorio.ExtDataSource;
import br.com.sigpr.util.relatorio.RelatorioArtefatosTarefa;

@ManagedBean(name = "relatorioArtefatoTarefaMBean")
@SessionScoped
public class RelatorioArtefatoTarefaManagedBean implements Serializable {
	
	private static final long serialVersionUID = 5053598456962581204L;

	private static final String RELATORIO_ARTEFATO_TAREFA =
		"/WEB-INF/classes/br/com/sigpr/relatorios/tarefa/ArtefatosTarefa.jasper";

	private static final String MSG_DADOS_INEXISTENTES_FILTRO = "Não foram encontrados Artefatos cadastrados para os filtros informados.";

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
	
	@EJB
	private GerenciarArtefato artefatoBean;
	
	private Colaborador colaborador;
	private Perfil perfil;
	private Projeto projeto;
	private Funcionalidade funcionalidade;
	private TipoTarefa tipoTarefa;
	private Tarefa tarefa;
	private Date periodoInicio;
	private Date periodoFim;
	
	private List<Perfil> listaPerfis;
	private List<Projeto> listaProjetos;
	private List<Funcionalidade> listaFuncionalidades;
	private List<Colaborador> listaColaboradores;
	private List<Tarefa> listaTarefas;
	
	public void setColaboradorLogado(long idColaborador) throws SIGPRException{
		this.reset();
		//this.colaborador = colaboradorBean.findById(Long.valueOf(idColaborador));
		
		consultarListaPerfis();
		consultarListaProjetos();
		consultarListaFuncionalidades();
		consultarListaColaboradores();
		consultarListaTarefas();
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
	
	public void consultarListaTarefas() throws SIGPRException{
		this.listaTarefas = tarefaBean.listar();
	}
	
	public TipoTarefa[] getListaTiposTarefa(){
		return TipoTarefa.values();
	}
	
	private void reset() {
		colaborador = null;
		perfil = null;
		projeto = null;
		funcionalidade = null;
		tarefa = null;
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
		
		sql.append("Select artefato from Artefato artefato ");
		
		if((projeto != null && projeto.getId() != null && projeto.getId().longValue() > 0) || 
				(colaborador != null  && colaborador.getId() != null && colaborador.getId().longValue() > 0) || 
				(perfil != null  && perfil.getId() != null && perfil.getId().longValue() > 0) || 
				(funcionalidade != null  && funcionalidade.getId() != null && funcionalidade.getId().longValue() > 0) ||
				(tarefa != null  && tarefa.getId() != null && tarefa.getId().longValue() > 0) ||
				(tipoTarefa != null) || (periodoInicio != null) || (periodoFim != null)){
			
			sql.append(" where ");
			int count = 1;
			if(projeto != null && projeto.getId() != null && projeto.getId().longValue() > 0){
				sql.append(" artefato.tarefa.funcionalidade.projeto = ?"+count);
				count ++;
				params.add(projeto);
			}
			
			if(funcionalidade != null  && funcionalidade.getId() != null && funcionalidade.getId().longValue() > 0){
				if(count > 1){
					sql.append(" and ");
				}
				sql.append(" artefato.tarefa.funcionalidade = ?"+count);
				params.add(funcionalidade);
				count ++;
			}
			
			if(tarefa != null  && tarefa.getId() != null && tarefa.getId().longValue() > 0){
				if(count > 1){
					sql.append(" and ");
				}
				sql.append(" artefato.tarefa = ?"+count);
				params.add(tarefa);
				count ++;
			}
			
			if(colaborador != null  && colaborador.getId() != null && colaborador.getId().longValue() > 0){
				if(count > 1){
					sql.append(" and ");
				}
				sql.append(" artefato.tarefa.colaborador = ?"+count);
				params.add(colaborador);
				count ++;
			}
			
			if(perfil != null  && perfil.getId() != null && perfil.getId().longValue() > 0){
				if(count > 1){
					sql.append(" and ");
				}
				sql.append(" artefato.tarefa.colaborador.perfil = ?"+count);
				params.add(perfil);
				count ++;
			}
			
			if(tipoTarefa != null){
				if(count > 1){
					sql.append(" and ");
				}
				sql.append(" artefato.tarefa.tipoTarefa = ?"+count);
				params.add(tipoTarefa);
				count ++;
			}
			if(periodoInicio != null || periodoFim != null){
				if(count > 1){
					sql.append(" and ");
				}
				if(periodoInicio != null && periodoFim == null){
					sql.append(" artefato.tarefa.prazo >= ?"+count);
					params.add(periodoInicio);
					count ++;
				}
				if(periodoInicio == null && periodoFim != null){
					sql.append(" artefato.tarefa.prazo <= ?"+count);
					params.add(periodoFim);
					count ++;
				}
				if(periodoInicio != null && periodoFim != null){
					sql.append(" artefato.tarefa.prazo between ?"+count+" and ?"+(count+1));
					params.add(periodoInicio);
					params.add(periodoFim);
					count ++; count++;
				}
			}
		}
		List<Artefato> artefatos = null;
		try{
			artefatos = artefatoBean.consultar(sql.toString(), params.toArray());
		}
		catch(SIGPRException e){
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMensagem(), ""));
			return null;
		}
		
		List<RelatorioArtefatosTarefa> lista = gerarListaRelatorio(artefatos);
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if(lista.isEmpty()){
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, MSG_DADOS_INEXISTENTES_FILTRO, ""));
		}
		else{
		
			String reportUrlReal = facesContext.getExternalContext().getRealPath(RELATORIO_ARTEFATO_TAREFA);
	
			// imprimir o relatório para um stream em PDF
			final JasperPrint jasperPrint = JasperFillManager.fillReport(reportUrlReal, new HashMap<String, Object>(),
					lista == null ? new JREmptyDataSource() : new ExtDataSource(lista));
			final ByteArrayOutputStream output = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, output);
	
			final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext()
			.getResponse();
	
			final byte[] pdf = output.toByteArray();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename=\"Relatorio Artefatos por Tarefa.pdf\"");
			response.setContentLength(pdf.length);
			final ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(pdf, 0, pdf.length);
			ouputStream.flush();
			ouputStream.close();
	
			FacesContext.getCurrentInstance().responseComplete();
		}

		return null;
	}


	private List<RelatorioArtefatosTarefa> gerarListaRelatorio(
			List<Artefato> artefatos) {
		
		List<RelatorioArtefatosTarefa> lista = new ArrayList<RelatorioArtefatosTarefa>();
		
		for(Artefato artefato: artefatos){
			
			for(Arquivo arquivo : artefato.getArquivos()){
				
				RelatorioArtefatosTarefa artef =  new RelatorioArtefatosTarefa();
				
				artef.setNocolaborador(artefato.getTarefa().getColaborador().getNome());
				artef.setNofuncionalidade(artefato.getTarefa().getFuncionalidade().getNome());
				artef.setNoprojeto(artefato.getTarefa().getFuncionalidade().getProjeto().getNome());
				artef.setNotarefa(artefato.getTarefa().getNome());
				artef.setNucolaborador(artefato.getTarefa().getColaborador().getId());
				artef.setNufuncionalidade(artefato.getTarefa().getFuncionalidade().getId());
				artef.setNuprojeto(artefato.getTarefa().getFuncionalidade().getProjeto().getId());
				artef.setPerfil(artefato.getTarefa().getColaborador().getPerfil().getNome());
				artef.setPrazo(SIGPRUtil.formatarData(artefato.getTarefa().getPrazo(), "dd/MM/yyyy"));
				artef.setTipotarefa(artefato.getTarefa().getTipoTarefa().getNome());
				artef.setNoartefato(artefato.getNome());
				artef.setDescricao(artefato.getDescricao());
				artef.setFileName(arquivo.getFileName());
				artef.setFileSize(String.valueOf(arquivo.getFileSize()/1024)+"Kb");
				artef.setFileType(arquivo.getFileType());
				
				lista.add(artef);
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

	public List<Tarefa> getListaTarefas() {
		return listaTarefas;
	}

	public void setListaTarefas(List<Tarefa> listaTarefas) {
		this.listaTarefas = listaTarefas;
	}

	public Tarefa getTarefa() {
		if(tarefa == null){
			tarefa = new Tarefa();
		}
		return tarefa;
	}

	public void setTarefa(Tarefa tarefa) {
		this.tarefa = tarefa;
	}

}
