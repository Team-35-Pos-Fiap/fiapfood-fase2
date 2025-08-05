package br.com.fiapfood.infraestructure.controllers.request.restaurante;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DadosTipoCulinariaDto(@NotNull(message = "O campo idTipoCulinaria precisa estar preenchido.")
									@Positive(message = "O campo idTipoCulinaria precisa ter valor maior do que 0.")
									Integer idTipoCulinaria) {

}