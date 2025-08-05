package br.com.fiapfood.core.usecases.atendimento.interfaces;

import java.util.UUID;

import br.com.fiapfood.core.entities.dto.atendimento.AtendimentoCoreDto;

public interface IAdicionarAtendimentoUseCase {
	void adicionar(UUID idRestaurante, AtendimentoCoreDto atendimento);
}