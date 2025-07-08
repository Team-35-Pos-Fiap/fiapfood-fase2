package br.com.fiapfood.core.presenters;

import java.util.List;

import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.entities.dto.PerfilDto;
import br.com.fiapfood.infraestructure.entities.PerfilEntity;

public class PerfilPresenter {

	public static PerfilDto toPerfilDto(PerfilEntity perfil) {
		return new PerfilDto(perfil.getId(), perfil.getNome());
	}
	
	public static PerfilDto toPerfilDto(Perfil perfil) {
		return new PerfilDto(perfil.getId(), perfil.getNome());
	}

	public static Perfil toPerfil(PerfilDto perfil) {
		return Perfil.criar(perfil.id(), perfil.nome());
	}

	public static List<Perfil> toListPerfil(List<PerfilDto> perfis) {
		return perfis.stream().map(p -> PerfilPresenter.toPerfil(p)).toList();
	}

	public static List<PerfilDto> toListDto(List<Perfil> perfis) {
		return perfis.stream().map(p -> PerfilPresenter.toPerfilDto(p)).toList();
	}
	
	
	public static List<PerfilDto> toListPerfilDto(List<PerfilEntity> perfis) {
		return perfis.stream().map(p -> PerfilPresenter.toPerfilDto(p)).toList();
	}

	public static PerfilEntity toPerfilEntity(PerfilDto perfil) {
		return new PerfilEntity(perfil.id(), perfil.nome());
	}
}