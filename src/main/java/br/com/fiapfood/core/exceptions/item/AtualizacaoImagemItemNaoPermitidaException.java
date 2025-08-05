package br.com.fiapfood.core.exceptions.item;

public class AtualizacaoImagemItemNaoPermitidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public AtualizacaoImagemItemNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}