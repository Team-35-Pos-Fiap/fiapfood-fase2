package br.com.fiapfood.infraestructure.repositories.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.com.fiapfood.core.entities.dto.tipo_culinaria.TipoCulinariaCoreDto;
import br.com.fiapfood.core.presenters.TipoCulinariaPresenter;
import br.com.fiapfood.infraestructure.entities.TipoCulinariaEntity;
import br.com.fiapfood.infraestructure.repositories.interfaces.ITipoCulinariaRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.jpa.ITipoCulinariaJpaRepository;

@Repository
public class TipoCulinariaRepository implements ITipoCulinariaRepository {

    private final ITipoCulinariaJpaRepository tipoCulinariaRepository;

	public TipoCulinariaRepository(ITipoCulinariaJpaRepository TipoCulinariaRepository) {
		this.tipoCulinariaRepository = TipoCulinariaRepository;
	}

	@Override
	public TipoCulinariaCoreDto buscarPorId(final Integer id) {
		final Optional<TipoCulinariaEntity> dados = tipoCulinariaRepository.findById(id);
		
		if(dados.isPresent()) {
			return TipoCulinariaPresenter.toTipoCulinariaDto(dados.get());
		} else {
			return null;
		}
	}

	@Override
	public List<TipoCulinariaCoreDto> buscarTodos() {
		return TipoCulinariaPresenter.toListTipoCulinariaDto(tipoCulinariaRepository.findAll());
	}

	@Override
	public void salvar(final TipoCulinariaEntity TipoCulinaria) {
		tipoCulinariaRepository.save(TipoCulinaria);
	}

	@Override
	public boolean nomeJaCadastrado(final String nome) {
		return tipoCulinariaRepository.existsByNomeIgnoreCase(nome);
	}
}