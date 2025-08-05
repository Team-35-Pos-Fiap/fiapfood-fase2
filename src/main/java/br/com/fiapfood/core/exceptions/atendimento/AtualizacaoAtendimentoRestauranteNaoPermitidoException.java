package br.com.fiapfood.core.exceptions.atendimento;

public class AtualizacaoAtendimentoRestauranteNaoPermitidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public AtualizacaoAtendimentoRestauranteNaoPermitidoException(String mensagem) {
        super(mensagem);
    }
}