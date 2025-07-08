package br.com.fiapfood.core.exceptions;

public class LoginInvalidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public LoginInvalidoException(String mensagem) {
        super(mensagem);
    }
}