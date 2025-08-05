package br.com.fiapfood.infraestructure.controllers.request.item;

import jakarta.validation.constraints.NotBlank;

public record DescricaoDto(@NotBlank(message = "O campo descricao precisa ser informado.") String descricao) {

}
