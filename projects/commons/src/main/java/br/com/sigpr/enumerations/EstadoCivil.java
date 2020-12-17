package br.com.sigpr.enumerations;

/**
 * Enum referente ao estado civil. 
 * 
 * @author Probank
 * @since 07/01/2011
 * @version 1
 */
public enum EstadoCivil {
	
	SOLTEIRO("Solteiro", 1),
	CASADO("Casado", 2),
	SEPARADO("Separado", 3),
	DESQUITADO("Desquitado", 4),
	DIVORCIADO("Divorciado", 5),
	VIUVO("Viúvo", 6),
	UNIAOESTAVEL("União Estável", 7),
	OUTROS("Outros", 8);
	
	final private String estadoCivil;
	final int codigo;
	
	private EstadoCivil(final String estadoCivil, final int codigo){
		this.estadoCivil = estadoCivil;
		this.codigo = codigo;
	}
	
	public String getEstadoCivil(){
		return estadoCivil;
	}
	
	public int getCodigo(){
		return codigo;
	}

	public static EstadoCivil getById(int id) {
		for (EstadoCivil compleicao : EstadoCivil.values()) {
			if (compleicao.getCodigo() == id) {
				return compleicao;
			}
		}

		return null;
	}
}
