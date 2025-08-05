package br.com.fiapfood.core.presenters;

import java.util.UUID;

import br.com.fiapfood.core.entities.Endereco;
import br.com.fiapfood.core.entities.dto.endereco.DadosEnderecoCoreDto;
import br.com.fiapfood.core.entities.dto.endereco.EnderecoCoreDto;
import br.com.fiapfood.infraestructure.controllers.request.endereco.DadosEnderecoDto;
import br.com.fiapfood.infraestructure.controllers.request.endereco.EnderecoDto;
import br.com.fiapfood.infraestructure.entities.EnderecoEntity;

public class EnderecoPresenter {

	public static EnderecoEntity toEnderecoEntity(EnderecoCoreDto enderecoDto) {
		return new EnderecoEntity(enderecoDto.id(), enderecoDto.cidade(), enderecoDto.cep(), enderecoDto.bairro(), enderecoDto.endereco(), enderecoDto.estado(), enderecoDto.numero(), enderecoDto.complemento());
	}

	public static EnderecoCoreDto toEnderecoDto(EnderecoEntity dadosEndereco) {
		return new EnderecoCoreDto(dadosEndereco.getId(), dadosEndereco.getCidade(), dadosEndereco.getCep(), 
							       dadosEndereco.getBairro(), dadosEndereco.getEndereco(), dadosEndereco.getEstado(), dadosEndereco.getNumero(), dadosEndereco.getComplemento());
	}

	public static Endereco toEndereco(EnderecoCoreDto dadosEndereco) {
		return new Endereco(dadosEndereco.id(), dadosEndereco.cidade(), dadosEndereco.cep(), 
				   			dadosEndereco.bairro(), dadosEndereco.endereco(), dadosEndereco.estado(), dadosEndereco.numero(), dadosEndereco.complemento());
	}

	public static Endereco toEndereco(DadosEnderecoCoreDto dadosEndereco) {
		return new Endereco(null, dadosEndereco.cidade(), dadosEndereco.cep(), 
				   			dadosEndereco.bairro(), dadosEndereco.endereco(), dadosEndereco.estado(), dadosEndereco.numero(), dadosEndereco.complemento());
	}

	public static EnderecoCoreDto toEnderecoDto(Endereco dadosEndereco) {
		return new EnderecoCoreDto(dadosEndereco.getId(), dadosEndereco.getCidade(), dadosEndereco.getCep(), 
							   	   dadosEndereco.getBairro(), dadosEndereco.getEndereco(), dadosEndereco.getEstado(), dadosEndereco.getNumero(), dadosEndereco.getComplemento());

	}

	public static EnderecoEntity toEnderecoResumidoEntity(UUID idEndereco) {
		return new EnderecoEntity(idEndereco, null, null, null, null, null, null, null);
	}

	public static EnderecoDto toEnderecoDto(EnderecoCoreDto dadosEndereco) {
		return new EnderecoDto(dadosEndereco.id(), dadosEndereco.cidade(), dadosEndereco.cep(), dadosEndereco.bairro(), 
							   dadosEndereco.endereco(), dadosEndereco.estado(), dadosEndereco.numero(), dadosEndereco.complemento());
	}

	public static DadosEnderecoCoreDto toEnderecoCoreDto(DadosEnderecoDto dadosEndereco) {
		return new DadosEnderecoCoreDto(dadosEndereco.cidade(), dadosEndereco.cep(), dadosEndereco.bairro(), 
				   						dadosEndereco.endereco(), dadosEndereco.estado(), dadosEndereco.numero(), dadosEndereco.complemento());
	}
}