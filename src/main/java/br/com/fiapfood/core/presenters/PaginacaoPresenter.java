package br.com.fiapfood.core.presenters;

import br.com.fiapfood.core.entities.dto.paginacao.PaginacaoCoreDto;
import br.com.fiapfood.infraestructure.controllers.request.paginacao.PaginacaoDto;

public class PaginacaoPresenter {

	public static PaginacaoCoreDto toDto(Integer paginaAtual, Integer totalPaginas, Integer totalItens) {
		return new PaginacaoCoreDto(paginaAtual + 1, totalPaginas, totalItens);
	}

	public static PaginacaoDto toPaginacaoDto(PaginacaoCoreDto paginacao) {
		return new PaginacaoDto(paginacao.paginaAtual(), paginacao.totalPaginas(), paginacao.totalItens());
	}
}