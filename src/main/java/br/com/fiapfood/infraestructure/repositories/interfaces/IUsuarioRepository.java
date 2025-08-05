package br.com.fiapfood.infraestructure.repositories.interfaces;

import java.util.List;
import java.util.UUID;

import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioInputDto;
import br.com.fiapfood.core.entities.dto.usuario.UsuarioPaginacaoInputDto;
import br.com.fiapfood.infraestructure.entities.UsuarioEntity;

public interface IUsuarioRepository {
	DadosUsuarioInputDto buscarPorId(UUID id);
	void salvar(UsuarioEntity usuario);
	boolean emailJaCadastrado(String email);
	List<DadosUsuarioInputDto> buscarPorIdPerfil(Integer idPerfil);
	UsuarioPaginacaoInputDto buscarTodos(Integer pagina);
	DadosUsuarioInputDto buscarPorMatriculaSenha(String matricula, String senha);
	boolean matriculaJaCadastrada(String matricula);
}