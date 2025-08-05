package br.com.fiapfood.core.usecases.atendimento.interfaces;

import java.util.UUID;

import br.com.fiapfood.core.entities.dto.atendimento.AtendimentoCoreDto;

public interface IAtualizarAtendimentoUseCase {
	void atualizar(UUID idRestaurante, AtendimentoCoreDto atendimento);
}