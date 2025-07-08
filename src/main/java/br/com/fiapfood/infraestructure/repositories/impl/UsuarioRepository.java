package br.com.fiapfood.infraestructure.repositories.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import br.com.fiapfood.core.entities.dto.PaginacaoDto;
import br.com.fiapfood.core.entities.dto.UsuarioDto;
import br.com.fiapfood.core.presenters.EnderecoPresenter;
import br.com.fiapfood.core.presenters.LoginPresenter;
import br.com.fiapfood.core.presenters.PaginacaoPresenter;
import br.com.fiapfood.core.presenters.PerfilPresenter;
import br.com.fiapfood.core.presenters.UsuarioPresenter;
import br.com.fiapfood.core.utils.MapUtils;
import br.com.fiapfood.infraestructure.entities.UsuarioEntity;
import br.com.fiapfood.infraestructure.repositories.interfaces.IUsuarioRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.jpa.IUsuarioJpaRepository;

@Repository
public class UsuarioRepository implements IUsuarioRepository {

	private final IUsuarioJpaRepository usuarioRepository;
	private final Integer QUANTIDADE_REGISTROS = 5;

	public UsuarioRepository(IUsuarioJpaRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	@Override
	public void salvar(final UsuarioEntity usuario) {
		usuarioRepository.save(usuario);	
	}

	@Override
	public boolean emailJaCadastrado(final String email) {
		return usuarioRepository.existsByEmail(email);
	}

	@Override
	public UsuarioDto buscarPorIdLogin(final UUID loginId) {
		final Optional<UsuarioEntity> usuario = usuarioRepository.findByIdLogin(loginId);

		if(usuario.isPresent()) {
			return UsuarioPresenter.toUsuarioDto(usuario.get(), 
												 PerfilPresenter.toPerfilDto(usuario.get().getPerfil()), 
												 LoginPresenter.toLoginDto(usuario.get().getDadosLogin()),
												 EnderecoPresenter.toEnderecoDto(usuario.get().getDadosEndereco()));
		} else {
			return null;
		}
	}
	
	@Override
	public UsuarioDto buscarPorId(final UUID id) {
		final Optional<UsuarioEntity> usuario = usuarioRepository.findById(id);

		if(usuario.isPresent()) {
			return UsuarioPresenter.toUsuarioDto(usuario.get(), 
												 PerfilPresenter.toPerfilDto(usuario.get().getPerfil()), 
												 LoginPresenter.toLoginDto(usuario.get().getDadosLogin()),
												 EnderecoPresenter.toEnderecoDto(usuario.get().getDadosEndereco()));
		} else {
			return null;
		}
	}

	@Override
	public Map<Class<?>, Object> buscarUsuariosComPaginacao(final Integer pagina) {
		return getDados(usuarioRepository.findAll(PageRequest.of(pagina - 1, QUANTIDADE_REGISTROS)));
	}
	
	private Map<Class<?>, Object> getDados(final Page<UsuarioEntity> resultado) {
		MapUtils mapUtils = new MapUtils();
		
		mapUtils.adicionarItens(UsuarioPresenter.toListUsuarioDto(resultado.get().collect(Collectors.toList())), List.class);
		mapUtils.adicionarItens(PaginacaoPresenter.toDto(resultado.getNumber(), resultado.getTotalPages(), Long.valueOf(resultado.getTotalElements()).intValue()), PaginacaoDto.class);

		return mapUtils.getMap();
	}
}