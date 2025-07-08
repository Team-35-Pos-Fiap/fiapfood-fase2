package br.com.fiapfood.core.gateways.interfaces;

import java.util.List;

import br.com.fiapfood.core.entities.Perfil;


public interface IPerfilGateway {
	List<Perfil> buscarTodos();
	Perfil buscarPorId(Integer id);
}