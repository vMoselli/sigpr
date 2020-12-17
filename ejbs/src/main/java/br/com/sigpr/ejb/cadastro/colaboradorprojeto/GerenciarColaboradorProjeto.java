package br.com.sigpr.ejb.cadastro.colaboradorprojeto;

import java.util.List;

import br.com.sigpr.ejb.interfaces.BasicInterface;
import br.com.sigpr.entity.colaborador.Colaborador;
import br.com.sigpr.entity.colaboradorprojeto.ColaboradorProjeto;
import br.com.sigpr.entity.projeto.Projeto;

public interface GerenciarColaboradorProjeto extends BasicInterface<ColaboradorProjeto> {
	
	List<ColaboradorProjeto> listarPorColaborador(Colaborador colaborador);
	List<ColaboradorProjeto> listarPorProjeto(Projeto projeto);
	
}
