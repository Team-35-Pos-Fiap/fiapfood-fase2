package br.com.fiapfood.core.usecases.login.impl;

import java.util.UUID;

import br.com.fiapfood.core.entities.Login;
import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.exceptions.usuario.UsuarioInativoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.presenters.UsuarioPresenter;
import br.com.fiapfood.core.usecases.login.interfaces.IAtualizarSenhaUseCase;

public class AtualizarSenhaUseCase implements IAtualizarSenhaUseCase {

	private final IUsuarioGateway usuarioGateway;
	private final IPerfilGateway perfilGateway;
	
	private final String USUARIO_INATIVO = "Não é possível alterar a senha, pois o usuário está inativo.";

	public AtualizarSenhaUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		this.usuarioGateway = usuarioGateway;
		this.perfilGateway = perfilGateway;
	}
	
	@Override
	public void atualizar(final UUID id, final String senha) {
		Usuario usuario = buscarUsuario(id);
		
		validarUsuario(usuario);

		final Login login = buscarLogin(usuario);
	
		atualizarSenha(login, senha);
		atualizarUsuario(usuario, login);
		
		salvar(usuario);
	}

	private void atualizarUsuario(Usuario usuario, Login login) {
		usuario.atualizarLogin(login);
	}

	private Login buscarLogin(Usuario usuario) {
		return usuario.getLogin();
	}
	
	private void validarUsuario(final Usuario usuario) {
		if (!usuario.getIsAtivo()) {
			throw new UsuarioInativoException(USUARIO_INATIVO);
		} 
	}

	private Usuario buscarUsuario(final UUID id) {
		return usuarioGateway.buscarPorId(id);
	}
	
	private void atualizarSenha(Login login, String senha) {
		login.atualizarSenha(senha);
	}
	
	private void salvar(final Usuario usuario) {
		usuarioGateway.salvar(UsuarioPresenter.toUsuarioDto(usuario, 
															buscarPerfil(usuario.getIdPerfil())));
	}
	
	private Perfil buscarPerfil(final Integer idPerfil) {
		return perfilGateway.buscarPorId(idPerfil);
	}
}