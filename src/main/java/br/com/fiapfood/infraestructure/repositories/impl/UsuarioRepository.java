package br.com.fiapfood.infraestructure.repositories.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioInputDto;
import br.com.fiapfood.core.entities.dto.usuario.UsuarioPaginacaoInputDto;
import br.com.fiapfood.core.presenters.UsuarioPresenter;
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
	public DadosUsuarioInputDto buscarPorId(final UUID id) {
		final Optional<UsuarioEntity> usuario = usuarioRepository.findById(id);

		if(usuario.isPresent()) {
			return UsuarioPresenter.toUsuarioInputDto(usuario.get());
		} else {
			return null;
		}
	}
	
	@Override
	public UsuarioPaginacaoInputDto buscarTodos(final Integer pagina) {
		Page<UsuarioEntity> dados = usuarioRepository.findAll(PageRequest.of(pagina - 1, QUANTIDADE_REGISTROS));
		
		if (!dados.toList().isEmpty()) {
			return UsuarioPresenter.toUsuarioPaginacaoInputDto(dados);
		} else {
			return null;
		}
	}

	@Override
	public List<DadosUsuarioInputDto> buscarPorIdPerfil(Integer idPerfil) {
		List<UsuarioEntity> usuarios = usuarioRepository.findAllByIdPerfil(idPerfil);
		
		if(!usuarios.isEmpty()) {
			return UsuarioPresenter.toListUsuarioDto(usuarios);
		} else {
			return null;
		}
	}

	@Override
	public DadosUsuarioInputDto buscarPorMatriculaSenha(String matricula, String senha) {
		final Optional<UsuarioEntity> usuario = usuarioRepository.findByMatriculaSenha(matricula, senha);

		if(usuario.isPresent()) {
			return UsuarioPresenter.toUsuarioInputDto(usuario.get());
		} else {
			return null;
		}
	}

	@Override
	public boolean matriculaJaCadastrada(String matricula) {
		final Optional<UsuarioEntity> usuario = usuarioRepository.findByMatricula(matricula);

		if(usuario.isPresent()) {
			return true;
		} else {
			return false;
		}
	}
}