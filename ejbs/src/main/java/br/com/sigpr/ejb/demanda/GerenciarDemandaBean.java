package br.com.sigpr.ejb.demanda;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.sigpr.ejb.cadastro.funcionalidade.GerenciarFuncionalidade;
import br.com.sigpr.ejb.cadastro.projeto.GerenciarProjeto;
import br.com.sigpr.ejb.cadastro.tarefa.GerenciarTarefa;
import br.com.sigpr.entity.demanda.Solicitacao;
import br.com.sigpr.entity.funcionalidade.Funcionalidade;
import br.com.sigpr.entity.projeto.Projeto;
import br.com.sigpr.entity.status.Status;
import br.com.sigpr.entity.tarefa.Tarefa;
import br.com.sigpr.enumerations.StatusSolicitacao;
import br.com.sigpr.exceptions.SIGPRException;
import br.com.sigpr.util.demanda.Demanda;

/**
 * Session Bean implementation class GerenciarDemandaBean
 */
@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GerenciarDemandaBean implements GerenciarDemanda {
	
	@PersistenceContext
	private EntityManager em;
	
	@EJB
	private GerenciarProjeto projetoBean;
	
	@EJB
	private GerenciarFuncionalidade funcionalidadeBean;
	
	@EJB
	private GerenciarTarefa tarefaBean;

    public GerenciarDemandaBean() {
    }

    @SuppressWarnings("unchecked")
	public List<Solicitacao> listar() throws SIGPRException {
		List<Solicitacao> lista = em.createQuery("Select solicitacao from Solicitacao solicitacao order by solicitacao.status asc, solicitacao.prioridade desc").getResultList();
		return lista;
	}

	public void incluir(Solicitacao entity) throws SIGPRException {
		em.persist(entity);
		em.flush();
	}

	public void alterar(Solicitacao entity) throws SIGPRException {
		em.merge(entity);
		em.flush();
	}

	public void excluir(Solicitacao entity) throws SIGPRException {
		em.merge(entity);
		entity.setStatus(StatusSolicitacao.Cancelada);
		em.flush();
	}

	public Solicitacao findById(Object primaryKey) throws SIGPRException {
		return em.find(Solicitacao.class, primaryKey);
	}

	@SuppressWarnings("unchecked")
	public List<Solicitacao> listarSolicitacoesPorStatus(StatusSolicitacao statusSolicitacao) {
		List<Solicitacao> lista = em.createQuery("Select solicitacao from Solicitacao solicitacao where " +
				"solicitacao.status = :statusSolicitacao " +
				"order by solicitacao.status asc, solicitacao.prioridade desc")
				.setParameter("statusSolicitacao", statusSolicitacao)
				.getResultList();
		return lista;
	}

	@Override
	public void atualizarDemanda(Demanda demanda) throws SIGPRException {
		
		//Projetos
		for(Projeto projeto : demanda.getProjetos()){
			
			Projeto proj = projetoBean.findByName(projeto.getNome());
			Status status = em.find(Status.class, projeto.getStatus().getId());
			if(proj != null){
				proj.setDescricao(projeto.getDescricao());
				proj.setPrazo(projeto.getPrazo());
				proj.setStatus(status);
			}
			else{
				projeto.setStatus(status);
				projetoBean.incluir(projeto);
			}
		}
		em.flush();
		
		//Funcionalidades
		for(Funcionalidade funcionalidade : demanda.getFuncionalidades()){
			
			Funcionalidade func = funcionalidadeBean.findByName(funcionalidade.getNome());
			Status status = em.find(Status.class, funcionalidade.getStatus().getId());
			if(func != null){
				func.setDescricao(funcionalidade.getDescricao());
				func.setPrazo(funcionalidade.getPrazo());
				func.setStatus(status);
			}
			else{
				funcionalidade.setProjeto(projetoBean.findByName(funcionalidade.getProjeto().getNome()));
				funcionalidade.setStatus(status);
				funcionalidadeBean.incluir(funcionalidade);
			}
		}
		em.flush();
		
		//Tarefas
		for(Tarefa tarefa : demanda.getTarefas()){
			
			Tarefa taref = tarefaBean.findByName(tarefa.getNome());
			Status status = em.find(Status.class, tarefa.getStatus().getId());
			
			if(taref != null){
				taref.setDescricao(tarefa.getDescricao());
				taref.setPrazo(tarefa.getPrazo());
				taref.setStatus(status);
				taref.setTipoTarefa(tarefa.getTipoTarefa());
			}
			else{
				tarefa.setStatus(status);
				tarefa.setFuncionalidade(funcionalidadeBean.findByName(tarefa.getFuncionalidade().getNome()));
				tarefaBean.incluir(tarefa);
			}
		}
		em.flush();
	}

}
