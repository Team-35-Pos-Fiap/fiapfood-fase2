package br.com.fiapfood.core.exceptions;

public class EmailDuplicadoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public EmailDuplicadoException(String mensagem) {
        super(mensagem);
    }
}