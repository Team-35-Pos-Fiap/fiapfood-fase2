package br.com.fiapfood.core.presenters;

import br.com.fiapfood.core.entities.Login;
import br.com.fiapfood.core.entities.dto.login.LoginCoreDto;
import br.com.fiapfood.infraestructure.controllers.request.login.LoginDto;
import br.com.fiapfood.infraestructure.entities.LoginEntity;

public class LoginPresenter {

	public static LoginCoreDto toLoginDto(LoginEntity login) {
		return new LoginCoreDto(login.getId(), login.getMatricula(), login.getSenha());
	}
	
	public static LoginEntity toLoginEntity(LoginCoreDto login) {
		return new LoginEntity(login.id(), login.matricula(), login.senha());
	}
	
	public static Login toLogin(LoginCoreDto login) {
		return Login.criar(login.id(), login.matricula(), login.senha());
	}
	
	public static LoginCoreDto toLoginDto(Login login) {
		return new LoginCoreDto(login.getId(), login.getMatricula(), login.getSenha());
	}

	public static LoginDto toLoginDto(LoginCoreDto login) {
		return new LoginDto(login.id(), login.matricula(), login.senha());
	}

	public static LoginCoreDto toLoginCoreDto(LoginDto login) {
		return new LoginCoreDto(login.id(), login.matricula(), login.senha());
	}
}