package br.com.fiapfood.core.usecases.atendimento.interfaces;

import java.util.UUID;

public interface IExcluirAtendimentoUseCase {
	void excluir(UUID idRestaurante, UUID idAtendimento);
}