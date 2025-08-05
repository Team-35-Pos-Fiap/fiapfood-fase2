package br.com.fiapfood.core.usecases.login.impl;

import java.util.UUID;

import br.com.fiapfood.core.entities.Login;
import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.exceptions.usuario.MatriculaDuplicadaException;
import br.com.fiapfood.core.exceptions.usuario.UsuarioInativoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.presenters.UsuarioPresenter;
import br.com.fiapfood.core.usecases.login.interfaces.IAtualizarMatriculaUseCase;

public class AtualizarMatriculaUseCase implements IAtualizarMatriculaUseCase {

	private final IUsuarioGateway usuarioGateway;
	private final IPerfilGateway perfilGateway;
	
	private final String USUARIO_INATIVO = "Não é possível alterar a matrícula, pois o usuário está inativo.";
	private final String MATRICULA_DUPLICADA = "Já existe um usuário com a matrícula informada.";
	
	public AtualizarMatriculaUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		this.usuarioGateway = usuarioGateway;
		this.perfilGateway = perfilGateway;
	}
	
	@Override
	public void atualizar(final UUID id, final String matriculaNova) {
		Usuario usuario = buscarUsuario(id);
		
		validarMatricula(matriculaNova);
		validarUsuario(usuario);

		final Login login = buscarLogin(usuario);
	
		atualizarMatricula(login, matriculaNova);
		atualizarUsuario(usuario, login);
		
		salvar(usuario);
	}

	private void atualizarUsuario(Usuario usuario, Login login) {
		usuario.atualizarLogin(login);
	}

	private Login buscarLogin(Usuario usuario) {
		return usuario.getLogin();
	}
	
	private void validarUsuario(final Usuario usuario) {
		if (!usuario.getIsAtivo()) {
			throw new UsuarioInativoException(USUARIO_INATIVO);
		} 
	}
	
	private void validarMatricula(final String matricula) {
		if(usuarioGateway.matriculaJaCadastrada(matricula)){
			throw new MatriculaDuplicadaException(MATRICULA_DUPLICADA);
		}
	}

	private Usuario buscarUsuario(final UUID id) {
		return usuarioGateway.buscarPorId(id);
	}
	
	private void atualizarMatricula(Login login, String matriculaNova) {
		login.atualizarMatricula(matriculaNova);
	}
	
	private void salvar(final Usuario usuario) {
		usuarioGateway.salvar(UsuarioPresenter.toUsuarioDto(usuario, 
															buscarPerfil(usuario.getIdPerfil())));
	}
	
	private Perfil buscarPerfil(final Integer idPerfil) {
		return perfilGateway.buscarPorId(idPerfil);
	}
}