package br.com.fiapfood.core.entities.dto.usuario;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.fiapfood.core.entities.dto.endereco.EnderecoCoreDto;
import br.com.fiapfood.core.entities.dto.login.LoginCoreDto;

public record DadosUsuarioInputDto(UUID id, String nome, Integer idPerfil, LoginCoreDto login, 
								   Boolean isAtivo, String email, LocalDateTime dataCriacao, 
								   LocalDateTime dataAtualizacao, EnderecoCoreDto dadosEndereco){
				
}