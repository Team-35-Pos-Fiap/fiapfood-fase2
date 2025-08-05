package br.com.fiapfood.core.usecases.restaurante.impl;

import java.util.UUID;

import br.com.fiapfood.core.entities.Restaurante;
import br.com.fiapfood.core.exceptions.restaurante.AtualizacaoTipoCulinariaRestauranteNaoPermitidaException;
import br.com.fiapfood.core.gateways.interfaces.IRestauranteGateway;
import br.com.fiapfood.core.presenters.RestaurantePresenter;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IAtualizarTipoCulinariaRestauranteUseCase;

public class AtualizarTipoCulinariaRestauranteUseCase implements IAtualizarTipoCulinariaRestauranteUseCase{
	private final IRestauranteGateway restauranteGateway;
	
	private final String RESTAURANTE_INATIVO = "Não é possível alterar o tipo de culinária do restaurante, pois ele se encontra inativo.";
	private final String TIPO_CULINARIA_DUPLICADO = "Não é possível atualizar o tipo de culínaia do restaurante, pois a identificação informada é igual a identificação atual do tipo de culinária do restaurante.";
	
	public AtualizarTipoCulinariaRestauranteUseCase(IRestauranteGateway restauranteGateway) {
		this.restauranteGateway = restauranteGateway;
	}

	@Override
	public void atualizar(final UUID id, final Integer idTipoCulinaria) {
		final Restaurante restaurante = buscarRestaurante(id);

		validarStatusRestaurante(restaurante);
		validarTipoCulinaria(idTipoCulinaria, restaurante);
			
		atualizarTipoCulinaria(restaurante, idTipoCulinaria);
		
		atualizar(restaurante);
	}

	private void validarStatusRestaurante(final Restaurante restaurante) {
		if (!restaurante.getIsAtivo()) {
			throw new AtualizacaoTipoCulinariaRestauranteNaoPermitidaException(RESTAURANTE_INATIVO);
		} 
	}

	private void validarTipoCulinaria(final Integer idTipoCulinaria, final Restaurante restaurante) {
		if(restaurante.getIdTipoCulinaria().equals(idTipoCulinaria)){
			throw new AtualizacaoTipoCulinariaRestauranteNaoPermitidaException(TIPO_CULINARIA_DUPLICADO);
		}
	}
	
	private Restaurante buscarRestaurante(final UUID id) {
		return restauranteGateway.buscarPorId(id);
	}
	
	private void atualizar(final Restaurante restaurante) {
		restauranteGateway.atualizar(RestaurantePresenter.toRestauranteDto(restaurante));
	}
	
	private void atualizarTipoCulinaria(final Restaurante restaurante, final Integer idTipoCulinaria) {
		restaurante.atualizarTipoCulinaria(idTipoCulinaria);	
	}
}