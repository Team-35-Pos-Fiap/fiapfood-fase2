package br.com.fiapfood.core.usecases.usuario.interfaces;

import br.com.fiapfood.core.entities.dto.DadosUsuariosComPaginacaoDto;

public interface IBuscarTodosUsuariosUseCase {
	DadosUsuariosComPaginacaoDto buscar(Integer pagina);
}