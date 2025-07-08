package br.com.fiapfood.core.usecases.usuario.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.fiapfood.core.entities.Endereco;
import br.com.fiapfood.core.entities.Login;
import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.entities.dto.DadosUsuariosComPaginacaoDto;
import br.com.fiapfood.core.entities.dto.PaginacaoDto;
import br.com.fiapfood.core.entities.dto.UsuarioDto;
import br.com.fiapfood.core.gateways.interfaces.IEnderecoGateway;
import br.com.fiapfood.core.gateways.interfaces.ILoginGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.presenters.UsuarioPresenter;
import br.com.fiapfood.core.usecases.usuario.interfaces.IBuscarTodosUsuariosUseCase;

public class BuscarTodosUsuariosUseCase implements IBuscarTodosUsuariosUseCase{
	private final IUsuarioGateway usuarioGateway;
	private final IPerfilGateway perfilGateway;
	private final ILoginGateway loginGateway;
	private final IEnderecoGateway enderecoGateway;

	public BuscarTodosUsuariosUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway, ILoginGateway loginGateway, IEnderecoGateway enderecoGateway) {
		this.usuarioGateway = usuarioGateway;
		this.perfilGateway = perfilGateway;
		this.loginGateway = loginGateway;
		this.enderecoGateway = enderecoGateway;
	}
	
	@Override
	public DadosUsuariosComPaginacaoDto buscar(final Integer pagina) {
		final Map<Class<?>, Object> dados = buscarUsuariosComPaginacao(pagina);
		
		final List<Usuario> usuarios = toList(getListDto(dados));
					
		return UsuarioPresenter.toUsuarioPaginacaoDto(toListDto(usuarios), (PaginacaoDto)dados.get(PaginacaoDto.class));
	}
	
	private Map<Class<?>, Object> buscarUsuariosComPaginacao(final Integer pagina) {
		return  usuarioGateway.buscarUsuariosComPaginacao(pagina);
	}
	
	@SuppressWarnings("unchecked")
	private List<UsuarioDto> getListDto(final Map<Class<?>, Object> dados) {
		return (List<UsuarioDto>) dados.get(List.class);
	}
	
	private Perfil buscarPerfil(final Integer idPerfil) {
		return perfilGateway.buscarPorId(idPerfil);
	}
	
	private Login buscarLogin(final UUID idLogin) {
		return loginGateway.buscarPorId(idLogin);
	}
	
	private List<Usuario> toList(final List<UsuarioDto> usuarios) {
		return UsuarioPresenter.toListUsuario(usuarios);
	}
	
	private List<UsuarioDto> toListDto(final List<Usuario> usuarios) {
		return usuarios.stream().map(u -> UsuarioPresenter.toUsuarioDto(u, 
																		buscarPerfil(u.getIdPerfil()), 
																		buscarLogin(u.getIdLogin()), 
																		buscarEndereco(u.getIdEndereco()))).toList();
	}
	
	private Endereco buscarEndereco(final UUID idEndereco) {
		return enderecoGateway.buscarPorId(idEndereco);
	}
}