package br.com.fiapfood.core.exceptions.usuario;

public class AtualizacaoStatusUsuarioNaoPermitidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public AtualizacaoStatusUsuarioNaoPermitidaException(String mensagem) {
        super(mensagem);
    }
}