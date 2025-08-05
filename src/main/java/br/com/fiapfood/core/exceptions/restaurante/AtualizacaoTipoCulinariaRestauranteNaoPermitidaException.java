package br.com.fiapfood.core.exceptions.restaurante;

public class AtualizacaoTipoCulinariaRestauranteNaoPermitidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public AtualizacaoTipoCulinariaRestauranteNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}