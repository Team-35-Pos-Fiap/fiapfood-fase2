package br.com.fiapfood.core.gateways.impl;

import java.util.UUID;

import br.com.fiapfood.core.entities.Endereco;
import br.com.fiapfood.core.entities.dto.EnderecoDto;
import br.com.fiapfood.core.exceptions.EnderecoUsuarioInvalidoException;
import br.com.fiapfood.core.gateways.interfaces.IEnderecoGateway;
import br.com.fiapfood.core.presenters.EnderecoPresenter;
import br.com.fiapfood.infraestructure.repositories.interfaces.IEnderecoRepository;

public class EnderecoGateway implements IEnderecoGateway {
	private final IEnderecoRepository enderecoRepository;
	
	public EnderecoGateway(IEnderecoRepository enderecoRepository) {
		this.enderecoRepository = enderecoRepository;
	}
	
	@Override
	public Endereco buscarPorId(final UUID id) {
		final EnderecoDto endereco = enderecoRepository.buscarPorId(id);
		
		if(endereco != null) {
			return EnderecoPresenter.toEndereco(endereco);		
		} else {
			throw new EnderecoUsuarioInvalidoException("Não foi encontrado nenhum endereço com o id informado.");
		}
	}
	
	@Override
	public void salvar(final EnderecoDto endereco) {
		enderecoRepository.salvar(EnderecoPresenter.toEnderecoEntity(endereco));
	}
}