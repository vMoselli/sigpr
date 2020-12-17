package br.com.sigpr.entity.perfil;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.sigpr.entity.colaborador.Colaborador;

@Entity
@Table( name = "TB_PERFIL",
		schema = "SIGPR")
@SequenceGenerator( name = "GENERATOR_SEQ_PERFIL", 
					sequenceName = "SEQ_PERFIL", 
					schema = "SIGPR", 
					allocationSize = 1)
public class Perfil implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PERFIL_ID", 
			nullable = false, 
			updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, 
					generator = "GENERATOR_SEQ_PERFIL")
	private Long id;
	
	@Column(name = "PERFIL_NOME", 
			nullable = false, 
			unique = true,
			length = 255)
	private String nome;
	
	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "PERFIL_DESCRICAO",
			nullable = false)
	private String descricao;
	
	@OneToMany(mappedBy = "perfil")
	private List<Colaborador> colaboradores;

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

	public List<Colaborador> getColaboradores() {
		return colaboradores;
	}

	public void setColaboradores(List<Colaborador> colaboradores) {
		this.colaboradores = colaboradores;
	}

}
