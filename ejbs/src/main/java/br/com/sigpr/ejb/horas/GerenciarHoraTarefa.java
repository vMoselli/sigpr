package br.com.sigpr.ejb.horas;

import java.util.List;

import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.horas.HoraTarefa;
import br.com.sigpr.entity.tarefa.Tarefa;
import br.com.sigpr.exceptions.SIGPRException;

public interface GerenciarHoraTarefa {
	
	void apontar(Tarefa tarefa, Colaborador colaborador);
	List<HoraTarefa> listarHoras(Tarefa tarefa);
	List<HoraTarefa> listarHoras(Tarefa tarefa, Colaborador colaborador);
	HoraTarefa consultarHoraIniciada(Tarefa tarefa, Colaborador colaborador);
	boolean isTarefaIniciada(Colaborador colaborador);
	boolean isTarefaIniciada(Tarefa tarefa, Colaborador colaborador);
	List<HoraTarefa> consultar(String string, Object... array) throws SIGPRException;

}
