package br.com.fiapfood.core.usecases.perfil.impl;

import java.util.List;

import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.entities.dto.PerfilDto;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.presenters.PerfilPresenter;
import br.com.fiapfood.core.usecases.perfil.interfaces.IBuscarTodosPerfisUseCase;

public class BuscarTodosPerfisUseCase implements IBuscarTodosPerfisUseCase{

	private final IPerfilGateway perfilGateway;
	
	public BuscarTodosPerfisUseCase(IPerfilGateway perfilGateway) {
		this.perfilGateway = perfilGateway;
	}
	
	@Override
	public List<PerfilDto> buscar() {
		final List<Perfil> perfis = perfilGateway.buscarTodos();
		
		return PerfilPresenter.toListDto(perfis);
	}
}