package br.com.fiapfood.core.exceptions.item;

public class AtualizacaoDisponibilidadeItemNaoPermitidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public AtualizacaoDisponibilidadeItemNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}