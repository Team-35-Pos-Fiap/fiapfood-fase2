package br.com.fiapfood.core.exceptions.restaurante;

public class ReativacaoRestauranteNaoPermitidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public ReativacaoRestauranteNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}