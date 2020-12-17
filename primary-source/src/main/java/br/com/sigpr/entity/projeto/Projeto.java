package br.com.sigpr.entity.projeto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.sigpr.entity.colaboradorprojeto.ColaboradorProjeto;
import br.com.sigpr.entity.funcionalidade.Funcionalidade;
import br.com.sigpr.entity.historico.Historico;
import br.com.sigpr.entity.status.Status;

/**
 * Entity implementation class for Entity: Projeto
 *
 */
@Entity
@Table( name="TB_PROJETO", 
		schema = "SIGPR")
@SequenceGenerator( name = "GENERATOR_SEQ_PROJETO", 
					sequenceName = "SEQ_PROJETO", 
					schema = "SIGPR", 
					allocationSize = 1)
public class Projeto implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false,
			updatable = false,
			name = "PROJETO_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, 
					generator = "GENERATOR_SEQ_PROJETO")
	private Long id;
	
	@Column(unique = true, 
			nullable = false, 
			name = "PROJETO_NOME",
			length = 255)
	private String nome;
	
	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "PROJETO_DESCRICAO", 
			nullable = false)
	private String descricao;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "PROJETO_PRAZO", 
			nullable = false)
	private Date prazo;
	
	@Column(name = "PROJETO_ATIVO")
	private boolean ativo;
	
	@OneToMany(mappedBy = "projeto")
	private List<Historico> historicos;
	
	@OneToMany(mappedBy = "projeto")
	private List<ColaboradorProjeto> colaboradores;
	
	@OneToMany(mappedBy = "projeto")
	private List<Funcionalidade> funcionalidades;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PROJETO_STATUS_ID", 
				referencedColumnName = "STATUS_ID")
	private Status status;

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

	public List<Historico> getHistoricos() {
		return historicos;
	}

	public void setHistoricos(List<Historico> historicos) {
		this.historicos = historicos;
	}

	public List<ColaboradorProjeto> getColaboradores() {
		return colaboradores;
	}

	public void setColaboradores(List<ColaboradorProjeto> colaboradores) {
		this.colaboradores = colaboradores;
	}

	public List<Funcionalidade> getFuncionalidades() {
		return funcionalidades;
	}

	public void setFuncionalidades(List<Funcionalidade> funcionalidades) {
		this.funcionalidades = funcionalidades;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
		   
}
