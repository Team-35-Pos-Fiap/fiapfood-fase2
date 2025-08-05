package br.com.fiapfood.core.usecases.restaurante.interfaces;

import br.com.fiapfood.core.entities.dto.restaurante.CadastrarRestauranteCoreDto;

public interface ICadastrarRestauranteUseCase {
	void cadastrar(CadastrarRestauranteCoreDto restaurante);
}