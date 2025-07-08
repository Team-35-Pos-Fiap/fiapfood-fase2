package br.com.fiapfood.core.exceptions;

public class UsuarioSemAcessoException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public UsuarioSemAcessoException(String mensagem) {
		super(mensagem);
	}
}
