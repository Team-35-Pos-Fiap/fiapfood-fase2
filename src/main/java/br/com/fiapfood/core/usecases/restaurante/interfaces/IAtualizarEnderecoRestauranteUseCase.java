package br.com.fiapfood.core.usecases.restaurante.interfaces;

import java.util.UUID;

import br.com.fiapfood.core.entities.dto.endereco.DadosEnderecoCoreDto;

public interface IAtualizarEnderecoRestauranteUseCase {
	void atualizar(UUID id, DadosEnderecoCoreDto dadosEndereco);
}