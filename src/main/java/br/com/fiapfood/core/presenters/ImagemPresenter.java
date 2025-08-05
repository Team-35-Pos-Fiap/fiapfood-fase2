package br.com.fiapfood.core.presenters;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import br.com.fiapfood.core.entities.Imagem;
import br.com.fiapfood.core.entities.dto.item.ImagemCoreDto;
import br.com.fiapfood.infraestructure.controllers.request.item.ImagemDto;
import br.com.fiapfood.infraestructure.entities.ImagemEntity;

public class ImagemPresenter {
	public static ImagemCoreDto toImagemDto(Imagem imagem) {
		return new ImagemCoreDto(imagem.getId(), imagem.getNome(), imagem.getConteudo(), imagem.getTipo());
	}
	
	public static ImagemCoreDto toImagemDto(MultipartFile imagem) throws IOException {
		return new ImagemCoreDto(null, imagem.getOriginalFilename(), imagem.getBytes(), imagem.getContentType());
	}
	
	public static ImagemEntity toImagemEntity(ImagemCoreDto imagem) {
		return new ImagemEntity(imagem.id(), imagem.nome(), imagem.conteudo(), imagem.tipo());
	}
	
	public static Imagem toImagem(ImagemCoreDto imagem) {
		return Imagem.criar(imagem.id(), imagem.nome(), imagem.conteudo(), imagem.tipo());
	}

	public static ImagemCoreDto toImagemDto(ImagemEntity imagem) {
		return new ImagemCoreDto(imagem.getId(), imagem.getNome(), imagem.getConteudo(), imagem.getTipo());
	}

	public static ImagemDto toImagemDto(ImagemCoreDto imagem) {
		return new ImagemDto(imagem.id(), imagem.nome(), imagem.conteudo(), imagem.tipo());
	}
}