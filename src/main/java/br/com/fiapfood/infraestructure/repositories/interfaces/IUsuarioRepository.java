package br.com.fiapfood.infraestructure.repositories.interfaces;

import java.util.Map;
import java.util.UUID;

import br.com.fiapfood.core.entities.dto.UsuarioDto;
import br.com.fiapfood.infraestructure.entities.UsuarioEntity;

public interface IUsuarioRepository {
    UsuarioDto buscarPorIdLogin(UUID loginId);
	UsuarioDto buscarPorId(UUID id);
	Map<Class<?>, Object> buscarUsuariosComPaginacao(final Integer pagina);
	void salvar(UsuarioEntity usuario);
	boolean emailJaCadastrado(String email);
}