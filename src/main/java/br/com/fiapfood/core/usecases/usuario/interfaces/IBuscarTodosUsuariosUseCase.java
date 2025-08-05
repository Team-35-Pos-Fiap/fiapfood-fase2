package br.com.fiapfood.core.usecases.usuario.interfaces;

import br.com.fiapfood.core.entities.dto.usuario.UsuarioPaginacaoCoreDto;

public interface IBuscarTodosUsuariosUseCase {
	UsuarioPaginacaoCoreDto buscar(Integer pagina);
}