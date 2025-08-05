package br.com.fiapfood.core.usecases.login.interfaces;

import java.util.UUID;

public interface IAtualizarSenhaUseCase {
	void atualizar(UUID id, String senha);
}