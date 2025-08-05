package br.com.fiapfood.core.usecases.usuario.impl;

import java.util.UUID;

import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioCoreDto;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.presenters.UsuarioPresenter;
import br.com.fiapfood.core.usecases.usuario.interfaces.IBuscarUsuarioPorIdUseCase;

public class BuscarUsuarioPorIdUseCase implements IBuscarUsuarioPorIdUseCase {

	private final IUsuarioGateway usuarioGateway;
	private final IPerfilGateway perfilGateway;

	public BuscarUsuarioPorIdUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		this.usuarioGateway = usuarioGateway;
		this.perfilGateway = perfilGateway;
	}
	
	@Override
	public DadosUsuarioCoreDto buscar(final UUID id) {
		return toDadosUsuarioOutputDto(buscarUsuario(id));
	}
	
	private DadosUsuarioCoreDto toDadosUsuarioOutputDto(Usuario usuario) {
		return UsuarioPresenter.toUsuarioDto(usuario,
											 buscarPerfil(usuario.getIdPerfil())); 
	}
	
	private Usuario buscarUsuario(final UUID id) {
		return usuarioGateway.buscarPorId(id);
	}
	
	private Perfil buscarPerfil(final Integer idPerfil) {
		return perfilGateway.buscarPorId(idPerfil);
	}
}