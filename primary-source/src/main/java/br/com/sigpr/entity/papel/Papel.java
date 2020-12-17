package br.com.sigpr.entity.papel;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

import br.com.sigpr.entity.colaboradorprojeto.ColaboradorProjeto;

/**
 * Entity implementation class for Entity: Papel
 * 
 */
@Entity
@Table( name = "TB_PAPEL",
		schema = "SIGPR")
@SequenceGenerator( name = "GENERATOR_SEQ_PAPEL", 
					sequenceName = "SEQ_PAPEL", 
					schema = "SIGPR", 
					allocationSize = 1)
public class Papel implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "PAPEL_ID", 
			nullable = false, 
			updatable = false)
	@GeneratedValue(strategy = SEQUENCE, 
					generator = "GENERATOR_SEQ_PAPEL")
	private Long id;

	@Column(name = "PAPEL_NOME", 
			nullable = false,
			unique = true,
			length = 255)
	private String nome;

	@Lob
	@Column(name = "PAPEL_DESCRICAO", 
			nullable = false)
	private String descricao;
	
	@Column(name = "PAPEL_ATIVO")
	private boolean ativo;
	
	@OneToMany(mappedBy = "papel")
	private Set<ColaboradorProjeto> colaboradorProjetos;

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

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Set<ColaboradorProjeto> getColaboradorProjetos() {
		return colaboradorProjetos;
	}

	public void setColaboradorProjetos(
			Set<ColaboradorProjeto> colaboradorProjetos) {
		this.colaboradorProjetos = colaboradorProjetos;
	}

}
