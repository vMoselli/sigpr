package br.com.sigpr.entity.demanda;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.enumerations.Prioridade;
import br.com.sigpr.enumerations.StatusSolicitacao;


/**
 * Entity implementation class for Entity: Solicitacao
 *
 */
@Entity
@Table( name = "TB_SOLICITACAO_DEMANDA",
		schema = "SIGPR")
@SequenceGenerator(allocationSize = 1, 
		schema = "SIGPR", 
		name = "GENERATOR_SEQ_SOLICITACAO", 
		sequenceName = "SEQ_SOLICITACAO")
public class Solicitacao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "GENERATOR_SEQ_SOLICITACAO", 
			strategy = GenerationType.SEQUENCE)

	@Column(name = "SOLICITACAO_ID", 
			nullable = false, 
			updatable = false)
	private Long id;

	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "SOLICITACAO_DESCRICAO")
	private String descricao;

	@Enumerated(EnumType.STRING)
	@Column(name = "SOLICITACAO_PRIORIDADE", 
			nullable = false)
	private Prioridade prioridade;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "SOLICITACAO_STATUS", 
			nullable = false)
	private StatusSolicitacao status;

	@Temporal(TemporalType.DATE)
	@Column(name = "SOLICITACAO_DATA_SOLICITACAO", 
			nullable = false)
	private Date dataSolicitacao;

	@Temporal(TemporalType.DATE)
	@Column(name = "SOLICITACAO_DATA_ATENDIMENTO", 
			nullable = true)
	private Date dataAtendimento;

	@ManyToOne
	@JoinColumn(name = "SOLICITACAO_COLABORADOR_ID",
			referencedColumnName = "COLABORADOR_ID")
	private Colaborador colaborador;
	
	public Solicitacao(){
		super();
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}   
	
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}   
	
	public Prioridade getPrioridade() {
		return this.prioridade;
	}

	public void setPrioridade(Prioridade prioridade) {
		this.prioridade = prioridade;
	}   
	
	public StatusSolicitacao getStatus() {
		return status;
	}

	public void setStatus(StatusSolicitacao status) {
		this.status = status;
	}

	public Date getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public Date getDataAtendimento() {
		return dataAtendimento;
	}

	public void setDataAtendimento(Date dataAtendimento) {
		this.dataAtendimento = dataAtendimento;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

}
