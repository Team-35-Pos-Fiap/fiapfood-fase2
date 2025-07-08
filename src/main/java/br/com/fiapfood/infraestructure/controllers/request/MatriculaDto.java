package br.com.fiapfood.infraestructure.controllers.request;

import jakarta.validation.constraints.NotBlank;


public record MatriculaDto(@NotBlank(message = "O campo senha precisa estar preenchido.")
                           String matricula) { }
