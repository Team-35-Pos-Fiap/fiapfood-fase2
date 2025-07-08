package br.com.fiapfood.core.entities.dto;

public record CadastrarUsuarioDto(String nome, Integer perfil, LoginDto dadosLogin, String email, DadosEnderecoDto dadosEndereco) {

}
