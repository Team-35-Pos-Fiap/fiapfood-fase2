package br.com.fiapfood.core.exceptions.item;

public class AtualizacaoDisponibilidadeConsumoPresencialItemNaoPermitidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public AtualizacaoDisponibilidadeConsumoPresencialItemNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}