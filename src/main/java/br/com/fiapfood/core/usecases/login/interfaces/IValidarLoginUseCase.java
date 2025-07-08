package br.com.fiapfood.core.usecases.login.interfaces;

import br.com.fiapfood.core.entities.dto.LoginDto;

public interface IValidarLoginUseCase {
	String validar(LoginDto dados);
}
