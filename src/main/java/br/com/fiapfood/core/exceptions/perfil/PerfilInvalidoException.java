package br.com.fiapfood.core.exceptions.perfil;

public class PerfilInvalidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public PerfilInvalidoException(String mensagem) {
        super(mensagem);
    }
}