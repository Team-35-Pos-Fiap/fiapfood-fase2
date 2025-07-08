package br.com.fiapfood.core.usecases.login.impl;

import java.util.UUID;

import br.com.fiapfood.core.entities.Login;
import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.exceptions.MatriculaDuplicadaException;
import br.com.fiapfood.core.exceptions.UsuarioInativoException;
import br.com.fiapfood.core.gateways.interfaces.ILoginGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.presenters.LoginPresenter;
import br.com.fiapfood.core.usecases.login.interfaces.IAtualizarMatriculaUseCase;

public class AtualizarMatriculaUseCase implements IAtualizarMatriculaUseCase {

	private final IUsuarioGateway usuarioGateway;
	private final ILoginGateway loginGateway;

	public AtualizarMatriculaUseCase(ILoginGateway loginGateway, IUsuarioGateway usuarioGateway) {
		this.loginGateway = loginGateway;
		this.usuarioGateway = usuarioGateway;
	}
	
	@Override
	public void atualizar(final String matricula, final String matriculaNova) {
		final Login login = buscarLogin(matricula);
	
		validarUsuario(login.getId());
		validarMatricula(matriculaNova);
		
		login.atualizarMatricula(matriculaNova);
		
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
	
	private void validarMatricula(final String matricula) {
		if(loginGateway.matriculaJaCadastrada(matricula)){
			throw new MatriculaDuplicadaException("Já existe um usuário com a matrícula informada.");
		}
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