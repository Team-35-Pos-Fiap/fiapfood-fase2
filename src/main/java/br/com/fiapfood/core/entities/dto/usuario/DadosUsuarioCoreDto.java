package br.com.fiapfood.core.entities.dto.usuario;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.fiapfood.core.entities.dto.endereco.EnderecoCoreDto;
import br.com.fiapfood.core.entities.dto.login.LoginCoreDto;
import br.com.fiapfood.core.entities.dto.perfil.PerfilCoreDto;

public record DadosUsuarioCoreDto(UUID id, String nome, PerfilCoreDto perfil, LoginCoreDto login, 
						 		  Boolean isAtivo, String email, LocalDateTime dataCriacao, 
						 		  LocalDateTime dataAtualizacao, EnderecoCoreDto endereco){
	
}