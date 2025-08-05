package br.com.fiapfood.infraestructure.controllers.request.login;

import jakarta.validation.constraints.NotBlank;


public record MatriculaDto(@NotBlank(message = "O campo matricula precisa estar preenchido.")
                           String matricula) { }
