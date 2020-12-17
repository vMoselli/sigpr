package br.com.sigpr.ejb.tarefa;

import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.tarefa.Tarefa;

public interface GerenciarFinalizacaoTarefa {
	
	void finalizarTarefa(Tarefa tarefa, Colaborador colaborador);

}
