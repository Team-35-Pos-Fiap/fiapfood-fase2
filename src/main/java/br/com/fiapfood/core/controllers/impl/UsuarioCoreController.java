package br.com.fiapfood.core.controllers.impl;

import java.util.UUID;

import br.com.fiapfood.core.controllers.interfaces.IUsuarioCoreController;
import br.com.fiapfood.core.presenters.EnderecoPresenter;
import br.com.fiapfood.core.presenters.UsuarioPresenter;
import br.com.fiapfood.core.usecases.login.interfaces.IAtualizarMatriculaUseCase;
import br.com.fiapfood.core.usecases.login.interfaces.IAtualizarSenhaUseCase;
import br.com.fiapfood.core.usecases.login.interfaces.IValidarAcessoUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarEmailUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarEnderecoUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarNomeUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarPerfilUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IBuscarTodosUsuariosUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IBuscarUsuarioPorIdUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.ICadastrarUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IInativarUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IReativarUsuarioUseCase;
import br.com.fiapfood.infraestructure.controllers.request.endereco.DadosEnderecoDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.CadastrarUsuarioDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.UsuarioDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.UsuarioPaginacaoDto;

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
	private final IValidarAcessoUseCase validarAcessoUseCase;
	private final IAtualizarSenhaUseCase atualizarSenhaUseCase;
	private final IAtualizarMatriculaUseCase atualizarMatriculaUseCase;
	
	public UsuarioCoreController(IBuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase, 
								 IBuscarTodosUsuariosUseCase buscarTodosUsuariosUseCase, 
								 ICadastrarUsuarioUseCase cadastrarUsuarioUseCase, 
								 IAtualizarEmailUsuarioUseCase atualizarEmailUsuarioUseCase,
								 IAtualizarNomeUsuarioUseCase atualizarNomeUsuarioUseCase, 
								 IInativarUsuarioUseCase inativarUsuarioUseCase,
								 IReativarUsuarioUseCase reativarUsuarioUseCase,
								 IAtualizarPerfilUsuarioUseCase atualizarPerfilUsuarioUseCase,
								 IAtualizarEnderecoUsuarioUseCase atualizarEnderecoUsuarioUseCase,
								 IValidarAcessoUseCase validarAcessoUseCase, 
								 IAtualizarSenhaUseCase atualizarSenhaUseCase, 
								 IAtualizarMatriculaUseCase atualizarMatriculaUseCase) {
		this.buscarUsuarioPorIdUseCase = buscarUsuarioPorIdUseCase;
		this.buscarTodosUsuariosUseCase = buscarTodosUsuariosUseCase;
		this.cadastrarUsuarioUseCase = cadastrarUsuarioUseCase;
		this.atualizarEmailUsuarioUseCase = atualizarEmailUsuarioUseCase;
		this.atualizarNomeUsuarioUseCase = atualizarNomeUsuarioUseCase;
		this.inativarUsuarioUseCase = inativarUsuarioUseCase;
		this.reativarUsuarioUseCase = reativarUsuarioUseCase;
		this.atualizarPerfilUsuarioUseCase = atualizarPerfilUsuarioUseCase;
		this.atualizarEnderecoUsuarioUseCase = atualizarEnderecoUsuarioUseCase;
		this.validarAcessoUseCase = validarAcessoUseCase;
		this.atualizarSenhaUseCase = atualizarSenhaUseCase;
		this.atualizarMatriculaUseCase = atualizarMatriculaUseCase;
	}
	
	@Override
	public UsuarioDto buscarUsuarioPorId(final UUID id) {
		return UsuarioPresenter.toUsuarioDto(buscarUsuarioPorIdUseCase.buscar(id));
	}
	
	@Override
	public UsuarioPaginacaoDto buscarTodos(final Integer pagina) {
		return UsuarioPresenter.toUsuarioPaginacaoDto(buscarTodosUsuariosUseCase.buscar(pagina));
	}

	@Override
	public void cadastrar(final CadastrarUsuarioDto usuario) {
		cadastrarUsuarioUseCase.cadastrar(UsuarioPresenter.toCadastrarUsuarioDto(usuario));		
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
		atualizarEnderecoUsuarioUseCase.atualizar(id, EnderecoPresenter.toEnderecoCoreDto(dadosEndereco));		
	}

	@Override
	public void atualizarMatricula(UUID id, String matricula) {
		atualizarMatriculaUseCase.atualizar(id, matricula);
	}

	@Override
	public void atualizarSenha(UUID id, String senha) {
		atualizarSenhaUseCase.atualizar(id, senha);
	}
	
	@Override
	public String validarAcesso(String matricula, String senha) {
		return validarAcessoUseCase.validar(matricula, senha);
	}
}