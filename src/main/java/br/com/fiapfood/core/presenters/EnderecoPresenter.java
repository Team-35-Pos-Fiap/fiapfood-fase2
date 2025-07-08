package br.com.fiapfood.core.presenters;

import br.com.fiapfood.core.entities.Endereco;
import br.com.fiapfood.core.entities.dto.DadosEnderecoDto;
import br.com.fiapfood.core.entities.dto.EnderecoDto;
import br.com.fiapfood.infraestructure.entities.EnderecoEntity;

public class EnderecoPresenter {

	public static EnderecoEntity toEnderecoEntity(EnderecoDto enderecoDto) {
		return new EnderecoEntity(enderecoDto.id(), enderecoDto.cidade(), enderecoDto.cep(), enderecoDto.bairro(), enderecoDto.endereco(), enderecoDto.estado(), enderecoDto.numero(), enderecoDto.complemento());
	}

	public static EnderecoDto toEnderecoDto(EnderecoEntity dadosEndereco) {
		return new EnderecoDto(dadosEndereco.getId(), dadosEndereco.getCidade(), dadosEndereco.getCep(), 
							   dadosEndereco.getBairro(), dadosEndereco.getEndereco(), dadosEndereco.getEstado(), dadosEndereco.getNumero(), dadosEndereco.getComplemento());
	}

	public static Endereco toEndereco(EnderecoDto dadosEndereco) {
		return new Endereco(dadosEndereco.id(), dadosEndereco.cidade(), dadosEndereco.cep(), 
				   			dadosEndereco.bairro(), dadosEndereco.endereco(), dadosEndereco.estado(), dadosEndereco.numero(), dadosEndereco.complemento());
	}

	public static Endereco toEndereco(DadosEnderecoDto dadosEndereco) {
		return new Endereco(null, dadosEndereco.cidade(), dadosEndereco.cep(), 
				   			dadosEndereco.bairro(), dadosEndereco.endereco(), dadosEndereco.estado(), dadosEndereco.numero(), dadosEndereco.complemento());
	}

	
	public static EnderecoDto toEnderecoEntity(Endereco dadosEndereco) {
		return new EnderecoDto(dadosEndereco.getId(), dadosEndereco.getCidade(), dadosEndereco.getCep(), 
				   			   dadosEndereco.getBairro(), dadosEndereco.getEndereco(), dadosEndereco.getEstado(), dadosEndereco.getNumero(), dadosEndereco.getComplemento());
	}

	public static EnderecoDto toEnderecoDto(Endereco dadosEndereco) {
		return new EnderecoDto(dadosEndereco.getId(), dadosEndereco.getCidade(), dadosEndereco.getCep(), 
							   dadosEndereco.getBairro(), dadosEndereco.getEndereco(), dadosEndereco.getEstado(), dadosEndereco.getNumero(), dadosEndereco.getComplemento());

	}
}