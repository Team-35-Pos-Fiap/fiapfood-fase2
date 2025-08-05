package br.com.fiapfood.core.exceptions.perfil;

public class NomePerfilDuplicadoException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public NomePerfilDuplicadoException(String mensagem) {
		super(mensagem);
	}
}
