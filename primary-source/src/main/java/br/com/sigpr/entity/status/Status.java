package br.com.sigpr.entity.status;

import java.io.Serializable;
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
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.sigpr.entity.tarefa.Tarefa;
import br.com.sigpr.enumerations.TipoStatus;
@Entity
@Table( name = "TB_STATUS", 
		schema = "SIGPR")

@SequenceGenerator( name = "GENERATOR_SEQ_STATUS", 
					sequenceName = "SEQ_STATUS", 
					schema = "SIGPR", 
					allocationSize = 1)
public class Status implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "STATUS_ID", 
			nullable = false, 
			updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, 
					generator = "GENERATOR_SEQ_STATUS")
	private Long id;
	
	@Column(name = "STATUS_NOME", 
			length = 255)
	private String nome;
	
	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "STATUS_DESCRICAO")
	private String descricao;
	
	@Column(name = "STATUS_ATIVO")
	private boolean ativo;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_TIPO_STATUS",
			length = 30,
			nullable = false)
	private TipoStatus tipoStatus;
	
	@OneToMany(mappedBy = "status")
	private List<Tarefa> tarefas;

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

	public TipoStatus getTipoStatus() {
		return tipoStatus;
	}

	public void setTipoStatus(TipoStatus tipoStatus) {
		this.tipoStatus = tipoStatus;
	}

	public List<Tarefa> getTarefas() {
		return tarefas;
	}

	public void setTarefas(List<Tarefa> tarefas) {
		this.tarefas = tarefas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Status other = (Status) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
