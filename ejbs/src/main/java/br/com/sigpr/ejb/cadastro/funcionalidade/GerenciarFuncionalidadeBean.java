/**
 * 
 */
package br.com.sigpr.ejb.cadastro.funcionalidade;

import java.util.List;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.sigpr.entity.funcionalidade.Funcionalidade;
import br.com.sigpr.entity.projeto.Projeto;
import br.com.sigpr.exceptions.SIGPRException;

/**
 * @author Thethis
 *
 */
@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GerenciarFuncionalidadeBean implements GerenciarFuncionalidade {
	
	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	public List<Funcionalidade> listar() {
		return em.createQuery("Select funcionalidade from Funcionalidade funcionalidade where funcionalidade.ativo = true order by funcionalidade.nome asc").getResultList();
	}

	public void incluir(Funcionalidade entity) {
		entity.setAtivo(true);
		em.persist(entity);
		em.flush();
	}

	public void alterar(Funcionalidade entity) {
		em.merge(entity);
		em.flush();
	}

	public void excluir(Funcionalidade entity) {
		entity = em.merge(entity);
		entity.setAtivo(false);
		em.flush();
	}

	public Funcionalidade findById(Object primaryKey) {
		return em.find(Funcionalidade.class, primaryKey);
	}

	@SuppressWarnings("unchecked")
	public List<Funcionalidade> consultarFuncionalidadePorProjeto(Projeto projeto) throws SIGPRException {
		return em.createQuery("Select funcionalidade from Funcionalidade funcionalidade where funcionalidade.projeto.id = :projeto order by funcionalidade.nome asc")
			.setParameter("projeto", projeto.getId())
			.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Funcionalidade> consultar(String query, Object... params) throws SIGPRException {
		List<Funcionalidade> lista = null;
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
	
	public Funcionalidade findByName(String nome){
		Funcionalidade funcionalidade = null;
		try{
			funcionalidade = (Funcionalidade) em.createQuery("Select funcionalidade from Funcionalidade funcionalidade where funcionalidade.nome = :nomeFuncionalidade")
				.setParameter("nomeFuncionalidade", nome)
				.getSingleResult();
		}
		catch(NoResultException ex){
			funcionalidade = null;
		}
		
		return funcionalidade;
	}

}
