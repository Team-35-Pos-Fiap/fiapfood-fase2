package br.com.fiapfood.core.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import br.com.fiapfood.core.exceptions.restaurante.DonoRestauranteInvalidoException;
import br.com.fiapfood.core.exceptions.restaurante.NomeRestauranteInvalidoException;
import br.com.fiapfood.core.exceptions.tipo_culinaria.TipoCulinariaInvalidoException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Restaurante {
    private UUID id;
    private String nome;
    private Endereco dadosEndereco;
    private UUID idDonoRestaurante;
    private Integer idTipoCulinaria;
    private Boolean isAtivo;
    private List<Atendimento> atendimentos;
    private List<Item> itens;
    
    private Restaurante(UUID id, String nome, Endereco dadosEndereco, UUID idDonoRestaurante, 
    					Integer idTipoCulinaria, Boolean isAtivo, List<Atendimento> atendimentos, List<Item> itens) {
    	this.id = id;
    	this.nome = nome;
    	this.dadosEndereco = dadosEndereco;
    	this.idDonoRestaurante = idDonoRestaurante;
    	this.idTipoCulinaria = idTipoCulinaria;
    	this.isAtivo = isAtivo;
    	this.atendimentos = atendimentos;
    	this.itens = itens;
    }
    
    public static Restaurante criar(UUID id, String nome, Endereco dadosEndereco, UUID idDonoRestaurante, 
    								Integer idTipoCulinaria, Boolean isAtivo, List<Atendimento> atendimentos, List<Item> itens) {
    	validarNome(nome);
    	validarUsuarioDono(idDonoRestaurante);
    	validarTipoCulinaria(idTipoCulinaria);
    	
    	return new Restaurante(id, nome, dadosEndereco, idDonoRestaurante, idTipoCulinaria, isAtivo, atendimentos, itens);
    }
    
    private static void validarTipoCulinaria(Integer idTipoCulinaria) {
    	if(idTipoCulinaria == null) {
    		throw new TipoCulinariaInvalidoException("A identificação do tipo de culinária é inválida.");
    	}
	}

	private static void validarUsuarioDono(UUID idDonoRestaurante) {
    	if(idDonoRestaurante == null) {
    		throw new DonoRestauranteInvalidoException("A identificação do dono do restaurante é inválida.");
    	}
	}

	private static void validarNome(String nome) {
    	if(StringUtils.isBlank(nome)) {
    		throw new NomeRestauranteInvalidoException("O nome do restaurante informado é inválido.");
    	}
    }   
	
	public void inativar() {
		this.isAtivo = false;
	}

	public void reativar() {
		this.isAtivo = true;
	}

	public void atualizarDono(UUID idDono) {
		this.idDonoRestaurante = idDono;
	}

	public void atualizarTipoCulinaria(Integer idTipoCulinaria) {
		this.idTipoCulinaria = idTipoCulinaria;
	}

	public void atualizarNome(String nome) {
		this.nome = nome;
	}

	public void limparAtendimentos() {
		this.atendimentos = new ArrayList<>();
	}
	
	public void limparItens() {
		this.itens = new ArrayList<>();
	}

	public void atualizarEndereco(Endereco dadosEndereco) {
		this.dadosEndereco = dadosEndereco;
	}
}