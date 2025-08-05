package br.com.fiapfood.core.exceptions.usuario;

public class AtualizacaoPerfilNaoPermitidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public AtualizacaoPerfilNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}