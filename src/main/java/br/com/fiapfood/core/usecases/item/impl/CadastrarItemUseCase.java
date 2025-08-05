package br.com.fiapfood.core.usecases.item.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.fiapfood.core.entities.Item;
import br.com.fiapfood.core.entities.Restaurante;
import br.com.fiapfood.core.entities.dto.item.ImagemCoreDto;
import br.com.fiapfood.core.exceptions.item.CadastrarItemNaoPermitidoException;
import br.com.fiapfood.core.gateways.interfaces.IRestauranteGateway;
import br.com.fiapfood.core.presenters.ItemPresenter;
import br.com.fiapfood.core.presenters.RestaurantePresenter;
import br.com.fiapfood.core.usecases.item.interfaces.ICadastrarItemUseCase;

public class CadastrarItemUseCase implements ICadastrarItemUseCase{

	private final IRestauranteGateway restauranteGateway;
	private final String RESTAURANTE_INATIVO = "Não é possível cadastrar o item, pois o restaurante se encontra inativo.";
	
	public CadastrarItemUseCase(IRestauranteGateway restauranteGateway) {
		this.restauranteGateway = restauranteGateway;
	}
	
	@Override
	public void cadastrar(UUID idRestaurante, String nome, String descricao, BigDecimal preco, Boolean disponivelParaConsumoPresencial, ImagemCoreDto dadosImagem) {
		final Restaurante restaurante = buscarRestaurante(idRestaurante);

		validarStatusRestaurante(restaurante);

		final Item item = toItem(nome, descricao, preco, disponivelParaConsumoPresencial, dadosImagem);

		atualizarItensRestaurante(restaurante, item);
		atualizar(restaurante);
	}

	private Restaurante buscarRestaurante(UUID idRestaurante) {
		return restauranteGateway.buscarPorId(idRestaurante);
	}
	
	private void validarStatusRestaurante(final Restaurante restaurante) {
		if (!restaurante.getIsAtivo()) {
			throw new CadastrarItemNaoPermitidoException(RESTAURANTE_INATIVO);
		} 
	}
	
	private void atualizar(final Restaurante restaurante) {
		restauranteGateway.atualizar(RestaurantePresenter.toRestauranteDto(restaurante));
	}
	
	private void atualizarItensRestaurante(Restaurante restaurante, Item item) {
		atualizarItem(restaurante, item);
	}
	
	private void atualizarItem(Restaurante restaurante, Item item) {		
		List<Item> itens = getItens(restaurante);
		
		atualizarItemNaLista(itens, item);
		
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
	
	private void atualizarItemNaLista(List<Item> itens, Item item) {
		itens.add(item);
	}
	
	private Item toItem(String nome, String descricao, BigDecimal preco, Boolean disponivelParaConsumoPresencial, ImagemCoreDto dadosImagem) {
		return ItemPresenter.toItem(nome, descricao, preco, disponivelParaConsumoPresencial, dadosImagem);
	}
}