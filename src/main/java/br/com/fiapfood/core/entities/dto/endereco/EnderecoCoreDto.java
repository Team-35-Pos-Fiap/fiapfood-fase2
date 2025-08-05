package br.com.fiapfood.core.entities.dto.endereco;

import java.util.UUID;

public record EnderecoCoreDto(UUID id,  String cidade, String cep, 
					          String bairro, String endereco, String estado,
					          Integer numero, String complemento) { }