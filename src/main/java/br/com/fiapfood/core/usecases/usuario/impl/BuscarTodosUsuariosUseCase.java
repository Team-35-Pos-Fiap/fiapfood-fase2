package br.com.fiapfood.core.usecases.usuario.impl;

import java.util.List;

import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.entities.dto.paginacao.PaginacaoCoreDto;
import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioCoreDto;
import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioInputDto;
import br.com.fiapfood.core.entities.dto.usuario.UsuarioPaginacaoCoreDto;
import br.com.fiapfood.core.entities.dto.usuario.UsuarioPaginacaoInputDto;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.presenters.UsuarioPresenter;
import br.com.fiapfood.core.usecases.usuario.interfaces.IBuscarTodosUsuariosUseCase;

public class BuscarTodosUsuariosUseCase implements IBuscarTodosUsuariosUseCase{
	private final IUsuarioGateway usuarioGateway;
	private final IPerfilGateway perfilGateway;

	public BuscarTodosUsuariosUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		this.usuarioGateway = usuarioGateway;
		this.perfilGateway = perfilGateway;
	}
	
	@Override
	public UsuarioPaginacaoCoreDto buscar(final Integer pagina) {
		final UsuarioPaginacaoInputDto dados = buscarTodos(pagina);
		
		return toUsuarioPaginacaoOutputDto(toListUsuarioDto(toListUsuario(dados.usuarios())), dados.paginacao());		
	}
	
	private UsuarioPaginacaoInputDto buscarTodos(final Integer pagina) {
		return usuarioGateway.buscarTodos(pagina);
	}
	
	private Perfil buscarPerfil(final Integer idPerfil) {
		return perfilGateway.buscarPorId(idPerfil);
	}
	
	private List<Usuario> toListUsuario(final List<DadosUsuarioInputDto> usuarios) {
		return UsuarioPresenter.toListUsuario(usuarios);
	}
	
	private List<DadosUsuarioCoreDto> toListUsuarioDto(final List<Usuario> usuarios) {
		return usuarios.stream().map(u -> UsuarioPresenter.toUsuarioDto(u, 
																		buscarPerfil(u.getIdPerfil()))).toList();
	}
	
	private UsuarioPaginacaoCoreDto toUsuarioPaginacaoOutputDto(final List<DadosUsuarioCoreDto> usuarios, final PaginacaoCoreDto paginacao) {
		return UsuarioPresenter.toUsuarioPaginacaoOutputDto(usuarios, paginacao);
	}
}