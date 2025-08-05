package br.com.fiapfood.core.exceptions.item;

public class ValorItemInvalidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public ValorItemInvalidoException(String mensagem) {
        super(mensagem);
    }
}