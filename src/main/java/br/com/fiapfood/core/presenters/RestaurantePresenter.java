package br.com.fiapfood.core.presenters;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import br.com.fiapfood.core.entities.Atendimento;
import br.com.fiapfood.core.entities.Endereco;
import br.com.fiapfood.core.entities.Item;
import br.com.fiapfood.core.entities.Restaurante;
import br.com.fiapfood.core.entities.TipoCulinaria;
import br.com.fiapfood.core.entities.dto.paginacao.PaginacaoCoreDto;
import br.com.fiapfood.core.entities.dto.restaurante.CadastrarRestauranteCoreDto;
import br.com.fiapfood.core.entities.dto.restaurante.DadosRestauranteCoreDto;
import br.com.fiapfood.core.entities.dto.restaurante.DadosRestauranteDto;
import br.com.fiapfood.core.entities.dto.restaurante.RestaurantePaginacaoCoreDto;
import br.com.fiapfood.core.entities.dto.restaurante.RestaurantePaginacaoInputDto;
import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioResumidoCoreDto;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.CadastrarRestauranteDto;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.RestauranteDto;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.RestaurantePaginacaoDto;
import br.com.fiapfood.infraestructure.entities.RestauranteEntity;

public class RestaurantePresenter {

	public static DadosRestauranteCoreDto toRestauranteDto(Restaurante restaurante, Endereco endereco, DadosUsuarioResumidoCoreDto usuario, 
														   TipoCulinaria tipoCulinaria, List<Atendimento> atendimentos) {
		return new DadosRestauranteCoreDto(restaurante.getId(), restaurante.getNome(), EnderecoPresenter.toEnderecoDto(endereco), restaurante.getIsAtivo(), 
											 usuario, TipoCulinariaPresenter.toTipoCulinariaDto(tipoCulinaria), AtendimentoPresenter.toListAtendimentoDto(atendimentos));
	}
	
	public static DadosRestauranteDto toRestauranteDto(RestauranteEntity restaurante) {
		return new DadosRestauranteDto(restaurante.getId(), restaurante.getNome(), EnderecoPresenter.toEnderecoDto(restaurante.getEndereco()), restaurante.getDono().getId(), 
									   restaurante.getTipoCulinaria().getId(), restaurante.getIsAtivo(), AtendimentoPresenter.toListAtendimentoDtos(restaurante.getAtendimentos()),
									   ItemPresenter.toListItemInputDto(restaurante.getItens()));
	}
	
	public static DadosRestauranteDto toRestauranteDto(Restaurante restaurante) {
		return new DadosRestauranteDto(restaurante.getId(), restaurante.getNome(), EnderecoPresenter.toEnderecoDto(restaurante.getDadosEndereco()), 
									   restaurante.getIdDonoRestaurante(), restaurante.getIdTipoCulinaria(), restaurante.getIsAtivo(), 
									   AtendimentoPresenter.toListAtendimentoDto(restaurante.getAtendimentos()),
									   ItemPresenter.toListItemDto(restaurante.getItens()));
	}
	
	public static Restaurante toRestaurante(CadastrarRestauranteCoreDto restaurante) {
		return Restaurante.criar(null, restaurante.nome(), EnderecoPresenter.toEndereco(restaurante.dadosEndereco()), restaurante.idDonoRestaurante(), 
								 restaurante.idTipoCulinaria(), true, AtendimentoPresenter.toListAtendimento(restaurante.atendimentos()), new ArrayList<Item>());
	}

	public static Restaurante toRestaurante(DadosRestauranteDto restaurante) {
		return Restaurante.criar(restaurante.id(), restaurante.nome(), EnderecoPresenter.toEndereco(restaurante.dadosEndereco()), restaurante.idDono(), 
								 restaurante.idTipoCulinaria(), restaurante.isAtivo(), AtendimentoPresenter.toListAtendimento(restaurante.atendimentos()),
								 ItemPresenter.toListItem(restaurante.itens()));
	}
	
	public static RestauranteEntity toRestauranteEntity(DadosRestauranteDto restaurante) {
		return new RestauranteEntity(restaurante.id(), 
									 restaurante.nome(), 
									 EnderecoPresenter.toEnderecoEntity(restaurante.dadosEndereco()), 
									 UsuarioPresenter.toUsuarioResumidoEntity(restaurante.idDono()), 
									 TipoCulinariaPresenter.toTipoCulinariaResumidoEntity(restaurante.idTipoCulinaria()),
									 restaurante.isAtivo(), 
									 null,
									 AtendimentoPresenter.toListAtendimentoEntity(restaurante.atendimentos()));
	}
		
	public static RestauranteEntity toAtualizacaoRestauranteEntity(DadosRestauranteDto restaurante) {
		return new RestauranteEntity(restaurante.id(), 
									 restaurante.nome(), 
									 EnderecoPresenter.toEnderecoEntity(restaurante.dadosEndereco()), 
									 UsuarioPresenter.toUsuarioResumidoEntity(restaurante.idDono()), 
									 TipoCulinariaPresenter.toTipoCulinariaResumidoEntity(restaurante.idTipoCulinaria()),
									 restaurante.isAtivo(), 
									 ItemPresenter.toListItemEntity(restaurante.itens()),
									 AtendimentoPresenter.toListAtendimentoEntity(restaurante.atendimentos()));
	}

	public static RestaurantePaginacaoInputDto toRestaurantePaginacaoInputDto(Page<RestauranteEntity> dados) {
		List<DadosRestauranteDto> restaurantes = dados.toList()
														   .stream()
														   .map(r -> RestaurantePresenter.toRestauranteDto(r))
														   .collect(Collectors.toList());

		PaginacaoCoreDto paginacao = new PaginacaoCoreDto(dados.getNumber() + 1, dados.getTotalPages(), Long.valueOf(dados.getTotalElements()).intValue());
		
		return new RestaurantePaginacaoInputDto(restaurantes, paginacao);
	}

	public static List<Restaurante> toListRestaurante(List<DadosRestauranteDto> restaurantes) {
		return restaurantes.stream().map(r -> RestaurantePresenter.toRestaurante(r)).toList();
	}
	
	public static RestaurantePaginacaoCoreDto toRestaurantePaginacaoOutputDto(List<DadosRestauranteCoreDto> restaurantes, PaginacaoCoreDto paginacao) {
		return new RestaurantePaginacaoCoreDto(restaurantes, paginacao);
	}

	public static RestaurantePaginacaoDto toListDadosRestauranteDto(RestaurantePaginacaoCoreDto dados) {
		return new RestaurantePaginacaoDto(RestaurantePresenter.toListRestauranteDto(dados.restaurantes()), 
										  PaginacaoPresenter.toPaginacaoDto(dados.paginacao()));
	}

	private static List<RestauranteDto> toListRestauranteDto(List<DadosRestauranteCoreDto> restaurantes) {
		return restaurantes.stream().map(r -> RestaurantePresenter.toDadosRestauranteDto(r)).toList();
	}

	public static RestauranteDto toDadosRestauranteDto(DadosRestauranteCoreDto restaurante) {
		return new RestauranteDto(restaurante.id(), restaurante.nome(), EnderecoPresenter.toEnderecoDto(restaurante.endereco()), restaurante.isAtivo(), 
								  UsuarioPresenter.toDadosUsuarioDto(restaurante.dono()), 
								  TipoCulinariaPresenter.toTipoCulinariaDto(restaurante.tipoCulinaria()), 
								  AtendimentoPresenter.toListAtendimentoOutputDto(restaurante.atendimentos()));
	}

	public static CadastrarRestauranteCoreDto toCadastrarRestaurante(CadastrarRestauranteDto restaurante) {
		return new CadastrarRestauranteCoreDto(restaurante.nome(), EnderecoPresenter.toEnderecoCoreDto(restaurante.dadosEndereco()), 
											   restaurante.idDonoRestaurante(), restaurante.idTipoCulinaria(), AtendimentoPresenter.toListAtendimentoCoreDto(restaurante.atendimentos()));
	}
}