package br.com.fiapfood.core.exceptions.perfil;

public class NomePerfilInvalidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public NomePerfilInvalidoException(String mensagem) {
        super(mensagem);
    }
}