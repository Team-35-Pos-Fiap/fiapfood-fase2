package br.com.fiapfood.core.usecases.restaurante.interfaces;

import java.util.UUID;

public interface IAtualizarNomeRestauranteUseCase {
	void atualizar(UUID id, String nome);
}