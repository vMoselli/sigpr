package br.com.sigpr.ejb.cadastro.colaborador;

import java.util.List;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.usuario.Usuario;
import br.com.sigpr.exceptions.LoginExistenteException;
import br.com.sigpr.exceptions.SIGPRException;

/**
 * Session Bean implementation class GerenciarColaboradorBean
 */
@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GerenciarColaboradorBean implements GerenciarColaborador {

	@PersistenceContext
	private EntityManager em;

	public GerenciarColaboradorBean() {
	}

	@SuppressWarnings("unchecked")
	public List<Colaborador> listar() {
		List<Colaborador> lista = em.createQuery("Select colaborador from Colaborador colaborador where colaborador.ativo = true order by colaborador.nome asc").getResultList();
		return lista;
	}

	public void incluir(Colaborador entity) throws SIGPRException {

		Usuario user = this.findByName(entity.getUsuario().getLogin().trim());

		if(user != null){
			throw new LoginExistenteException("Colaborador", "Login existente.", null);
		}

		em.persist(entity);
		em.flush();
	}

	public void alterar(Colaborador entity) throws SIGPRException {
		Usuario user = this.findByName(entity.getUsuario().getLogin().trim());

		if(user != null && !user.equals(entity.getUsuario())){
			throw new LoginExistenteException("Colaborador", "Login existente.", null);
		}

		em.merge(entity);
		em.flush();	
	}

	public void excluir(Colaborador entity) {

		entity = em.merge(entity);
		entity.setAtivo(false);
		em.flush();
	}

	public Colaborador findById(Object primaryKey) {
		Colaborador colaborador = em.find(Colaborador.class, primaryKey);
		return colaborador;
	}

	private Usuario findByName(String nomeUsuario) throws LoginExistenteException{
		Usuario user = null;
		try{
			user = (Usuario) em.createQuery("Select usuario from Usuario usuario where usuario.login = :userName").setParameter("userName", nomeUsuario).getSingleResult();
		}catch (NoResultException e) {
			// Usuario inexistente.
			user = null;
		}
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Colaborador> consultar(String query, Object... params) throws SIGPRException {
		List<Colaborador> lista = null;
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
