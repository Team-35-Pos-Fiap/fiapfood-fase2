package br.com.fiapfood.infraestructure.repositories.interfaces;

import java.util.List;

import br.com.fiapfood.core.entities.dto.tipo_culinaria.TipoCulinariaCoreDto;
import br.com.fiapfood.infraestructure.entities.TipoCulinariaEntity;

public interface ITipoCulinariaRepository {
	TipoCulinariaCoreDto buscarPorId(Integer id);
    List<TipoCulinariaCoreDto> buscarTodos();
	void salvar(TipoCulinariaEntity perfil);
	boolean nomeJaCadastrado(String nome);
}