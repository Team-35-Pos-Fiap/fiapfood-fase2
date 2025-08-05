package br.com.fiapfood.core.utils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ImagemUtils {
	public final static List<String> TIPOS_PERMITIDOS = Arrays.asList("image/png", "image/jpg", "image/jpeg");
	
	private final static String LINK_DOWNLOAD_IMAGEM = "http://localhost:8080/fiapfood/restaurantes/{id-restaurante}/itens/{id-item}/imagem/download";
	
	public final static String prepararLinkDownloadImagem(final UUID idRestaurante, final UUID idItem) {
		return LINK_DOWNLOAD_IMAGEM.replace("{id-restaurante}", idRestaurante.toString()).replace("{id-item}", idItem.toString());
	}
}