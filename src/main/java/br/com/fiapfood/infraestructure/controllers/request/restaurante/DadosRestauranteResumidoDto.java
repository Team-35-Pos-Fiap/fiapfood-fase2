package br.com.fiapfood.infraestructure.controllers.request.restaurante;

import java.util.UUID;

public record DadosRestauranteResumidoDto(UUID id, String nome, Boolean isAtivo) {}