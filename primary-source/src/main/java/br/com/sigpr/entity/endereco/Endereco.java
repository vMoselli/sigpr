package br.com.sigpr.entity.endereco;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.sigpr.entity.unidade.Unidade;
import br.com.sigpr.enumerations.Estado;

@Entity
@Table( name = "TB_ENDERECO", 
		schema = "SIGPR")

@SequenceGenerator( name = "GENERATOR_SEQ_ENDERECO", 
					sequenceName = "SEQ_ENDERECO", 
					schema = "SIGPR", 
					allocationSize = 1)
public class Endereco implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, 
					generator = "GENERATOR_SEQ_ENDERECO")
	@Column(name = "ENDERECO_ID",
			updatable = false)
	private Long id;
	
	@Column(name = "ENDERECO_LOGRADOURO",
			length = 255,
			nullable = false)
	private String logradouro;
	
	@Column(name = "ENDERECO_NUMERO",
			nullable = false)
	private Long numero;
	
	@Column(name = "ENDERECO_COMPLEMENTO",
			length = 100)
	private String complemento;
	
	@Column(name = "ENDERECO_BAIRRO",
			length = 100,
			nullable = false)
	private String bairro;
	
	@Column(name = "ENDERECO_CIDADE",
			length = 255,
			nullable = false)
	private String cidade;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ENDERECO_ESTADO",
			length = 2)
	private Estado estado;
	
	@Column(name = "ENDERECO_CEP",
			length = 9)
	private String cep;
	
	@OneToOne(mappedBy = "endereco", optional = false)
	private Unidade unidade;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

}
