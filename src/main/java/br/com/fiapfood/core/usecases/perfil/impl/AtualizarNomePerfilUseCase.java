package br.com.fiapfood.core.usecases.perfil.impl;

import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.exceptions.perfil.NomePerfilDuplicadoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.presenters.PerfilPresenter;
import br.com.fiapfood.core.usecases.perfil.interfaces.IAtualizarNomePerfilUseCase;

public class AtualizarNomePerfilUseCase implements IAtualizarNomePerfilUseCase {

	private final IPerfilGateway perfilGateway;
	
	private final String PERFIL_DUPLICADO = "Já existe um perfil com o nome informado.";

	public AtualizarNomePerfilUseCase(IPerfilGateway perfilGateway) {
		this.perfilGateway = perfilGateway;
	}
	
	@Override
	public void atualizar(Integer id, String nome) {
		validaNomeJaCadastrado(nome);
		
		final Perfil perfil = buscarPerfil(id);
		
		atualizarNome(perfil, nome);
		
		perfilGateway.salvar(PerfilPresenter.toPerfilDto(perfil));
	}
	
	private void atualizarNome(Perfil perfil, String nome) {
		perfil.atualizarNome(nome);		
	}

	private void validaNomeJaCadastrado(final String nome) {
		if(perfilGateway.nomeJaCadastrado(nome)) {
			throw new NomePerfilDuplicadoException(PERFIL_DUPLICADO);
		}
	}

	private Perfil buscarPerfil(final Integer id) {
		return perfilGateway.buscarPorId(id);
	}
}
