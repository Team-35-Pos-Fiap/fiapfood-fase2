package br.com.fiapfood.core.presenters;

import java.util.List;
import java.util.UUID;

import br.com.fiapfood.core.entities.Atendimento;
import br.com.fiapfood.core.entities.dto.atendimento.AtendimentoCoreDto;
import br.com.fiapfood.infraestructure.controllers.request.atendimento.AtendimentoDto;
import br.com.fiapfood.infraestructure.entities.AtendimentoEntity;
import br.com.fiapfood.infraestructure.enums.Dia;

public class AtendimentoPresenter {

	public static List<AtendimentoCoreDto> toListAtendimentoDtos(List<AtendimentoEntity> atendimentos) {
		return atendimentos.stream().map(a -> AtendimentoPresenter.toAtendimentoDto(a)).toList();
	}

	public static List<AtendimentoCoreDto> toListAtendimentoDto(List<Atendimento> atendimentos) {
		return atendimentos.stream().map(a -> AtendimentoPresenter.toAtendimentoDto(a)).toList();
	}

	private static AtendimentoCoreDto toAtendimentoDto(Atendimento atendimento) {
		return new AtendimentoCoreDto(atendimento.getId(), atendimento.getDia(), atendimento.getInicioAtendimento(), atendimento.getTerminoAtendimento());
	}

	public static List<Atendimento> toListAtendimento(List<AtendimentoCoreDto> atendimentos) {
		return atendimentos.stream().map(a -> AtendimentoPresenter.toAtendimento(a)).toList();
	}

	public static Atendimento toAtendimento(AtendimentoCoreDto atendimento) {
		return Atendimento.criar(atendimento.id(), atendimento.dia(), atendimento.inicioAtendimento(), atendimento.terminoAtendimento());
	}
	
	public static List<AtendimentoEntity> toListAtendimentoEntity(List<AtendimentoCoreDto> atendimentos) {
		return atendimentos.stream().map(a -> AtendimentoPresenter.toAtendimentoEntity(a)).toList();
	}

	private static AtendimentoEntity toAtendimentoEntity(AtendimentoCoreDto atendimento) {
		return new AtendimentoEntity(atendimento.id(), Dia.getDia(atendimento.dia()), atendimento.inicioAtendimento(), atendimento.terminoAtendimento());
	}
	
	private static AtendimentoCoreDto toAtendimentoDto(AtendimentoEntity atendimento) {
		return new AtendimentoCoreDto(atendimento.getId(), atendimento.getDia().getValue().toString(), atendimento.getInicioAtendimento(), atendimento.getTerminoAtendimento());
	}

	public static List<AtendimentoEntity> toListAtendimentoResumidoEntity(List<AtendimentoCoreDto> atendimentos) {
		return atendimentos.stream().map(a -> AtendimentoPresenter.toAtendimentoResumidoEntity(a.id())).toList();
	}
	
	public static AtendimentoEntity toAtendimentoResumidoEntity(UUID idAtendimento) {
		return new AtendimentoEntity(idAtendimento, null, null, null);
	}
	
	public static List<AtendimentoDto> toListAtendimentoOutputDto(List<AtendimentoCoreDto> atendimentos) {
		return atendimentos.stream().map(a -> AtendimentoPresenter.toAtendimentoDto(a)).toList();
	}
	
	public static AtendimentoDto toAtendimentoDto(AtendimentoCoreDto atendimento) {
		return new AtendimentoDto(atendimento.id(), atendimento.dia(), atendimento.inicioAtendimento(), atendimento.terminoAtendimento());
	}

	public static List<AtendimentoCoreDto> toListAtendimentoCoreDto(List<AtendimentoDto> atendimentos) {
		return atendimentos.stream().map(a -> AtendimentoPresenter.toAtendimentoDto(a)).toList();
	}
	
	public static AtendimentoCoreDto toAtendimentoDto(AtendimentoDto atendimento) {
		return new AtendimentoCoreDto(atendimento.id(), atendimento.dia(), atendimento.inicioAtendimento(), atendimento.terminoAtendimento());
	}

	public static AtendimentoCoreDto toAtendimentoCoreDto(AtendimentoDto atendimento) {
		return new AtendimentoCoreDto(atendimento.id(), atendimento.dia(), atendimento.inicioAtendimento(), atendimento.terminoAtendimento());
	}
}