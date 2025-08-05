package br.com.fiapfood.core.usecases.restaurante.interfaces;

import java.util.UUID;

import br.com.fiapfood.core.entities.dto.restaurante.DadosRestauranteCoreDto;

public interface IBuscarRestaurantePorId {
	DadosRestauranteCoreDto buscar(UUID id);
}