package br.com.fiapfood.core.exceptions.atendimento;

public class AtendimentoRestauranteNaoEncontradoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public AtendimentoRestauranteNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}