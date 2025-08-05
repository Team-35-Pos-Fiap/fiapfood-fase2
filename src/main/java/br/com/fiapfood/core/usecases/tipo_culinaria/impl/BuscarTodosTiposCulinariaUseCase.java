package br.com.fiapfood.core.usecases.tipo_culinaria.impl;

import java.util.List;

import br.com.fiapfood.core.entities.TipoCulinaria;
import br.com.fiapfood.core.entities.dto.tipo_culinaria.TipoCulinariaCoreDto;
import br.com.fiapfood.core.gateways.interfaces.ITipoCulinariaGateway;
import br.com.fiapfood.core.presenters.TipoCulinariaPresenter;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.IBuscarTodosTiposCulinariaUseCase;

public class BuscarTodosTiposCulinariaUseCase implements IBuscarTodosTiposCulinariaUseCase{

	private final ITipoCulinariaGateway tipoCulinariaGateway;
	
	public BuscarTodosTiposCulinariaUseCase(ITipoCulinariaGateway tipoCulinariaGateway) {
		this.tipoCulinariaGateway = tipoCulinariaGateway;
	}
	
	@Override
	public List<TipoCulinariaCoreDto> buscar() {
		return toListDto(buscarTodos());
	}
	
	private List<TipoCulinaria> buscarTodos() {
		return tipoCulinariaGateway.buscarTodos();
	}
	
	private List<TipoCulinariaCoreDto> toListDto(final List<TipoCulinaria> tiposCulinaria) {
		return TipoCulinariaPresenter.toListDto(tiposCulinaria);
	} 
}