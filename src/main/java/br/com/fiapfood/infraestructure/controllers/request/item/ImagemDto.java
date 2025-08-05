package br.com.fiapfood.infraestructure.controllers.request.item;

import java.util.UUID;

public record ImagemDto(UUID id, String nome, byte[] conteudo, String tipo) {

}
