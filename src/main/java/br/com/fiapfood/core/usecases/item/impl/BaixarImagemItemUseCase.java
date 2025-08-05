package br.com.fiapfood.core.usecases.item.impl;

import java.util.Optional;
import java.util.UUID;

import br.com.fiapfood.core.entities.Imagem;
import br.com.fiapfood.core.entities.Item;
import br.com.fiapfood.core.entities.Restaurante;
import br.com.fiapfood.core.entities.dto.item.ImagemCoreDto;
import br.com.fiapfood.core.exceptions.item.ItemNaoEncontradoException;
import br.com.fiapfood.core.gateways.interfaces.IRestauranteGateway;
import br.com.fiapfood.core.presenters.ImagemPresenter;
import br.com.fiapfood.core.usecases.item.interfaces.IBaixarImagemItemUseCase;

public class BaixarImagemItemUseCase implements IBaixarImagemItemUseCase {

	private final IRestauranteGateway restauranteGateway;
	
	private final String ITEM_NAO_ENCONTRADO = "Não foi encontrado nenhum item com o id informado para o restaurante.";

	public BaixarImagemItemUseCase(IRestauranteGateway restauranteGateway) {
		this.restauranteGateway = restauranteGateway;
	}

	@Override
	public ImagemCoreDto baixar(UUID idRestaurante, UUID idItem) {
		final Restaurante restaurante = buscarRestaurante(idRestaurante);;
		final Item item  = buscarItem(restaurante, idItem);
		
		return toImagemDto(item.getImagem());
	}
	
	private Item buscarItem(Restaurante restaurante, final UUID idItem) {
		Optional<Item> item = filtrarItem(restaurante, idItem);
		
		if(item != null) {
			return item.get();
		} else {
			throw new ItemNaoEncontradoException(ITEM_NAO_ENCONTRADO);			
		}
	}
	
	private Restaurante buscarRestaurante(UUID idRestaurante) {
		return restauranteGateway.buscarPorId(idRestaurante);
	}
	
	private Optional<Item> filtrarItem(Restaurante restaurante, final UUID idItem) {
		return restaurante.getItens().stream().filter(i -> i.getId().equals(idItem)).findFirst();
	}
	
	private ImagemCoreDto toImagemDto(Imagem imagem) {
		return ImagemPresenter.toImagemDto(imagem);
	}
}