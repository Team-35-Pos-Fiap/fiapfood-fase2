package br.com.fiapfood.infraestructure.controllers.request.restaurante;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record DadosDonoDto(@NotNull(message = "O campo idDono precisa estar preenchido.")
						   UUID idDono) {

}