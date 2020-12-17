package br.com.sigpr.enumerations;

/**
 * Enum referente aos meses do ano. 
 * 
 * @author vinicius.moselli
 * @since 26/01/2011
 * @version 1
 */
public enum Mes {
	
	JANEIRO("Janeiro", "Jan", 1),
	FEVEREIRO("Fevereiro", "Fev", 2),
	MARCO("Mar�o", "Mar", 3),
	ABRIL("Abril", "Abr", 4),
	MAIO("Maio", "Mai", 5),
	JUNHO("Junho", "Jun", 6),
	JULHO("Julho", "Jal", 7),
	AGOSTO("Agosto", "Ago", 8),
	SETEMBRO("Setembro", "Set", 9),
	OUTUBRO("Outubro", "Out", 10),
	NOVEMBRO("Novembro", "Nov", 11),
	DEZEMBRO("Dezembro", "Dez", 12);
	
	private String nome;
	private String sigla;
	private int numero;
	
	/**
	 * Construtor privado.
	 * 
	 * @param nome - Nome do M�s.
	 * @param sigla - Sigla do M�s.
	 * @param numero - N�mero do M�s.
	 */
	private Mes(final String nome, final String  sigla, final int numero){
		this.nome = nome;
		this.sigla = sigla;
		this.numero = numero;
	}

	/**
	 * Retorna o nome do m�s.
	 * 
	 * @return String nome.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Retorna a sigla do m�s.
	 * 
	 * @return String sigla.
	 */
	public String getSigla() {
		return sigla;
	}

	/**
	 * Retorna o n�mero do m�s.
	 * 
	 * @return int numero.
	 */
	public int getNumero() {
		return numero;
	}


}
