package br.com.sigpr.ejb.horas;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.horas.HoraTarefa;
import br.com.sigpr.entity.tarefa.Tarefa;
import br.com.sigpr.exceptions.SIGPRException;
import br.com.sigpr.util.SIGPRUtil;

@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
public class HoraTarefaBean implements GerenciarHoraTarefa {

	@PersistenceContext
	private EntityManager em;

	private static final String CONSULTA_HORAS_TAREFA = "Select hora from HoraTarefa hora where hora.tarefa = :tarefa order by hora.inicio desc";
	private static final String CONSULTA_HORAS_TAREFA_COLABORADOR = "Select hora from HoraTarefa hora where hora.tarefa = :tarefa and hora.colaborador = :colaborador order by hora.inicio desc";
	private static final String CONSULTA_HORAS_TAREFA_COLABORADOR_DIA = "Select hora from HoraTarefa hora where hora.tarefa = :tarefa and hora.colaborador = :colaborador and hora.dataReferencia = :dataReferencia order by hora.inicio desc";
	private static final String CONSULTA_HORA_TAREFA_INICIADA_COLABORADOR = "Select hora from HoraTarefa hora where hora.colaborador = :colaborador and hora.dataReferencia = :dataReferencia and hora.fim is null order by hora.inicio desc";
	private static final String CONSULTA_HORA_TAREFA_INICIADA_TAREFA_COLABORADOR = "Select hora from HoraTarefa hora where hora.tarefa = :tarefa and hora.colaborador = :colaborador and hora.dataReferencia = :dataReferencia and hora.fim is null order by hora.inicio desc";

	public void apontar(Tarefa tarefa, Colaborador colaborador) {
		Calendar dataAtual = Calendar.getInstance(SIGPRUtil.getLocale());

		List<HoraTarefa> horas = listarHorasDia(tarefa, colaborador);

		if (horas.isEmpty()) {
			HoraTarefa hora = new HoraTarefa();
			hora.setDataReferencia(dataAtual.getTime());
			hora.setInicio(dataAtual.getTime());
			hora.setFim(null);
			hora.setColaborador(colaborador);
			hora.setTarefa(tarefa);
			hora.setAnoReferencia(dataAtual.get(Calendar.YEAR));
			hora.setMesReferencia(dataAtual.get(Calendar.MONTH)+1);

			em.persist(hora);
			em.flush();
		} else {

			HoraTarefa hora = horas.get(0);

			if (hora.getInicio() != null && hora.getFim() == null) {
				hora.setFim(dataAtual.getTime());

				em.merge(hora);
				em.flush();
			} else if (hora.getInicio() != null && hora.getFim() != null) {
				hora = new HoraTarefa();
				hora.setDataReferencia(dataAtual.getTime());
				hora.setInicio(dataAtual.getTime());
				hora.setFim(null);
				hora.setColaborador(colaborador);
				hora.setTarefa(tarefa);
				hora.setAnoReferencia(dataAtual.get(Calendar.YEAR));
				hora.setMesReferencia(dataAtual.get(Calendar.MONTH)+1);

				em.persist(hora);
				em.flush();
			}

		}
	}

	@SuppressWarnings("unchecked")
	private List<HoraTarefa> listarHorasDia(Tarefa tarefa,
			Colaborador colaborador) {
		Calendar dataAtual = Calendar.getInstance(SIGPRUtil.getLocale());

		return em.createQuery(CONSULTA_HORAS_TAREFA_COLABORADOR_DIA)
				.setParameter("tarefa", tarefa)
				.setParameter("colaborador", colaborador)
				.setParameter("dataReferencia", dataAtual.getTime())
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<HoraTarefa> listarHoras(Tarefa tarefa) {
		return em.createQuery(CONSULTA_HORAS_TAREFA)
				.setParameter("tarefa", tarefa)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<HoraTarefa> listarHoras(Tarefa tarefa, Colaborador colaborador) {
		return em.createQuery(CONSULTA_HORAS_TAREFA_COLABORADOR)
				.setParameter("tarefa", tarefa)
				.setParameter("colaborador", colaborador)
				.getResultList();
	}

	public HoraTarefa consultarHoraIniciada(Tarefa tarefa, Colaborador colaborador) {
		Calendar dataAtual = Calendar.getInstance(SIGPRUtil.getLocale());
		try{
			
			return (HoraTarefa) em.createQuery(CONSULTA_HORA_TAREFA_INICIADA_TAREFA_COLABORADOR)
					.setParameter("tarefa", tarefa)
					.setParameter("colaborador", colaborador)
					.setParameter("dataReferencia", dataAtual.getTime())
					.getSingleResult();
		}
		catch(NoResultException nrex){
			return null;
		}
	}

	public boolean isTarefaIniciada(Colaborador colaborador) {
		Calendar dataAtual = Calendar.getInstance(SIGPRUtil.getLocale());
		HoraTarefa hora = null;
		try{
			hora = (HoraTarefa) em.createQuery(CONSULTA_HORA_TAREFA_INICIADA_COLABORADOR)
					.setParameter("colaborador", colaborador)
					.setParameter("dataReferencia", dataAtual.getTime())
					.getSingleResult();
			
		}
		catch(NoResultException nrex){
			hora = null;
		}
		
		if(hora == null){
			return false;
		}
		else{
			return true;
		}
	}
	
	public boolean isTarefaIniciada(Tarefa tarefa, Colaborador colaborador) {
		HoraTarefa hora = null;
		try{
			hora = consultarHoraIniciada(tarefa, colaborador);
		}
		catch(NoResultException nrex){
			hora = null;
		}
		
		if(hora == null){
			return false;
		}
		else{
			return true;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<HoraTarefa> consultar(String query, Object... params) throws SIGPRException {
		List<HoraTarefa> lista = null;
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
