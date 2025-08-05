package br.com.fiapfood.infraestructure.repositories.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import br.com.fiapfood.core.entities.dto.restaurante.DadosRestauranteDto;
import br.com.fiapfood.core.entities.dto.restaurante.RestaurantePaginacaoInputDto;
import br.com.fiapfood.core.presenters.RestaurantePresenter;
import br.com.fiapfood.infraestructure.entities.RestauranteEntity;
import br.com.fiapfood.infraestructure.repositories.interfaces.IRestauranteRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.jpa.IRestauranteJpaRepository;

@Repository
public class RestauranteRepository implements IRestauranteRepository {

	private final IRestauranteJpaRepository restauranteRepository;
	private final Integer QUANTIDADE_REGISTROS = 5;

	public RestauranteRepository(IRestauranteJpaRepository restauranteRepository) {
		this.restauranteRepository = restauranteRepository;
	}	

	@Override
	public DadosRestauranteDto buscarPorId(UUID id) {
		Optional<RestauranteEntity> restaurante = restauranteRepository.findById(id);
		
		if(restaurante.isPresent()) {
			return RestaurantePresenter.toRestauranteDto(restaurante.get());
		} else {
			return null;
		}
	}
	
	@Override
	public void salvarRestaurante(RestauranteEntity restaurante) {
		restauranteRepository.save(restaurante);
	}

	@Override
	public RestaurantePaginacaoInputDto buscarRestaurantesComPaginacao(Integer pagina) {
		Page<RestauranteEntity> dados = restauranteRepository.findAll(PageRequest.of(pagina - 1, QUANTIDADE_REGISTROS));
		
		if (!dados.toList().isEmpty()) {
			return RestaurantePresenter.toRestaurantePaginacaoInputDto(dados);
		} else {
			return null;
		}
	}
}