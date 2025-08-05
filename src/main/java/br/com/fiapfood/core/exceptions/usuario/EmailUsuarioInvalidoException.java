package br.com.fiapfood.core.exceptions.usuario;

public class EmailUsuarioInvalidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public EmailUsuarioInvalidoException(String mensagem) {
        super(mensagem);
    }
}