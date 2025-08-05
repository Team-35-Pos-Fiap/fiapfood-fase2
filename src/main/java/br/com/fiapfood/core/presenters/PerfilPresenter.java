package br.com.fiapfood.core.presenters;

import java.time.LocalDate;
import java.util.List;

import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.entities.dto.perfil.PerfilCoreDto;
import br.com.fiapfood.infraestructure.controllers.request.perfil.PerfilDto;
import br.com.fiapfood.infraestructure.entities.PerfilEntity;

public class PerfilPresenter {

	public static PerfilCoreDto toPerfilDto(PerfilEntity perfil) {
		return new PerfilCoreDto(perfil.getId(), perfil.getNome(), perfil.getDataCriacao(), perfil.getDataInativacao());
	}
	
	public static PerfilCoreDto toPerfilDto(Perfil perfil) {
		return new PerfilCoreDto(perfil.getId(), perfil.getNome(), perfil.getDataCriacao(), perfil.getDataInativacao());
	}

	public static Perfil toPerfil(PerfilCoreDto perfil) {
		return Perfil.criar(perfil.id(), perfil.nome(), perfil.dataCriacao(), perfil.dataInativacao());
	}

	public static List<Perfil> toListPerfil(List<PerfilCoreDto> perfis) {
		return perfis.stream().map(p -> PerfilPresenter.toPerfil(p)).toList();
	}

	public static List<PerfilCoreDto> toListDto(List<Perfil> perfis) {
		return perfis.stream().map(p -> PerfilPresenter.toPerfilDto(p)).toList();
	}
	
	public static List<PerfilCoreDto> toListPerfilDto(List<PerfilEntity> perfis) {
		return perfis.stream().map(p -> PerfilPresenter.toPerfilDto(p)).toList();
	}

	public static PerfilEntity toPerfilEntity(PerfilCoreDto perfil) {
		return new PerfilEntity(perfil.id(), perfil.nome(), perfil.dataCriacao(), perfil.dataInativacao());
	}

	public static Perfil toPerfil(String nome) {
		return Perfil.criar(null, nome, LocalDate.now(), null);
	}

	public static List<PerfilDto> toListPerfilOutputDto(List<PerfilCoreDto> perfis) {
		return perfis.stream().map(p -> PerfilPresenter.toPerfilDto(p)).toList();
	}

	public static PerfilDto toPerfilDto(PerfilCoreDto perfil) {
		return new PerfilDto(perfil.id(), perfil.nome(), perfil.dataCriacao(), perfil.dataInativacao());
	}
}