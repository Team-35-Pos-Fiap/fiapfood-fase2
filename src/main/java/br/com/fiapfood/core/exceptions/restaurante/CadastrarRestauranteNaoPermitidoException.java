package br.com.fiapfood.core.exceptions.restaurante;

public class CadastrarRestauranteNaoPermitidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public CadastrarRestauranteNaoPermitidoException(String mensagem) {
        super(mensagem);
    }
}