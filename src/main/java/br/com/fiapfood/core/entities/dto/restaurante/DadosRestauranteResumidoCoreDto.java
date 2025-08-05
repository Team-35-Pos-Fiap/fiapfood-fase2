package br.com.fiapfood.core.entities.dto.restaurante;

import java.util.UUID;

public record DadosRestauranteResumidoCoreDto(UUID id, String nome, Boolean isAtivo) {}