package br.com.fiapfood.core.usecases.usuario.interfaces;

import br.com.fiapfood.core.entities.dto.CadastrarUsuarioDto;

public interface ICadastrarUsuarioUseCase {
	public void cadastrar(CadastrarUsuarioDto usuario);
}