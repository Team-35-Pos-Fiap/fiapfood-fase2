package br.com.fiapfood.infraestructure.controllers.request.restaurante;

import java.util.List;

import br.com.fiapfood.infraestructure.controllers.request.paginacao.PaginacaoDto;

public record RestaurantePaginacaoDto(List<RestauranteDto> restaurantes, PaginacaoDto paginacao) {}