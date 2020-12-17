/**
 * 
 */
package br.com.sigpr.ejb.cadastro.projeto;

import java.util.List;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.projeto.Projeto;
import br.com.sigpr.exceptions.SIGPRException;

/**
 * @author Thethis
 *
 */
@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GerenciarProjetoBean implements GerenciarProjeto {
	
	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	public List<Projeto> listar() {
		return em.createQuery("Select projeto from Projeto projeto where projeto.ativo = true " +
					"order by projeto.nome asc")
						.getResultList();
	}

	public void incluir(Projeto entity) {
		entity.setAtivo(true);
		em.persist(entity);
		em.flush();
	}

	public void alterar(Projeto entity) {
		em.merge(entity);
		em.flush();
	}

	public void excluir(Projeto entity) {
		entity = em.merge(entity);
		entity.setAtivo(false);
		em.flush();
	}

	public Projeto findById(Object primaryKey) {
		return em.find(Projeto.class, primaryKey);
	}

	@SuppressWarnings("unchecked")
	public List<Projeto> listarProjetosNaoAssociados(Colaborador colaborador) {
		return em.createQuery("Select projeto from Projeto projeto where projeto.ativo = true and " +
					"projeto.id not in " +
					"(Select colaboradorProjeto.projeto.id from ColaboradorProjeto colaboradorProjeto where colaboradorProjeto.colaborador.id = :colaborador) " +
					"order by projeto.nome asc")
						.setParameter("colaborador", colaborador.getId())
						.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Projeto> consultar(String query, Object... params) throws SIGPRException {
		List<Projeto> lista = null;
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
	
	public Projeto findByName(String nome){
		Projeto projeto = null;
		try{
			projeto = (Projeto) em.createQuery("Select projeto from Projeto projeto where projeto.nome = :nomeProjeto")
				.setParameter("nomeProjeto", nome)
				.getSingleResult();
		}
		catch(NoResultException ex){
			projeto = null;
		}
		
		return projeto;
	}

}
