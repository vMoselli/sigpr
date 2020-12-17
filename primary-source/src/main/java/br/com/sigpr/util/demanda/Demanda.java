package br.com.sigpr.util.demanda;

import br.com.sigpr.entity.funcionalidade.Funcionalidade;
import br.com.sigpr.entity.projeto.Projeto;
import br.com.sigpr.entity.tarefa.Tarefa;

import java.util.List;

public class Demanda {

	private List<Projeto> projetos;
	private List<Funcionalidade> funcionalidades;
	private List<Tarefa> tarefas;

	public List<Projeto> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}

	public List<Funcionalidade> getFuncionalidades() {
		return funcionalidades;
	}

	public void setFuncionalidades(List<Funcionalidade> funcionalidades) {
		this.funcionalidades = funcionalidades;
	}

	public List<Tarefa> getTarefas() {
		return tarefas;
	}

	public void setTarefas(List<Tarefa> tarefas) {
		this.tarefas = tarefas;
	}

}
