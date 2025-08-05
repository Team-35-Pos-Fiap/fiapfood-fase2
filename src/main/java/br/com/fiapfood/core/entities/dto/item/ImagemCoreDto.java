package br.com.fiapfood.core.entities.dto.item;

import java.util.UUID;

public record ImagemCoreDto(UUID id, String nome, byte[] conteudo, String tipo) {

}
