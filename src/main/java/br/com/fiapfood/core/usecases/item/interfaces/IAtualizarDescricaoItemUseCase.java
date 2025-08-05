package br.com.fiapfood.core.usecases.item.interfaces;

import java.util.UUID;

public interface IAtualizarDescricaoItemUseCase {
	void atualizar(UUID idRestaurante, UUID idItem, String descricao);
}