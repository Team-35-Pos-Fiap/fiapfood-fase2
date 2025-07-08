package br.com.fiapfood.core.usecases.perfil.interfaces;

import java.util.List;

import br.com.fiapfood.core.entities.dto.PerfilDto;

public interface IBuscarTodosPerfisUseCase {
	List<PerfilDto> buscar();
}