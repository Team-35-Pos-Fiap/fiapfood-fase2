package br.com.fiapfood.core.entities.dto.restaurante;

import java.util.List;
import java.util.UUID;

import br.com.fiapfood.core.entities.dto.atendimento.AtendimentoCoreDto;
import br.com.fiapfood.core.entities.dto.endereco.EnderecoCoreDto;
import br.com.fiapfood.core.entities.dto.tipo_culinaria.TipoCulinariaCoreDto;
import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioResumidoCoreDto;

public record DadosRestauranteCoreDto(UUID id, String nome, EnderecoCoreDto endereco,
										Boolean isAtivo, DadosUsuarioResumidoCoreDto dono, TipoCulinariaCoreDto tipoCulinaria, List<AtendimentoCoreDto> atendimentos) {}