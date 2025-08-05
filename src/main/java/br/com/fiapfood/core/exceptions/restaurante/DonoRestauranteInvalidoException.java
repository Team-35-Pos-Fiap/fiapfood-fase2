package br.com.fiapfood.core.exceptions.restaurante;

public class DonoRestauranteInvalidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public DonoRestauranteInvalidoException(String mensagem) {
        super(mensagem);
    }
}