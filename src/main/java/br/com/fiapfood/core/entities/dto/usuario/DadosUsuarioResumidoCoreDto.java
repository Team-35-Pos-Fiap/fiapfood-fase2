package br.com.fiapfood.core.entities.dto.usuario;

import java.util.UUID;

public record DadosUsuarioResumidoCoreDto(UUID id, String nome, String matricula, String email){
	
}