package br.com.fiapfood.infraestructure.controllers.request.login;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(UUID id,
					   @NotBlank(message = "O campo matricula precisa ser informado.") 
					   String matricula, 
					   
					   @NotBlank(message = "O campo senha precisa ser informado.") 
					   String senha) {

}