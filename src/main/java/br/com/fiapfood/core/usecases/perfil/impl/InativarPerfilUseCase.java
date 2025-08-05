package br.com.fiapfood.core.usecases.perfil.impl;

import java.util.List;

import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.exceptions.perfil.ExclusaoPerfilNaoPermitidaException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.presenters.PerfilPresenter;
import br.com.fiapfood.core.usecases.perfil.interfaces.IInativarPerfilUseCase;

public class InativarPerfilUseCase implements IInativarPerfilUseCase {

	private final IPerfilGateway perfilGateway;
	private final IUsuarioGateway usuarioGateway;
	
	private final String USUARIOS_ATIVOS = "Não é possível inativar o perfil pois há usuário ativo associado ao perfil.";
	
	public InativarPerfilUseCase(IPerfilGateway perfilGateway, IUsuarioGateway usuarioGateway) {
		this.perfilGateway = perfilGateway;
		this.usuarioGateway = usuarioGateway;
	}
	
	@Override
	public void inativar(Integer id) {
		Perfil perfil = buscarPerfil(id);
		
		verificaUsuariosAssociados(perfil.getId());
		
		inativarPerfil(perfil);

		salvar(perfil);
	}
	
	private Perfil buscarPerfil(Integer id) {
		return perfilGateway.buscarPorId(id);
	}
	
	private void salvar(Perfil perfil) {
		perfilGateway.salvar(PerfilPresenter.toPerfilDto(perfil));
	}

	private void inativarPerfil(Perfil perfil) {
		perfil.inativar();
	}

	private void verificaUsuariosAssociados(final Integer id) {
		List<Usuario> usuarios = usuarioGateway.buscarPorIdPerfil(id);
		
		if(usuarios != null && possuiUsuariosAtivos(usuarios)) {
			throw new ExclusaoPerfilNaoPermitidaException(USUARIOS_ATIVOS);
		}
	}

	private boolean possuiUsuariosAtivos(List<Usuario> usuarios) {
		return usuarios.stream().filter(u -> u.getIsAtivo() == true).findAny().isPresent();
	}
}