package br.com.sigpr.ejb.login;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import br.com.sigpr.entity.perfil.Perfil;
import br.com.sigpr.entity.usuario.Usuario;
import br.com.sigpr.exceptions.LoginException;
import br.com.sigpr.exceptions.UsuarioInexistenteException;

@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ControleLoginBean implements ControleLogin {

	@PersistenceContext
	private EntityManager em;

	private static final String CONSULTA_USUARIO = "Select usuario from Usuario usuario where usuario.login = :username";
	private static final String VALIDA_LOGIN = "Select usuario from Usuario usuario where usuario.login = :username and usuario.senha = :password";
	private static final String CONSULTA_PERFIL = "Select usuario.colaborador.perfil from Usuario usuario where usuario.id = :idUsuario";

	public Usuario login(String username, String password) throws UsuarioInexistenteException,
			LoginException {
		
		Usuario user = null;
		
			// Verificar se existe o usuário passado para login
			try{
				user = (Usuario) em.createQuery(CONSULTA_USUARIO).setParameter("username", username).getSingleResult();
			}
			catch (NoResultException noResult) {
				throw new UsuarioInexistenteException("Login", "Usuário Inexistente", noResult);
			}
			
			if(user != null){
				try {
					user = (Usuario) em.createQuery(VALIDA_LOGIN).setParameter("username", username).setParameter("password", password).getSingleResult();
				} catch (NoResultException noResult) {
					throw new LoginException("Login", "Senha inválida para o usuário informado.", noResult);
				}
			}
			
		return user;
	}

	public Perfil getPerfil(Usuario usuario) {
		Perfil perfil = null;
		try{
			perfil = (Perfil) em.createQuery(CONSULTA_PERFIL).setParameter("idUsuario", usuario.getId()).getSingleResult();
		}catch (NoResultException e) {
			perfil = null;
		}
		return perfil;
	}

}
