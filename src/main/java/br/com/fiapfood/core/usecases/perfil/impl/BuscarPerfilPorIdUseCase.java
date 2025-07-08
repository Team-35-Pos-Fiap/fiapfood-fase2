package br.com.fiapfood.core.usecases.perfil.impl;

import br.com.fiapfood.core.entities.dto.PerfilDto;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.presenters.PerfilPresenter;
import br.com.fiapfood.core.usecases.perfil.interfaces.IBuscarPerfilPorIdUseCase;

public class BuscarPerfilPorIdUseCase implements IBuscarPerfilPorIdUseCase{

	private final IPerfilGateway perfilGateway;
	
	public BuscarPerfilPorIdUseCase(IPerfilGateway perfilGateway) {
		this.perfilGateway = perfilGateway;
	}
	
	@Override
	public PerfilDto buscar(final Integer id) {
		return PerfilPresenter.toPerfilDto(perfilGateway.buscarPorId(id));
	}
}