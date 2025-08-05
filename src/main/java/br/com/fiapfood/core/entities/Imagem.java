package br.com.fiapfood.core.entities;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import br.com.fiapfood.core.exceptions.item.NomeImagemItemInvalidoException;
import br.com.fiapfood.core.exceptions.item.TamanhoNomeImagemItemInvalidoException;
import br.com.fiapfood.core.exceptions.item.TipoImagemItemInvalidoException;
import br.com.fiapfood.core.utils.ImagemUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@Getter
public class Imagem {
	private UUID id;	
	private String nome;
	private byte[] conteudo;
	private String tipo;
	
	private Imagem(UUID id, String nome, byte[] conteudo, String tipo) {
		this.id = id;
		this.nome = nome;
		this.conteudo = conteudo;
		this.tipo = tipo;
	}
	
	public static Imagem criar(UUID id, String nome, byte[] conteudo, String tipo) {
		validarNome(nome);
		validarTamanhoNome(nome);
		validarTipo(tipo);
		
		return new Imagem(id, nome, conteudo, tipo);
	}
	
	public void atualizarNome(String nome) {
		validarNome(nome);
		validarTamanhoNome(nome);
		
		this.nome = nome;
	}
	
	public void atualizarTipo(String tipo) {
		validarTipo(tipo);
		
		this.tipo = tipo;
	}
	
	public void atualizarConteudo(byte[] conteudo) {
		this.conteudo = conteudo;
	}
	
	private static void validarTipo(String tipo) {
		if(!ImagemUtils.TIPOS_PERMITIDOS.contains(tipo)) {
			throw new TipoImagemItemInvalidoException("O tipo do documento não é suportado. Os tipos suportados são jpg, jpeg e png.");
		}
	}
	
	private static void validarNome(String nome) {
		if(StringUtils.isBlank(nome)) {
			throw new NomeImagemItemInvalidoException("O nome da imagem informado é inválido.");
		}
	}
	
	private static void validarTamanhoNome(String nome) {
		if(nome.length() > 50) {
			throw new TamanhoNomeImagemItemInvalidoException("O tamanho da imagem excede a quantidade de 50 caracteres.");
		}
	}
}