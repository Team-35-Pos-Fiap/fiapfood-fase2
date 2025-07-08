package br.com.fiapfood.core.gateways.impl;

import java.util.List;

import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.entities.dto.PerfilDto;
import br.com.fiapfood.core.exceptions.PerfilInvalidoException;
import br.com.fiapfood.core.exceptions.PerfilNaoEncontradoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.presenters.PerfilPresenter;
import br.com.fiapfood.infraestructure.repositories.interfaces.IPerfilRepository;

public class PerfilGateway implements IPerfilGateway {

	private final IPerfilRepository perfilRepository;
	
	public PerfilGateway(IPerfilRepository perfilRepository) {
		this.perfilRepository = perfilRepository;
	}
	
	@Override
	public List<Perfil> buscarTodos() {
		final List<PerfilDto> perfis = perfilRepository.buscarTodos();
		
		if(perfis.size() > 0) {
			return PerfilPresenter.toListPerfil(perfis);
		} else {
			throw new PerfilNaoEncontradoException("Não foi encontrado nenhum perfil na base de dados.");			
		}
	}
	
	@Override
	public Perfil buscarPorId(final Integer id) {
		final PerfilDto perfil = perfilRepository.buscarPorId(id);
		
		if(perfil != null) {
			return PerfilPresenter.toPerfil(perfil);
		} else {
			throw new PerfilInvalidoException("Não foi encontrado nenhum perfil com o id informado.");			
		}
	}
}