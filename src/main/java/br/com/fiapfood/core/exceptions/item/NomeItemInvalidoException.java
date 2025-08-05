package br.com.fiapfood.core.exceptions.item;

public class NomeItemInvalidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public NomeItemInvalidoException(String mensagem) {
        super(mensagem);
    }
}