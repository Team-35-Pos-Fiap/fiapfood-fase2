package br.com.fiapfood.infraestructure.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class UsuarioEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	private String nome;
	@Column(unique = true)
	private String email;
	
	@Column(name = "data_criacao", nullable = false)
	private LocalDateTime dataCriacao;
	
	@Column(name = "data_atualizacao", nullable = true)
	private LocalDateTime dataAtualizacao;
	
	@Column(name = "ativo", columnDefinition = "tinyint")
	private Boolean isAtivo;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_endereco")
	private EnderecoEntity dadosEndereco;
	
	@ManyToOne
	@JoinColumn(name = "id_perfil")
	private PerfilEntity perfil;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_login")
	private LoginEntity dadosLogin;
	
	public void inativar() {
		this.isAtivo = false;
		this.dataAtualizacao = getDataAtual();
	}
	
	public void reativar() {
		this.isAtivo = true;
		this.dataAtualizacao = getDataAtual();
	}

	public void atualizarPerfil(PerfilEntity perfil) {
		this.perfil = perfil;
		this.dataAtualizacao = getDataAtual();
	}
	
	public void atualizarNome(String nome) {
		this.nome = nome;
		this.dataAtualizacao = getDataAtual();
	}
	
	public void atualizarEmail(String email) {
		this.email = email;
		this.dataAtualizacao = getDataAtual();
	}

	public void atualizarEndereco(String endereco, String cidade, String bairro,
								  String estado, Integer numero, String cep, String complemento) {
		this.dadosEndereco.atualizarDados(endereco, cidade, bairro, estado, numero, cep, complemento);
		this.dataAtualizacao = getDataAtual();
	}

	public void atualizarLogin(String matricula, String senha) {
		this.dadosLogin.atualizarMatricula(matricula);
		this.dadosLogin.atualizarSenha(senha);
		this.dataAtualizacao = getDataAtual();
	}

	private LocalDateTime getDataAtual() {
		return LocalDateTime.now();
	}
}