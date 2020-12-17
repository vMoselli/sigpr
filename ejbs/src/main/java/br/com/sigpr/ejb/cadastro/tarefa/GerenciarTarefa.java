package br.com.sigpr.ejb.cadastro.tarefa;

import java.util.List;

import br.com.sigpr.ejb.interfaces.BasicInterface;
import br.com.sigpr.entity.funcionalidade.Funcionalidade;
import br.com.sigpr.entity.tarefa.Tarefa;
import br.com.sigpr.exceptions.SIGPRException;

public interface GerenciarTarefa extends BasicInterface<Tarefa> {
	
	List<Tarefa> consultarTarefaPorFuncionalidade(Funcionalidade funcionalidade);

	void atribuirTarefa(Tarefa tarefa);

	List<Tarefa> consultar(String string, Object... array) throws SIGPRException;

	Tarefa findByName(String nome);

}
