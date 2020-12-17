package br.com.sigpr.ejb.cadastro.projeto;

import java.util.List;

import br.com.sigpr.ejb.interfaces.BasicInterface;
import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.projeto.Projeto;
import br.com.sigpr.exceptions.SIGPRException;

public interface GerenciarProjeto extends BasicInterface<Projeto> {

	List<Projeto> listarProjetosNaoAssociados(Colaborador colaborador);

	List<Projeto> consultar(String string, Object... params) throws SIGPRException;

	Projeto findByName(String nome);
	
}
