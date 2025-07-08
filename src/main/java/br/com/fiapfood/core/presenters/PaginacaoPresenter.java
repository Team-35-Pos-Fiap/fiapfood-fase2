package br.com.fiapfood.core.presenters;

import br.com.fiapfood.core.entities.dto.PaginacaoDto;

public class PaginacaoPresenter {

	public static PaginacaoDto toDto(Integer paginaAtual, Integer totalPaginas, Integer totalItens) {
		return new PaginacaoDto(paginaAtual + 1, totalPaginas, totalItens);
	}
}