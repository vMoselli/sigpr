package br.com.sigpr.ejb.cadastro.funcionalidade;

import java.util.List;

import br.com.sigpr.ejb.interfaces.BasicInterface;
import br.com.sigpr.entity.funcionalidade.Funcionalidade;
import br.com.sigpr.entity.projeto.Projeto;
import br.com.sigpr.exceptions.SIGPRException;

public interface GerenciarFuncionalidade extends BasicInterface<Funcionalidade> {

	List<Funcionalidade> consultarFuncionalidadePorProjeto(Projeto projeto) throws SIGPRException;

	List<Funcionalidade> consultar(String string, Object... array) throws SIGPRException;

	Funcionalidade findByName(String nome);

}
