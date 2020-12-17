package br.com.sigpr.ejb.cadastro.perfil;

import java.util.List;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.sigpr.entity.perfil.Perfil;


/**
 * Session Bean implementation class CadastroPerfilBean
 */
@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GerenciarPerfilBean implements GerenciarPerfil {

	@PersistenceContext
	private EntityManager em;
	
	public GerenciarPerfilBean() {
	}

	@SuppressWarnings("unchecked")
	public List<Perfil> listar() {
		return em.createQuery("Select perfil from Perfil as perfil order by perfil.nome asc").getResultList();
	}
	
	public void incluir(Perfil perfil){
		em.persist(perfil);
		em.flush();
	}
	
	public void alterar(Perfil perfil){
		em.merge(perfil);
		em.flush();
	}
	
	public void excluir(Perfil perfil){
		em.remove(em.merge(perfil));
		em.flush();
	}
	
	public Perfil findById(Object primaryKey){
		return em.find(Perfil.class, primaryKey);
	}

}
