package br.com.sigpr.ejb.cadastro.unidade;

import java.util.List;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.sigpr.entity.unidade.Unidade;

/**
 * Session Bean implementation class UnidadeBean
 */
@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GerenciarUnidadeBean implements GerenciarUnidade {
	
	@PersistenceContext
	private EntityManager em;

    public GerenciarUnidadeBean() {
    }

	@SuppressWarnings("unchecked")
	public List<Unidade> listar() {
		return em.createQuery("Select unidade from Unidade unidade where unidade.ativo = true order by unidade.nome asc")
			.getResultList();
	}

	public void incluir(Unidade unidade) {
		em.persist(unidade);
		em.flush();
	}

	public void alterar(Unidade unidade) {
		em.merge(unidade);
		em.flush();
	}

	public void excluir(Unidade unidade) {
		unidade = em.merge(unidade);
		unidade.setAtivo(false);
		em.flush();
	}

	public Unidade findById(Object primaryKey) {
		return em.find(Unidade.class, primaryKey);
	}

	@SuppressWarnings("unchecked")
	public List<Unidade> listarUnidades(String query) {
		return em.createQuery(query).getResultList();
	}

}
