package br.com.sigpr.ejb.cadastro.colaboradorprojeto;

import java.util.List;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.colaboradorprojeto.ColaboradorProjeto;
import br.com.sigpr.entity.papel.Papel;
import br.com.sigpr.entity.projeto.Projeto;

/**
 * Session Bean implementation class GerenciarColaboradorProjetoBean
 */
@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GerenciarColaboradorProjetoBean implements GerenciarColaboradorProjeto {

	@PersistenceContext
	private EntityManager em;
	
    public GerenciarColaboradorProjetoBean() {
    }

	@SuppressWarnings("unchecked")
	public List<ColaboradorProjeto> listar() {
		return em.createQuery("Select colaboradorProjeto from ColaboradorProjeto colaboradorProjeto " +
				"where colaboradorProjeto.ativo = true " +
				"order by colaboradorProjeto.projeto.nome asc, colaboradorProjeto.colaborador.nome asc").getResultList();
	}

	public void incluir(ColaboradorProjeto entity) {
		entity.setColaborador(em.find(Colaborador.class,entity.getColaborador().getId()));
		entity.setProjeto(em.find(Projeto.class, entity.getProjeto().getId()));
		entity.setPapel(em.find(Papel.class, entity.getPapel().getId()));
		
		em.persist(entity);
		em.flush();		
		return;
	}

	public void alterar(ColaboradorProjeto entity) {
		entity.setColaborador(em.find(Colaborador.class,entity.getColaborador().getId()));
		entity.setProjeto(em.find(Projeto.class, entity.getProjeto().getId()));
		entity.setPapel(em.find(Papel.class, entity.getPapel().getId()));
		
		em.merge(entity);
		em.flush();	
		return;
	}

	public void excluir(ColaboradorProjeto entity) {
		entity.setColaborador(em.find(Colaborador.class,entity.getColaborador().getId()));
		entity.setProjeto(em.find(Projeto.class, entity.getProjeto().getId()));
		entity.setPapel(em.find(Papel.class, entity.getPapel().getId()));
		
		entity = em.merge(entity);
		entity.setAtivo(false);
		em.flush();
		return;
	}

	public ColaboradorProjeto findById(Object primaryKey) {
		return em.find(ColaboradorProjeto.class, primaryKey);
	}

	@SuppressWarnings("unchecked")
	public List<ColaboradorProjeto> listarPorColaborador(Colaborador colaborador) {
		return em.createQuery("Select colaboradorProjeto from ColaboradorProjeto colaboradorProjeto " +
				"where colaboradorProjeto.ativo = true and colaboradorProjeto.colaborador.id = :colaborador " +
				"order by colaboradorProjeto.projeto.nome asc")
					.setParameter("colaborador", colaborador.getId())
					.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ColaboradorProjeto> listarPorProjeto(Projeto projeto) {
		return em.createQuery("Select colaboradorProjeto from ColaboradorProjeto colaboradorProjeto " +
				"where colaboradorProjeto.ativo = true and colaboradorProjeto.projeto.id = :projeto " +
				"order by colaboradorProjeto.projeto.nome asc")
					.setParameter("projeto", projeto.getId())
					.getResultList();
	}

}
