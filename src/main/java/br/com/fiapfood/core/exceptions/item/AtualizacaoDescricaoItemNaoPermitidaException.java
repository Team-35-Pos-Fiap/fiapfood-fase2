package br.com.fiapfood.core.exceptions.item;

public class AtualizacaoDescricaoItemNaoPermitidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public AtualizacaoDescricaoItemNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}