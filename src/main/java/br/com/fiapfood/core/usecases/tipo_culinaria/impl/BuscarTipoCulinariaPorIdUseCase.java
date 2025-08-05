package br.com.fiapfood.core.usecases.tipo_culinaria.impl;

import br.com.fiapfood.core.entities.TipoCulinaria;
import br.com.fiapfood.core.entities.dto.tipo_culinaria.TipoCulinariaCoreDto;
import br.com.fiapfood.core.gateways.interfaces.ITipoCulinariaGateway;
import br.com.fiapfood.core.presenters.TipoCulinariaPresenter;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.IBuscarTipoCulinariaPorIdUseCase;

public class BuscarTipoCulinariaPorIdUseCase implements IBuscarTipoCulinariaPorIdUseCase{

	private final ITipoCulinariaGateway tipoCulinariaGateway;
	
	public BuscarTipoCulinariaPorIdUseCase(ITipoCulinariaGateway tipoCulinariaGateway) {
		this.tipoCulinariaGateway = tipoCulinariaGateway;
	}
	
	@Override
	public TipoCulinariaCoreDto buscar(final Integer id) {
		return toTipoCulinariaDto(buscarPorId(id));
	}
	
	private TipoCulinaria buscarPorId(final Integer id) {
		return tipoCulinariaGateway.buscarPorId(id);
	}
	
	private TipoCulinariaCoreDto toTipoCulinariaDto(final TipoCulinaria tipoCulinaria) {
		return TipoCulinariaPresenter.toTipoCulinariaDto(tipoCulinaria);
	}
}