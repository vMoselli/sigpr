package br.com.sigpr.ejb.cadastro.unidade;
import java.util.List;

import br.com.sigpr.ejb.interfaces.BasicInterface;
import br.com.sigpr.entity.unidade.Unidade;

public interface GerenciarUnidade extends BasicInterface<Unidade> {
	
	List<Unidade> listarUnidades(String query);
	
}
