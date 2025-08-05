package br.com.fiapfood.core.entities.dto.perfil;

import java.time.LocalDate;

public record PerfilCoreDto(Integer id, String nome, LocalDate dataCriacao, LocalDate dataInativacao) { }