package br.com.fiapfood.core.exceptions.usuario;

public class AtualizacaoEmailUsuarioNaoPermitidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public AtualizacaoEmailUsuarioNaoPermitidoException(String mensagem) {
        super(mensagem);
    }
}