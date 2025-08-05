package br.com.fiapfood.core.exceptions.tipo_culinaria;

public class TipoCulinariaInvalidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public TipoCulinariaInvalidoException(String mensagem) {
        super(mensagem);
    }
}