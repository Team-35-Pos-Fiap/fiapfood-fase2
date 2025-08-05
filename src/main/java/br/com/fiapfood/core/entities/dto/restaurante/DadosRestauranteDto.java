package br.com.fiapfood.core.entities.dto.restaurante;

import java.util.List;
import java.util.UUID;

import br.com.fiapfood.core.entities.dto.atendimento.AtendimentoCoreDto;
import br.com.fiapfood.core.entities.dto.endereco.EnderecoCoreDto;
import br.com.fiapfood.core.entities.dto.item.ItemInputDto;

public record DadosRestauranteDto(UUID id, String nome, EnderecoCoreDto dadosEndereco, UUID idDono, 
        						  Integer idTipoCulinaria, Boolean isAtivo,
        						  List<AtendimentoCoreDto> atendimentos, List<ItemInputDto> itens) {
}