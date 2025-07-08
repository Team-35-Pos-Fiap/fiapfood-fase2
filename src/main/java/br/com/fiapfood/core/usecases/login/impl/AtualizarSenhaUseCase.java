package br.com.fiapfood.core.usecases.login.impl;

import java.util.UUID;

import br.com.fiapfood.core.entities.Login;
import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.exceptions.UsuarioInativoException;
import br.com.fiapfood.core.gateways.interfaces.ILoginGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.presenters.LoginPresenter;
import br.com.fiapfood.core.usecases.login.interfaces.IAtualizarSenhaUseCase;

public class AtualizarSenhaUseCase implements IAtualizarSenhaUseCase {

	private final IUsuarioGateway usuarioGateway;
	private final ILoginGateway loginGateway;

	public AtualizarSenhaUseCase(ILoginGateway loginGateway, IUsuarioGateway usuarioGateway) {
		this.loginGateway = loginGateway;
		this.usuarioGateway = usuarioGateway;
	}
	
	@Override
	public void atualizar(final String matricula, final String senha) {
		final Login login = buscarLogin(matricula);
	
		validarUsuario(login.getId());
		
		atualizarSenha(login, senha);
		
		salvar(login);
	}
	
	private void validarUsuario(final UUID idLogin) {
		final Usuario usuario = buscarUsuario(idLogin);
		
		validarUsuario(usuario);
	}
	
	private void validarUsuario(final Usuario usuario) {
		if (!usuario.getIsAtivo()) {
			throw new UsuarioInativoException("Não é possível alterar a senha de um usuário inativo.");
		} 
	}
	
	private void atualizarSenha(final Login login, final String senha) {
		login.atualizarSenha(senha);
	}
	
	private void salvar(final Login login) {
		loginGateway.salvar(LoginPresenter.toLogin(login));
	}
	
	private Login buscarLogin(final String matricula) {
		return loginGateway.buscarPorMatricula(matricula);
	}
	
	private Usuario buscarUsuario(final UUID idLogin) {
		return usuarioGateway.buscarPorIdLogin(idLogin);
	}
}