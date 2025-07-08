package br.com.fiapfood.infraestructure.repositories.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.com.fiapfood.core.entities.dto.PerfilDto;
import br.com.fiapfood.core.presenters.PerfilPresenter;
import br.com.fiapfood.infraestructure.entities.PerfilEntity;
import br.com.fiapfood.infraestructure.repositories.interfaces.IPerfilRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.jpa.IPerfilJpaRepository;

@Repository
public class PerfilRepository implements IPerfilRepository {

    private final IPerfilJpaRepository perfilRepository;

	public PerfilRepository(IPerfilJpaRepository perfilRepository) {
		this.perfilRepository = perfilRepository;
	}

	@Override
	public PerfilDto buscarPorId(final Integer id) {
		final Optional<PerfilEntity> dados = perfilRepository.findById(id);
		
		if(dados.isPresent()) {
			return PerfilPresenter.toPerfilDto(dados.get());
		} else {
			return null;
		}
	}

	@Override
	public List<PerfilDto> buscarTodos() {
		return PerfilPresenter.toListPerfilDto(perfilRepository.findAll());
	}
}