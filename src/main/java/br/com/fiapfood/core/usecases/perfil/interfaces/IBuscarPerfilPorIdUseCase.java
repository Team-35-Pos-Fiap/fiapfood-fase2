package br.com.fiapfood.core.usecases.perfil.interfaces;

import br.com.fiapfood.core.entities.dto.perfil.PerfilCoreDto;

public interface IBuscarPerfilPorIdUseCase {
	PerfilCoreDto buscar(Integer id);
}