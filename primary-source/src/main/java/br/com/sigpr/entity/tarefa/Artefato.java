package br.com.sigpr.entity.tarefa;

import java.io.Serializable;
import java.util.List;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table( name = "TB_ARTEFATO", 
		schema = "SIGPR")

@SequenceGenerator( name = "GENERATOR_SEQ_ARTEFATO", 
					sequenceName = "SEQ_ARTEFATO", 
					schema = "SIGPR", 
					allocationSize = 1)
public class Artefato implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ARTEFATO_ID",
			nullable = false,
			updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, 
					generator = "GENERATOR_SEQ_ARTEFATO")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "ARTEFATO_TAREFA_ID", 
				referencedColumnName = "TAREFA_ID")
	private Tarefa tarefa;
	
	@OneToMany(mappedBy = "artefato", cascade = CascadeType.ALL)
	private List<Arquivo> arquivos;
	
	@Column(name = "ARTEFATO_NOME", 
			nullable = false,
			length = 255)
	private String nome;
	
	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "ARTEFATO_DESCRICAO")
	private String descricao;
	
	@Column(name = "ARTEFATO_ATIVO")
	private boolean ativo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Tarefa getTarefa() {
		return tarefa;
	}

	public void setTarefa(Tarefa tarefa) {
		this.tarefa = tarefa;
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

	public List<Arquivo> getArquivos() {
		return arquivos;
	}

	public void setArquivos(List<Arquivo> arquivos) {
		this.arquivos = arquivos;
	}

}
