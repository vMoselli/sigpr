package br.com.sigpr.ejb.cadastro.artefato;

import java.util.List;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.sigpr.entity.tarefa.Arquivo;
import br.com.sigpr.entity.tarefa.Artefato;
import br.com.sigpr.entity.tarefa.Tarefa;
import br.com.sigpr.exceptions.SIGPRException;

@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GerenciarArtefatoBean implements GerenciarArtefato {

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	public List<Artefato> listar() {
		return em
				.createQuery(
						"Select artefato from Artefato artefato where artefato.ativo = true order by artefato.nome asc")
				.getResultList();
	}

	public void incluir(Artefato entity) {
		em.persist(entity);
		em.flush();
	}

	public void alterar(Artefato entity) {
		em.merge(entity);
		em.flush();
	}

	public void excluir(Artefato entity) {
		entity = em.merge(entity);
		entity.setAtivo(false);
		em.flush();
	}

	public Artefato findById(Object primaryKey) {
		return em.find(Artefato.class, primaryKey);
	}

	public void excluirArquivo(Arquivo arquivo) {
		em.remove(em.merge(arquivo));
		em.flush();
	}

	@SuppressWarnings("unchecked")
	public List<Artefato> consultarArtefatosPorTarefa(Tarefa tarefa) {
		return em
				.createQuery(
						"Select artefato from Artefato artefato where artefato.ativo = true and artefato.tarefa.id = :tarefa order by artefato.nome asc")
				.setParameter("tarefa", tarefa.getId()).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Artefato> consultar(String query, Object... params) throws SIGPRException {
		List<Artefato> lista = null;
		Query q = em.createQuery(query);
		
		try{
			int count = 1;
			for(Object obj : params){
				q.setParameter(count, obj);
				count++;
			}
			
			lista = q.getResultList();
		}
		catch(Exception e){
			throw new SIGPRException("Geração de Relatórios", "Ocorreu um erro ao gerar o relatório", e);
		}
		
		return lista;
		
	}

}
