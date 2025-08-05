package br.com.fiapfood.core.usecases.restaurante.impl;

import java.util.List;
import java.util.UUID;

import br.com.fiapfood.core.entities.Restaurante;
import br.com.fiapfood.core.entities.TipoCulinaria;
import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.entities.dto.paginacao.PaginacaoCoreDto;
import br.com.fiapfood.core.entities.dto.restaurante.DadosRestauranteCoreDto;
import br.com.fiapfood.core.entities.dto.restaurante.DadosRestauranteDto;
import br.com.fiapfood.core.entities.dto.restaurante.RestaurantePaginacaoCoreDto;
import br.com.fiapfood.core.entities.dto.restaurante.RestaurantePaginacaoInputDto;
import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioResumidoCoreDto;
import br.com.fiapfood.core.gateways.interfaces.IRestauranteGateway;
import br.com.fiapfood.core.gateways.interfaces.ITipoCulinariaGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.presenters.RestaurantePresenter;
import br.com.fiapfood.core.presenters.UsuarioPresenter;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IBuscarTodosRestaurantesUseCase;

public class BuscarTodosRestaurantesUseCase implements IBuscarTodosRestaurantesUseCase {

	private final IRestauranteGateway restauranteGateway;
	private final IUsuarioGateway usuarioGateway;
	private final ITipoCulinariaGateway tipoCulinariaGateway;
		
	public BuscarTodosRestaurantesUseCase (IRestauranteGateway restauranteGateway, IUsuarioGateway usuarioGateway, 
								   		   ITipoCulinariaGateway tipoCulinariaGateway) {
		this.restauranteGateway = restauranteGateway;
		this.usuarioGateway = usuarioGateway;
		this.tipoCulinariaGateway = tipoCulinariaGateway;
	}
	
	@Override
	public RestaurantePaginacaoCoreDto buscar(final Integer pagina) {
		RestaurantePaginacaoInputDto dados = restauranteGateway.buscarTodos(pagina);
		
		return toRestaurantePaginacaoOutputDto(toListRestauranteDto(toListRestaurante(dados.restaurantes())), 
											   dados.paginacao());
	}

	private DadosUsuarioResumidoCoreDto buscarUsuario(UUID id) {
		Usuario usuario = usuarioGateway.buscarPorId(id);
		
		return UsuarioPresenter.toUsuarioOutputDto(usuario);
	}
	
	private List<Restaurante> toListRestaurante(final List<DadosRestauranteDto> restaurantes) {
		return RestaurantePresenter.toListRestaurante(restaurantes);
	}
	
	private List<DadosRestauranteCoreDto> toListRestauranteDto(final List<Restaurante> restaurantes) {
		return restaurantes.stream().map(r -> RestaurantePresenter.toRestauranteDto(r, 
																					r.getDadosEndereco(),
																					buscarUsuario(r.getIdDonoRestaurante()),
																					buscarTipoCulinaria(r.getIdTipoCulinaria()),
																					r.getAtendimentos())).toList();
	}
	
	private RestaurantePaginacaoCoreDto toRestaurantePaginacaoOutputDto(final List<DadosRestauranteCoreDto> restaurantes, final PaginacaoCoreDto paginacao) {
		return RestaurantePresenter.toRestaurantePaginacaoOutputDto(restaurantes, paginacao);
	}
	
	private TipoCulinaria buscarTipoCulinaria(Integer idTipoCulinaria) {
		return tipoCulinariaGateway.buscarPorId(idTipoCulinaria);
	}
}