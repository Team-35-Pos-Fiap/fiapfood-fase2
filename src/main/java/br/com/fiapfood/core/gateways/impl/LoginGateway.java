package br.com.fiapfood.core.gateways.impl;

import java.util.UUID;

import br.com.fiapfood.core.entities.Login;
import br.com.fiapfood.core.entities.dto.LoginDto;
import br.com.fiapfood.core.exceptions.LoginNaoEncontradoException;
import br.com.fiapfood.core.gateways.interfaces.ILoginGateway;
import br.com.fiapfood.core.presenters.LoginPresenter;
import br.com.fiapfood.infraestructure.repositories.interfaces.ILoginRepository;

public class LoginGateway implements ILoginGateway {

	private final ILoginRepository loginRepository;
	
	public LoginGateway(ILoginRepository loginRepository) {
		this.loginRepository = loginRepository;
	}
	
	@Override
	public Login buscarPorMatricula(final String matricula) {
		final LoginDto loginDto = loginRepository.buscarPorMatricula(matricula);
		
		if(loginDto != null) {
			return LoginPresenter.toLogin(loginDto);
		} else {
			throw new LoginNaoEncontradoException("Não foi encontrado nenhum login com a matrícula informada.");
		}
	}

	@Override
	public Login buscarPorMatriculaSenha(final String matricula, final String senha) {
		final LoginDto loginDto = loginRepository.buscarPorMatriculaSenha(matricula, senha);
		
		if(loginDto != null) {
			return LoginPresenter.toLogin(loginDto);
		} else {
			throw new LoginNaoEncontradoException("Não foi encontrado nenhum login com a matrícula e senha informados.");
		}
	}

	@Override
	public void salvar(final LoginDto login) {
		loginRepository.salvar(LoginPresenter.toLoginEntity(login));
	}

	@Override
	public Login buscarPorId(final UUID id) {
		final LoginDto loginDto = loginRepository.buscarPorId(id);
		
		if(loginDto != null) {
			return LoginPresenter.toLogin(loginDto);
		} else {
			throw new LoginNaoEncontradoException("Não foi encontrado nenhum login com o id informado.");
		}
	}

	@Override
	public boolean matriculaJaCadastrada(final String matricula) {
		return loginRepository.matriculaJaCadastrada(matricula);
	}
}