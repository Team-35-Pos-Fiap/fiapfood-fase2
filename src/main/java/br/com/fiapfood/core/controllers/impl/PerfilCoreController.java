package br.com.fiapfood.core.controllers.impl;

import java.util.List;

import br.com.fiapfood.core.controllers.interfaces.IPerfilCoreController;
import br.com.fiapfood.core.presenters.PerfilPresenter;
import br.com.fiapfood.core.usecases.perfil.interfaces.IAtualizarNomePerfilUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.IBuscarPerfilPorIdUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.IBuscarTodosPerfisUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.ICadastrarPerfilUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.IInativarPerfilUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.IReativarPerfilUseCase;
import br.com.fiapfood.infraestructure.controllers.request.perfil.PerfilDto;

public class PerfilCoreController implements IPerfilCoreController{

	private final IBuscarTodosPerfisUseCase buscarTodosUseCase;
	private final IBuscarPerfilPorIdUseCase buscarPorIdUseCase;
	private final ICadastrarPerfilUseCase cadastrarPerfilUseCase;
	private final IAtualizarNomePerfilUseCase atualizarNomePerfilUseCase;
	private final IInativarPerfilUseCase inativarPerfilUseCase;
	private final IReativarPerfilUseCase reativarPerfilUseCase;
	
	public PerfilCoreController(IBuscarTodosPerfisUseCase buscarTodosUseCase, IBuscarPerfilPorIdUseCase buscarPorIdUseCase,
								ICadastrarPerfilUseCase cadastrarPerfilUseCase, IAtualizarNomePerfilUseCase atualizarNomePerfilUseCase,
								IInativarPerfilUseCase inativarPerfilUseCase, IReativarPerfilUseCase reativarPerfilUseCase) {
		this.buscarTodosUseCase = buscarTodosUseCase;
		this.buscarPorIdUseCase = buscarPorIdUseCase;
		this.cadastrarPerfilUseCase = cadastrarPerfilUseCase;
		this.atualizarNomePerfilUseCase = atualizarNomePerfilUseCase;
		this.inativarPerfilUseCase = inativarPerfilUseCase;
		this.reativarPerfilUseCase = reativarPerfilUseCase;
	}
	
	@Override
	public List<PerfilDto> buscarTodos() {
		return PerfilPresenter.toListPerfilOutputDto(buscarTodosUseCase.buscar());
	}

	@Override
	public PerfilDto buscarPorId(final Integer id) {
		return PerfilPresenter.toPerfilDto(buscarPorIdUseCase.buscar(id));
	}
	
	@Override
	public void cadastrar(final String nome) {
		cadastrarPerfilUseCase.cadastrar(nome);
	}
	
	@Override
	public void atualizarNome(final Integer id, final String nome) {
		atualizarNomePerfilUseCase.atualizar(id, nome);
	}

	@Override
	public void inativar(final Integer id) {
		inativarPerfilUseCase.inativar(id);		
	}
	
	@Override
	public void reativar(final Integer id) {
		reativarPerfilUseCase.reativar(id);		
	}
}