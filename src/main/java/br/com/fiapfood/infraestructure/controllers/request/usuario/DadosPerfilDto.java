package br.com.fiapfood.infraestructure.controllers.request.usuario;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DadosPerfilDto(@NotNull(message = "O campo idPerfil precisa estar preenchido.")
							 @Positive(message = "O campo idPerfil precisa ter valor maior do que 0.")
							 Integer idPerfil) { }