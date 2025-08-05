package br.com.fiapfood.infraestructure.controllers.request.item;

import jakarta.validation.constraints.NotBlank;

public record NomeDto(@NotBlank(message = "O campo nome precisa ser informado.") 
                      String nome) {

}
