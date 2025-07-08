package br.com.fiapfood.core.controllers.impl;

import br.com.fiapfood.core.controllers.interfaces.ILoginCoreController;
import br.com.fiapfood.core.entities.dto.LoginDto;
import br.com.fiapfood.core.usecases.login.interfaces.IAtualizarMatriculaUseCase;
import br.com.fiapfood.core.usecases.login.interfaces.IAtualizarSenhaUseCase;
import br.com.fiapfood.core.usecases.login.interfaces.IValidarLoginUseCase;

public class LoginCoreController implements ILoginCoreController{

	private final IValidarLoginUseCase validarLoginUseCase;
	private final IAtualizarSenhaUseCase atualizarSenhaUseCase;
	private final IAtualizarMatriculaUseCase atualizarMatriculaUseCase;
	
	public LoginCoreController(IValidarLoginUseCase validarLoginUseCase, IAtualizarSenhaUseCase atualizarSenhaUseCase, IAtualizarMatriculaUseCase atualizarMatriculaUseCase) {
		this.validarLoginUseCase = validarLoginUseCase;
		this.atualizarSenhaUseCase = atualizarSenhaUseCase;
		this.atualizarMatriculaUseCase = atualizarMatriculaUseCase;
	}
	
	@Override
	public String validar(final LoginDto dados) {
		return validarLoginUseCase.validar(dados);
	}

	@Override
	public void atualizarSenha(final String matricula, final String senha) {
		atualizarSenhaUseCase.atualizar(matricula, senha);
	}

	@Override
	public void atualizarMatricula(final String matricula, final String matriculaNova) {
		atualizarMatriculaUseCase.atualizar(matricula, matriculaNova);
	}
}