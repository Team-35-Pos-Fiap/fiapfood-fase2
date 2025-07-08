package br.com.fiapfood.core.usecases.usuario.interfaces;

import java.util.UUID;

import br.com.fiapfood.core.entities.dto.UsuarioDto;

public interface IBuscarUsuarioPorIdUseCase {
	UsuarioDto buscar(UUID id);
}