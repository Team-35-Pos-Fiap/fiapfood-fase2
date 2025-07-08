package br.com.fiapfood.core.entities.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioDto(UUID id, String nome, PerfilDto perfil, LoginDto login, 
						 Boolean isAtivo, String email, LocalDateTime dataCriacao, 
						 LocalDateTime dataAtualizacao, EnderecoDto endereco){
	
}