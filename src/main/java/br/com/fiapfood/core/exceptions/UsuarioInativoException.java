package br.com.fiapfood.core.exceptions;

public class UsuarioInativoException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public UsuarioInativoException(String mensagem) {
		super(mensagem);
	}
}
