package br.com.fiapfood.core.usecases.item.interfaces;

import java.util.UUID;

import br.com.fiapfood.core.entities.dto.item.ImagemCoreDto;

public interface IBaixarImagemItemUseCase {
	ImagemCoreDto baixar(UUID idRestaurante, UUID idItem);
}