package br.com.sigpr.entity.colaboradorprojeto;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.papel.Papel;
import br.com.sigpr.entity.projeto.Projeto;

@Entity
@Table( name = "TB_COLABORADOR_PROJETO",
		schema = "SIGPR")

@SequenceGenerator( name = "GENERATOR_SEQ_COLABORADOR_PROJETO", 
					sequenceName = "SEQ_COLABORADOR_PROJETO", 
					schema = "SIGPR", 
					allocationSize = 1)
public class ColaboradorProjeto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = SEQUENCE, 
					generator = "GENERATOR_SEQ_COLABORADOR_PROJETO")
	@Column(name = "COLAB_PROJ_ID",
			nullable = false,
			updatable = false)
	private Long id;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "COLAB_PROJ_COLAB_ID",
				referencedColumnName = "COLABORADOR_ID")
	private Colaborador colaborador;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "COLAB_PROJ_PROJ_ID",
				referencedColumnName = "PROJETO_ID")
	private Projeto projeto;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "COLAB_PROJ_PAPEL_ID",
				referencedColumnName = "PAPEL_ID")
	private Papel papel;
	
	@Column(name = "COLAB_PROJ_ATIVO")
	private boolean ativo;

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public Papel getPapel() {
		return papel;
	}

	public void setPapel(Papel papel) {
		this.papel = papel;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

}
