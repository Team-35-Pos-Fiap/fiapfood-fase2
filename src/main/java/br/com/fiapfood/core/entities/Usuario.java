package br.com.fiapfood.core.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import br.com.fiapfood.core.exceptions.perfil.PerfilInvalidoException;
import br.com.fiapfood.core.exceptions.usuario.EmailUsuarioInvalidoException;
import br.com.fiapfood.core.exceptions.usuario.NomeUsuarioInvalidoException;
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
	private Endereco dadosEndereco;
	private Integer idPerfil;
	private Login login;

	private Usuario(UUID id, String nome, Integer idPerfil, Boolean isAtivo, 
					String email, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, Login login, Endereco dadosEndereco) {
		this.id = id;
		this.nome = nome;
		this.idPerfil = idPerfil;
		this.dataCriacao = dataCriacao;
		this.isAtivo = isAtivo;
		this.dataAtualizacao = dataAtualizacao;
		this.email = email;
		this.login = login;
		this.dadosEndereco = dadosEndereco;
	}
	
	public static Usuario criar(UUID id, String nome, Integer idPerfil, Login login, Boolean isAtivo, 
								String email, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, Endereco dadosEndereco) {
		validarNome(nome);
		validarPerfil(idPerfil);
		validarEmail(email);
		
		return new Usuario(id, nome, idPerfil, isAtivo, email, dataCriacao, dataAtualizacao, login, dadosEndereco);
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
	
	private LocalDateTime getDataAtual() {
		return LocalDateTime.now();
	}

	public void atualizarEndereco(Endereco dadosEndereco) {
		this.dadosEndereco = dadosEndereco;
		this.dataAtualizacao = getDataAtual();
	}
	
	public void atualizarLogin(Login login) {
		this.login = login;
		this.dataAtualizacao = getDataAtual();
	}
}