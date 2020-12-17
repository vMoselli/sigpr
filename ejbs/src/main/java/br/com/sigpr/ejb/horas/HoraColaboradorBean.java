package br.com.sigpr.ejb.horas;

import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.horas.HoraColaborador;
import br.com.sigpr.exceptions.SIGPRException;
import br.com.sigpr.exceptions.TarefaIniciadaException;
import br.com.sigpr.util.SIGPRUtil;

@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
public class HoraColaboradorBean implements GerenciarHoraColaborador {

	@PersistenceContext
	private EntityManager em;
	
	@EJB
	private GerenciarHoraTarefa horaTarefaBean;
	
	
	private static final String CONSULTA_HORAS = "Select hora from HoraColaborador hora where hora.colaborador = :colaborador and hora.mesReferencia = :mes order by hora.inicio desc";
	private static final String CONSULTA_HORAS_DIA = "Select hora from HoraColaborador hora where hora.colaborador = :colaborador and hora.dataReferencia = :dataReferencia order by hora.inicio desc";
	private static final String CONSULTA_HORA_INICIADA_DIA = "Select hora from HoraColaborador hora where hora.colaborador = :colaborador and hora.dataReferencia = :dataReferencia and hora.fim is null";

	public void apontar(Colaborador colaborador, String justificativa) throws TarefaIniciadaException{

		Calendar dataAtual = Calendar.getInstance(SIGPRUtil.getLocale());

		List<HoraColaborador> horas = listarHorasDia(colaborador);

		if (horas.isEmpty()) {
			HoraColaborador hora = new HoraColaborador();
			hora.setDataReferencia(dataAtual.getTime());
			hora.setInicio(dataAtual.getTime());
			hora.setFim(null);
			hora.setColaborador(colaborador);
			hora.setJustificativa(justificativa);
			hora.setAnoReferencia(dataAtual.get(Calendar.YEAR));
			hora.setMesReferencia(dataAtual.get(Calendar.MONTH)+1);

			em.persist(hora);
			em.flush();
		} else {

			HoraColaborador hora = horas.get(0);

			if (hora.getInicio() != null && hora.getFim() == null) {
				
				if(horaTarefaBean.isTarefaIniciada(colaborador)){
					throw new TarefaIniciadaException("Apontamento de Hora - Colaborador",
							"Uma tarefa está iniciada, não sendo possível realizar o apontamento de parada.", null);
				}
				hora.setFim(dataAtual.getTime());
				hora.setJustificativa(justificativa);

				em.merge(hora);
				em.flush();
			} else if (hora.getInicio() != null && hora.getFim() != null) {
				hora = new HoraColaborador();
				hora.setDataReferencia(dataAtual.getTime());
				hora.setInicio(dataAtual.getTime());
				hora.setFim(null);
				hora.setColaborador(colaborador);
				hora.setJustificativa(justificativa);
				hora.setAnoReferencia(dataAtual.get(Calendar.YEAR));
				hora.setMesReferencia(dataAtual.get(Calendar.MONTH)+1);

				em.persist(hora);
				em.flush();
			}

		}
	}

	@SuppressWarnings("unchecked")
	public List<HoraColaborador> listarHoras(Colaborador colaborador) {
		List<HoraColaborador> horas = null;
		Calendar dataAtual = Calendar.getInstance(SIGPRUtil.getLocale());

		horas = em.createQuery(CONSULTA_HORAS)
				.setParameter("colaborador", colaborador)
				.setParameter("mes", dataAtual.get(Calendar.MONTH)+1)
				.getResultList();

		return horas;
	}
	
	@SuppressWarnings("unchecked")
	public List<HoraColaborador> listarHorasDia(Colaborador colaborador) {
		List<HoraColaborador> horas = null;
		Calendar dataAtual = Calendar.getInstance(SIGPRUtil.getLocale());

		horas = em.createQuery(CONSULTA_HORAS_DIA)
				.setParameter("colaborador", colaborador)
				.setParameter("dataReferencia", dataAtual.getTime())
				.getResultList();

		return horas;
	}
	
	public HoraColaborador consultarHoraIniciada(Colaborador colaborador){
		Calendar dataAtual = Calendar.getInstance(SIGPRUtil.getLocale());
		try{
			
			return (HoraColaborador) em.createQuery(CONSULTA_HORA_INICIADA_DIA)
					.setParameter("colaborador", colaborador)
					.setParameter("dataReferencia", dataAtual.getTime())
					.getSingleResult();
		}
		catch(NoResultException nrex){
			return null;
		}
		
	}

	public boolean isHoraIniciada(Colaborador colaborador) {
		HoraColaborador hora = consultarHoraIniciada(colaborador);
		
		if(hora != null){
			return true;
		}
		else{
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<HoraColaborador> consultar(String query, Object... params) throws SIGPRException {
		List<HoraColaborador> lista = null;
		Query q = em.createQuery(query);
		
		try{
			int count = 1;
			for(Object obj : params){
				q.setParameter(count, obj);
				count++;
			}
			
			lista = q.getResultList();
		}
		catch(Exception e){
			throw new SIGPRException("Geração de Relatórios", "Ocorreu um erro ao gerar o relatório", e);
		}
		
		return lista;
		
	}

}
