package br.com.fiapfood.core.usecases.item.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiapfood.core.entities.Imagem;
import br.com.fiapfood.core.entities.Item;
import br.com.fiapfood.core.entities.Restaurante;
import br.com.fiapfood.core.entities.dto.item.ImagemCoreDto;
import br.com.fiapfood.core.exceptions.item.AtualizacaoImagemItemNaoPermitidaException;
import br.com.fiapfood.core.exceptions.item.ItemNaoEncontradoException;
import br.com.fiapfood.core.gateways.interfaces.IRestauranteGateway;
import br.com.fiapfood.core.presenters.RestaurantePresenter;
import br.com.fiapfood.core.usecases.item.interfaces.IAtualizarImagemItemUseCase;

public class AtualizarImagemItemUseCase implements IAtualizarImagemItemUseCase {

	private final IRestauranteGateway restauranteGateway;
	private final String RESTAURANTE_INATIVO = "Não é possível atualizar o atendimento pois o restaurante se encontra inativo.";
	private final String ITEM_NAO_ENCONTRADO = "Não foi encontrado nenhum item com o id informado para o restaurante.";

	public AtualizarImagemItemUseCase(IRestauranteGateway restauranteGateway) {
		this.restauranteGateway = restauranteGateway;
	}

	@Override
	public void atualizar(UUID idRestaurante, UUID idItem, ImagemCoreDto dadosImagem) {
		final Restaurante restaurante = buscarRestaurante(idRestaurante);

		validarStatusRestaurante(restaurante);
		
		final Item item = buscarItem(restaurante, idItem);
		final Imagem imagem = item.getImagem();
	
		atualizarDados(imagem, dadosImagem);

		atualizarItem(item, imagem);
		atualizarItensRestaurante(restaurante, item);
		atualizar(restaurante);
	}

	private void atualizarItem(Item item, Imagem imagem) {
		item.atualizarImagem(imagem);
	}

	private void atualizarDados(Imagem imagem, ImagemCoreDto dadosImagem) {
		imagem.atualizarNome(dadosImagem.nome());
		imagem.atualizarTipo(dadosImagem.tipo());
		imagem.atualizarConteudo(dadosImagem.conteudo());
	}

	private Item buscarItem(Restaurante restaurante, final UUID idItem) {
		Optional<Item> item = filtrarItem(restaurante, idItem);
		
		if(item != null) {
			return item.get();
		} else {
			throw new ItemNaoEncontradoException(ITEM_NAO_ENCONTRADO);			
		}
	}
	
	private Optional<Item> filtrarItem(Restaurante restaurante, final UUID idItem) {
		return restaurante.getItens().stream().filter(i -> i.getId().equals(idItem)).findFirst();
	}
	
	private Restaurante buscarRestaurante(UUID idRestaurante) {
		return restauranteGateway.buscarPorId(idRestaurante);
	}

	private void validarStatusRestaurante(final Restaurante restaurante) {
		if (!restaurante.getIsAtivo()) {
			throw new AtualizacaoImagemItemNaoPermitidaException(RESTAURANTE_INATIVO);
		} 
	}
	
	private void atualizar(final Restaurante restaurante) {
		restauranteGateway.atualizar(RestaurantePresenter.toRestauranteDto(restaurante));
	}
	
	private void atualizarItensRestaurante(Restaurante restaurante, Item item) {
		atualizarItem(restaurante, item, buscarPosicaoNaListaItens(restaurante, item));
	}

	private void atualizarItem(Restaurante restaurante, Item item, int indice) {		
		List<Item> itens = getItens(restaurante);
		
		atualizarItemNaLista(itens, indice, item);
		
		limparItens(restaurante);
		
		associarItens(restaurante, itens); 
	}

	private List<Item> getItens(Restaurante restaurante) {
		return new ArrayList<>(restaurante.getItens());	
	}
	
	private void associarItens(Restaurante restaurante, List<Item> itens) {
		restaurante.getItens().addAll(itens);
	}

	private void limparItens(Restaurante restaurante) {
		restaurante.limparItens();
	}

	private void atualizarItemNaLista(List<Item> itens, int indice, Item item) {
		itens.set(indice, item);
	}

	private int buscarPosicaoNaListaItens(Restaurante restaurante, Item item) {
		return restaurante.getItens().indexOf(item);
	}
}