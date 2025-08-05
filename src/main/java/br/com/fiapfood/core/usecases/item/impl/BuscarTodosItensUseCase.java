package br.com.fiapfood.core.usecases.item.impl;

import java.util.List;
import java.util.UUID;

import br.com.fiapfood.core.entities.Item;
import br.com.fiapfood.core.entities.Restaurante;
import br.com.fiapfood.core.entities.dto.item.ItemOutputCoreDto;
import br.com.fiapfood.core.gateways.interfaces.IRestauranteGateway;
import br.com.fiapfood.core.presenters.ItemPresenter;
import br.com.fiapfood.core.usecases.item.interfaces.IBuscarTodosItensUseCase;
import br.com.fiapfood.core.utils.ImagemUtils;

public class BuscarTodosItensUseCase implements IBuscarTodosItensUseCase{

	private final IRestauranteGateway restauranteGateway;
	
	public BuscarTodosItensUseCase(IRestauranteGateway restauranteGateway) {
		this.restauranteGateway = restauranteGateway;
	}
	
	@Override
	public List<ItemOutputCoreDto> buscar(final UUID idRestaurante) {
		Restaurante restaurante = buscarRestaurante(idRestaurante);

		return buscarItens(restaurante);
	}
	
	private Restaurante buscarRestaurante(UUID idRestaurante) {
		return restauranteGateway.buscarPorId(idRestaurante);
	}
	
	private List<ItemOutputCoreDto> buscarItens(Restaurante restaurante) {
		return restaurante.getItens().stream().map(i -> toItemOutputDto(restaurante, i)).toList();
	}

	private ItemOutputCoreDto toItemOutputDto(Restaurante restaurante, Item item) {
		return ItemPresenter.toItemDto(item, ImagemUtils.prepararLinkDownloadImagem(restaurante.getId(), item.getId()));
	}	
}