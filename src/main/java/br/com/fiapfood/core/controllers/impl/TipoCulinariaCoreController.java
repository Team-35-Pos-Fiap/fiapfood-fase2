package br.com.fiapfood.core.controllers.impl;

import java.util.List;

import br.com.fiapfood.core.controllers.interfaces.ITipoCulinariaCoreController;
import br.com.fiapfood.core.presenters.TipoCulinariaPresenter;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.IAtualizarNomeTipoCulinariaUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.IBuscarTipoCulinariaPorIdUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.IBuscarTodosTiposCulinariaUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.ICadastrarTipoCulinariaUseCase;
import br.com.fiapfood.infraestructure.controllers.request.tipo_culinaria.TipoCulinariaDto;

public class TipoCulinariaCoreController implements ITipoCulinariaCoreController{

	private final IBuscarTodosTiposCulinariaUseCase buscarTodosUseCase;
	private final IBuscarTipoCulinariaPorIdUseCase buscarPorIdUseCase;
	private final ICadastrarTipoCulinariaUseCase cadastrarTipoCulinariaUseCase;
	private final IAtualizarNomeTipoCulinariaUseCase atualizarNomeTipoCulinariaUseCase;
	
	public TipoCulinariaCoreController(IBuscarTodosTiposCulinariaUseCase buscarTodosUseCase, IBuscarTipoCulinariaPorIdUseCase buscarPorIdUseCase,
									   ICadastrarTipoCulinariaUseCase cadastrarTipoCulinariaUseCase, IAtualizarNomeTipoCulinariaUseCase atualizarNomeTipoCulinariaUseCase) {
		this.buscarTodosUseCase = buscarTodosUseCase;
		this.buscarPorIdUseCase = buscarPorIdUseCase;
		this.cadastrarTipoCulinariaUseCase = cadastrarTipoCulinariaUseCase;
		this.atualizarNomeTipoCulinariaUseCase = atualizarNomeTipoCulinariaUseCase;
	}
	
	@Override
	public List<TipoCulinariaDto> buscarTodos() {
		return TipoCulinariaPresenter.toListTipoCulinariaOutputDto(buscarTodosUseCase.buscar());
	}

	@Override
	public TipoCulinariaDto buscarPorId(final Integer id) {
		return TipoCulinariaPresenter.toTipoCulinariaDto(buscarPorIdUseCase.buscar(id));
	}
	
	@Override
	public void cadastrar(final String nome) {
		cadastrarTipoCulinariaUseCase.cadastrar(nome);
	}
	
	@Override
	public void atualizar(final Integer id, final String nome) {
		atualizarNomeTipoCulinariaUseCase.atualizar(id, nome);
	}
}