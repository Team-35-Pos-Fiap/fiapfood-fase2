package br.com.fiapfood.infraestructure.repositories.interfaces;

import java.util.UUID;

import br.com.fiapfood.core.entities.dto.restaurante.DadosRestauranteDto;
import br.com.fiapfood.core.entities.dto.restaurante.RestaurantePaginacaoInputDto;
import br.com.fiapfood.infraestructure.entities.RestauranteEntity;

public interface IRestauranteRepository {
    void salvarRestaurante(RestauranteEntity restaurante);
    DadosRestauranteDto buscarPorId(UUID id);
    RestaurantePaginacaoInputDto buscarRestaurantesComPaginacao(Integer pagina);
}