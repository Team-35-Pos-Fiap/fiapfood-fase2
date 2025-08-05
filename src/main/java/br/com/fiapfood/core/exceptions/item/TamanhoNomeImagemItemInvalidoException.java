package br.com.fiapfood.core.exceptions.item;

public class TamanhoNomeImagemItemInvalidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public TamanhoNomeImagemItemInvalidoException(String mensagem) {
        super(mensagem);
    }
}