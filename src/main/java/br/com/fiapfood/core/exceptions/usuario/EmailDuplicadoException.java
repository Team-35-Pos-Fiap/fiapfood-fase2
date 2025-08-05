package br.com.fiapfood.core.exceptions.usuario;

public class EmailDuplicadoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public EmailDuplicadoException(String mensagem) {
        super(mensagem);
    }
}