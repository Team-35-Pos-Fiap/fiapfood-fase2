package br.com.fiapfood.infraestructure.repositories.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.com.fiapfood.core.entities.dto.EnderecoDto;
import br.com.fiapfood.core.presenters.EnderecoPresenter;
import br.com.fiapfood.infraestructure.entities.EnderecoEntity;
import br.com.fiapfood.infraestructure.repositories.interfaces.IEnderecoRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.jpa.IEnderecoJpaRepository;

@Repository
public class EnderecoRepository implements IEnderecoRepository {

	private final IEnderecoJpaRepository enderecoRepository;

	public EnderecoRepository(IEnderecoJpaRepository enderecoRepository) {
		this.enderecoRepository = enderecoRepository;
	}

	@Override
	public EnderecoDto buscarPorId(final UUID id) {
		final Optional<EnderecoEntity> dados = enderecoRepository.findById(id);
		
		if(dados != null) {
			return EnderecoPresenter.toEnderecoDto(dados.get());			
		} else {
			return null;
		}
		
	}

	@Override
	public void salvar(final EnderecoEntity endereco) {
		enderecoRepository.save(endereco);
	}
}