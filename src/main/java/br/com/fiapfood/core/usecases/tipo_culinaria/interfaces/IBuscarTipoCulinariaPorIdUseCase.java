package br.com.fiapfood.core.usecases.tipo_culinaria.interfaces;

import br.com.fiapfood.core.entities.dto.tipo_culinaria.TipoCulinariaCoreDto;

public interface IBuscarTipoCulinariaPorIdUseCase {
	TipoCulinariaCoreDto buscar(Integer id);
}