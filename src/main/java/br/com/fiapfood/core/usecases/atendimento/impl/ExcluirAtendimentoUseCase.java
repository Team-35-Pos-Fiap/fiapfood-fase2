package br.com.fiapfood.core.usecases.atendimento.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiapfood.core.entities.Atendimento;
import br.com.fiapfood.core.entities.Restaurante;
import br.com.fiapfood.core.exceptions.atendimento.AtendimentoRestauranteNaoEncontradoException;
import br.com.fiapfood.core.exceptions.atendimento.ExclusaoAtendimentoRestauranteNaoPermitidoException;
import br.com.fiapfood.core.gateways.interfaces.IRestauranteGateway;
import br.com.fiapfood.core.presenters.RestaurantePresenter;
import br.com.fiapfood.core.usecases.atendimento.interfaces.IExcluirAtendimentoUseCase;

public class ExcluirAtendimentoUseCase implements IExcluirAtendimentoUseCase {

	private final IRestauranteGateway restauranteGateway;
	
	private final String ATENDIMENTO_NAO_ENCONTRADO = "Não foi encontrado nenhum atendimento com o identificador informado para o restaurante.";
	private final String RESTAURANTE_INATIVO = "Não é possível excluir o atendimento pois o restaurante se encontra inativo.";

	public ExcluirAtendimentoUseCase(IRestauranteGateway restauranteGateway) {
		this.restauranteGateway = restauranteGateway;
	}
	
	@Override
	public void excluir(UUID idRestaurante, UUID idAtendimento) {
		final Restaurante restaurante = buscarRestaurante(idRestaurante);

		validarStatusRestaurante(restaurante);
		
		final Atendimento atendimento = buscarAtendimento(restaurante.getAtendimentos(), idAtendimento);
		
		atualizarAtendimentosRestaurante(restaurante, atendimento);
		atualizar(restaurante);
	}
	
	private void atualizarAtendimentosRestaurante(Restaurante restaurante, Atendimento atendimento) {
		atualizarAtendimento(restaurante, atendimento);
	}

	private void atualizarAtendimento(Restaurante restaurante, Atendimento atendimento) {		
		List<Atendimento> atendimentos = getAtendimentos(restaurante);
		
		removerAtendimentoNaLista(atendimentos, atendimento);
		
		limparAtendimentos(restaurante);
		
		associarAtendimentos(restaurante, atendimentos); 
	}

	private List<Atendimento> getAtendimentos(Restaurante restaurante) {
		return new ArrayList<>(restaurante.getAtendimentos());	
	}
	
	private void associarAtendimentos(Restaurante restaurante, List<Atendimento> atendimentos) {
		restaurante.getAtendimentos().addAll(atendimentos);
	}

	private void limparAtendimentos(Restaurante restaurante) {
		restaurante.limparAtendimentos();
	}

	private void removerAtendimentoNaLista(List<Atendimento> atendimentos, Atendimento atendimento) {
		atendimentos.remove(atendimento);
	}

	private Atendimento buscarAtendimento(List<Atendimento> atendimentos, UUID idAtendimento) {
		final Optional<Atendimento> dados = filtrarAtendimento(atendimentos, idAtendimento);
		
		if (dados.isPresent()) {
			return dados.get();
		} else {
			throw new AtendimentoRestauranteNaoEncontradoException(ATENDIMENTO_NAO_ENCONTRADO);
		}
	}

	private Optional<Atendimento> filtrarAtendimento(List<Atendimento> atendimentos, UUID idAtendimento) {		
		return atendimentos.stream().filter(a -> a.getId().equals(idAtendimento)).findFirst();
	}

	private void validarStatusRestaurante(final Restaurante restaurante) {
		if (!restaurante.getIsAtivo()) {
			throw new ExclusaoAtendimentoRestauranteNaoPermitidoException(RESTAURANTE_INATIVO);
		} 
	}

	private Restaurante buscarRestaurante(final UUID id) {
		return restauranteGateway.buscarPorId(id);
	}
	
	private void atualizar(final Restaurante restaurante) {
		restauranteGateway.atualizar(RestaurantePresenter.toRestauranteDto(restaurante));
	}
}
