package br.com.fiapfood.core.usecases.perfil.interfaces;

import br.com.fiapfood.core.entities.dto.PerfilDto;

public interface IBuscarPerfilPorIdUseCase {
	PerfilDto buscar(Integer id);
}