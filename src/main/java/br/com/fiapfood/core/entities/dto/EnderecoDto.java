package br.com.fiapfood.core.entities.dto;

import java.util.UUID;

public record EnderecoDto(UUID id,  String cidade, String cep, 
					      String bairro, String endereco, String estado,
					      Integer numero, String complemento) { }