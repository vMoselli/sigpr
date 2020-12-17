/**
 * 
 */
package br.com.sigpr.ejb.cadastro.tarefa;

import java.util.List;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.sigpr.entity.funcionalidade.Funcionalidade;
import br.com.sigpr.entity.tarefa.Tarefa;
import br.com.sigpr.exceptions.SIGPRException;

/**
 * @author Thethis
 *
 */
@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GerenciarTarefaBean implements GerenciarTarefa {
	
	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	public List<Tarefa> listar() {
		return em.createQuery("Select tarefa from Tarefa tarefa where tarefa.ativo = true order by tarefa.nome asc").getResultList();
	}

	public void incluir(Tarefa entity) {
		entity.setAtivo(true);
		em.persist(entity);
		em.flush();
	}

	public void alterar(Tarefa entity) {
		em.merge(entity);
		em.flush();
	}

	public void excluir(Tarefa entity) {
		entity = em.merge(entity);
		entity.setAtivo(false);
		em.flush();
	}

	public Tarefa findById(Object primaryKey) {
		return em.find(Tarefa.class, primaryKey);
	}

	@SuppressWarnings("unchecked")
	public List<Tarefa> consultarTarefaPorFuncionalidade(Funcionalidade funcionalidade) {
		return em.createQuery("Select tarefa from Tarefa tarefa where tarefa.ativo = true and tarefa.funcionalidade.id = :funcionalidade order by tarefa.nome asc")
			.setParameter("funcionalidade", funcionalidade.getId())
			.getResultList();
	}
	
	/**
	 * Realiza a atribuição da tarefa ao Colaborador Selecionado.
	 * 
	 * @param Tarefa - tarefa
	 */
	public void atribuirTarefa(Tarefa tarefa){
		em.merge(tarefa);
		em.flush();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Tarefa> consultar(String query, Object... params) throws SIGPRException {
		List<Tarefa> lista = null;
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
	
	public Tarefa findByName(String nome){
		Tarefa tarefa = null;
		try{
			tarefa = (Tarefa) em.createQuery("Select tarefa from Tarefa tarefa where tarefa.nome = :nomeTarefa")
				.setParameter("nomeTarefa", nome)
				.getSingleResult();
		}
		catch(NoResultException ex){
			tarefa = null;
		}
		
		return tarefa;
	}

}
