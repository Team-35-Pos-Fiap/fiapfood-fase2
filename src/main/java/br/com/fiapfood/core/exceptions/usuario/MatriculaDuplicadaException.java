package br.com.fiapfood.core.exceptions.usuario;

public class MatriculaDuplicadaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public MatriculaDuplicadaException(String mensagem) {
        super(mensagem);
    }
}