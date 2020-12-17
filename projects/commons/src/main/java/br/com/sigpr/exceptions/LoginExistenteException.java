package br.com.sigpr.exceptions;

public class LoginExistenteException extends SIGPRException {

	private static final long serialVersionUID = -4687603700253118705L;
	
	public LoginExistenteException(String funcionalidade, String mensagem,
			Exception exDefault) {
		super(funcionalidade, mensagem, exDefault);
	}

}
