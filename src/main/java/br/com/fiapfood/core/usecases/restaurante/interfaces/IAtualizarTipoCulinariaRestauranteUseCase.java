package br.com.fiapfood.core.usecases.restaurante.interfaces;

import java.util.UUID;

public interface IAtualizarTipoCulinariaRestauranteUseCase {
	void atualizar(UUID id, Integer idTipoCulinaria);
}