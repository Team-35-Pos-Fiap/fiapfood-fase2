package br.com.fiapfood.core.gateways.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.entities.dto.UsuarioDto;
import br.com.fiapfood.core.exceptions.UsuarioNaoEncontradoException;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.presenters.EnderecoPresenter;
import br.com.fiapfood.core.presenters.LoginPresenter;
import br.com.fiapfood.core.presenters.PerfilPresenter;
import br.com.fiapfood.core.presenters.UsuarioPresenter;
import br.com.fiapfood.infraestructure.repositories.interfaces.IUsuarioRepository;

public class UsuarioGateway implements IUsuarioGateway {

	private final IUsuarioRepository usuarioRepository;
	
	public UsuarioGateway(IUsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	@Override
	public Usuario buscarPorIdLogin(final UUID idLogin) {
		final UsuarioDto usuario = usuarioRepository.buscarPorIdLogin(idLogin);
		
		if(usuario != null) {
			return UsuarioPresenter.toUsuario(usuario);
		} else {
			throw new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o login informado.");
		}
	}
	
	@Override
	public Usuario buscarPorId(final UUID id) {
		final UsuarioDto usuario = usuarioRepository.buscarPorId(id);
		
		if(usuario != null) {
			return UsuarioPresenter.toUsuario(usuario);
		} else {
			throw new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o id informado.");
		}
	}

	@Override
	public Map<Class<?>, Object> buscarUsuariosComPaginacao(final Integer pagina) {
		final Map<Class<?>, Object> dados = usuarioRepository.buscarUsuariosComPaginacao(pagina);
		
		if(dados.get(List.class) != null) {
			return dados;
		} else {
			throw new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o login informado.");
		}
	}

	@Override
	public void salvar(final UsuarioDto usuario) {
		usuarioRepository.salvar(UsuarioPresenter.toUsuarioAtualizadoEntity(usuario, 
														 				    EnderecoPresenter.toEnderecoEntity(usuario.endereco()), 
																		    PerfilPresenter.toPerfilEntity(usuario.perfil()), 
																		    LoginPresenter.toLoginEntity(usuario.login())));		
	}

	@Override
	public boolean emailJaCadastrado(final String email) {
		return usuarioRepository.emailJaCadastrado(email);
	}	
}