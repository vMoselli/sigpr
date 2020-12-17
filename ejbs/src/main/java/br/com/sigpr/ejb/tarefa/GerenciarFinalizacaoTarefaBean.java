/**
 * 
 */
package br.com.sigpr.ejb.tarefa;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.sigpr.ejb.horas.GerenciarHoraTarefa;
import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.tarefa.Tarefa;

/**
 * @author Thethis
 *
 */
@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GerenciarFinalizacaoTarefaBean implements GerenciarFinalizacaoTarefa {
	
	@PersistenceContext
	private EntityManager em;
	
	@EJB
	private GerenciarHoraTarefa horaTarefaBean;

	@Override
	public void finalizarTarefa(Tarefa tarefa, Colaborador colaborador) {
		tarefa.setColaborador(colaborador);
		horaTarefaBean.apontar(tarefa, colaborador);
		em.merge(tarefa);
		em.flush();
	}


}
