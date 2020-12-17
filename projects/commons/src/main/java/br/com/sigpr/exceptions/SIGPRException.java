package br.com.sigpr.exceptions;

public class SIGPRException extends Exception {
	
	private static final long serialVersionUID = 1286915628579186608L;
	
	private transient final String funcionalidade;
	private transient final String mensagem;
	private transient final Exception defaultException;
	
	public SIGPRException(final String funcionalidade, final String mensagem, final Exception exDefault){
		super(mensagem, exDefault);
		this.funcionalidade = funcionalidade;
		this.mensagem = mensagem;
		this.defaultException = exDefault;
		
	}
	
	public String getFuncionalidade() {
		return funcionalidade;
	}
	public String getMensagem() {
		return mensagem;
	}
	public Exception getDefaultException() {
		return defaultException;
	}

}
