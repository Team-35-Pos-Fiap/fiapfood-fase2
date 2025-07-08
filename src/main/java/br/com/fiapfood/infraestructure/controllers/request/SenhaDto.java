package br.com.fiapfood.infraestructure.controllers.request;

import jakarta.validation.constraints.NotBlank;


public record SenhaDto(@NotBlank(message = "O campo senha precisa estar preenchido.")
                                 String senha) { }
