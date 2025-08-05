package br.com.fiapfood.infraestructure.controllers.request.item;

import java.util.List;

import br.com.fiapfood.infraestructure.controllers.request.paginacao.PaginacaoDto;

public record ItemPaginacaoDto (List<ItemDto> itens, PaginacaoDto paginacao) {

}