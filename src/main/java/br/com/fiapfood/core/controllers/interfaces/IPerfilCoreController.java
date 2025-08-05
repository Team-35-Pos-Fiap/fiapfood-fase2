package br.com.fiapfood.core.controllers.interfaces;

import java.util.List;

import br.com.fiapfood.infraestructure.controllers.request.perfil.PerfilDto;

public interface IPerfilCoreController {
	List<PerfilDto> buscarTodos();
	PerfilDto buscarPorId(Integer id);
	void cadastrar(String nome);
	void atualizarNome(Integer id, String nome);
	void inativar(Integer id);
	void reativar(Integer id);
}