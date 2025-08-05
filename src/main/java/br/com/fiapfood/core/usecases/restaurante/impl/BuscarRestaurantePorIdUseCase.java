package br.com.fiapfood.core.usecases.restaurante.impl;

import java.util.UUID;

import br.com.fiapfood.core.entities.Restaurante;
import br.com.fiapfood.core.entities.TipoCulinaria;
import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.entities.dto.restaurante.DadosRestauranteCoreDto;
import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioResumidoCoreDto;
import br.com.fiapfood.core.gateways.interfaces.IRestauranteGateway;
import br.com.fiapfood.core.gateways.interfaces.ITipoCulinariaGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.presenters.RestaurantePresenter;
import br.com.fiapfood.core.presenters.UsuarioPresenter;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IBuscarRestaurantePorId;

public class BuscarRestaurantePorIdUseCase implements IBuscarRestaurantePorId {
	private final IRestauranteGateway restauranteGateway;
	private final IUsuarioGateway usuarioGateway;
	private final ITipoCulinariaGateway tipoCulinariaGateway;
		
	public BuscarRestaurantePorIdUseCase(IRestauranteGateway restauranteGateway, IUsuarioGateway usuarioGateway, 
								   		 ITipoCulinariaGateway tipoCulinariaGateway) {
		this.restauranteGateway = restauranteGateway;
		this.usuarioGateway = usuarioGateway;
		this.tipoCulinariaGateway = tipoCulinariaGateway;
	}
	
	@Override
	public DadosRestauranteCoreDto buscar(UUID id) {
		return toDadosRestauranteOutputDto(buscarPorId(id));
	}
	
	private DadosRestauranteCoreDto toDadosRestauranteOutputDto(Restaurante restaurante) {
		return RestaurantePresenter.toRestauranteDto(restaurante, 
													 restaurante.getDadosEndereco(), 
													 buscarUsuario(restaurante.getIdDonoRestaurante()),
													 buscarTipoCulinaria(restaurante.getIdTipoCulinaria()),
													 restaurante.getAtendimentos());
	}
	
	private Restaurante buscarPorId(UUID id) {
		return restauranteGateway.buscarPorId(id);
	}
	
	private TipoCulinaria buscarTipoCulinaria(Integer idTipoCulinaria) {
		return tipoCulinariaGateway.buscarPorId(idTipoCulinaria);
	}

	private DadosUsuarioResumidoCoreDto buscarUsuario(UUID id) {
		Usuario usuario = buscarUsuarioPorId(id);
		
		return UsuarioPresenter.toUsuarioOutputDto(usuario);
	}
	
	private Usuario buscarUsuarioPorId(final UUID idUsuario) {
		return usuarioGateway.buscarPorId(idUsuario);
	}
}