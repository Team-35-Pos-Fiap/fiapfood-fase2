package br.com.fiapfood.core.exceptions;

public class LoginNaoEncontradoException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public LoginNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
}
