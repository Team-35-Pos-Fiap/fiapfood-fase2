package br.com.fiapfood.infraestructure.controllers.request.restaurante;

import java.util.List;
import java.util.UUID;

import br.com.fiapfood.infraestructure.controllers.request.atendimento.AtendimentoDto;
import br.com.fiapfood.infraestructure.controllers.request.endereco.EnderecoDto;
import br.com.fiapfood.infraestructure.controllers.request.tipo_culinaria.TipoCulinariaDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.DadosUsuarioDto;

public record RestauranteDto(UUID id, String nome, EnderecoDto endereco, 
							 Boolean isAtivo, DadosUsuarioDto dono, TipoCulinariaDto tipoCulinaria, 
							 List<AtendimentoDto> atendimentos) {}