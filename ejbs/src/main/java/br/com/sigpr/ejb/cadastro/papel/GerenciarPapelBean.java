package br.com.sigpr.ejb.cadastro.papel;

import java.util.List;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.sigpr.entity.papel.Papel;

@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GerenciarPapelBean implements GerenciarPapel {

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	public List<Papel> listar() {
		return em.createQuery("Select papel from Papel papel " +
					"where papel.ativo = true order by papel.nome asc")
						.getResultList();
	}

	public void incluir(Papel entity) {
		em.persist(entity);
		em.flush();
		return;
	}

	public void alterar(Papel entity) {
		em.merge(entity);
		em.flush();
		return;

	}

	public void excluir(Papel entity) {
		entity = em.merge(entity);
		entity.setAtivo(false);
		return;
	}

	public Papel findById(Object primaryKey) {
		return em.find(Papel.class, primaryKey);
	}

}
