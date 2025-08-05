package br.com.fiapfood.core.exceptions.usuario;

public class NomeUsuarioInvalidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public NomeUsuarioInvalidoException(String mensagem) {
        super(mensagem);
    }
}