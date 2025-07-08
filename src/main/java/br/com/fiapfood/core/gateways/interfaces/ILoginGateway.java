package br.com.fiapfood.core.gateways.interfaces;

import java.util.UUID;

import br.com.fiapfood.core.entities.Login;
import br.com.fiapfood.core.entities.dto.LoginDto;

public interface ILoginGateway {
	Login buscarPorMatricula(String matricula);
	Login buscarPorMatriculaSenha(String matricula, String senha);
	Login buscarPorId(UUID id);
	void salvar(LoginDto login);
	boolean matriculaJaCadastrada(String matricula);
}