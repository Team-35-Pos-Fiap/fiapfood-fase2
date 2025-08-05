package br.com.fiapfood.core.exceptions.restaurante;

public class AtualizacaoStatusRestauranteNaoPermitidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public AtualizacaoStatusRestauranteNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}