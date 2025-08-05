package br.com.fiapfood.core.usecases.usuario.impl;

import java.util.UUID;

import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.exceptions.usuario.AtualizacaoNomeUsuarioNaoPermitidoException;
import br.com.fiapfood.core.exceptions.usuario.UsuarioInativoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.presenters.UsuarioPresenter;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarNomeUsuarioUseCase;

public class AtualizarNomeUsuarioUseCase implements IAtualizarNomeUsuarioUseCase {
	private final IUsuarioGateway usuarioGateway;
	private final IPerfilGateway perfilGateway;

	private final String USUARIO_DUPLICADO = "Não é possível alterar o nome do usuário, pois ele é igual ao nome do usuário.";
	private final String USUARIO_INATIVO = "Não é possível alterar o nome de um usuário inativo.";
	
	public AtualizarNomeUsuarioUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		this.usuarioGateway = usuarioGateway;
		this.perfilGateway = perfilGateway;
	}

	@Override
	public void atualizar(final UUID id, final String nome) {
		final Usuario usuario = buscarUsuario(id);
		
		validarUsuario(usuario);
		validarNome(usuario, nome);
		
		atualizarNome(usuario, nome);
		
		salvar(usuario);
	}

	private void validarNome(Usuario usuario, String nome) {
		if (usuario.getNome().equals(nome)) {
			throw new AtualizacaoNomeUsuarioNaoPermitidoException(USUARIO_DUPLICADO);
		} 
	}

	private void atualizarNome(Usuario usuario, String nome) {
		usuario.atualizarNome(nome);
	}

	private void validarUsuario(final Usuario usuario) {
		if (!usuario.getIsAtivo()) {
			throw new UsuarioInativoException(USUARIO_INATIVO);
		} 
	}

	private void salvar(final Usuario usuario) {
		usuarioGateway.salvar(UsuarioPresenter.toUsuarioDto(usuario, 
															buscarPerfil(usuario.getIdPerfil())));
	}
	
	private Usuario buscarUsuario(final UUID id) {
		return usuarioGateway.buscarPorId(id);
	}
	
	private Perfil buscarPerfil(final Integer idPerfil) {
		return perfilGateway.buscarPorId(idPerfil);
	}
}