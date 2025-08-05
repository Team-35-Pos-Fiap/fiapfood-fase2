package br.com.fiapfood.core.usecases.atendimento.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiapfood.core.entities.Atendimento;
import br.com.fiapfood.core.entities.Restaurante;
import br.com.fiapfood.core.entities.dto.atendimento.AtendimentoCoreDto;
import br.com.fiapfood.core.exceptions.atendimento.AtendimentoRestauranteNaoEncontradoException;
import br.com.fiapfood.core.exceptions.atendimento.AtualizacaoAtendimentoRestauranteNaoPermitidoException;
import br.com.fiapfood.core.exceptions.atendimento.DiaAtendimentoRestauranteInvalidoException;
import br.com.fiapfood.core.gateways.interfaces.IRestauranteGateway;
import br.com.fiapfood.core.presenters.RestaurantePresenter;
import br.com.fiapfood.core.usecases.atendimento.interfaces.IAtualizarAtendimentoUseCase;

public class AtualizarAtendimentoUseCase implements IAtualizarAtendimentoUseCase {

	private final IRestauranteGateway restauranteGateway;
	
	private final String ATENDIMENTO_NAO_ENCONTRADO = "Não foi encontrado nenhum atendimento com o identificador informado para o restaurante.";
	private final String RESTAURANTE_INATIVO = "Não é possível atualizar a imagem do item, pois o restaurante se encontra inativo.";
	private final String ATENDIMENTO_DUPLICADO = "Não é possível atualizar o atendimento, pois já existe um outro atendimento para o mesmo dia.";

	public AtualizarAtendimentoUseCase(IRestauranteGateway restauranteGateway) {
		this.restauranteGateway = restauranteGateway;
	}
	
	@Override
	public void atualizar(UUID idRestaurante, AtendimentoCoreDto atendimentoDto) {
		final Restaurante restaurante = buscarRestaurante(idRestaurante);

		validarStatusRestaurante(restaurante);
		validaDiaAtendimento(restaurante.getAtendimentos(), atendimentoDto);
		
		final Atendimento atendimento = buscarAtendimento(restaurante.getAtendimentos(), atendimentoDto.id());
		
		atualizarAtendimento(atendimento, atendimentoDto);
		
		atualizarAtendimentosRestaurante(restaurante, atendimento);
		atualizar(restaurante);
	}

	private void atualizarAtendimentosRestaurante(Restaurante restaurante, Atendimento atendimento) {
		atualizarAtendimento(restaurante, atendimento, buscarPosicaoNaListaAtendimentos(restaurante, atendimento));
	}

	private void atualizarAtendimento(Restaurante restaurante, Atendimento atendimento, int indice) {		
		List<Atendimento> atendimentos = getAtendimentos(restaurante);
		
		atualizarAtendimentoNaLista(atendimentos, indice, atendimento);
		
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

	private void atualizarAtendimentoNaLista(List<Atendimento> atendimentos, int indice, Atendimento atendimento) {
		atendimentos.set(indice, atendimento);
	}

	private int buscarPosicaoNaListaAtendimentos(Restaurante restaurante, Atendimento atendimento) {
		return restaurante.getAtendimentos().indexOf(atendimento);
	}

	private void atualizarAtendimento(Atendimento atendimento, AtendimentoCoreDto atendimentoDto) {
		atendimento.atualizarDados(atendimentoDto.dia(), atendimentoDto.inicioAtendimento(), atendimentoDto.terminoAtendimento());
	}

	private void validaDiaAtendimento(List<Atendimento> atendimentos, AtendimentoCoreDto atendimento) {
		if(atendimentos.stream().filter(a -> a.getDia().equals(atendimento.dia()) && !a.getId().equals(atendimento.id())).findAny().isPresent()) {
			throw new DiaAtendimentoRestauranteInvalidoException(ATENDIMENTO_DUPLICADO);
		}
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
			throw new AtualizacaoAtendimentoRestauranteNaoPermitidoException(RESTAURANTE_INATIVO);
		} 
	}

	private Restaurante buscarRestaurante(final UUID id) {
		return restauranteGateway.buscarPorId(id);
	}
	
	private void atualizar(final Restaurante restaurante) {
		restauranteGateway.atualizar(RestaurantePresenter.toRestauranteDto(restaurante));
	}
}
