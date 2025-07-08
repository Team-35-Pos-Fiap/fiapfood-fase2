package br.com.fiapfood.core.controllers.interfaces;

import br.com.fiapfood.core.entities.dto.LoginDto;

public interface ILoginCoreController {
	String validar(LoginDto dados);
	void atualizarSenha(String matricula, String senha);
	void atualizarMatricula(String matricula, String matriculaNova);
}