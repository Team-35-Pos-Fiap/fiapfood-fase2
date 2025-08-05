package br.com.fiapfood.core.exceptions.item;

public class ImagemItemInvalidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public ImagemItemInvalidaException(String mensagem) {
        super(mensagem);
    }
}