package br.com.fiapfood.core.entities.dto.item;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemOutputCoreDto(UUID id, String nome, String descricao, BigDecimal preco, 
							Boolean isDisponivelConsumoPresencial, Boolean isDisponivel, 
							String linkFoto) {

}