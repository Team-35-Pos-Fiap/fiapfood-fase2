package br.com.fiapfood.core.exceptions.item;

public class AtualizacaoPrecoItemNaoPermitidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public AtualizacaoPrecoItemNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}