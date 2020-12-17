package br.com.sigpr.util.comparators;

import br.com.sigpr.entity.colaborador.Colaborador;

import java.util.Comparator;

public class QuantidadeTarefasComparator implements Comparator<Colaborador> {

	public int compare(Colaborador colaborador1, Colaborador colaborador2) {
		
		if(colaborador1.getTarefas() != null && colaborador2.getTarefas() != null){
			
			if(colaborador1.getTarefas().size() == colaborador2.getTarefas().size()){
				return 0;
			}
			else if(colaborador1.getTarefas().size() > colaborador2.getTarefas().size()){
				return 1;
			}
			else if(colaborador1.getTarefas().size() < colaborador2.getTarefas().size()){
				return -1;
			}
		}		
		return 0;
	}

}
