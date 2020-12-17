package br.com.sigpr.entity.usuario;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.sigpr.entity.colaborador.Colaborador;

@Entity
@Table( name="TB_USUARIO",
		schema = "SIGPR")
@SequenceGenerator( name = "GENERATOR_SEQ_USUARIO",
					sequenceName = "SEQ_USUARIO",
					schema = "SIGPR",
					allocationSize = 1)
public class Usuario implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "GENERATOR_SEQ_USUARIO")
	@Column(name = "USUARIO_ID", 
			updatable = false)
	private Long id;

	@Column(name = "USUARIO_LOGIN", 
			nullable = false, 
			unique = true,
			length = 30)
	private String login;

	@Column(name = "USUARIO_SENHA", 
			nullable = false,
			length = 255)
	private String senha;
	
	@OneToOne(mappedBy = "usuario", optional = true, fetch = FetchType.EAGER)
	private Colaborador colaborador;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
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
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}