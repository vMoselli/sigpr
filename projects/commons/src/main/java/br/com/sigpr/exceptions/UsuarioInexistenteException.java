package br.com.sigpr.exceptions;

public class UsuarioInexistenteException extends SIGPRException {

	private static final long serialVersionUID = 2772363643561419762L;

	public UsuarioInexistenteException(String funcionalidade, String mensagem, Exception exDefault) {
		super(funcionalidade, mensagem, exDefault);
	}

}
