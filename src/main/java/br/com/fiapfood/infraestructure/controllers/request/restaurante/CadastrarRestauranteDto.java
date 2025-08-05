package br.com.fiapfood.infraestructure.controllers.request.restaurante;

import java.util.List;
import java.util.UUID;

import br.com.fiapfood.infraestructure.controllers.request.atendimento.AtendimentoDto;
import br.com.fiapfood.infraestructure.controllers.request.endereco.DadosEnderecoDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastrarRestauranteDto(@NotBlank(message = "O campo nome precisa estar preenchido.")
									  String nome, 
									  @NotNull(message = "O campo dadosEndereco precisa estar preenchido.")
									  DadosEnderecoDto dadosEndereco, 
									  
									  @NotNull(message = "O campo idDonoRestaurante precisa estar preenchido.")
									  UUID idDonoRestaurante, 
									  
									  @NotNull(message = "O campo idTipoCulinaria precisa estar preenchido.")
									  Integer idTipoCulinaria, 
									  
									  @NotNull(message = "O campo atendimentos precisa estar preenchido.")
									  List<AtendimentoDto> atendimentos) {

}