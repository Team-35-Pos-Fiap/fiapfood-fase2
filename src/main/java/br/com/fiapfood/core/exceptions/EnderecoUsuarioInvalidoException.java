package br.com.fiapfood.core.exceptions;

public class EnderecoUsuarioInvalidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public EnderecoUsuarioInvalidoException(String mensagem) {
        super(mensagem);
    }
}