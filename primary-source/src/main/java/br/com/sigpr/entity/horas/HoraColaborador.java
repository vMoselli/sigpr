package br.com.sigpr.entity.horas;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import br.com.sigpr.util.SIGPRUtil;

@Entity
@Table( name="TB_HORA_COLABORADOR", 
		schema = "SIGPR")

@SequenceGenerator( name = "GENERATOR_SEQ_HORA_COLABORADOR", 
					sequenceName = "SEQ_HORA_COLABORADOR", 
					schema = "SIGPR", 
					allocationSize = 1)
public class HoraColaborador implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "GENERATOR_SEQ_HORA_COLABORADOR")
	@Column(name="HR_COLAB_ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "HR_COLAB_COLAB_ID", 
				referencedColumnName = "COLABORADOR_ID")
	private Colaborador colaborador;
	
	@Temporal(TemporalType.DATE)
	@Column(name="HR_COLAB_DATA_REFERENCIA",
			nullable = false)
	private Date dataReferencia;
	
	@Column(name="HR_COLAB_MES_REFERENCIA",
			nullable = false, precision = 2, scale = 0)
	private int mesReferencia;
	
	@Column(name="HR_COLAB_ANO_REFERENCIA",
			nullable = false, precision = 4, scale = 0)
	private int anoReferencia;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HR_COLAB_INICIO",
			nullable = false)
	private Date inicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HR_COLAB_FIM",
			nullable = true)
	private Date fim;
	
	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "HR_COLAB_JUSTIFICATIVA")
	private String justificativa;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
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

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
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
	
	public String getTotalHoras(){
		if(this.fim == null){
			return null;
		}
		else{
			return SIGPRUtil.calculaDiferenciaHoras(this.inicio, this.fim);
		}
	}

}
