package br.com.fiapfood.core.gateways.interfaces;

import java.util.List;

import br.com.fiapfood.core.entities.TipoCulinaria;
import br.com.fiapfood.core.entities.dto.tipo_culinaria.TipoCulinariaCoreDto;


public interface ITipoCulinariaGateway {
	List<TipoCulinaria> buscarTodos();
	TipoCulinaria buscarPorId(Integer id);
	void salvar(TipoCulinariaCoreDto perfil);
	boolean nomeJaCadastrado(String nome);
}