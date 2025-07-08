package br.com.fiapfood.core.usecases.usuario.interfaces;

import java.util.UUID;

public interface IAtualizarNomeUsuarioUseCase {
	void atualizar(UUID id, String nome);
}