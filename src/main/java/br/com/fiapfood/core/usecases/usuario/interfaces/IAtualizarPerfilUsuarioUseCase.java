package br.com.fiapfood.core.usecases.usuario.interfaces;

import java.util.UUID;

public interface IAtualizarPerfilUsuarioUseCase {
	void atualizar(UUID id, Integer idPerfil);
}