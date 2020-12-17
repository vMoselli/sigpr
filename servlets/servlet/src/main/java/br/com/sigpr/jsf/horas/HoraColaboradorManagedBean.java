package br.com.sigpr.jsf.horas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.com.sigpr.ejb.cadastro.colaborador.GerenciarColaborador;
import br.com.sigpr.ejb.horas.GerenciarHoraColaborador;
import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.horas.HoraColaborador;
import br.com.sigpr.exceptions.SIGPRException;
import br.com.sigpr.exceptions.TarefaIniciadaException;
import br.com.sigpr.util.SIGPRUtil;

@ManagedBean(name = "apontamentoMBean")
@SessionScoped
public class HoraColaboradorManagedBean implements Serializable {

	private static final long serialVersionUID = -8338405024481485753L;

	private static final String HORAS_TRABALHADAS = "horasTrabalhadas";
	private static final String MSG_SUCESSO_APONTAMENTO = "Apontamento realizado com sucesso.";

	@EJB
	private transient GerenciarHoraColaborador horaColaboradorBean;

	@EJB
	private transient GerenciarColaborador colaboradorBean;

	private Colaborador colaborador;
	private String justificativa;

	private List<HoraColaborador> listaHoras;
	private List<HoraColaborador> listaHorasDataAtual;

	public void setColaboradorLogado(long idColaborador) throws SIGPRException{
		this.colaborador = colaboradorBean.findById(Long.valueOf(idColaborador));

		consultarHorasColaborador();
		consultarHorasColaboradorDataAtual();
		consultarHoraIniciada();
	}

	private void consultarHoraIniciada() {
		HoraColaborador horaIniciada = horaColaboradorBean.consultarHoraIniciada(colaborador);

		if(horaIniciada != null){
			this.justificativa = horaIniciada.getJustificativa();
		}
	}

	private void consultarHorasColaboradorDataAtual() {
		listaHorasDataAtual = horaColaboradorBean.listarHorasDia(colaborador);
	}

	private void consultarHorasColaborador() {
		listaHoras = horaColaboradorBean.listarHoras(colaborador);
	}

	public List<HoraColaborador> getListaHoras() {
		if(listaHoras == null){
			listaHoras = new ArrayList<HoraColaborador>();
		}
		return listaHoras;
	}

	public String apontar() throws SIGPRException{

		try{
			horaColaboradorBean.apontar(colaborador, justificativa);
		}
		catch (TarefaIniciadaException e) {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMensagem(), e.getMensagem()));
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, MSG_SUCESSO_APONTAMENTO, ""));
		consultarHorasColaborador();
		consultarHorasColaboradorDataAtual();
		consultarHoraIniciada();
		return HORAS_TRABALHADAS;
	}

	public boolean isHoraExtra(){
		if(listaHorasDataAtual == null){
			consultarHorasColaboradorDataAtual();
		}

		if(listaHorasDataAtual.isEmpty()){
			return false;
		}

		double totalHorasDia = 0d;
		for(HoraColaborador hora: listaHorasDataAtual){
			if(hora.getFim() != null){
				totalHorasDia += SIGPRUtil.calculaDiferencaHoras(hora.getInicio(), hora.getFim());
			}
			else{
				totalHorasDia += SIGPRUtil.calculaDiferencaHoras(hora.getInicio(), new Date());
			}
		}

		return totalHorasDia >= 8.25d;
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

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

}
