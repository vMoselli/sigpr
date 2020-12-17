package br.com.sigpr.entity.funcionalidade;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.sigpr.entity.historico.Historico;
import br.com.sigpr.entity.projeto.Projeto;
import br.com.sigpr.entity.status.Status;
import br.com.sigpr.entity.tarefa.Tarefa;

@Entity
@Table( name = "TB_FUNCIONALIDADE", 
		schema = "SIGPR")

@SequenceGenerator( name = "GENERATOR_SEQ_FUNCIONALIDADE", 
					sequenceName = "SEQ_FUNCIONALIDADE", 
					schema = "SIGPR", 
					allocationSize = 1)
public class Funcionalidade implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = SEQUENCE, 
					generator = "GENERATOR_SEQ_FUNCIONALIDADE")
	@Column(name = "FUNCIONALIDADE_ID",
			nullable = false,
			updatable = false)
	private Long id;
	
	@Column(name = "FUNCIONALIDADE_NOME",
			length = 255,
			nullable = false)
	private String nome;
	
	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "FUNCIONALIDADE_DESCRICAO")
	private String descricao;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FUNCIONALIDADE_PRAZO", 
			nullable = false)
	private Date prazo;
	
	@Column(name = "FUNCIONALIDADE_ATIVO")
	private boolean ativo;
	
	@ManyToOne
	@JoinColumn(name = "FUNCIONALIDADE_PROJETO_ID", 
				referencedColumnName = "PROJETO_ID")
	private Projeto projeto;
	
	@ManyToOne
	@JoinColumn(name = "FUNCIONALIDADE_STATUS_ID", 
				referencedColumnName = "STATUS_ID")
	private Status status;
	
	@OneToMany(mappedBy = "funcionalidade")
	private List<Tarefa> tarefas;
	
	@OneToMany(mappedBy = "funcionalidade")
	private List<Historico> historicos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
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

	public Date getPrazo() {
		return prazo;
	}

	public void setPrazo(Date prazo) {
		this.prazo = prazo;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setTarefas(List<Tarefa> tarefas) {
		this.tarefas = tarefas;
	}

	public List<Tarefa> getTarefas() {
		return tarefas;
	}

	public void setHistoricos(List<Historico> historicos) {
		this.historicos = historicos;
	}

	public List<Historico> getHistoricos() {
		return historicos;
	}
	
	

}
