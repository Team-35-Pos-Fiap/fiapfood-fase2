package br.com.fiapfood.core.usecases.usuario.impl;

import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.entities.dto.usuario.CadastrarUsuarioCoreDto;
import br.com.fiapfood.core.exceptions.usuario.EmailDuplicadoException;
import br.com.fiapfood.core.exceptions.usuario.MatriculaDuplicadaException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.presenters.UsuarioPresenter;
import br.com.fiapfood.core.usecases.usuario.interfaces.ICadastrarUsuarioUseCase;

public class CadastrarUsuarioUseCase implements ICadastrarUsuarioUseCase {

	private final IUsuarioGateway usuarioGateway;
	private final IPerfilGateway perfilGateway;
	
	private final String EMAIL_DUPLICADO = "Já existe um usuário com o email informado.";
	private final String MATRICULA_DUPLICADA = "Já existe um usuário com a matrícula informada.";
	
	public CadastrarUsuarioUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		this.usuarioGateway = usuarioGateway;
		this.perfilGateway = perfilGateway;
	}
	
	@Override
	public void cadastrar(final CadastrarUsuarioCoreDto usuarioDto) {
		validarEmail(usuarioDto.email());
		validarMatricula(usuarioDto.dadosLogin().matricula());

		final Usuario usuario = toUsuario(usuarioDto);
		
		salvar(usuario);
	}
	
	private Perfil buscarPerfil(final Integer idPerfil) {
		return perfilGateway.buscarPorId(idPerfil);
	}
	
	private Usuario toUsuario(final CadastrarUsuarioCoreDto usuario) {
		return UsuarioPresenter.toUsuario(usuario);
	}
	
	private void salvar(final Usuario usuario) {
		usuarioGateway.salvar(UsuarioPresenter.toUsuarioDto(usuario, 
				   			  buscarPerfil(usuario.getIdPerfil())));
	}
	
	private void validarEmail(final String email) {
		if(usuarioGateway.emailJaCadastrado(email)){
			throw new EmailDuplicadoException(EMAIL_DUPLICADO);
		}
	}
	
	private void validarMatricula(final String matricula) {
		if(usuarioGateway.matriculaJaCadastrada(matricula)){
			throw new MatriculaDuplicadaException(MATRICULA_DUPLICADA);
		}
	}
}