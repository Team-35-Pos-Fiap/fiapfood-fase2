package br.com.fiapfood.core.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import br.com.fiapfood.core.exceptions.EmailUsuarioInvalidoException;
import br.com.fiapfood.core.exceptions.NomeUsuarioInvalidoException;
import br.com.fiapfood.core.exceptions.PerfilInvalidoException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Usuario {
	private UUID id;
	private String nome;
	private String email;
	private LocalDateTime dataCriacao;
	private LocalDateTime dataAtualizacao;
	private Boolean isAtivo;
	private UUID idEndereco;
	private Integer idPerfil;
	private UUID idLogin;

	private Usuario(UUID id, String nome, Integer idPerfil, Boolean isAtivo, String email, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, UUID idLogin, UUID idEndereco) {
		this.id = id;
		this.nome = nome;
		this.idPerfil = idPerfil;
		this.dataCriacao = dataCriacao;
		this.isAtivo = isAtivo;
		this.dataAtualizacao = dataAtualizacao;
		this.email = email;
		this.idLogin = idLogin;
		this.idEndereco = idEndereco;
	}
	
	public static Usuario criar(UUID id, String nome, Integer idPerfil, UUID idLogin, Boolean isAtivo, String email, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, UUID idEndereco) {
		validarNome(nome);
		validarPerfil(idPerfil);

		return new Usuario(id, nome, idPerfil, isAtivo, email, dataCriacao, dataAtualizacao, idLogin, idEndereco);
	}
	
	private static void validarNome(String nome) {
		if(StringUtils.isBlank(nome)) {
			throw new NomeUsuarioInvalidoException("O nome do usuário informado é inválido.");
		}
	}

	private static void validarPerfil(Integer idPerfil) {
		if(idPerfil == null || idPerfil < 1) {
			throw new PerfilInvalidoException("O perfil informado é inválido.");
		}
	}
	
	private static void validarEmail(String email) {
		if(StringUtils.isBlank(email)) {
			throw new EmailUsuarioInvalidoException("O email do usuário informado é inválido.");
		}
	}
	
	public void inativar() {
		this.isAtivo = false;
		this.dataAtualizacao = getDataAtual();
	}
	
	public void reativar() {
		this.isAtivo = true;
		this.dataAtualizacao = getDataAtual();
	}

	public void atualizarEmail(String email) {
		validarEmail(email);
		
		this.email = email;
		this.dataAtualizacao = getDataAtual();
	}
	
	public void atualizarNome(String nome) {
		validarNome(nome);
		
		this.nome = nome;
		this.dataAtualizacao = getDataAtual();
	}
	
	public void atualizarPerfil(Integer idPerfil) {
		this.idPerfil = idPerfil;
		this.dataAtualizacao = getDataAtual();
	}
	
	/*
	
	public void atualizarEndereco(String endereco, String cidade, String bairro,
								  String estado, Integer numero, String cep, String complemento) {
		this.dadosEndereco.atualizarDados(endereco, cidade, bairro, estado, numero, cep, complemento);
		this.dataAtualizacao = getDataAtual();
	}
	
	

	public void atualizarLogin(String matricula, String senha) {
		this.dadosLogin.atualizarMatricula(matricula);
		this.dadosLogin.atualizarSenha(senha);
		this.dataAtualizacao = getDataAtual();
	}*/

	private LocalDateTime getDataAtual() {
		return LocalDateTime.now();
	}
}