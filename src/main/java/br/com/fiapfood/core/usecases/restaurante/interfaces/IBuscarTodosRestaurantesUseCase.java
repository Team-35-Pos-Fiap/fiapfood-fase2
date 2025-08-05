package br.com.fiapfood.core.usecases.restaurante.interfaces;

import br.com.fiapfood.core.entities.dto.restaurante.RestaurantePaginacaoCoreDto;

public interface IBuscarTodosRestaurantesUseCase {
	RestaurantePaginacaoCoreDto buscar(Integer pagina);
}