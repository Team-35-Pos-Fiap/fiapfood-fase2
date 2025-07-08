package br.com.fiapfood.core.usecases.usuario.interfaces;

import java.util.UUID;

public interface IAtualizarEmailUsuarioUseCase {
	void atualizar(UUID id, String email);
}