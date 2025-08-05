package br.com.fiapfood.core.usecases.item.interfaces;

import java.math.BigDecimal;
import java.util.UUID;

import br.com.fiapfood.core.entities.dto.item.ImagemCoreDto;


public interface ICadastrarItemUseCase {
	void cadastrar(UUID idRestaurante, String nome, String descricao, BigDecimal preco, Boolean disponivelParaConsumoPresencial, ImagemCoreDto dadosImagem);
}