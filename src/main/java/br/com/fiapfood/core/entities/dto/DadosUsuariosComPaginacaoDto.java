package br.com.fiapfood.core.entities.dto;

import java.util.List;

public record DadosUsuariosComPaginacaoDto (List<UsuarioDto> usuarios, PaginacaoDto paginacao) {

}