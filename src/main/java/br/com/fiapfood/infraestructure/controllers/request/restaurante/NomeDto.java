package br.com.fiapfood.infraestructure.controllers.request.restaurante;

import jakarta.validation.constraints.NotBlank;

public record NomeDto(@NotBlank(message = "O campo nome precisa estar preenchido.") 
					  String nome) {

}