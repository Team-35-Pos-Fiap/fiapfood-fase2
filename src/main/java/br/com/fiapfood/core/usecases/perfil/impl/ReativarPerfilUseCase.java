package br.com.fiapfood.core.usecases.perfil.impl;

import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.presenters.PerfilPresenter;
import br.com.fiapfood.core.usecases.perfil.interfaces.IReativarPerfilUseCase;

public class ReativarPerfilUseCase implements IReativarPerfilUseCase {

	private final IPerfilGateway perfilGateway;
	
	public ReativarPerfilUseCase(IPerfilGateway perfilGateway) {
		this.perfilGateway = perfilGateway;
	}
	
	@Override
	public void reativar(Integer id) {
		Perfil perfil = buscarPerfil(id);
		
		reativarPerfil(perfil);

		salvar(perfil);
	}
	
	private Perfil buscarPerfil(Integer id) {
		return perfilGateway.buscarPorId(id);
	}
	
	private void salvar(Perfil perfil) {
		perfilGateway.salvar(PerfilPresenter.toPerfilDto(perfil));
	}

	private void reativarPerfil(Perfil perfil) {
		perfil.reativar();
	}
}