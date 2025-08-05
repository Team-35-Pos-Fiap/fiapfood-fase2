package br.com.fiapfood.core.usecases.usuario.impl;

import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.exceptions.usuario.AtualizacaoStatusUsuarioNaoPermitidaException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.presenters.UsuarioPresenter;
import br.com.fiapfood.core.usecases.usuario.interfaces.IInativarUsuarioUseCase;

import java.util.UUID;

public class InativarUsuarioUseCase implements IInativarUsuarioUseCase {
	private final IUsuarioGateway usuarioGateway;
	private final IPerfilGateway perfilGateway;

	private final String INATIVACAO_NAO_PERMITIDA = "Não é possível inativar o usuário, pois ele já se encontra inativo.";
	
	public InativarUsuarioUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		this.usuarioGateway = usuarioGateway;
		this.perfilGateway = perfilGateway;
	}
	
	@Override
	public void inativar(final UUID id) {
		final Usuario usuario = buscarUsuario(id);
		
		validarUsuario(usuario);
		
		inativar(usuario);
		
		salvar(usuario);
	}

	private void inativar(Usuario usuario) {
		usuario.inativar();		
	}

	private void validarUsuario(final Usuario usuario) {
		if (!usuario.getIsAtivo()) {
			throw new AtualizacaoStatusUsuarioNaoPermitidaException(INATIVACAO_NAO_PERMITIDA);
		} 
	}

	private void salvar(final Usuario usuario) {
		usuarioGateway.salvar(UsuarioPresenter.toUsuarioDto(usuario, 
															buscarPerfil(usuario.getIdPerfil())));
	}
	
	private Usuario buscarUsuario(final UUID id) {
		return usuarioGateway.buscarPorId(id);
	}
	
	private Perfil buscarPerfil(final Integer idPerfil) {
		return perfilGateway.buscarPorId(idPerfil);
	}
}