package br.com.fiapfood.core.exceptions.restaurante;

public class AtualizacaoNomeRestauranteNaoPermitidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public AtualizacaoNomeRestauranteNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}