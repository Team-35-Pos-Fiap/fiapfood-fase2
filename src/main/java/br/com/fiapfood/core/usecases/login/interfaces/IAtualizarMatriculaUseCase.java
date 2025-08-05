package br.com.fiapfood.core.usecases.login.interfaces;

import java.util.UUID;

public interface IAtualizarMatriculaUseCase {
	void atualizar(UUID id, String matriculaNova);
}