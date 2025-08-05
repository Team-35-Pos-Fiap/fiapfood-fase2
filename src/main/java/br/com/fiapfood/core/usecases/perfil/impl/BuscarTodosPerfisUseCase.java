package br.com.fiapfood.core.usecases.perfil.impl;

import java.util.List;

import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.entities.dto.perfil.PerfilCoreDto;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.presenters.PerfilPresenter;
import br.com.fiapfood.core.usecases.perfil.interfaces.IBuscarTodosPerfisUseCase;

public class BuscarTodosPerfisUseCase implements IBuscarTodosPerfisUseCase{

	private final IPerfilGateway perfilGateway;
	
	public BuscarTodosPerfisUseCase(IPerfilGateway perfilGateway) {
		this.perfilGateway = perfilGateway;
	}
	
	@Override
	public List<PerfilCoreDto> buscar() {
		return toListPerfilDto(buscarTodos());
	}
	
	private List<Perfil> buscarTodos() {
		return perfilGateway.buscarTodos();
	}
	
	private List<PerfilCoreDto> toListPerfilDto(List<Perfil> perfis) {
		return PerfilPresenter.toListDto(perfis);
	}
}