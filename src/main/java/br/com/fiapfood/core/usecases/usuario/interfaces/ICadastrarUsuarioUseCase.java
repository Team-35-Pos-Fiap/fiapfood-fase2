package br.com.fiapfood.core.usecases.usuario.interfaces;

import br.com.fiapfood.core.entities.dto.usuario.CadastrarUsuarioCoreDto;

public interface ICadastrarUsuarioUseCase {
	void cadastrar(CadastrarUsuarioCoreDto usuario);
}