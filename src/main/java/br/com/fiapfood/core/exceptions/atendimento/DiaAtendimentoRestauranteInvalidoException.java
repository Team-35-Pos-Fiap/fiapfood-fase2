package br.com.fiapfood.core.exceptions.atendimento;

public class DiaAtendimentoRestauranteInvalidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public DiaAtendimentoRestauranteInvalidoException(String mensagem) {
        super(mensagem);
    }
}