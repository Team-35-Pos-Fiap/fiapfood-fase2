package br.com.fiapfood.core.usecases.tipo_culinaria.impl;

import br.com.fiapfood.core.entities.TipoCulinaria;
import br.com.fiapfood.core.exceptions.perfil.NomePerfilDuplicadoException;
import br.com.fiapfood.core.gateways.interfaces.ITipoCulinariaGateway;
import br.com.fiapfood.core.presenters.TipoCulinariaPresenter;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.IAtualizarNomeTipoCulinariaUseCase;

public class AtualizarNomeTipoCulinariaUseCase implements IAtualizarNomeTipoCulinariaUseCase {

	private final ITipoCulinariaGateway tipoCulinariaGateway;
	
	private final String PERFIL_DUPLICADO = "Já existe um tipo de culinária com o nome informado.";
	
	public AtualizarNomeTipoCulinariaUseCase(ITipoCulinariaGateway tipoCulinariaGateway) {
		this.tipoCulinariaGateway = tipoCulinariaGateway;
	}
	
	@Override
	public void atualizar(Integer id, String nome) {
		validaNomeJaCadastrado(nome);
		
		final TipoCulinaria tipoCulinaria = buscarTipoCulinaria(id);
		
		atualizarNome(tipoCulinaria, nome);
		
		salvar(tipoCulinaria);
	}
	
	private void salvar(TipoCulinaria tipoCulinaria) {
		tipoCulinariaGateway.salvar(TipoCulinariaPresenter.toTipoCulinariaDto(tipoCulinaria));
	}

	private void atualizarNome(TipoCulinaria tipoCulinaria, String nome) {
		tipoCulinaria.atualizarNome(nome);
	}

	private void validaNomeJaCadastrado(final String nome) {
		if(tipoCulinariaGateway.nomeJaCadastrado(nome)) {
			throw new NomePerfilDuplicadoException(PERFIL_DUPLICADO);
		}
	}

	private TipoCulinaria buscarTipoCulinaria(final Integer id) {
		return tipoCulinariaGateway.buscarPorId(id);
	}
}