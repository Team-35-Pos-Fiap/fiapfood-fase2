package br.com.fiapfood.core.exceptions.item;

public class AtualizacaoNomeItemNaoPermitidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public AtualizacaoNomeItemNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}