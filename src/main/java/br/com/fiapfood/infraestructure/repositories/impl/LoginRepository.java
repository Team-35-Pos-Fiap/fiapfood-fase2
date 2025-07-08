package br.com.fiapfood.infraestructure.repositories.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.com.fiapfood.core.entities.dto.LoginDto;
import br.com.fiapfood.core.presenters.LoginPresenter;
import br.com.fiapfood.infraestructure.entities.LoginEntity;
import br.com.fiapfood.infraestructure.repositories.interfaces.ILoginRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.jpa.ILoginJpaRepository;

@Repository
public class LoginRepository implements ILoginRepository {

    private final ILoginJpaRepository loginRepository;

	public LoginRepository(ILoginJpaRepository loginRepository) {
		this.loginRepository = loginRepository;
	}

	@Override
	public LoginDto buscarPorMatriculaSenha(final String matricula, final String senha) {
		final Optional<LoginEntity> login = loginRepository.findByMatriculaAndSenha(matricula, senha);
		
		if (login.isPresent()) {
			return LoginPresenter.toLoginDto(login.get());
		} else {
			return null;
		}
	}

	@Override
	public void salvar(final LoginEntity login) {
		loginRepository.save(login);		
	}

	@Override
	public LoginDto buscarPorMatricula(final String matricula) {
		final Optional<LoginEntity> login = loginRepository.findByMatricula(matricula);
		
		if (login.isPresent()) {
			return LoginPresenter.toLoginDto(login.get());
		} else {
			return null;
		}
	}
	
	@Override
	public LoginDto buscarPorId(final UUID id) {
		final Optional<LoginEntity> login = loginRepository.findById(id);
		
		if (login.isPresent()) {
			return LoginPresenter.toLoginDto(login.get());
		} else {
			return null;
		}
	}
	
	@Override
	public boolean matriculaJaCadastrada(final String matricula) {
		return loginRepository.existsByMatricula(matricula);
	}
}