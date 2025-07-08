package br.com.fiapfood.core.presenters;

import java.time.LocalDateTime;
import java.util.List;

import br.com.fiapfood.core.entities.Endereco;
import br.com.fiapfood.core.entities.Login;
import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.entities.dto.CadastrarUsuarioDto;
import br.com.fiapfood.core.entities.dto.DadosUsuariosComPaginacaoDto;
import br.com.fiapfood.core.entities.dto.EnderecoDto;
import br.com.fiapfood.core.entities.dto.LoginDto;
import br.com.fiapfood.core.entities.dto.PaginacaoDto;
import br.com.fiapfood.core.entities.dto.PerfilDto;
import br.com.fiapfood.core.entities.dto.UsuarioDto;
import br.com.fiapfood.infraestructure.entities.EnderecoEntity;
import br.com.fiapfood.infraestructure.entities.LoginEntity;
import br.com.fiapfood.infraestructure.entities.PerfilEntity;
import br.com.fiapfood.infraestructure.entities.UsuarioEntity;

public class UsuarioPresenter {

	public static UsuarioDto toUsuarioDto(UsuarioEntity usuario, PerfilDto perfil, LoginDto login, EnderecoDto endereco) {
		return new UsuarioDto(usuario.getId(), usuario.getNome(), perfil, login, usuario.getIsAtivo(), usuario.getEmail(), usuario.getDataCriacao(), usuario.getDataAtualizacao(), endereco);
	}
		
	public static Usuario toUsuario(UsuarioDto usuario) {
		return Usuario.criar(usuario.id(), usuario.nome(), usuario.perfil().id(), usuario.login().id(), usuario.isAtivo(), usuario.email(), usuario.dataCriacao(), usuario.dataAtualizacao(), usuario.endereco().id());
	}

	public static UsuarioDto toUsuarioDto(Usuario usuario, Perfil perfil, Login login, Endereco endereco) {
		return new UsuarioDto(usuario.getId(), usuario.getNome(), PerfilPresenter.toPerfilDto(perfil), LoginPresenter.toLogin(login), 
							  usuario.getIsAtivo(), usuario.getEmail(), usuario.getDataCriacao(), usuario.getDataAtualizacao(), EnderecoPresenter.toEnderecoEntity(endereco));
	}
	
	public static DadosUsuariosComPaginacaoDto toUsuarioPaginacaoDto(List<UsuarioDto> usuarios, PaginacaoDto paginacao) {
		return new DadosUsuariosComPaginacaoDto(usuarios, paginacao);
	}

	public static List<UsuarioDto> toListUsuarioDto(List<UsuarioEntity> usuarios) {
		return usuarios.stream().map(u -> UsuarioPresenter.toUsuarioDto(u, 
																		PerfilPresenter.toPerfilDto(u.getPerfil()), 
																		LoginPresenter.toLoginDto(u.getDadosLogin()), 
																		EnderecoPresenter.toEnderecoDto(u.getDadosEndereco()))).toList();
	}
	
	public static List<Usuario> toListUsuario(List<UsuarioDto> usuarios) {
		return usuarios.stream().map(u -> UsuarioPresenter.toUsuario(u)).toList();
	}

	public static UsuarioEntity toUsuarioEntity(UsuarioDto usuario, EnderecoEntity endereco, PerfilEntity perfil, LoginEntity login) {
		return new UsuarioEntity(null, usuario.nome(), usuario.email(), LocalDateTime.now(), null, true, endereco, perfil, login);
	}

	public static UsuarioEntity toUsuarioAtualizadoEntity(UsuarioDto usuario, EnderecoEntity endereco, PerfilEntity perfil, LoginEntity login) {
		return new UsuarioEntity(usuario.id(), usuario.nome(), usuario.email(), usuario.dataCriacao(), usuario.dataAtualizacao(), usuario.isAtivo(), endereco, perfil, login);
	}
	
	public static Usuario toUsuario(CadastrarUsuarioDto usuario) {
		return Usuario.criar(null, usuario.nome(), usuario.perfil(), null, true, usuario.email(), LocalDateTime.now(), null, null);
	}
}