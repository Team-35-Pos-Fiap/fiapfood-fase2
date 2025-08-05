package br.com.fiapfood.core.exceptions.usuario;

public class AtualizacaoNomeUsuarioNaoPermitidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public AtualizacaoNomeUsuarioNaoPermitidoException(String mensagem) {
        super(mensagem);
    }
}