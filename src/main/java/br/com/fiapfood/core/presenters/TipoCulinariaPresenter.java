package br.com.fiapfood.core.presenters;

import java.util.List;

import br.com.fiapfood.core.entities.TipoCulinaria;
import br.com.fiapfood.core.entities.dto.tipo_culinaria.TipoCulinariaCoreDto;
import br.com.fiapfood.infraestructure.controllers.request.tipo_culinaria.TipoCulinariaDto;
import br.com.fiapfood.infraestructure.entities.TipoCulinariaEntity;

public class TipoCulinariaPresenter {

	public static TipoCulinariaCoreDto toTipoCulinariaDto(TipoCulinariaEntity tipoCulinaria) {
		return new TipoCulinariaCoreDto(tipoCulinaria.getId(), tipoCulinaria.getNome());
	}
	
	public static TipoCulinariaCoreDto toTipoCulinariaDto(TipoCulinaria tipoCulinaria) {
		return new TipoCulinariaCoreDto(tipoCulinaria.getId(), tipoCulinaria.getNome());
	}

	public static TipoCulinaria toTipoCulinaria(TipoCulinariaCoreDto tipoCulinaria) {
		return TipoCulinaria.criar(tipoCulinaria.id(), tipoCulinaria.nome());
	}

	public static List<TipoCulinaria> toListTipoCulinaria(List<TipoCulinariaCoreDto> tiposCulinaria) {
		return tiposCulinaria.stream().map(tc -> TipoCulinariaPresenter.toTipoCulinaria(tc)).toList();
	}

	public static List<TipoCulinariaCoreDto> toListDto(List<TipoCulinaria> tiposCulinaria) {
		return tiposCulinaria.stream().map(tc -> TipoCulinariaPresenter.toTipoCulinariaDto(tc)).toList();
	}
	
	public static List<TipoCulinariaCoreDto> toListTipoCulinariaDto(List<TipoCulinariaEntity> tiposCulinaria) {
		return tiposCulinaria.stream().map(tc -> TipoCulinariaPresenter.toTipoCulinariaDto(tc)).toList();
	}

	public static TipoCulinariaEntity toTipoCulinariaEntity(TipoCulinariaCoreDto tipoCulinaria) {
		return new TipoCulinariaEntity(tipoCulinaria.id(), tipoCulinaria.nome());
	}

	public static TipoCulinaria toTipoCulinaria(String nome) {
		return TipoCulinaria.criar(null, nome);
	}

	public static TipoCulinariaEntity toTipoCulinariaResumidoEntity(Integer idTipoCulinaria) {
		return new TipoCulinariaEntity(idTipoCulinaria, null);
	}

	public static List<TipoCulinariaDto> toListTipoCulinariaOutputDto(List<TipoCulinariaCoreDto> tipos) {
		return tipos.stream().map(t -> TipoCulinariaPresenter.toTipoCulinariaDto(t)).toList();
	}

	public static TipoCulinariaDto toTipoCulinariaDto(TipoCulinariaCoreDto tipoCulinaria) {
		return new TipoCulinariaDto(tipoCulinaria.id(), tipoCulinaria.nome());
	}
}