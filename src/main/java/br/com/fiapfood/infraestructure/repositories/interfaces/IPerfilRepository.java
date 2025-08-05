package br.com.fiapfood.infraestructure.repositories.interfaces;

import java.util.List;

import br.com.fiapfood.core.entities.dto.perfil.PerfilCoreDto;
import br.com.fiapfood.infraestructure.entities.PerfilEntity;

public interface IPerfilRepository {
    PerfilCoreDto buscarPorId(Integer id);
    List<PerfilCoreDto> buscarTodos();
	void salvar(PerfilEntity perfil);
	boolean nomeJaCadastrado(String nome);
}