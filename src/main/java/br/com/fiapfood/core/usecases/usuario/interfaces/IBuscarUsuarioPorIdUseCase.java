package br.com.fiapfood.core.usecases.usuario.interfaces;

import java.util.UUID;

import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioCoreDto;

public interface IBuscarUsuarioPorIdUseCase {
	DadosUsuarioCoreDto buscar(UUID id);
}