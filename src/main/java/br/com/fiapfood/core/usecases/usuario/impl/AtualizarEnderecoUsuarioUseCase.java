package br.com.fiapfood.core.usecases.usuario.impl;

import java.util.UUID;

import br.com.fiapfood.core.entities.Endereco;
import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.entities.dto.DadosEnderecoDto;
import br.com.fiapfood.core.exceptions.UsuarioInativoException;
import br.com.fiapfood.core.gateways.interfaces.IEnderecoGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.presenters.EnderecoPresenter;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarEnderecoUsuarioUseCase;

public class AtualizarEnderecoUsuarioUseCase implements IAtualizarEnderecoUsuarioUseCase {
	private final IUsuarioGateway usuarioGateway;
	private final IEnderecoGateway enderecoGateway;

	public AtualizarEnderecoUsuarioUseCase(IUsuarioGateway usuarioGateway, IEnderecoGateway enderecoGateway) {
		this.usuarioGateway = usuarioGateway;
		this.enderecoGateway = enderecoGateway;
	}
	
	@Override
	public void atualizar(final UUID id, final DadosEnderecoDto dadosEndereco) {
		final Usuario usuario = buscarUsuario(id);
		
		validarUsuario(usuario);
		
		final Endereco endereco = buscarEndereco(usuario.getIdEndereco());
		
		endereco.atualizarDados(dadosEndereco.endereco(), 
								dadosEndereco.cidade(), 
								dadosEndereco.bairro(), 
								dadosEndereco.estado(), 
								dadosEndereco.numero(), 
								dadosEndereco.cep(), 
								dadosEndereco.complemento());
		
		salvar(endereco);
	}

	private void validarUsuario(final Usuario usuario) {
		if (!usuario.getIsAtivo()) {
			throw new UsuarioInativoException("Não é possível alterar o nome de um usuário inativo.");
		} 
	}

	private void salvar(final Endereco endereco) {
		enderecoGateway.salvar(EnderecoPresenter.toEnderecoDto(endereco));
	}
	
	private Usuario buscarUsuario(final UUID id) {
		return usuarioGateway.buscarPorId(id);
	}

	private Endereco buscarEndereco(final UUID idEndereco) {
		return enderecoGateway.buscarPorId(idEndereco);
	}
}