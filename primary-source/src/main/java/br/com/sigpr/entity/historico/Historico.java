package br.com.sigpr.entity.historico;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.funcionalidade.Funcionalidade;
import br.com.sigpr.entity.projeto.Projeto;
import br.com.sigpr.entity.tarefa.Tarefa;
import br.com.sigpr.enumerations.TipoHistorico;

@Entity
@Table( name = "TB_HISTORICO", 
		schema = "SIGPR")

@SequenceGenerator( name = "GENERATOR_SEQ_HISTORICO", 
					sequenceName = "SEQ_HISTORICO", 
					schema = "SIGPR", 
					allocationSize = 1)
public class Historico implements Serializable {
	
	private static final long serialVersionUID = -6925862480888347969L;

	@Id
	@GeneratedValue(strategy = SEQUENCE, 
					generator = "GENERATOR_SEQ_HISTORICO")
	@Column(name = "HISTORICO_ID",
			nullable = false,
			updatable = false)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HISTORICO_DATA",
			nullable = false)
	private Date dataHistorico;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "HITORICO_TIPO_HISTORICO", 
			nullable = false,
			length = 15)
	private TipoHistorico tipoHistorico;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "HISTORICO_TAREFA_ID", 
			referencedColumnName = "TAREFA_ID")
	private Tarefa tarefa;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "HISTORICO_FUNCIONALIDADE_ID", 
			referencedColumnName = "FUNCIONALIDADE_ID")
	private Funcionalidade funcionalidade;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "HISTORICO_PROJETO_ID", 
			referencedColumnName = "PROJETO_ID")
	private Projeto projeto;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "HISTORICO_COLABORADOR_ID", 
			referencedColumnName = "COLABORADOR_ID")
	private Colaborador colaborador;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataHistorico() {
		return dataHistorico;
	}

	public void setDataHistorico(Date dataHistorico) {
		this.dataHistorico = dataHistorico;
	}

	public TipoHistorico getTipoHistorico() {
		return tipoHistorico;
	}

	public void setTipoHistorico(TipoHistorico tipoHistorico) {
		this.tipoHistorico = tipoHistorico;
	}

	public Tarefa getTarefa() {
		return tarefa;
	}

	public void setTarefa(Tarefa tarefa) {
		this.tarefa = tarefa;
	}

	public Funcionalidade getFuncionalidade() {
		return funcionalidade;
	}

	public void setFuncionalidade(Funcionalidade funcionalidade) {
		this.funcionalidade = funcionalidade;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

}
