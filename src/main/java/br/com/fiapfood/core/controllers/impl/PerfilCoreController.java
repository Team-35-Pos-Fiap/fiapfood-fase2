package br.com.fiapfood.core.controllers.impl;

import java.util.List;

import br.com.fiapfood.core.controllers.interfaces.IPerfilCoreController;
import br.com.fiapfood.core.entities.dto.PerfilDto;
import br.com.fiapfood.core.usecases.perfil.interfaces.IBuscarPerfilPorIdUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.IBuscarTodosPerfisUseCase;

public class PerfilCoreController implements IPerfilCoreController{

	private final IBuscarTodosPerfisUseCase buscarTodosUseCase;
	private final IBuscarPerfilPorIdUseCase buscarPorIdUseCase;
	
	public PerfilCoreController(IBuscarTodosPerfisUseCase buscarTodosUseCase, IBuscarPerfilPorIdUseCase buscarPorIdUseCase) {
		this.buscarTodosUseCase = buscarTodosUseCase;
		this.buscarPorIdUseCase = buscarPorIdUseCase;
	}
	
	@Override
	public List<PerfilDto> buscarTodos() {
		return buscarTodosUseCase.buscar();
	}

	@Override
	public PerfilDto buscarPorId(final Integer id) {
		return buscarPorIdUseCase.buscar(id);
	}
}