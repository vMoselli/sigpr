package br.com.sigpr.ejb.cadastro.status;
import java.util.List;

import br.com.sigpr.ejb.interfaces.BasicInterface;
import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.status.Status;
import br.com.sigpr.enumerations.TipoStatus;

public interface GerenciarStatus extends BasicInterface<Status> {
	
	List<Status> listarPorTipo(TipoStatus tipoStatus);
	List<Status> listarTarefasPorColaborador(Colaborador colaborador);
	List<Status> listarTarefas();
	Status findByNameAndTipoStatus(String nomeStatus, TipoStatus tipoStatus);

}
