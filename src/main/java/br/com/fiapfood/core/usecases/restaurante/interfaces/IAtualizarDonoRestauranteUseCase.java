package br.com.fiapfood.core.usecases.restaurante.interfaces;

import java.util.UUID;

public interface IAtualizarDonoRestauranteUseCase {
	void atualizar(UUID id, UUID idDono);
}