package br.com.fiapfood.core.usecases.item.interfaces;

import java.util.UUID;

import br.com.fiapfood.core.entities.dto.item.ImagemCoreDto;

public interface IAtualizarImagemItemUseCase {
	void atualizar(UUID idRestaurante, UUID idItem, ImagemCoreDto dadosImagem);
}