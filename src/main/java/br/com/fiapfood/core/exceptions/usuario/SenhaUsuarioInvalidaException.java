package br.com.fiapfood.core.exceptions.usuario;

public class SenhaUsuarioInvalidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public SenhaUsuarioInvalidaException(String mensagem) {
        super(mensagem);
    }
}