package br.com.fiapfood.infraestructure.controllers.request.usuario;

import java.util.List;

import br.com.fiapfood.infraestructure.controllers.request.paginacao.PaginacaoDto;

public record UsuarioPaginacaoDto (List<UsuarioDto> usuarios, PaginacaoDto paginacao) {

}