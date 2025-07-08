package br.com.fiapfood.core.exceptions;

public class UsuarioNaoEncontradoException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public UsuarioNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
}
