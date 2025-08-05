package br.com.fiapfood.core.usecases.item.interfaces;

import java.math.BigDecimal;
import java.util.UUID;

public interface IAtualizarPrecoItemUseCase {
	void atualizar(UUID idRestaurante, UUID idItem, BigDecimal preco);
}