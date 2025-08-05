package br.com.fiapfood.core.usecases.item.impl;

import java.util.Optional;
import java.util.UUID;

import br.com.fiapfood.core.entities.Item;
import br.com.fiapfood.core.entities.Restaurante;
import br.com.fiapfood.core.entities.dto.item.ItemOutputCoreDto;
import br.com.fiapfood.core.exceptions.item.ItemNaoEncontradoException;
import br.com.fiapfood.core.gateways.interfaces.IRestauranteGateway;
import br.com.fiapfood.core.presenters.ItemPresenter;
import br.com.fiapfood.core.usecases.item.interfaces.IBuscarItemPorIdUseCase;
import br.com.fiapfood.core.utils.ImagemUtils;

public class BuscarItemPorIdUseCase implements IBuscarItemPorIdUseCase{

	private final IRestauranteGateway restauranteGateway;
	private final String ITEM_NAO_ENCONTRADO = "Não foi encontrado nenhum item com o id informado para o restaurante.";

	public BuscarItemPorIdUseCase(IRestauranteGateway restauranteGateway) {
		this.restauranteGateway = restauranteGateway;
	}
	
	@Override
	public ItemOutputCoreDto buscar(final UUID idRestaurante, final UUID id) {
		Restaurante restaurante = buscarRestaurante(idRestaurante);
		
		Optional<Item> item = buscarItem(restaurante, id);
		
		if(item.isPresent()) {
			return toItemOutputDto(restaurante, item.get());			
		} else {
			throw new ItemNaoEncontradoException(ITEM_NAO_ENCONTRADO);			
		}
	}

	private Restaurante buscarRestaurante(UUID idRestaurante) {
		return restauranteGateway.buscarPorId(idRestaurante);
	}
	
	private ItemOutputCoreDto toItemOutputDto(Restaurante restaurante, Item item) {
		return ItemPresenter.toItemDto(item, ImagemUtils.prepararLinkDownloadImagem(restaurante.getId(), item.getId()));
	}
	
	private Optional<Item> buscarItem(Restaurante restaurante, UUID idItem) {
		return restaurante.getItens().stream().filter(i -> i.getId().equals(idItem)).findFirst();
	}
}