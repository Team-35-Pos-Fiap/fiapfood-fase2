package br.com.fiapfood.core.entities;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import br.com.fiapfood.core.exceptions.item.NomeItemInvalidoException;
import br.com.fiapfood.core.exceptions.item.ValorItemInvalidoException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Item {    
	private UUID id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Boolean isDisponivelConsumoPresencial;
    private Boolean isDisponivel;
    private Imagem imagem;
    private UUID idRestaurante;

    private Item(UUID id, String nome, String descricao, BigDecimal preco, Boolean isDisponivelConsumoPresencial, Boolean disponivel, Imagem imagem) {
    	this.id = id;
    	this.nome = nome;
    	this.descricao = descricao;
    	this.preco = preco;
    	this.isDisponivelConsumoPresencial = isDisponivelConsumoPresencial;
    	this.isDisponivel = disponivel;
    	this.imagem = imagem;
    }

    public static Item criar(UUID id, String nome, String descricao, BigDecimal preco, 
    						 Boolean disponivelApenasRestaurante, Boolean disponivel, Imagem imagem) {
    	validarNome(nome);
    	validarPreco(preco);
    	
    	return new Item(id, nome, descricao, preco, disponivelApenasRestaurante, disponivel, imagem);
    }

    private static void validarNome(String nome) {
		if(StringUtils.isBlank(nome)) {
			throw new NomeItemInvalidoException("O nome do item informado é inválido.");
		}
	}
	
	private static void validarPreco(BigDecimal preco) {
		if(preco == null) {
			throw new ValorItemInvalidoException("O valor do item informado é inválido.");
		}
		
		if(preco.doubleValue() < 0) {
			throw new ValorItemInvalidoException("O valor do item não pode ser menor do que 0.");
		}
	}
	
	public void atualizarNome(String nome) {
    	validarNome(nome);

    	this.nome = nome;
	}
	
	public void atualizarPreco(BigDecimal preco) {
    	validarPreco(preco);

    	this.preco = preco;
	}
		
	public void atualizarDescricao(String descricao) {
    	this.descricao = descricao;
	}
	
	public void atualizarDisponibilidadeConsumoPresencial(Boolean isDisponivelConsumoPresencial) {
    	this.isDisponivelConsumoPresencial = isDisponivelConsumoPresencial;
	}
	
	public void atualizarDisponibilidade(Boolean isDisponivel) {
    	this.isDisponivel = isDisponivel;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		Item other = (Item) obj;
		
		return Objects.equals(id, other.id);
	}

	public void atualizarImagem(Imagem imagem) {
		this.imagem = imagem;
	}
}