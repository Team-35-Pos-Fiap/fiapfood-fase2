package br.com.fiapfood.core.controllers.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import br.com.fiapfood.infraestructure.controllers.request.atendimento.AtendimentoDto;
import br.com.fiapfood.infraestructure.controllers.request.endereco.DadosEnderecoDto;
import br.com.fiapfood.infraestructure.controllers.request.item.ImagemDto;
import br.com.fiapfood.infraestructure.controllers.request.item.ItemDto;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.CadastrarRestauranteDto;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.RestauranteDto;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.RestaurantePaginacaoDto;

public interface IRestauranteCoreController {
	RestaurantePaginacaoDto buscarTodos(Integer pagina);
	RestauranteDto buscarPorId(UUID id);
	void cadastrar(CadastrarRestauranteDto restaurante);
	void reativar(UUID idRestaurante);
	void inativar(UUID idRestaurante);
	void atualizarTipoCulinaria(UUID idRestaurante, Integer tipoCulinaria);
	void atualizarNome(UUID idRestaurante, String nome);
	void atualizarDono(UUID idRestaurante, UUID idDono);
	void atualizarEndereco(UUID idRestaurante, DadosEnderecoDto endereco);
	void atualizarAtendimento(UUID idRestaurante, AtendimentoDto atendimento);
	void excluirAtendimento(UUID idRestaurante, UUID idAtendimento);
	void adicionarAtendimento(UUID idRestaurante, AtendimentoDto atendimento);
	void atualizarDescricaoItem(UUID idRestaurante, UUID idItem, String descricao);
	void atualizarNomeItem(UUID idRestaurante, UUID idItem, String nome);
	void atualizarDisponibilidadeConsumoPresencialItem(UUID idRestaurante, UUID idItem, Boolean isDisponivelParaConsumoPresencial);
	void atualizarDisponibilidadeItem(UUID idRestaurante, UUID idItem, Boolean isDisponivel);
	void atualizarImagemItem(UUID idRestaurante, UUID idItem, MultipartFile imagem);
	void atualizarPrecoItem(UUID idRestaurante, UUID idItem, BigDecimal preco);
	ItemDto buscarItemPorId(UUID idRestaurante, UUID idItem);
	List<ItemDto> buscarTodosItens(UUID idRestaurante);
	void cadastrar(UUID idRestaurante, String nome, String descricao, BigDecimal preco, Boolean disponivelParaConsumoPresencial, MultipartFile imagem);
	ImagemDto baixarImagemItem(UUID idRestaurante, UUID idItem);
}