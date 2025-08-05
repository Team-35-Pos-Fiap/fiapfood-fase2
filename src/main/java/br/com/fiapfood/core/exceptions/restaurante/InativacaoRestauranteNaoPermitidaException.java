package br.com.fiapfood.core.exceptions.restaurante;

public class InativacaoRestauranteNaoPermitidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public InativacaoRestauranteNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}