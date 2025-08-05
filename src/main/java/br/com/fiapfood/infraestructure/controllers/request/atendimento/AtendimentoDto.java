package br.com.fiapfood.infraestructure.controllers.request.atendimento;

import java.time.LocalTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record AtendimentoDto(UUID id, 
							 
							 @NotBlank(message = "O campo dia precisa ser informado.")
							 String dia, 
							 
							 @NotBlank(message = "O campo inicioAtendimento precisa ser informado.")
							 LocalTime inicioAtendimento, 

							 @NotBlank(message = "O campo terminoAtendimento precisa ser informado.")
							 LocalTime terminoAtendimento) {

}