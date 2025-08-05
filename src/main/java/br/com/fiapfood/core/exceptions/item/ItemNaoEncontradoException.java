package br.com.fiapfood.core.exceptions.item;

public class ItemNaoEncontradoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public ItemNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}