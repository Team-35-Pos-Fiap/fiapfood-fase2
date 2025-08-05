package br.com.fiapfood.infraestructure.controllers.request.item;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record PrecoDto(@NotNull(message = "O campo bairro precisa ser informado.")
					   @PositiveOrZero(message = "O preço informado precisa ser maior ou igual a 0.")		   
					   BigDecimal preco) {

}
