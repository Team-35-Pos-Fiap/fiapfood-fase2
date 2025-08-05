package br.com.fiapfood.core.exceptions.item;

public class CadastrarItemNaoPermitidoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public CadastrarItemNaoPermitidoException(String mensagem) {
        super(mensagem);
    }
}