package br.com.sigpr.ejb.cadastro.artefato;

import java.util.List;

import br.com.sigpr.ejb.interfaces.BasicInterface;
import br.com.sigpr.entity.tarefa.Arquivo;
import br.com.sigpr.entity.tarefa.Artefato;
import br.com.sigpr.entity.tarefa.Tarefa;
import br.com.sigpr.exceptions.SIGPRException;

public interface GerenciarArtefato extends BasicInterface<Artefato>{
	
	void excluirArquivo(Arquivo arquivo);

	List<Artefato> consultarArtefatosPorTarefa(Tarefa tarefa);

	List<Artefato> consultar(String string, Object... array) throws SIGPRException;

}
