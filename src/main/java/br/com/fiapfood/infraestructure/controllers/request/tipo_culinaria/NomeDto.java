package br.com.fiapfood.infraestructure.controllers.request.tipo_culinaria;

import jakarta.validation.constraints.NotBlank;

public record NomeDto(@NotBlank(message = "O campo nome precisa estar preenchido.") 
					  String nome) {

}