package br.com.fiapfood.core.entities;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import br.com.fiapfood.core.exceptions.usuario.MatriculaInvalidaException;
import br.com.fiapfood.core.exceptions.usuario.SenhaUsuarioInvalidaException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Login {
	private UUID id;
	private String matricula;
	private String senha;
	
	private Login(UUID id, String matricula, String senha) {
		this.id = id;
		this.matricula = matricula;
		this.senha = senha;
	}
	
	public static Login criar(UUID id, String matricula, String senha) {
		validarMatricula(matricula);
		validarSenha(senha);
		
		return new Login(id, matricula, senha);
	}
	
	private static void validarMatricula(String matricula) {
		if(StringUtils.isBlank(matricula) ) {
			throw new MatriculaInvalidaException("A informação da matrícula não é válida.");
		}
	}
	
	private static void validarSenha(String senha) {
		if(StringUtils.isBlank(senha) ) {
			throw new SenhaUsuarioInvalidaException("A informação da senha não é válida.");
		}
	}

	public void atualizarSenha(String senha) {
		this.senha = senha;
	}
	
	public void atualizarMatricula(String matricula) {
		this.matricula = matricula;
	}
}