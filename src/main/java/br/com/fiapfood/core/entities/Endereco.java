package br.com.fiapfood.core.entities;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Endereco {
	private UUID id;
	private String cidade;
	private String cep;
	private String bairro;
	private String endereco;
	private String estado;
	private Integer numero;
	private String complemento;

	public void atualizarDados(String endereco, String cidade, String bairro, String estado, Integer numero, String cep, String complemento) {
		this.endereco = endereco;
		this.cidade = cidade;
		this.bairro = bairro;
		this.estado = estado;
		this.numero = numero;
		this.cep = cep;
		this.complemento = complemento;
	}
}