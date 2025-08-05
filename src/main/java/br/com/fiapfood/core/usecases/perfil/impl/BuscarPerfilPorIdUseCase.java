package br.com.fiapfood.core.usecases.perfil.impl;

import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.entities.dto.perfil.PerfilCoreDto;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.presenters.PerfilPresenter;
import br.com.fiapfood.core.usecases.perfil.interfaces.IBuscarPerfilPorIdUseCase;

public class BuscarPerfilPorIdUseCase implements IBuscarPerfilPorIdUseCase{

	private final IPerfilGateway perfilGateway;
	
	public BuscarPerfilPorIdUseCase(IPerfilGateway perfilGateway) {
		this.perfilGateway = perfilGateway;
	}
	
	@Override
	public PerfilCoreDto buscar(final Integer id) {
		return toPerfilDto(buscarPerfil(id));
	}
	
	private Perfil buscarPerfil(Integer id) {
		return perfilGateway.buscarPorId(id);
	}
	
	private PerfilCoreDto toPerfilDto(Perfil perfil) {
		return PerfilPresenter.toPerfilDto(perfil);
	} 
}