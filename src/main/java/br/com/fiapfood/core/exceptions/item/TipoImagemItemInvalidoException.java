package br.com.fiapfood.core.exceptions.item;

public class TipoImagemItemInvalidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public TipoImagemItemInvalidoException(String mensagem) {
        super(mensagem);
    }
}