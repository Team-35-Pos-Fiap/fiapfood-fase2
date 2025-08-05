package br.com.fiapfood.infraestructure.controllers.request.item;

import jakarta.validation.constraints.NotNull;

public record DisponibilidadeDto(@NotNull(message = "O campo isDisponivel precisa ser informado.") Boolean isDisponivel) {

}
