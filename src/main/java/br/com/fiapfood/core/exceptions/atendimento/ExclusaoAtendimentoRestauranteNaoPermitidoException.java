package br.com.fiapfood.core.exceptions.atendimento;

public class ExclusaoAtendimentoRestauranteNaoPermitidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public ExclusaoAtendimentoRestauranteNaoPermitidoException(String mensagem) {
        super(mensagem);
    }
}