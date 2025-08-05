package br.com.fiapfood.core.entities.dto.usuario;

import br.com.fiapfood.core.entities.dto.endereco.DadosEnderecoCoreDto;
import br.com.fiapfood.core.entities.dto.login.LoginCoreDto;

public record CadastrarUsuarioCoreDto(String nome, Integer perfil, LoginCoreDto dadosLogin, 
									  String email, DadosEnderecoCoreDto dadosEndereco) {

}