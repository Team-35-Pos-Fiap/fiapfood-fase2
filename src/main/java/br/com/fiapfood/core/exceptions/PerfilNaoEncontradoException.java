package br.com.fiapfood.core.exceptions;

public class PerfilNaoEncontradoException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public PerfilNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
}
