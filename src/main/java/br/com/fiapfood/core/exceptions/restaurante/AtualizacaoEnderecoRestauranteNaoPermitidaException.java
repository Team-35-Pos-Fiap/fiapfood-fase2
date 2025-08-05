package br.com.fiapfood.core.exceptions.restaurante;

public class AtualizacaoEnderecoRestauranteNaoPermitidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public AtualizacaoEnderecoRestauranteNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}