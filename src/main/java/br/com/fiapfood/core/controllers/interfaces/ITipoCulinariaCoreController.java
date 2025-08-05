package br.com.fiapfood.core.controllers.interfaces;

import java.util.List;

import br.com.fiapfood.infraestructure.controllers.request.tipo_culinaria.TipoCulinariaDto;

public interface ITipoCulinariaCoreController {
	List<TipoCulinariaDto> buscarTodos();
	TipoCulinariaDto buscarPorId(Integer id);
	void cadastrar(String nome);
	void atualizar(Integer id, String nome);
}