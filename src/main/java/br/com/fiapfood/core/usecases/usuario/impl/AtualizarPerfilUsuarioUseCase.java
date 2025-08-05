package br.com.fiapfood.core.usecases.usuario.impl;

import java.util.UUID;

import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.exceptions.usuario.AtualizacaoPerfilNaoPermitidaException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.presenters.UsuarioPresenter;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarPerfilUsuarioUseCase;

public class AtualizarPerfilUsuarioUseCase implements IAtualizarPerfilUsuarioUseCase{
	private final IUsuarioGateway usuarioGateway;
	private final IPerfilGateway perfilGateway;

	private final String USUARIO_INATIVO = "Não é possível alterar o perfil de um usuário inativo.";
	private final String PERFIL_DUPLICADO = "O perfil selecionado é o mesmo que o usuário já possui.";

	public AtualizarPerfilUsuarioUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		this.usuarioGateway = usuarioGateway;
		this.perfilGateway = perfilGateway;
	}
	
	@Override
	public void atualizar(final UUID id, final Integer idPerfil) {
		final Usuario usuario = buscarUsuario(id);
		
		validarUsuario(usuario);
		validarPerfil(usuario, idPerfil);
		
		atualizarPerfil(usuario, idPerfil);
		
		salvar(usuario);
	}

	private void atualizarPerfil(Usuario usuario, Integer idPerfil) {
		usuario.atualizarPerfil(idPerfil);		
	}

	private void validarUsuario(final Usuario usuario) {
		if (!usuario.getIsAtivo()) {
			throw new AtualizacaoPerfilNaoPermitidaException(USUARIO_INATIVO);
		} 
	}
	
	private void validarPerfil(final Usuario usuario, final Integer idPerfil) {
		if (usuario.getIdPerfil().equals(idPerfil)) {
			throw new AtualizacaoPerfilNaoPermitidaException(PERFIL_DUPLICADO);
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