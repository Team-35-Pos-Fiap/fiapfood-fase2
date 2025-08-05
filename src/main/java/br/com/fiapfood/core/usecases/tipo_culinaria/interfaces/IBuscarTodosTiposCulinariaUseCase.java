package br.com.fiapfood.core.usecases.tipo_culinaria.interfaces;

import java.util.List;

import br.com.fiapfood.core.entities.dto.tipo_culinaria.TipoCulinariaCoreDto;

public interface IBuscarTodosTiposCulinariaUseCase {
	List<TipoCulinariaCoreDto> buscar();
}