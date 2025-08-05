package br.com.fiapfood.core.entities.dto.restaurante;

import java.util.List;

import br.com.fiapfood.core.entities.dto.paginacao.PaginacaoCoreDto;

public record RestaurantePaginacaoInputDto(List<DadosRestauranteDto> restaurantes, PaginacaoCoreDto paginacao) {}