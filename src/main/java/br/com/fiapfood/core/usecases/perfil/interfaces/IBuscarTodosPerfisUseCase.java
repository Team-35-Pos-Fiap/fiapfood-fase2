package br.com.fiapfood.core.usecases.perfil.interfaces;

import java.util.List;

import br.com.fiapfood.core.entities.dto.perfil.PerfilCoreDto;

public interface IBuscarTodosPerfisUseCase {
	List<PerfilCoreDto> buscar();
}