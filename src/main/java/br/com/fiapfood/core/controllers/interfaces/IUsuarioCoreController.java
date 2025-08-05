package br.com.fiapfood.core.controllers.interfaces;

import java.util.UUID;

import br.com.fiapfood.infraestructure.controllers.request.endereco.DadosEnderecoDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.CadastrarUsuarioDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.UsuarioDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.UsuarioPaginacaoDto;

public interface IUsuarioCoreController {
	UsuarioDto buscarUsuarioPorId(UUID id);
	UsuarioPaginacaoDto buscarTodos(Integer pagina);
	void cadastrar(CadastrarUsuarioDto usuario);
	void atualizarEmail(UUID id, String email);
	void atualizarNome(UUID id, String nome);
	void inativar(UUID id);
	void reativar(UUID id);
	void atualizarPerfil(UUID id, Integer idPerfil);
	void atualizarDadosEndereco(UUID id, DadosEnderecoDto dadosEndereco);
	void atualizarMatricula(UUID id, String matricula);
	void atualizarSenha(UUID id, String senha);
	String validarAcesso(String matricula, String senha);
}