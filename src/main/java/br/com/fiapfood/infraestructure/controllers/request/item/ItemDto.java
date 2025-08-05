package br.com.fiapfood.infraestructure.controllers.request.item;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemDto(UUID id, String nome, String descricao, BigDecimal preco, 
							Boolean isDisponivelConsumoPresencial, Boolean isDisponivel, 
							String linkFoto) {

}