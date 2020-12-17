package br.com.sigpr.ejb.horas;

import java.util.List;

import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.horas.HoraColaborador;
import br.com.sigpr.exceptions.SIGPRException;
import br.com.sigpr.exceptions.TarefaIniciadaException;

public interface GerenciarHoraColaborador {

	void apontar(Colaborador colaborador, String justificativa) throws TarefaIniciadaException;
	List<HoraColaborador> listarHoras(Colaborador colaborador);
	List<HoraColaborador> listarHorasDia(Colaborador colaborador);
	HoraColaborador consultarHoraIniciada(Colaborador colaborador);
	boolean isHoraIniciada(Colaborador colaborador);
	List<HoraColaborador> consultar(String string, Object... array) throws SIGPRException;
	
}
