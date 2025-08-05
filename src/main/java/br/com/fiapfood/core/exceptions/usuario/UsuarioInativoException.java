package br.com.fiapfood.core.exceptions.usuario;

public class UsuarioInativoException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public UsuarioInativoException(String mensagem) {
		super(mensagem);
	}
}
