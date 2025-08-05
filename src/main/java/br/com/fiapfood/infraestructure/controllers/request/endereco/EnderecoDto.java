package br.com.fiapfood.infraestructure.controllers.request.endereco;

import java.util.UUID;

public record EnderecoDto(UUID id, String cidade, String cep, 
					      String bairro, String endereco, String estado,
					      Integer numero, String complemento) { }