package br.com.fiapfood.core.entities;

import org.apache.commons.lang3.StringUtils;

import br.com.fiapfood.core.exceptions.tipo_culinaria.NomeTipoCulinariaInvalidoException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TipoCulinaria {
	private Integer id;
	private String nome;
	
	private TipoCulinaria(Integer id, String nome) {
		this.id = id;
		this.nome = nome;
	}
	
	public static TipoCulinaria criar(Integer id, String nome) {
		validarNome(nome);
		
		return new TipoCulinaria(id, nome);
	}

	private static void validarNome(String nome) {
		if(StringUtils.isBlank(nome)) {
			throw new NomeTipoCulinariaInvalidoException("O nome do tipo de culinária é inválido.");
		}
	}

	public void atualizarNome(String nome) {
		validarNome(nome);
		
		this.nome = nome;
	}
}