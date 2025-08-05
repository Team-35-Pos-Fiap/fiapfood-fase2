package br.com.fiapfood.core.usecases.restaurante.impl;

import java.util.UUID;

import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.entities.Restaurante;
import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.entities.dto.restaurante.CadastrarRestauranteCoreDto;
import br.com.fiapfood.core.exceptions.restaurante.CadastrarRestauranteNaoPermitidoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IRestauranteGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.presenters.RestaurantePresenter;
import br.com.fiapfood.core.usecases.restaurante.interfaces.ICadastrarRestauranteUseCase;

public class CadastrarRestauranteUseCase implements ICadastrarRestauranteUseCase {

	private final IRestauranteGateway restauranteGateway;
	private final IUsuarioGateway usuarioGateway;
	private final IPerfilGateway perfilGateway;
	private final String PERFIL_USUARIO_VALIDO = "Dono";
	private final String USUARIO_DONO = "Não é possível cadastrar o restaurante, pois o responsável não possui o perfil de dono.";
	private final String USUARIO_INATIVO = "Não é possível cadastrar o restaurante, pois o responsável se encontra inativo.";
	
	public CadastrarRestauranteUseCase(IRestauranteGateway restauranteGateway, IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		this.restauranteGateway = restauranteGateway;
		this.usuarioGateway = usuarioGateway;
		this.perfilGateway = perfilGateway;
	}
	
	@Override
	public void cadastrar(CadastrarRestauranteCoreDto restauranteDto) {
		Restaurante restaurante = toRestaurante(restauranteDto);
		Usuario usuario = buscarUsuario(restaurante.getIdDonoRestaurante());
		
		validarUsuarioAtivo(usuario);
		validarPerfilUsuario(usuario);
		
		cadastrar(restaurante);
	}

	private void cadastrar(Restaurante restaurante) {
		restauranteGateway.cadastrar(RestaurantePresenter.toRestauranteDto(restaurante));		
	}

	private void validarPerfilUsuario(Usuario usuario) {
		Perfil perfil = buscarPerfil(usuario.getIdPerfil());
		
		if(!perfil.getNome().equalsIgnoreCase(PERFIL_USUARIO_VALIDO)) {
			throw new CadastrarRestauranteNaoPermitidoException(USUARIO_DONO);
		}
	}

	private Usuario buscarUsuario(UUID idDono) {
		return usuarioGateway.buscarPorId(idDono);
	}
	
	private void validarUsuarioAtivo(final Usuario usuario) {
		if (!usuario.getIsAtivo()) {
			throw new CadastrarRestauranteNaoPermitidoException(USUARIO_INATIVO);
		}
	}
	
	private Restaurante toRestaurante(CadastrarRestauranteCoreDto restaurante) {
		return RestaurantePresenter.toRestaurante(restaurante);
	}
	
	private Perfil buscarPerfil(Integer idPerfil) {
		return perfilGateway.buscarPorId(idPerfil);
	}
}