package br.com.fiapfood.core.entities.dto.login;

import java.util.UUID;

public record LoginCoreDto(UUID id, String matricula, String senha) {

}