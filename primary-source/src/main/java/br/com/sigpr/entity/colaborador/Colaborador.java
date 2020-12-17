package br.com.sigpr.entity.colaborador;


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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.sigpr.entity.colaboradorprojeto.ColaboradorProjeto;
import br.com.sigpr.entity.demanda.Solicitacao;
import br.com.sigpr.entity.historico.Historico;
import br.com.sigpr.entity.horas.HoraColaborador;
import br.com.sigpr.entity.horas.HoraTarefa;
import br.com.sigpr.entity.perfil.Perfil;
import br.com.sigpr.entity.tarefa.Tarefa;
import br.com.sigpr.entity.unidade.Unidade;
import br.com.sigpr.entity.usuario.Usuario;

/**
 * Entity implementation class for Entity: Colaborador
 *
 */
@Entity
@Table( name="TB_COLABORADOR", 
		schema = "SIGPR")

@SequenceGenerator( name = "GENERATOR_SEQ_COLABORADOR", 
					sequenceName = "SEQ_COLABORADOR", 
					schema = "SIGPR", 
					allocationSize = 1)
public class Colaborador implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	@Id    
	@GeneratedValue(strategy = GenerationType.SEQUENCE, 
					generator = "GENERATOR_SEQ_COLABORADOR")
	@Column(name = "COLABORADOR_ID",
			nullable = false,
			updatable = false)
	private Long id;
	
	@Column(name = "COLABORADOR_NOME", 
			nullable = false,
			length = 255)
	private String nome;
	
	@Column(name = "COLABORADOR_CPF", 
			nullable = false, 
			unique = true,
			length = 14)
	private String cpf;
	
	@Column(name = "COLABORADOR_RAMAL",
			length = 20)
	private String ramal;
	
	@Column(name = "COLABORADOR_CELULAR",
			length = 14)
	private String celular;
	
	@Column(name = "COLABORADOR_ATIVO")
	private boolean ativo;
	
	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name  = "COLABORADOR_IMAGEM")
	private byte[] imagem;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "COLABORADOR_USUARIO_ID",
			referencedColumnName = "USUARIO_ID", 
			updatable = false)	
	private Usuario usuario;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COLABORADOR_PERFIL_ID",
				referencedColumnName = "PERFIL_ID")
	private Perfil perfil;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COLABORADOR_UNIDADE_ID",
				referencedColumnName = "UNIDADE_ID")
	private Unidade unidade;
	
	@OneToMany(mappedBy = "colaborador")
	private List<ColaboradorProjeto> projetos;
	
	@OneToMany(mappedBy = "colaborador")
	private List<HoraColaborador> horas;
	
	@OneToMany(mappedBy = "colaborador")
	private List<Historico> historico;
	
	@OneToMany(mappedBy = "colaborador")
	private List<Tarefa> tarefas;
	
	@OneToMany(mappedBy = "colaborador")
	private List<Solicitacao> solicitacoesAtendidas;
	
	@OneToMany(mappedBy = "colaborador")
	private List<HoraTarefa> horasTarefa;
	
	public Colaborador() {
		super();
	}

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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRamal() {
		return ramal;
	}

	public void setRamal(String ramal) {
		this.ramal = ramal;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public byte[] getImagem() {
		return imagem;
	}

	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	public List<ColaboradorProjeto> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<ColaboradorProjeto> projetos) {
		this.projetos = projetos;
	}

	public List<HoraColaborador> getHoras() {
		return horas;
	}

	public void setHoras(List<HoraColaborador> horas) {
		this.horas = horas;
	}

	public List<Historico> getHistorico() {
		return historico;
	}

	public void setHistorico(List<Historico> historico) {
		this.historico = historico;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Tarefa> getTarefas() {
		return tarefas;
	}

	public void setTarefas(List<Tarefa> tarefas) {
		this.tarefas = tarefas;
	}
	
	public List<Solicitacao> getSolicitacoesAtendidas() {
		return solicitacoesAtendidas;
	}

	public void setSolicitacoesAtendidas(List<Solicitacao> solicitacoesAtendidas) {
		this.solicitacoesAtendidas = solicitacoesAtendidas;
	}

	public List<HoraTarefa> getHorasTarefa() {
		return horasTarefa;
	}

	public void setHorasTarefa(List<HoraTarefa> horasTarefa) {
		this.horasTarefa = horasTarefa;
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
		Colaborador other = (Colaborador) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
