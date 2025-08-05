package br.com.fiapfood.core.gateways.interfaces;

import java.util.UUID;

import br.com.fiapfood.core.entities.Restaurante;
import br.com.fiapfood.core.entities.dto.restaurante.DadosRestauranteDto;
import br.com.fiapfood.core.entities.dto.restaurante.RestaurantePaginacaoInputDto;

public interface IRestauranteGateway {
	Restaurante buscarPorId(UUID id);
	RestaurantePaginacaoInputDto buscarTodos(Integer pagina);
	void atualizar(DadosRestauranteDto restaurante);
	void cadastrar(DadosRestauranteDto restaurante);
}