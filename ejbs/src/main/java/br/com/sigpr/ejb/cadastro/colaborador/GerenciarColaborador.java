package br.com.sigpr.ejb.cadastro.colaborador;

import java.util.List;

import br.com.sigpr.ejb.interfaces.BasicInterface;
import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.exceptions.SIGPRException;

public interface GerenciarColaborador extends BasicInterface<Colaborador> {

	List<Colaborador> consultar(String query, Object... params) throws SIGPRException;
	
}
