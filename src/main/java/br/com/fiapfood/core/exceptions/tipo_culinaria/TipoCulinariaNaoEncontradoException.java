package br.com.fiapfood.core.exceptions.tipo_culinaria;

public class TipoCulinariaNaoEncontradoException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public TipoCulinariaNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
}
