package br.com.fiapfood.core.usecases.item.interfaces;

import java.util.UUID;

public interface IAtualizarDisponibilidadeItemUseCase {
	void atualizar(UUID idRestaurante, UUID idItem, Boolean isDisponivel);
}