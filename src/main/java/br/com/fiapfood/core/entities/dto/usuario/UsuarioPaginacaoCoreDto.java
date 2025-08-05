package br.com.fiapfood.core.entities.dto.usuario;

import java.util.List;

import br.com.fiapfood.core.entities.dto.paginacao.PaginacaoCoreDto;

public record UsuarioPaginacaoCoreDto (List<DadosUsuarioCoreDto> usuarios, PaginacaoCoreDto paginacao) {

}