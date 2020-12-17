package br.com.sigpr.exceptions;

public class TarefaIniciadaException extends SIGPRException {

	private static final long serialVersionUID = 4964691448380962349L;

	public TarefaIniciadaException(String funcionalidade, String mensagem, Exception exDefault) {
		super(funcionalidade, mensagem, exDefault);
	}

}
