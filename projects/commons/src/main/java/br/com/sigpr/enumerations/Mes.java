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
	MARCO("Março", "Mar", 3),
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
	 * @param nome - Nome do Mês.
	 * @param sigla - Sigla do Mês.
	 * @param numero - Número do Mês.
	 */
	private Mes(final String nome, final String  sigla, final int numero){
		this.nome = nome;
		this.sigla = sigla;
		this.numero = numero;
	}

	/**
	 * Retorna o nome do mês.
	 * 
	 * @return String nome.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Retorna a sigla do mês.
	 * 
	 * @return String sigla.
	 */
	public String getSigla() {
		return sigla;
	}

	/**
	 * Retorna o número do mês.
	 * 
	 * @return int numero.
	 */
	public int getNumero() {
		return numero;
	}


}
