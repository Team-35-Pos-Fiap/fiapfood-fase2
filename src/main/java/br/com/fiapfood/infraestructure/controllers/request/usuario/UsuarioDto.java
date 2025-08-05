package br.com.fiapfood.infraestructure.controllers.request.usuario;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.fiapfood.infraestructure.controllers.request.endereco.EnderecoDto;
import br.com.fiapfood.infraestructure.controllers.request.login.LoginDto;
import br.com.fiapfood.infraestructure.controllers.request.perfil.PerfilDto;

public record UsuarioDto(UUID id, String nome, PerfilDto perfil, LoginDto login, 
						 Boolean isAtivo, String email, LocalDateTime dataCriacao, 
						 LocalDateTime dataAtualizacao, EnderecoDto endereco){
	
}