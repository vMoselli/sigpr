package br.com.sigpr.entity.unidade;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.endereco.Endereco;

@Entity
@Table( name = "TB_UNIDADE",
		schema = "SIGPR")

@SequenceGenerator( name = "GENERATOR_SEQ_UNIDADE", 
					sequenceName="SEQ_UNIDADE", 
					schema = "SIGPR",
					allocationSize = 1)
public class Unidade implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "UNIDADE_ID", 
			updatable = false,
			nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERATOR_SEQ_UNIDADE")
	private Long id;
	
	@Column(name = "UNIDADE_NOME",
			length = 255,
			nullable = false,
			unique = true)
	private String nome;
	
	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "UNIDADE_DESCRICAO")
	private String descricao;
	
	@Column(name = "UNIDADE_TELEFONE",
			length = 14)
	private String telefone;
	
	@Column(name = "UNIDADE_ATIVO")
	private boolean ativo;
	
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "UNIDADE_ENDERECO_ID",
				referencedColumnName = "ENDERECO_ID")
	private Endereco endereco;
	
	@OneToMany(mappedBy = "unidade")
	private Set<Colaborador> colaboradores;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Set<Colaborador> getColaboradores() {
		return colaboradores;
	}

	public void setColaboradores(Set<Colaborador> colaboradores) {
		this.colaboradores = colaboradores;
	}

}
