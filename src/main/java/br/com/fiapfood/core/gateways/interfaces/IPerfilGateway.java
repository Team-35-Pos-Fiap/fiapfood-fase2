package br.com.fiapfood.core.gateways.interfaces;

import java.util.List;

import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.entities.dto.perfil.PerfilCoreDto;

public interface IPerfilGateway {
	List<Perfil> buscarTodos();
	Perfil buscarPorId(Integer id);
	void salvar(PerfilCoreDto perfil);
	boolean nomeJaCadastrado(String nome);
}