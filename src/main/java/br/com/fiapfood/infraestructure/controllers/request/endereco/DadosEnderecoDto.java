package br.com.fiapfood.infraestructure.controllers.request.endereco;

import jakarta.validation.constraints.NotBlank;

public record DadosEnderecoDto(@NotBlank(message = "O campo cidade precisa ser informado.")
							   String cidade,
							   
							   @NotBlank(message = "O campo cep precisa ser informado.") 
							   String cep, 
	 
						       @NotBlank(message = "O campo bairro precisa ser informado.")
						       String bairro, 
						  
						       @NotBlank(message = "O campo endereco precisa ser informado.")
						       String endereco, 
						  
							   @NotBlank(message = "O campo estado precisa ser informado.")
							   String estado,
						       Integer numero,
						       String complemento) { }