package br.com.fiapfood.core.exceptions.item;

public class NomeImagemItemInvalidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public NomeImagemItemInvalidoException(String mensagem) {
        super(mensagem);
    }
}