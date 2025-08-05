package br.com.fiapfood.core.entities;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;

import br.com.fiapfood.core.exceptions.perfil.NomePerfilInvalidoException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Perfil {
	private Integer id;
	private String nome;
	private LocalDate dataCriacao;
	private LocalDate dataInativacao;
	
	private Perfil(Integer id, String nome, LocalDate dataCriacao, LocalDate dataInativacao) {
		this.id = id;
		this.nome = nome;
		this.dataCriacao = dataCriacao;
		this.dataInativacao = dataInativacao;
	}
	
	public static Perfil criar(Integer id, String nome, LocalDate dataCriacao, LocalDate dataInativacao) {
		validarNome(nome);
		
		return new Perfil(id, nome, dataCriacao, dataInativacao);
	}

	private static void validarNome(String nome) {
		if(StringUtils.isBlank(nome)) {
			throw new NomePerfilInvalidoException("O nome do perfil é inválido.");
		}
	}

	public void atualizarNome(String nome) {
		validarNome(nome);
		
		this.nome = nome;
	}

	public void inativar() {
		this.dataInativacao = LocalDate.now();
	}

	public void reativar() {
		this.dataInativacao = null;		
	}
}