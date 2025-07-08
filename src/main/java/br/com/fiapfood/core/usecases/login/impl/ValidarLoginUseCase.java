package br.com.fiapfood.core.usecases.login.impl;

import java.util.UUID;

import br.com.fiapfood.core.entities.Login;
import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.entities.dto.LoginDto;
import br.com.fiapfood.core.exceptions.UsuarioSemAcessoException;
import br.com.fiapfood.core.gateways.interfaces.ILoginGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.login.interfaces.IValidarLoginUseCase;

public class ValidarLoginUseCase implements IValidarLoginUseCase {

	private final ILoginGateway loginGateway;
	private final IUsuarioGateway usuarioGateway;

	public ValidarLoginUseCase(ILoginGateway loginGateway, IUsuarioGateway usuarioGateway) {
		this.loginGateway = loginGateway;
		this.usuarioGateway = usuarioGateway;
	}
	
	@Override
	public String validar(final LoginDto dados) {
		final Login login = buscarLoginPorMatriculaSenha(dados.matricula(), dados.senha());
		
		validaAcessoUsuario(login.getId());
		
		return "Acesso liberado.";
	}
	
	private Login buscarLoginPorMatriculaSenha(final String matricula, final String senha) {
		return loginGateway.buscarPorMatriculaSenha(matricula, senha);
	}
	
	private void validaAcessoUsuario(final UUID idLogin) {
		validarUsuario(usuarioGateway.buscarPorIdLogin(idLogin));
	}
	
	private void validarUsuario(final Usuario usuario) {
		if (!usuario.getIsAtivo()) {
			throw new UsuarioSemAcessoException("Não é possível realizar o login para usuários inativos.");
		} 
	}
}