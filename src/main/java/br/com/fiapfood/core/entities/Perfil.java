package br.com.fiapfood.core.entities;

import org.apache.commons.lang3.StringUtils;

import br.com.fiapfood.core.exceptions.NomePerfilInvalidoException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Perfil {
	private Integer id;
	private String nome;
	
	private Perfil(Integer id, String nome) {
		this.id = id;
		this.nome = nome;
	}
	
	public static Perfil criar(Integer id, String nome) {
		validarNome(nome);
		
		return new Perfil(id, nome);
	}

	private static void validarNome(String nome) {
		if(StringUtils.isBlank(nome)) {
			throw new NomePerfilInvalidoException("O nome do perfil é inválido.");
		}
	}
}