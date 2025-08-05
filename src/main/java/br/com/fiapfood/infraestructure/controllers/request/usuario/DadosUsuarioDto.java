package br.com.fiapfood.infraestructure.controllers.request.usuario;

import java.util.UUID;

public record DadosUsuarioDto(UUID id, String nome, String matricula, String email){
	
}