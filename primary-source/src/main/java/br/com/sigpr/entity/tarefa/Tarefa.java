package br.com.sigpr.entity.tarefa;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.funcionalidade.Funcionalidade;
import br.com.sigpr.entity.historico.Historico;
import br.com.sigpr.entity.horas.HoraTarefa;
import br.com.sigpr.entity.status.Status;
import br.com.sigpr.enumerations.TipoTarefa;

@Entity
@Table( name = "TB_TAREFA", 
		schema = "SIGPR")

@SequenceGenerator( name = "GENERATOR_SEQ_TAREFA", 
					sequenceName = "SEQ_TAREFA", 
					schema = "SIGPR", 
					allocationSize = 1)
public class Tarefa implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "TAREFA_ID", 
			nullable = false, 
			updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, 
					generator = "GENERATOR_SEQ_TAREFA")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "TAREFA_FUNCIONALIDADE_ID", 
				referencedColumnName = "FUNCIONALIDADE_ID")
	private Funcionalidade funcionalidade;
	
	@Column(name = "TAREFA_NOME",
			nullable = false,
			length = 255)
	private String nome;
	
	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "TAREFA_DESCRICAO")
	private String descricao;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "TAREFA_PRAZO", 
			nullable = false)
	private Date prazo;
	
	@Column(name = "TAREFA_ATIVO")
	private boolean ativo;
	
	@ManyToOne
	@JoinColumn(name = "TAREFA_STATUS_ID",
				referencedColumnName = "STATUS_ID")
	private Status status;
	
	@ManyToOne
	@JoinColumn(name = "TAREFA_COLABORADOR_ID",
				referencedColumnName = "COLABORADOR_ID")
	private Colaborador colaborador;
	
	@OneToMany(mappedBy = "tarefa")
	private List<Artefato> artefatos;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TAREFA_TIPO_TAREFA",
			nullable = false)
	private TipoTarefa tipoTarefa;
	
	@OneToMany(mappedBy = "tarefa")
	private List<HoraTarefa> horas;
	
	@OneToMany(mappedBy = "tarefa")
	private List<Historico> historicos;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Funcionalidade getFuncionalidade() {
		return funcionalidade;
	}

	public void setFuncionalidade(Funcionalidade funcionalidade) {
		this.funcionalidade = funcionalidade;
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

	public List<Artefato> getArtefatos() {
		return artefatos;
	}

	public void setArtefatos(List<Artefato> artefatos) {
		this.artefatos = artefatos;
	}

	public TipoTarefa getTipoTarefa() {
		return tipoTarefa;
	}

	public void setTipoTarefa(TipoTarefa tipoTarefa) {
		this.tipoTarefa = tipoTarefa;
	}

	public List<HoraTarefa> getHoras() {
		return horas;
	}

	public void setHoras(List<HoraTarefa> horas) {
		this.horas = horas;
	}

	public List<Historico> getHistoricos() {
		return historicos;
	}

	public void setHistoricos(List<Historico> historicos) {
		this.historicos = historicos;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}
	
	public boolean isTarefaEmAtraso(){
		if(!status.getNome().equals("Finalizada")){
			return (Calendar.getInstance().getTime().after(getPrazo()));
		}
		else{
			return false;
		}
	}

}
