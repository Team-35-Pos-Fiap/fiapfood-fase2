package br.com.fiapfood.core.usecases.usuario.impl;

import java.util.UUID;

import br.com.fiapfood.core.entities.Endereco;
import br.com.fiapfood.core.entities.Login;
import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.exceptions.UsuarioInativoException;
import br.com.fiapfood.core.gateways.interfaces.IEnderecoGateway;
import br.com.fiapfood.core.gateways.interfaces.ILoginGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.presenters.UsuarioPresenter;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarNomeUsuarioUseCase;

public class AtualizarNomeUsuarioUseCase implements IAtualizarNomeUsuarioUseCase {
	private final IUsuarioGateway usuarioGateway;
	private final IPerfilGateway perfilGateway;
	private final ILoginGateway loginGateway;
	private final IEnderecoGateway enderecoGateway;

	public AtualizarNomeUsuarioUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway, ILoginGateway loginGateway, IEnderecoGateway enderecoGateway) {
		this.usuarioGateway = usuarioGateway;
		this.perfilGateway = perfilGateway;
		this.loginGateway = loginGateway;
		this.enderecoGateway = enderecoGateway;
	}

	@Override
	public void atualizar(final UUID id, final String nome) {
		final Usuario usuario = buscarUsuario(id);
		
		validarUsuario(usuario);
		
		usuario.atualizarNome(nome);
		
		salvar(usuario);
	}

	private void validarUsuario(final Usuario usuario) {
		if (!usuario.getIsAtivo()) {
			throw new UsuarioInativoException("Não é possível alterar o nome de um usuário inativo.");
		} 
	}

	private void salvar(final Usuario usuario) {
		usuarioGateway.salvar(UsuarioPresenter.toUsuarioDto(usuario, 
															buscarPerfil(usuario.getIdPerfil()), 
															buscarLogin(usuario.getIdLogin()), 
															buscarEndereco(usuario.getIdEndereco())));
	}
	
	private Usuario buscarUsuario(final UUID id) {
		return usuarioGateway.buscarPorId(id);
	}
	
	private Perfil buscarPerfil(final Integer idPerfil) {
		return perfilGateway.buscarPorId(idPerfil);
	}
	
	private Login buscarLogin(final UUID idLogin) {
		return loginGateway.buscarPorId(idLogin);
	}
	
	private Endereco buscarEndereco(final UUID idEndereco) {
		return enderecoGateway.buscarPorId(idEndereco);
	}
}