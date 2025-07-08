package br.com.fiapfood.core.presenters;

import br.com.fiapfood.core.entities.Login;
import br.com.fiapfood.core.entities.dto.LoginDto;
import br.com.fiapfood.infraestructure.entities.LoginEntity;

public class LoginPresenter {

	public static LoginDto toLoginDto(LoginEntity login) {
		return new LoginDto(login.getId(), login.getMatricula(), login.getSenha());
	}
	
	public static LoginEntity toLoginEntity(LoginDto login) {
		return new LoginEntity(login.id(), login.matricula(), login.senha());
	}
	
	public static Login toLogin(LoginDto login) {
		return Login.criar(login.id(), login.matricula(), login.senha());
	}
	
	public static LoginDto toLogin(Login login) {
		return new LoginDto(login.getId(), login.getMatricula(), login.getSenha());
	}
}