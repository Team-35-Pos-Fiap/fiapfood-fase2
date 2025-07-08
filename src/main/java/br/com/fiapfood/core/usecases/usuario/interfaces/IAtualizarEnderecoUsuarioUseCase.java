package br.com.fiapfood.core.usecases.usuario.interfaces;

import java.util.UUID;

import br.com.fiapfood.core.entities.dto.DadosEnderecoDto;

public interface IAtualizarEnderecoUsuarioUseCase {
	void atualizar(UUID id, DadosEnderecoDto dadosEndereco);
}