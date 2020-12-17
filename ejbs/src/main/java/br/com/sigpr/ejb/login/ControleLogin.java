package br.com.sigpr.ejb.login;

import br.com.sigpr.entity.perfil.Perfil;
import br.com.sigpr.entity.usuario.Usuario;
import br.com.sigpr.exceptions.LoginException;
import br.com.sigpr.exceptions.UsuarioInexistenteException;

public interface ControleLogin {

	Usuario login(String username, String password) throws UsuarioInexistenteException, LoginException;

	Perfil getPerfil(Usuario usuario);

}
