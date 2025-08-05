package br.com.fiapfood.core.entities.dto.restaurante;

import java.util.List;
import java.util.UUID;

import br.com.fiapfood.core.entities.dto.atendimento.AtendimentoCoreDto;
import br.com.fiapfood.core.entities.dto.endereco.DadosEnderecoCoreDto;

public record CadastrarRestauranteCoreDto(String nome, DadosEnderecoCoreDto dadosEndereco, 
									  UUID idDonoRestaurante, Integer idTipoCulinaria, List<AtendimentoCoreDto> atendimentos) {

}