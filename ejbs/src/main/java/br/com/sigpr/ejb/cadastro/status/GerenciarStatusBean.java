package br.com.sigpr.ejb.cadastro.status;

import java.util.List;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.status.Status;
import br.com.sigpr.enumerations.TipoStatus;


/**
 * Session Bean implementation class CadastroStatusBean
 */
@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GerenciarStatusBean implements GerenciarStatus {

	@PersistenceContext
	private EntityManager em;
	
	public GerenciarStatusBean() {
	}

	@SuppressWarnings("unchecked")
	public List<Status> listar() {
		return (List<Status>) em.createQuery("Select status from Status status order by status.nome asc").getResultList();
	}
	
	public void incluir(Status status){
		em.persist(status);
		em.flush();
	}
	
	public void alterar(Status status){
		em.merge(status);
		em.flush();
	}
	
	public void excluir(Status status){
		status = em.merge(status);
		status.setAtivo(false);
		em.flush();
	}
	
	public Status findById(Object primaryKey){
		return em.find(Status.class, primaryKey);
	}

	@SuppressWarnings("unchecked")
	public List<Status> listarPorTipo(TipoStatus tipoStatus) {
		return (List<Status>) em.createQuery("Select status from Status status " +
				"where status.ativo = true " +
				"and status.tipoStatus = :tipoStatus " +
				"order by status.nome asc")
			.setParameter("tipoStatus", tipoStatus)
			.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Status> listarTarefasPorColaborador(Colaborador colaborador) {
		return (List<Status>) em.createQuery("Select s from Status s left outer join s.tarefas t " +
				"where t.colaborador = :colaborador " +
				"and s.ativo = true " +
				"and t.ativo = true " +
				"and s.tipoStatus = :tipoStatus " +
				"order by s.nome asc, t.prazo asc, t.funcionalidade.projeto.nome asc, t.funcionalidade.nome asc, t.nome asc")
			.setParameter("colaborador", colaborador)
			.setParameter("tipoStatus", TipoStatus.Tarefa)
			.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Status> listarTarefas() {
		return (List<Status>) em.createQuery("Select s from Status s left outer join s.tarefas t " +
				"where s.ativo = true " +
				"and s.tipoStatus = :tipoStatus " +
				"and t.ativo = true " +
				"order by s.nome asc, t.prazo asc, t.funcionalidade.projeto.nome asc, t.funcionalidade.nome asc, t.nome asc")
			.setParameter("tipoStatus", TipoStatus.Tarefa)
			.getResultList();
	}

	public Status findByNameAndTipoStatus(String nomeStatus, TipoStatus tipoStatus) {
		return (Status) em.createQuery("Select status from Status status " +
				"where status.ativo = true " +
				"and status.tipoStatus = :tipoStatus " +
				"and status.nome = :nomeStatus")
			.setParameter("tipoStatus", tipoStatus)
			.setParameter("nomeStatus", nomeStatus)
			.getSingleResult();
	}
	

}
