package br.com.fiapfood.core.controllers.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import br.com.fiapfood.core.controllers.interfaces.IRestauranteCoreController;
import br.com.fiapfood.core.entities.dto.item.ItemOutputCoreDto;
import br.com.fiapfood.core.exceptions.item.ImagemItemInvalidaException;
import br.com.fiapfood.core.presenters.AtendimentoPresenter;
import br.com.fiapfood.core.presenters.EnderecoPresenter;
import br.com.fiapfood.core.presenters.ImagemPresenter;
import br.com.fiapfood.core.presenters.ItemPresenter;
import br.com.fiapfood.core.presenters.RestaurantePresenter;
import br.com.fiapfood.core.usecases.atendimento.interfaces.IAdicionarAtendimentoUseCase;
import br.com.fiapfood.core.usecases.atendimento.interfaces.IAtualizarAtendimentoUseCase;
import br.com.fiapfood.core.usecases.atendimento.interfaces.IExcluirAtendimentoUseCase;
import br.com.fiapfood.core.usecases.item.interfaces.IAtualizarDescricaoItemUseCase;
import br.com.fiapfood.core.usecases.item.interfaces.IAtualizarDisponibilidadeConsumoPresencialItemUseCase;
import br.com.fiapfood.core.usecases.item.interfaces.IAtualizarDisponibilidadeItemUseCase;
import br.com.fiapfood.core.usecases.item.interfaces.IAtualizarImagemItemUseCase;
import br.com.fiapfood.core.usecases.item.interfaces.IAtualizarNomeItemUseCase;
import br.com.fiapfood.core.usecases.item.interfaces.IAtualizarPrecoItemUseCase;
import br.com.fiapfood.core.usecases.item.interfaces.IBaixarImagemItemUseCase;
import br.com.fiapfood.core.usecases.item.interfaces.IBuscarItemPorIdUseCase;
import br.com.fiapfood.core.usecases.item.interfaces.IBuscarTodosItensUseCase;
import br.com.fiapfood.core.usecases.item.interfaces.ICadastrarItemUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IAtualizarDonoRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IAtualizarEnderecoRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IAtualizarNomeRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IAtualizarTipoCulinariaRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IBuscarRestaurantePorId;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IBuscarTodosRestaurantesUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.ICadastrarRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IInativarRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IReativarRestauranteUseCase;
import br.com.fiapfood.infraestructure.controllers.request.atendimento.AtendimentoDto;
import br.com.fiapfood.infraestructure.controllers.request.endereco.DadosEnderecoDto;
import br.com.fiapfood.infraestructure.controllers.request.item.ImagemDto;
import br.com.fiapfood.infraestructure.controllers.request.item.ItemDto;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.CadastrarRestauranteDto;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.RestauranteDto;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.RestaurantePaginacaoDto;

public class RestauranteCoreController implements IRestauranteCoreController {
	private final IBuscarRestaurantePorId buscarRestaurantePorId;
	private final IBuscarTodosRestaurantesUseCase buscarTodosRestaurantesUseCase;
	private final ICadastrarRestauranteUseCase cadastrarRestauranteUseCase;
	private final IReativarRestauranteUseCase reativarRestauranteUseCase;
	private final IInativarRestauranteUseCase inativarRestauranteUseCase;
	private final IAtualizarDonoRestauranteUseCase atualizarDonoRestauranteUseCase;
	private final IAtualizarEnderecoRestauranteUseCase atualizarEnderecoRestauranteUseCase;
	private final IAtualizarNomeRestauranteUseCase atualizarNomeRestauranteUseCase;
	private final IAtualizarTipoCulinariaRestauranteUseCase atualizarTipoCulinariaRestauranteUseCase;
	private final IAtualizarAtendimentoUseCase atualizarAtendimentoUseCase;
	private final IAdicionarAtendimentoUseCase adicionarAtendimentoUseCase;
	private final IExcluirAtendimentoUseCase excluirAtendimentoUseCase;
	private final IAtualizarDescricaoItemUseCase atualizarDescricaoItemUseCase;
	private final IAtualizarNomeItemUseCase atualizarNomeItemUseCase;
	private final IAtualizarDisponibilidadeConsumoPresencialItemUseCase atualizarDisponibilidadeConsumoPresencialItemUseCase;
	private final IAtualizarDisponibilidadeItemUseCase atualizarDisponibilidadeItemUseCase;
	private final IAtualizarImagemItemUseCase atualizarImagemItemUseCase;
	private final IAtualizarPrecoItemUseCase atualizarPrecoItemUseCase;
	private final IBaixarImagemItemUseCase baixarImagemItemUseCase;
	private final IBuscarItemPorIdUseCase buscarItemPorIdUseCase;
	private final IBuscarTodosItensUseCase buscarTodosItensUseCase;
	private final ICadastrarItemUseCase cadastrarItemUseCase;
	
	private final String ERRO_CARREGAR_DADOS_IMAGEM = "Ocorreu um erro inesperado ao recuperar os dados da imagem do item.";
	
	public RestauranteCoreController(IBuscarRestaurantePorId buscarRestaurantePorId, IBuscarTodosRestaurantesUseCase buscarTodosRestaurantesUseCase,
									 ICadastrarRestauranteUseCase cadastrarRestauranteUseCase, IReativarRestauranteUseCase reativarRestauranteUseCase,
									 IInativarRestauranteUseCase inativarRestauranteUseCase, IAtualizarDonoRestauranteUseCase atualizarDonoRestauranteUseCase,
									 IAtualizarEnderecoRestauranteUseCase atualizarEnderecoRestauranteUseCase, IAtualizarNomeRestauranteUseCase atualizarNomeRestauranteUseCase,
									 IAtualizarTipoCulinariaRestauranteUseCase atualizarTipoCulinariaRestauranteUseCase,
									 IAtualizarAtendimentoUseCase atualizarAtendimentoUseCase, IAdicionarAtendimentoUseCase adicionarAtendimentoUseCase,
									 IExcluirAtendimentoUseCase excluirAtendimentoUseCase, IAtualizarDescricaoItemUseCase atualizarDescricaoItemUseCase,
									 IAtualizarNomeItemUseCase atualizarNomeItemUseCase, IAtualizarDisponibilidadeConsumoPresencialItemUseCase atualizarDisponibilidadeConsumoPresencialItemUseCase,
									 IAtualizarDisponibilidadeItemUseCase atualizarDisponibilidadeItemUseCase, IAtualizarImagemItemUseCase atualizarImagemItemUseCase,
									 IAtualizarPrecoItemUseCase atualizarPrecoItemUseCase, IBaixarImagemItemUseCase baixarImagemItemUseCase,
									 IBuscarItemPorIdUseCase buscarItemPorIdUseCase, IBuscarTodosItensUseCase buscarTodosItensUseCase,
									 ICadastrarItemUseCase cadastrarItemUseCase) {
		this.buscarRestaurantePorId = buscarRestaurantePorId;
		this.buscarTodosRestaurantesUseCase = buscarTodosRestaurantesUseCase; 
		this.cadastrarRestauranteUseCase = cadastrarRestauranteUseCase;
		this.reativarRestauranteUseCase = reativarRestauranteUseCase;
		this.inativarRestauranteUseCase = inativarRestauranteUseCase;
		this.atualizarDonoRestauranteUseCase = atualizarDonoRestauranteUseCase;
		this.atualizarEnderecoRestauranteUseCase = atualizarEnderecoRestauranteUseCase;
		this.atualizarNomeRestauranteUseCase = atualizarNomeRestauranteUseCase;
		this.atualizarTipoCulinariaRestauranteUseCase = atualizarTipoCulinariaRestauranteUseCase;
		this.atualizarAtendimentoUseCase = atualizarAtendimentoUseCase;
		this.adicionarAtendimentoUseCase = adicionarAtendimentoUseCase;
		this.excluirAtendimentoUseCase = excluirAtendimentoUseCase;
		this.atualizarDescricaoItemUseCase = atualizarDescricaoItemUseCase;
		this.atualizarNomeItemUseCase = atualizarNomeItemUseCase;
		this.atualizarDisponibilidadeConsumoPresencialItemUseCase = atualizarDisponibilidadeConsumoPresencialItemUseCase;
		this.atualizarImagemItemUseCase = atualizarImagemItemUseCase;
		this.atualizarDisponibilidadeItemUseCase = atualizarDisponibilidadeItemUseCase;
		this.atualizarPrecoItemUseCase = atualizarPrecoItemUseCase;
		this.baixarImagemItemUseCase = baixarImagemItemUseCase;
		this.buscarItemPorIdUseCase = buscarItemPorIdUseCase;
		this.buscarTodosItensUseCase = buscarTodosItensUseCase;
		this.cadastrarItemUseCase = cadastrarItemUseCase;
	}
	
	@Override
	public RestaurantePaginacaoDto buscarTodos(final Integer pagina) {
		return RestaurantePresenter.toListDadosRestauranteDto(buscarTodosRestaurantesUseCase.buscar(pagina));
	}

	@Override
	public RestauranteDto buscarPorId(final UUID id) {
		return RestaurantePresenter.toDadosRestauranteDto(buscarRestaurantePorId.buscar(id));
	}

	@Override
	public void cadastrar(CadastrarRestauranteDto restaurante) {
		cadastrarRestauranteUseCase.cadastrar(RestaurantePresenter.toCadastrarRestaurante(restaurante));
	}
	
	@Override
	public void inativar(final UUID id) {
		inativarRestauranteUseCase.inativar(id);
	}
	
	@Override
	public void reativar(final UUID id) {
		reativarRestauranteUseCase.reativar(id);
	}
	
	@Override
	public void atualizarDono(final UUID id, final UUID idDono) {
		atualizarDonoRestauranteUseCase.atualizar(id, idDono);
	}
	
	@Override
	public void atualizarNome(final UUID id, final String nome) {
		atualizarNomeRestauranteUseCase.atualizar(id, nome);
	}
	
	@Override
	public void atualizarEndereco(final UUID id, final DadosEnderecoDto endereco) {
		atualizarEnderecoRestauranteUseCase.atualizar(id, EnderecoPresenter.toEnderecoCoreDto(endereco));
	}
	
	@Override
	public void atualizarTipoCulinaria(final UUID id, final Integer idTipoCulinaria) {
		atualizarTipoCulinariaRestauranteUseCase.atualizar(id, idTipoCulinaria);
	}
	
	@Override
	public void atualizarAtendimento(final UUID id, final AtendimentoDto atendimento) {
		atualizarAtendimentoUseCase.atualizar(id, AtendimentoPresenter.toAtendimentoCoreDto(atendimento));
	}
	
	@Override
	public void adicionarAtendimento(final UUID id, final AtendimentoDto atendimento) {
		adicionarAtendimentoUseCase.adicionar(id, AtendimentoPresenter.toAtendimentoCoreDto(atendimento));
	}
	
	@Override
	public void excluirAtendimento(final UUID idRestaurante, final UUID idAtendimento) {
		excluirAtendimentoUseCase.excluir(idRestaurante, idAtendimento);
	}

	@Override
	public void atualizarDescricaoItem(UUID idRestaurante, UUID idItem, String descricao) {
		atualizarDescricaoItemUseCase.atualizar(idRestaurante, idItem, descricao);
	}
	
	@Override
	public void atualizarNomeItem(UUID idRestaurante, UUID idItem, String nome) {
		atualizarNomeItemUseCase.atualizar(idRestaurante, idItem, nome);
	}
	
	@Override
	public void atualizarDisponibilidadeConsumoPresencialItem(UUID idRestaurante, UUID idItem, Boolean isDisponivelParaConsumoPresencial) {
		atualizarDisponibilidadeConsumoPresencialItemUseCase.atualizar(idRestaurante, idItem, isDisponivelParaConsumoPresencial);
	}

	@Override
	public void atualizarDisponibilidadeItem(UUID idRestaurante, UUID idItem, Boolean isDisponivel) {
		atualizarDisponibilidadeItemUseCase.atualizar(idRestaurante, idItem, isDisponivel);
	}
	
	@Override
	public void atualizarImagemItem(UUID idRestaurante, UUID idItem, final MultipartFile imagem) {
		try {
			atualizarImagemItemUseCase.atualizar(idRestaurante, idItem, ImagemPresenter.toImagemDto(imagem));
		} catch (IOException e) {
			throw new ImagemItemInvalidaException(ERRO_CARREGAR_DADOS_IMAGEM);
		}	 
	}

	@Override
	public void atualizarPrecoItem(UUID idRestaurante, UUID idItem, BigDecimal preco) {
		atualizarPrecoItemUseCase.atualizar(idRestaurante, idItem, preco);		
	}
	
	@Override
	public ImagemDto baixarImagemItem(UUID idRestaurante, UUID idItem) {
		return ImagemPresenter.toImagemDto(baixarImagemItemUseCase.baixar(idRestaurante, idItem));		
	}
	
	@Override
	public ItemDto buscarItemPorId(final UUID idRestaurante, final UUID idItem) {
		return ItemPresenter.toItemOutputDto(buscarItemPorIdUseCase.buscar(idRestaurante, idItem));
	}

	@Override
	public List<ItemDto> buscarTodosItens(final UUID idRestaurante) {
		List<ItemOutputCoreDto> itens = buscarTodosItensUseCase.buscar(idRestaurante);
		
		return ItemPresenter.toListItemOutputDto(itens);
	}

	@Override
	public void cadastrar(UUID idRestaurante, String nome, String descricao, BigDecimal preco, Boolean disponivelParaConsumoPresencial, MultipartFile imagem) {
		try {
			cadastrarItemUseCase.cadastrar(idRestaurante, nome, descricao, preco, disponivelParaConsumoPresencial, ImagemPresenter.toImagemDto(imagem));
		} catch (IOException e) {
			throw new ImagemItemInvalidaException(ERRO_CARREGAR_DADOS_IMAGEM);
		}		
	}
}