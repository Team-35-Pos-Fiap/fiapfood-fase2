package br.com.fiapfood.core.usecases.restaurante.impl;

import java.util.UUID;

import br.com.fiapfood.core.entities.Endereco;
import br.com.fiapfood.core.entities.Restaurante;
import br.com.fiapfood.core.entities.dto.endereco.DadosEnderecoCoreDto;
import br.com.fiapfood.core.exceptions.restaurante.AtualizacaoEnderecoRestauranteNaoPermitidaException;
import br.com.fiapfood.core.gateways.interfaces.IRestauranteGateway;
import br.com.fiapfood.core.presenters.RestaurantePresenter;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IAtualizarEnderecoRestauranteUseCase;

public class AtualizarEnderecoRestauranteUseCase implements IAtualizarEnderecoRestauranteUseCase {
	private final IRestauranteGateway restauranteGateway;

	private final String RESTAURANTE_INATIVO = "Não é possível alterar o nome do restaurante, pois ele se encontra inativo.";
	
	public AtualizarEnderecoRestauranteUseCase(IRestauranteGateway restauranteGateway) {
		this.restauranteGateway = restauranteGateway;
	}
	
	@Override
	public void atualizar(final UUID id, final DadosEnderecoCoreDto dadosEnderecoDto) {
		final Restaurante restaurante = buscarRestaurante(id);

		validarStatusRestaurante(restaurante);
		
		final Endereco dadosEndereco = buscarEndereco(restaurante);
		
		atualizarEndereco(dadosEndereco, dadosEnderecoDto);
		atualizarRestaurante(restaurante, dadosEndereco);
		
		atualizar(restaurante);
	}
	
	private void atualizarEndereco(Endereco dadosEndereco, DadosEnderecoCoreDto dadosEnderecoDto) {
		dadosEndereco.atualizarDados(dadosEnderecoDto.endereco(), 
									 dadosEnderecoDto.cidade(), 
									 dadosEnderecoDto.bairro(), 
									 dadosEnderecoDto.estado(), 
									 dadosEnderecoDto.numero(), 
									 dadosEnderecoDto.cep(), 
									 dadosEnderecoDto.complemento());		
	}

	private Restaurante buscarRestaurante(final UUID id) {
		return restauranteGateway.buscarPorId(id);
	}
	
	private void validarStatusRestaurante(final Restaurante restaurante) {
		if (!restaurante.getIsAtivo()) {
			throw new AtualizacaoEnderecoRestauranteNaoPermitidaException(RESTAURANTE_INATIVO);
		} 
	}

	private void atualizarRestaurante(Restaurante restaurante, Endereco dadosEndereco) {
		restaurante.atualizarEndereco(dadosEndereco);
	}

	private Endereco buscarEndereco(final Restaurante restaurante) {
		return restaurante.getDadosEndereco();
	}
	
	private void atualizar(final Restaurante restaurante) {
		restauranteGateway.atualizar(RestaurantePresenter.toRestauranteDto(restaurante));
	}
}