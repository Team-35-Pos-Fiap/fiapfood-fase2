package br.com.fiapfood.core.usecases.usuario.impl;

import java.util.UUID;

import br.com.fiapfood.core.entities.Endereco;
import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.entities.dto.endereco.DadosEnderecoCoreDto;
import br.com.fiapfood.core.exceptions.usuario.UsuarioInativoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.presenters.UsuarioPresenter;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarEnderecoUsuarioUseCase;

public class AtualizarEnderecoUsuarioUseCase implements IAtualizarEnderecoUsuarioUseCase {
	private final IUsuarioGateway usuarioGateway;
	private final IPerfilGateway perfilGateway;
	
	private final String USUARIO_INATIVO = "Não é possível alterar o endereço de um usuário inativo.";

	public AtualizarEnderecoUsuarioUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		this.usuarioGateway = usuarioGateway;
		this.perfilGateway = perfilGateway;
	}
	
	@Override
	public void atualizar(final UUID id, final DadosEnderecoCoreDto dadosEndereco) {
		final Usuario usuario = buscarUsuario(id);
		
		validarUsuario(usuario);
		
		final Endereco endereco = buscarEndereco(usuario);
		
		atualizarDados(endereco, dadosEndereco);
		atualizarUsuario(usuario, endereco);
		
		salvar(usuario);
	}

	private void atualizarDados(Endereco endereco, DadosEnderecoCoreDto dadosEndereco) {
		endereco.atualizarDados(dadosEndereco.endereco(), 
								dadosEndereco.cidade(), 
								dadosEndereco.bairro(), 
								dadosEndereco.estado(), 
								dadosEndereco.numero(), 
								dadosEndereco.cep(), 
								dadosEndereco.complemento());
	}

	private void validarUsuario(final Usuario usuario) {
		if (!usuario.getIsAtivo()) {
			throw new UsuarioInativoException(USUARIO_INATIVO);
		} 
	}

	private void salvar(final Usuario usuario) {
		usuarioGateway.salvar(UsuarioPresenter.toUsuarioDto(usuario, buscarPerfil(usuario.getIdPerfil())));
	}
	
	private Usuario buscarUsuario(final UUID id) {
		return usuarioGateway.buscarPorId(id);
	}

	private Endereco buscarEndereco(final Usuario usuario) {
		return usuario.getDadosEndereco();
	}

	private void atualizarUsuario(final Usuario usuario, final Endereco endereco) {
		usuario.atualizarEndereco(endereco);
	}
	
	private Perfil buscarPerfil(final Integer idPerfil) {
		return perfilGateway.buscarPorId(idPerfil);
	}
}