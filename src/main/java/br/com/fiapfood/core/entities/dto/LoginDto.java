package br.com.fiapfood.core.entities.dto;

import java.util.UUID;

public record LoginDto(UUID id, String matricula, String senha) {

}