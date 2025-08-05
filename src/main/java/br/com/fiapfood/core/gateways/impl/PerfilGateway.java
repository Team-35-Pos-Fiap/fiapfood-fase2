package br.com.fiapfood.core.gateways.impl;

import java.util.List;

import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.entities.dto.perfil.PerfilCoreDto;
import br.com.fiapfood.core.exceptions.perfil.PerfilInvalidoException;
import br.com.fiapfood.core.exceptions.perfil.PerfilNaoEncontradoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.presenters.PerfilPresenter;
import br.com.fiapfood.infraestructure.repositories.interfaces.IPerfilRepository;

public class PerfilGateway implements IPerfilGateway {

	private final IPerfilRepository perfilRepository;
	
	private final String PERFIL_NAO_ENCONTRADO = "Não foi encontrado nenhum perfil na base de dados.";
	private final String PERFIS_NAO_ENCONTRADOS = "Não foi encontrado nenhum perfil com o id informado.";

	public PerfilGateway(IPerfilRepository perfilRepository) {
		this.perfilRepository = perfilRepository;
	}
	
	@Override
	public List<Perfil> buscarTodos() {
		final List<PerfilCoreDto> perfis = perfilRepository.buscarTodos();
		
		if(perfis.size() > 0) {
			return PerfilPresenter.toListPerfil(perfis);
		} else {
			throw new PerfilNaoEncontradoException(PERFIL_NAO_ENCONTRADO);			
		}
	}
	
	@Override
	public Perfil buscarPorId(final Integer id) {
		final PerfilCoreDto perfil = perfilRepository.buscarPorId(id);
		
		if(perfil != null) {
			return PerfilPresenter.toPerfil(perfil);
		} else {
			throw new PerfilInvalidoException(PERFIS_NAO_ENCONTRADOS);			
		}
	}

	@Override
	public void salvar(final PerfilCoreDto perfil) {
		perfilRepository.salvar(PerfilPresenter.toPerfilEntity(perfil));
	}

	@Override
	public boolean nomeJaCadastrado(final String nome) {
		return perfilRepository.nomeJaCadastrado(nome);
	}
}