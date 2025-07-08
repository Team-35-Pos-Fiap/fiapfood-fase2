package br.com.fiapfood.core.controllers.impl;

import java.util.UUID;

import br.com.fiapfood.core.controllers.interfaces.IUsuarioCoreController;
import br.com.fiapfood.core.entities.dto.CadastrarUsuarioDto;
import br.com.fiapfood.core.entities.dto.DadosEnderecoDto;
import br.com.fiapfood.core.entities.dto.DadosUsuariosComPaginacaoDto;
import br.com.fiapfood.core.entities.dto.UsuarioDto;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarEmailUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarEnderecoUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarNomeUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarPerfilUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IBuscarTodosUsuariosUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IBuscarUsuarioPorIdUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.ICadastrarUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IInativarUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IReativarUsuarioUseCase;

public class UsuarioCoreController implements IUsuarioCoreController {

	private final IBuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;
	private final IBuscarTodosUsuariosUseCase buscarTodosUsuariosUseCase;
	private final ICadastrarUsuarioUseCase cadastrarUsuarioUseCase;
	private final IAtualizarEmailUsuarioUseCase atualizarEmailUsuarioUseCase;
	private final IAtualizarNomeUsuarioUseCase atualizarNomeUsuarioUseCase;
	private final IInativarUsuarioUseCase inativarUsuarioUseCase;
	private final IReativarUsuarioUseCase reativarUsuarioUseCase;
	private final IAtualizarPerfilUsuarioUseCase atualizarPerfilUsuarioUseCase;
	private final IAtualizarEnderecoUsuarioUseCase atualizarEnderecoUsuarioUseCase;
	
	public UsuarioCoreController(IBuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase, 
								 IBuscarTodosUsuariosUseCase buscarTodosUsuariosUseCase, 
								 ICadastrarUsuarioUseCase cadastrarUsuarioUseCase, 
								 IAtualizarEmailUsuarioUseCase atualizarEmailUsuarioUseCase,
								 IAtualizarNomeUsuarioUseCase atualizarNomeUsuarioUseCase, 
								 IInativarUsuarioUseCase inativarUsuarioUseCase,
								 IReativarUsuarioUseCase reativarUsuarioUseCase,
								 IAtualizarPerfilUsuarioUseCase atualizarPerfilUsuarioUseCase,
								 IAtualizarEnderecoUsuarioUseCase atualizarEnderecoUsuarioUseCase) {
		this.buscarUsuarioPorIdUseCase = buscarUsuarioPorIdUseCase;
		this.buscarTodosUsuariosUseCase = buscarTodosUsuariosUseCase;
		this.cadastrarUsuarioUseCase = cadastrarUsuarioUseCase;
		this.atualizarEmailUsuarioUseCase = atualizarEmailUsuarioUseCase;
		this.atualizarNomeUsuarioUseCase = atualizarNomeUsuarioUseCase;
		this.inativarUsuarioUseCase = inativarUsuarioUseCase;
		this.reativarUsuarioUseCase = reativarUsuarioUseCase;
		this.atualizarPerfilUsuarioUseCase = atualizarPerfilUsuarioUseCase;
		this.atualizarEnderecoUsuarioUseCase = atualizarEnderecoUsuarioUseCase;
	}
	
	@Override
	public UsuarioDto buscarUsuarioPorId(final UUID id) {
		return buscarUsuarioPorIdUseCase.buscar(id);
	}
	
	@Override
	public DadosUsuariosComPaginacaoDto buscarTodos(final Integer pagina) {
		return buscarTodosUsuariosUseCase.buscar(pagina);
	}

	@Override
	public void cadastrar(final CadastrarUsuarioDto usuario) {
		cadastrarUsuarioUseCase.cadastrar(usuario);		
	}
	
	@Override
	public void atualizarEmail(final UUID id, final String email) {
		atualizarEmailUsuarioUseCase.atualizar(id, email);		
	}

	@Override
	public void atualizarNome(final UUID id, final String nome) {
		atualizarNomeUsuarioUseCase.atualizar(id, nome);
	}
	
	@Override
	public void atualizarPerfil(final UUID id, final Integer idPerfil) {
		atualizarPerfilUsuarioUseCase.atualizar(id, idPerfil);
	}
	
	@Override
	public void inativar(final UUID id) {
		inativarUsuarioUseCase.inativar(id);
	}
	
	@Override
	public void reativar(final UUID id) {
		reativarUsuarioUseCase.reativar(id);
	}

	@Override
	public void atualizarDadosEndereco(final UUID id, final DadosEnderecoDto dadosEndereco) {
		atualizarEnderecoUsuarioUseCase.atualizar(id, dadosEndereco);		
	}
}