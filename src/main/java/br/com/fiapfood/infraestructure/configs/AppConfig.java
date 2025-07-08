package br.com.fiapfood.infraestructure.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiapfood.core.controllers.impl.LoginCoreController;
import br.com.fiapfood.core.controllers.impl.PerfilCoreController;
import br.com.fiapfood.core.controllers.impl.UsuarioCoreController;
import br.com.fiapfood.core.controllers.interfaces.ILoginCoreController;
import br.com.fiapfood.core.controllers.interfaces.IPerfilCoreController;
import br.com.fiapfood.core.controllers.interfaces.IUsuarioCoreController;
import br.com.fiapfood.core.gateways.impl.EnderecoGateway;
import br.com.fiapfood.core.gateways.impl.LoginGateway;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.impl.UsuarioGateway;
import br.com.fiapfood.core.gateways.interfaces.IEnderecoGateway;
import br.com.fiapfood.core.gateways.interfaces.ILoginGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.login.impl.AtualizarMatriculaUseCase;
import br.com.fiapfood.core.usecases.login.impl.AtualizarSenhaUseCase;
import br.com.fiapfood.core.usecases.login.impl.ValidarLoginUseCase;
import br.com.fiapfood.core.usecases.login.interfaces.IAtualizarMatriculaUseCase;
import br.com.fiapfood.core.usecases.login.interfaces.IAtualizarSenhaUseCase;
import br.com.fiapfood.core.usecases.login.interfaces.IValidarLoginUseCase;
import br.com.fiapfood.core.usecases.perfil.impl.BuscarPerfilPorIdUseCase;
import br.com.fiapfood.core.usecases.perfil.impl.BuscarTodosPerfisUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.IBuscarPerfilPorIdUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.IBuscarTodosPerfisUseCase;
import br.com.fiapfood.core.usecases.usuario.impl.AtualizarEmailUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.impl.AtualizarEnderecoUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.impl.AtualizarNomeUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.impl.AtualizarPerfilUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.impl.BuscarTodosUsuariosUseCase;
import br.com.fiapfood.core.usecases.usuario.impl.BuscarUsuarioPorIdUseCase;
import br.com.fiapfood.core.usecases.usuario.impl.CadastrarUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.impl.InativarUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.impl.ReativarUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarEmailUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarEnderecoUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarNomeUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarPerfilUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IBuscarTodosUsuariosUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IBuscarUsuarioPorIdUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.ICadastrarUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IInativarUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IReativarUsuarioUseCase;
import br.com.fiapfood.infraestructure.repositories.interfaces.IEnderecoRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.ILoginRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.IPerfilRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.IUsuarioRepository;

@Configuration
public class AppConfig {
	//Controllers
	
	@Bean
	public ILoginCoreController iLoginCoreController(IValidarLoginUseCase validarLoginUseCase, IAtualizarSenhaUseCase atualizarSenhaUseCase, IAtualizarMatriculaUseCase atualizarMatriculaUseCase) {
		return new LoginCoreController(validarLoginUseCase, atualizarSenhaUseCase, atualizarMatriculaUseCase);
	}
		
	@Bean
	public IPerfilCoreController iPerfilCoreController(IBuscarTodosPerfisUseCase buscarTodosUseCase, IBuscarPerfilPorIdUseCase buscarPorIdUseCase) {
		return new PerfilCoreController(buscarTodosUseCase, buscarPorIdUseCase);
	}
	
	@Bean
	public IUsuarioCoreController iUsuarioCoreController(IBuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase, 
														 IBuscarTodosUsuariosUseCase buscarTodosUsuariosUseCase,
														 ICadastrarUsuarioUseCase cadastrarUsuarioUseCase,
														 IAtualizarEmailUsuarioUseCase atualizarEmailUsuarioUseCase,
														 IAtualizarNomeUsuarioUseCase atualizarNomeUsuarioUseCase, 
														 IInativarUsuarioUseCase inativarUsuarioUseCase,
														 IReativarUsuarioUseCase reativarUsuarioUseCase,
														 IAtualizarPerfilUsuarioUseCase atualizarPerfilUsuarioUseCase,
														 IAtualizarEnderecoUsuarioUseCase atualizarEnderecoUsuarioUseCase) {
		return new UsuarioCoreController(buscarUsuarioPorIdUseCase, 
										 buscarTodosUsuariosUseCase, 
										 cadastrarUsuarioUseCase, 
										 atualizarEmailUsuarioUseCase, 
										 atualizarNomeUsuarioUseCase, 
										 inativarUsuarioUseCase,
										 reativarUsuarioUseCase,
										 atualizarPerfilUsuarioUseCase,
										 atualizarEnderecoUsuarioUseCase);
	}
	
	// Usecases
	
	@Bean
	public IValidarLoginUseCase iValidarLoginUseCase(ILoginGateway loginGateway, IUsuarioGateway usuarioGateway) {
		return new ValidarLoginUseCase(loginGateway, usuarioGateway);
	}
	
	@Bean
	public IAtualizarMatriculaUseCase iAtualizarMatriculaUseCase(ILoginGateway loginGateway, IUsuarioGateway usuarioGateway) {
		return new AtualizarMatriculaUseCase(loginGateway, usuarioGateway);
	}
	
	@Bean
	public IAtualizarSenhaUseCase iAtualizarSenhaUseCase(ILoginGateway loginGateway, IUsuarioGateway usuarioGateway) {
		return new AtualizarSenhaUseCase(loginGateway, usuarioGateway);
	}
	
	@Bean
	public IBuscarTodosPerfisUseCase iBuscarTodosUseCase(IPerfilGateway perfilGateway) {
		return new BuscarTodosPerfisUseCase(perfilGateway);
	}
	
	@Bean
	public IBuscarPerfilPorIdUseCase iBuscarPorIdUseCase(IPerfilGateway perfilGateway) {
		return new BuscarPerfilPorIdUseCase(perfilGateway);
	}
	
	@Bean
	public IBuscarUsuarioPorIdUseCase iBuscarUsuarioPorIdUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway, ILoginGateway loginGateway, IEnderecoGateway enderecoGateway) {
		return new BuscarUsuarioPorIdUseCase(usuarioGateway, perfilGateway, loginGateway, enderecoGateway);
	}
	
	@Bean
	public IBuscarTodosUsuariosUseCase iBuscarTodosUsuariosUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway, ILoginGateway loginGateway, IEnderecoGateway enderecoGateway) {
		return new BuscarTodosUsuariosUseCase(usuarioGateway, perfilGateway, loginGateway, enderecoGateway);
	}
	
	@Bean
	public ICadastrarUsuarioUseCase iCadastrarUsuarioUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway, ILoginGateway loginGateway) {
		return new CadastrarUsuarioUseCase(usuarioGateway, perfilGateway, loginGateway);
	}
	
	@Bean
	public IAtualizarPerfilUsuarioUseCase iAtualizarPerfilUsuarioUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway, ILoginGateway loginGateway, IEnderecoGateway enderecoGateway) {
		return new AtualizarPerfilUsuarioUseCase(usuarioGateway, perfilGateway, loginGateway, enderecoGateway);
	}

	@Bean
	public IAtualizarNomeUsuarioUseCase iAtualizarNomeUsuarioUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway, ILoginGateway loginGateway, IEnderecoGateway enderecoGateway) {
		return new AtualizarNomeUsuarioUseCase(usuarioGateway, perfilGateway, loginGateway, enderecoGateway);
	}
	
	@Bean
	public IAtualizarEmailUsuarioUseCase iAtualizarEmailUsuarioUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway, ILoginGateway loginGateway, IEnderecoGateway enderecoGateway) {
		return new AtualizarEmailUsuarioUseCase(usuarioGateway, perfilGateway, loginGateway, enderecoGateway);
	}
	
	@Bean
	public IAtualizarEnderecoUsuarioUseCase iAtualizarEnderecoUsuarioUseCase(IUsuarioGateway usuarioGateway, IEnderecoGateway enderecoGateway) {
		return new AtualizarEnderecoUsuarioUseCase(usuarioGateway, enderecoGateway);
	}
		
	@Bean
	public IInativarUsuarioUseCase iInativarUsuarioUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway, ILoginGateway loginGateway, IEnderecoGateway enderecoGateway) {
		return new InativarUsuarioUseCase(usuarioGateway, perfilGateway, loginGateway, enderecoGateway);
	}
	
	@Bean
	public IReativarUsuarioUseCase iReativarUsuarioUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway, ILoginGateway loginGateway, IEnderecoGateway enderecoGateway) {
		return new ReativarUsuarioUseCase(usuarioGateway, perfilGateway, loginGateway, enderecoGateway);
	}
	
	// Gateways
	
	@Bean
	public ILoginGateway iLoginGateway(ILoginRepository loginRepository) {
		return new LoginGateway(loginRepository);
	}
	
	@Bean
	public IUsuarioGateway iUsuarioGateway(IUsuarioRepository usuarioRepository) {
		return new UsuarioGateway(usuarioRepository);
	}
	
	@Bean
	public IPerfilGateway iPerfilGateway(IPerfilRepository perfilRepository) {
		return new PerfilGateway(perfilRepository);
	}
	
	@Bean
	public IEnderecoGateway iEnderecoGateway(IEnderecoRepository enderecoRepository) {
		return new EnderecoGateway(enderecoRepository);
	}
}