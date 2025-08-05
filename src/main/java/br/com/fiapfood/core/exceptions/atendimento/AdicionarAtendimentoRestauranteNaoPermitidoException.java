package br.com.fiapfood.core.exceptions.atendimento;

public class AdicionarAtendimentoRestauranteNaoPermitidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public AdicionarAtendimentoRestauranteNaoPermitidoException(String mensagem) {
        super(mensagem);
    }
}