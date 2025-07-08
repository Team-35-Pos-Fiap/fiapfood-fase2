package br.com.fiapfood.core.controllers.interfaces;

import java.util.List;

import br.com.fiapfood.core.entities.dto.PerfilDto;

public interface IPerfilCoreController {

	List<PerfilDto> buscarTodos();
	PerfilDto buscarPorId(Integer id);
}