package br.com.fiapfood.infraestructure.controllers.request.perfil;

import java.time.LocalDate;

public record PerfilDto(Integer id, String nome, LocalDate dataCriacao, LocalDate dataInativacao) { }