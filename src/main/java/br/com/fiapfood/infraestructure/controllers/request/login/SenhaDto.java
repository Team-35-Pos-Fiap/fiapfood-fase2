package br.com.fiapfood.infraestructure.controllers.request.login;

import jakarta.validation.constraints.NotBlank;


public record SenhaDto(@NotBlank(message = "O campo senha precisa estar preenchido.") 
					   String senha) { }
