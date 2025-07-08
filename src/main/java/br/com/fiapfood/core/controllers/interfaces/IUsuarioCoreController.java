package br.com.fiapfood.core.controllers.interfaces;

import java.util.UUID;

import br.com.fiapfood.core.entities.dto.CadastrarUsuarioDto;
import br.com.fiapfood.core.entities.dto.DadosEnderecoDto;
import br.com.fiapfood.core.entities.dto.DadosUsuariosComPaginacaoDto;
import br.com.fiapfood.core.entities.dto.UsuarioDto;

public interface IUsuarioCoreController {
	UsuarioDto buscarUsuarioPorId(UUID id);
	DadosUsuariosComPaginacaoDto buscarTodos(Integer pagina);
	void cadastrar(CadastrarUsuarioDto usuario);
	void atualizarEmail(UUID id, String email);
	void atualizarNome(UUID id, String nome);
	void inativar(UUID id);
	void reativar(UUID id);
	void atualizarPerfil(UUID id, Integer idPerfil);
	void atualizarDadosEndereco(UUID id, DadosEnderecoDto dadosEndereco);
}