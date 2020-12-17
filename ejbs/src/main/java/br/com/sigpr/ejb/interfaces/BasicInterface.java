package br.com.sigpr.ejb.interfaces;

import java.util.List;

import br.com.sigpr.exceptions.SIGPRException;

public interface BasicInterface<T> {
	
	List<T> listar() throws SIGPRException;
	void incluir(T entity) throws SIGPRException;
	void alterar(T entity) throws SIGPRException;
	void excluir(T entity) throws SIGPRException;
	T findById(Object primaryKey) throws SIGPRException;

}
