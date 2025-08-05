package br.com.fiapfood.core.usecases.restaurante.impl;

import java.util.UUID;

import br.com.fiapfood.core.entities.Restaurante;
import br.com.fiapfood.core.exceptions.restaurante.InativacaoRestauranteNaoPermitidaException;
import br.com.fiapfood.core.gateways.interfaces.IRestauranteGateway;
import br.com.fiapfood.core.presenters.RestaurantePresenter;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IInativarRestauranteUseCase;

public class InativarRestauranteUseCase implements IInativarRestauranteUseCase {
	private final IRestauranteGateway restauranteGateway;

	private final String INATIVACAO_NAO_PERMITIDA = "Não é possível inativar o restaurante pois ele já se encontra inativo.";
	
	public InativarRestauranteUseCase(IRestauranteGateway restauranteGateway) {
		this.restauranteGateway = restauranteGateway;
	}
	
	@Override
	public void inativar(final UUID id) {
		final Restaurante restaurante = buscarRestaurante(id);

		validarStatusRestaurante(restaurante);
			
		inativar(restaurante);
		atualizar(restaurante);
	}

	private void inativar(Restaurante restaurante) {		
		restaurante.inativar();
	}

	private void validarStatusRestaurante(final Restaurante restaurante) {
		if (!restaurante.getIsAtivo()) {
			throw new InativacaoRestauranteNaoPermitidaException(INATIVACAO_NAO_PERMITIDA);
		} 
	}

	private Restaurante buscarRestaurante(final UUID id) {
		return restauranteGateway.buscarPorId(id);
	}
	
	private void atualizar(final Restaurante restaurante) {
		restauranteGateway.atualizar(RestaurantePresenter.toRestauranteDto(restaurante));
	}
}