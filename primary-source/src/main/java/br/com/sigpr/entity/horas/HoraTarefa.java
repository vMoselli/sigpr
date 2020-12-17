package br.com.sigpr.entity.horas;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.tarefa.Tarefa;
import br.com.sigpr.util.SIGPRUtil;

@Entity
@Table( name="TB_HORA_TAREFA", 
		schema = "SIGPR")

@SequenceGenerator( name = "GENERATOR_SEQ_HORA_TAREFA", 
					sequenceName = "SEQ_HORA_TAREFA", 
					schema = "SIGPR", 
					allocationSize = 1)
public class HoraTarefa implements Serializable {
	
	private static final long serialVersionUID = -3286340329038791507L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, 
					generator = "GENERATOR_SEQ_HORA_TAREFA")
	@Column(name = "HORA_TAREFA_ID",
			nullable = false,
			updatable = false)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "HORA_TAREFA_TAREFA_ID", 
				referencedColumnName = "TAREFA_ID")
	private Tarefa tarefa;
	
	@ManyToOne
	@JoinColumn(name = "HORA_TAREFA_COLABORADOR_ID", 
				referencedColumnName = "COLABORADOR_ID")
	private Colaborador colaborador;
	
	@Temporal(TemporalType.DATE)
	@Column(name="HORA_TAREFA_DATA_REFERENCIA",
			nullable = false)
	private Date dataReferencia;
	
	@Column(name="HORA_TAREFA_MES_REFERENCIA",
			nullable = false, precision = 2, scale = 0)
	private int mesReferencia;
	
	@Column(name="HORA_TAREFA_ANO_REFERENCIA",
			nullable = false, precision = 4, scale = 0)
	private int anoReferencia;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HORA_TAREFA_INICIO", 
			nullable = false)
	private Date inicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HORA_TAREFA_FIM", 
			nullable = true)
	private Date fim;
	
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

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	public Date getDataReferencia() {
		return dataReferencia;
	}

	public void setDataReferencia(Date dataReferencia) {
		this.dataReferencia = dataReferencia;
	}

	public int getMesReferencia() {
		return mesReferencia;
	}

	public void setMesReferencia(int mesReferencia) {
		this.mesReferencia = mesReferencia;
	}

	public int getAnoReferencia() {
		return anoReferencia;
	}

	public void setAnoReferencia(int anoReferencia) {
		this.anoReferencia = anoReferencia;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}
	
	public String getTotalHoras(){
		if(this.fim == null){
			return null;
		}
		else{
			return SIGPRUtil.calculaDiferenciaHoras(this.inicio, this.fim);
		}
	}

}
