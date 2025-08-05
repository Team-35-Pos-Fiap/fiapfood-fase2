package br.com.fiapfood.core.exceptions.perfil;

public class ExclusaoPerfilNaoPermitidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public ExclusaoPerfilNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}