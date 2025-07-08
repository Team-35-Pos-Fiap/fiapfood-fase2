package br.com.fiapfood.infraestructure.repositories.interfaces;

import java.util.List;

import br.com.fiapfood.core.entities.dto.PerfilDto;

public interface IPerfilRepository {
    PerfilDto buscarPorId(Integer id);
    List<PerfilDto> buscarTodos();
}