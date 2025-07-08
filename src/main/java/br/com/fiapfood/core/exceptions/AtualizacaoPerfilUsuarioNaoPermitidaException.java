package br.com.fiapfood.core.exceptions;

public class AtualizacaoPerfilUsuarioNaoPermitidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public AtualizacaoPerfilUsuarioNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}