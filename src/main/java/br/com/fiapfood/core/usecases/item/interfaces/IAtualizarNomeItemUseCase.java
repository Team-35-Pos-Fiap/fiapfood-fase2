package br.com.fiapfood.core.usecases.item.interfaces;

import java.util.UUID;

public interface IAtualizarNomeItemUseCase {
	void atualizar(UUID idRestaurante, UUID idItem, String nome);
}