package br.com.sigpr.ejb.demanda;

import java.util.List;

import br.com.sigpr.ejb.interfaces.BasicInterface;
import br.com.sigpr.entity.demanda.Solicitacao;
import br.com.sigpr.enumerations.StatusSolicitacao;
import br.com.sigpr.exceptions.SIGPRException;
import br.com.sigpr.util.demanda.Demanda;

public interface GerenciarDemanda extends BasicInterface<Solicitacao> {

	List<Solicitacao> listarSolicitacoesPorStatus(
            StatusSolicitacao statusSolicitacao);

	void atualizarDemanda(Demanda demanda) throws SIGPRException;

}
