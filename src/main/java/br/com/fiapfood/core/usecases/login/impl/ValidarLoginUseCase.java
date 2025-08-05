package br.com.fiapfood.core.usecases.login.impl;

import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.exceptions.usuario.UsuarioSemAcessoException;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.login.interfaces.IValidarAcessoUseCase;

public class ValidarLoginUseCase implements IValidarAcessoUseCase {

	private final IUsuarioGateway usuarioGateway;
	
	private final String ACESSO_LIBERADO = "Acesso liberado para o usuário ";
	private final String USUARIO_INATIVO = "Não é possível realizar o login para usuários inativos.";
	
	public ValidarLoginUseCase(IUsuarioGateway usuarioGateway) {
		this.usuarioGateway = usuarioGateway;
	}
	
	@Override
	public String validar(final String matricula, final String senha) {
		Usuario usuario = buscarUsuario(matricula, senha);
		
		validarUsuario(usuario);
		
		return ACESSO_LIBERADO + usuario.getNome();
	}
	
	private void validarUsuario(final Usuario usuario) {
		if (!usuario.getIsAtivo()) {
			throw new UsuarioSemAcessoException(USUARIO_INATIVO);
		} 
	}
	
	private Usuario buscarUsuario(final String matricula, final String senha) {
		return usuarioGateway.buscarPorMatriculaSenha(matricula, senha);
	}
}