package br.com.fiapfood.core.usecases.item.interfaces;

import java.util.List;
import java.util.UUID;

import br.com.fiapfood.core.entities.dto.item.ItemOutputCoreDto;

public interface IBuscarTodosItensUseCase {
	List<ItemOutputCoreDto> buscar(UUID idRestaurante);
}