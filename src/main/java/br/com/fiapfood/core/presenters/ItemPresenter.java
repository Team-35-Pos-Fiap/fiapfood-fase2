package br.com.fiapfood.core.presenters;

import java.math.BigDecimal;
import java.util.List;

import br.com.fiapfood.core.entities.Item;
import br.com.fiapfood.core.entities.dto.item.ImagemCoreDto;
import br.com.fiapfood.core.entities.dto.item.ItemInputDto;
import br.com.fiapfood.core.entities.dto.item.ItemOutputCoreDto;
import br.com.fiapfood.infraestructure.controllers.request.item.ItemDto;
import br.com.fiapfood.infraestructure.entities.ItemEntity;

public class ItemPresenter {

	public static ItemInputDto toItemDto(ItemEntity item) {
		return new ItemInputDto(item.getId(), item.getNome(), item.getDescricao(), 
								item.getPreco(), item.getIsDisponivelConsumoPresencial(), 
								item.getIsDisponivel(), ImagemPresenter.toImagemDto(item.getImagem()), null);
	}
	
	public static ItemInputDto toItemDto(Item item) {
		return new ItemInputDto(item.getId(), item.getNome(), item.getDescricao(), 
								item.getPreco(), item.getIsDisponivelConsumoPresencial(), 
								item.getIsDisponivel(), ImagemPresenter.toImagemDto(item.getImagem()), item.getIdRestaurante());
	}
	
	public static ItemOutputCoreDto toItemDto(Item item, String linkFoto) {
		return new ItemOutputCoreDto(item.getId(), item.getNome(), item.getDescricao(), item.getPreco(), 
								     item.getIsDisponivelConsumoPresencial(), item.getIsDisponivel(), linkFoto);
	}

	public static Item toItem(ItemInputDto item) {
		return Item.criar(item.id(), item.nome(), item.descricao(), item.preco(), item.isDisponivelConsumoPresencial(), 
				          item.isDisponivel(), ImagemPresenter.toImagem(item.imagem()));
	}

	public static List<Item> toListItem(List<ItemInputDto> itens) {
		return itens.stream().map(i -> ItemPresenter.toItem(i)).toList();
	}

	public static Item toItem(String nome, String descricao, BigDecimal preco, Boolean disponivelParaConsumoPresencial, ImagemCoreDto dadosImagem) {
		return Item.criar(null, nome, descricao, preco, disponivelParaConsumoPresencial, true, ImagemPresenter.toImagem(dadosImagem));
	}

	public static ItemDto toItemOutputDto(ItemOutputCoreDto item) {
		return new ItemDto(item.id(), item.nome(), item.descricao(), item.preco(),
								 item.isDisponivelConsumoPresencial(), item.isDisponivel(),
								 item.linkFoto());
	}

	public static List<ItemDto> toListItemOutputDto(List<ItemOutputCoreDto> itens) {
		return itens.stream().map(i -> ItemPresenter.toItemOutputDto(i)).toList();
	}

	public static List<ItemInputDto> toListItemInputDto(List<ItemEntity> itens) {
		return itens.stream().map(i -> ItemPresenter.toItemDto(i)).toList();
	}

	public static List<ItemInputDto> toListItemDto(List<Item> itens) {
		return itens.stream().map(i -> ItemPresenter.toItemDto(i)).toList();
	}

	public static List<ItemEntity> toListItemEntity(List<ItemInputDto> itens) {
		return itens.stream().map(i -> ItemPresenter.toItemItemEntity(i)).toList();
	}

	private static ItemEntity toItemItemEntity(ItemInputDto item) {
		return new ItemEntity(item.id(), item.nome(), item.descricao(), item.preco(), 
							  item.isDisponivelConsumoPresencial(), item.isDisponivel(), ImagemPresenter.toImagemEntity(item.imagem()));
	}
}