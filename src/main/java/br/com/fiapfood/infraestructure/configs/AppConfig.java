package br.com.fiapfood.infraestructure.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiapfood.core.controllers.impl.PerfilCoreController;
import br.com.fiapfood.core.controllers.impl.RestauranteCoreController;
import br.com.fiapfood.core.controllers.impl.TipoCulinariaCoreController;
import br.com.fiapfood.core.controllers.impl.UsuarioCoreController;
import br.com.fiapfood.core.controllers.interfaces.IPerfilCoreController;
import br.com.fiapfood.core.controllers.interfaces.IRestauranteCoreController;
import br.com.fiapfood.core.controllers.interfaces.ITipoCulinariaCoreController;
import br.com.fiapfood.core.controllers.interfaces.IUsuarioCoreController;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.impl.RestauranteGateway;
import br.com.fiapfood.core.gateways.impl.TipoCulinariaGateway;
import br.com.fiapfood.core.gateways.impl.UsuarioGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IRestauranteGateway;
import br.com.fiapfood.core.gateways.interfaces.ITipoCulinariaGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.atendimento.impl.AdicionarAtendimentoUseCase;
import br.com.fiapfood.core.usecases.atendimento.impl.AtualizarAtendimentoUseCase;
import br.com.fiapfood.core.usecases.atendimento.impl.ExcluirAtendimentoUseCase;
import br.com.fiapfood.core.usecases.atendimento.interfaces.IAdicionarAtendimentoUseCase;
import br.com.fiapfood.core.usecases.atendimento.interfaces.IAtualizarAtendimentoUseCase;
import br.com.fiapfood.core.usecases.atendimento.interfaces.IExcluirAtendimentoUseCase;
import br.com.fiapfood.core.usecases.item.impl.AtualizarDescricaoItemUseCase;
import br.com.fiapfood.core.usecases.item.impl.AtualizarDisponibilidadeConsumoPresencialItemUseCase;
import br.com.fiapfood.core.usecases.item.impl.AtualizarDisponibilidadeItemUseCase;
import br.com.fiapfood.core.usecases.item.impl.AtualizarImagemItemUseCase;
import br.com.fiapfood.core.usecases.item.impl.AtualizarNomeItemUseCase;
import br.com.fiapfood.core.usecases.item.impl.AtualizarPrecoItemUseCase;
import br.com.fiapfood.core.usecases.item.impl.BaixarImagemItemUseCase;
import br.com.fiapfood.core.usecases.item.impl.BuscarItemPorIdUseCase;
import br.com.fiapfood.core.usecases.item.impl.BuscarTodosItensUseCase;
import br.com.fiapfood.core.usecases.item.impl.CadastrarItemUseCase;
import br.com.fiapfood.core.usecases.item.interfaces.IAtualizarDescricaoItemUseCase;
import br.com.fiapfood.core.usecases.item.interfaces.IAtualizarDisponibilidadeConsumoPresencialItemUseCase;
import br.com.fiapfood.core.usecases.item.interfaces.IAtualizarDisponibilidadeItemUseCase;
import br.com.fiapfood.core.usecases.item.interfaces.IAtualizarImagemItemUseCase;
import br.com.fiapfood.core.usecases.item.interfaces.IAtualizarNomeItemUseCase;
import br.com.fiapfood.core.usecases.item.interfaces.IAtualizarPrecoItemUseCase;
import br.com.fiapfood.core.usecases.item.interfaces.IBaixarImagemItemUseCase;
import br.com.fiapfood.core.usecases.item.interfaces.IBuscarItemPorIdUseCase;
import br.com.fiapfood.core.usecases.item.interfaces.IBuscarTodosItensUseCase;
import br.com.fiapfood.core.usecases.item.interfaces.ICadastrarItemUseCase;
import br.com.fiapfood.core.usecases.login.impl.AtualizarMatriculaUseCase;
import br.com.fiapfood.core.usecases.login.impl.AtualizarSenhaUseCase;
import br.com.fiapfood.core.usecases.login.impl.ValidarLoginUseCase;
import br.com.fiapfood.core.usecases.login.interfaces.IAtualizarMatriculaUseCase;
import br.com.fiapfood.core.usecases.login.interfaces.IAtualizarSenhaUseCase;
import br.com.fiapfood.core.usecases.login.interfaces.IValidarAcessoUseCase;
import br.com.fiapfood.core.usecases.perfil.impl.AtualizarNomePerfilUseCase;
import br.com.fiapfood.core.usecases.perfil.impl.BuscarPerfilPorIdUseCase;
import br.com.fiapfood.core.usecases.perfil.impl.BuscarTodosPerfisUseCase;
import br.com.fiapfood.core.usecases.perfil.impl.CadastrarPerfilUseCase;
import br.com.fiapfood.core.usecases.perfil.impl.InativarPerfilUseCase;
import br.com.fiapfood.core.usecases.perfil.impl.ReativarPerfilUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.IAtualizarNomePerfilUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.IBuscarPerfilPorIdUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.IBuscarTodosPerfisUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.ICadastrarPerfilUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.IInativarPerfilUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.IReativarPerfilUseCase;
import br.com.fiapfood.core.usecases.restaurante.impl.AtualizarDonoRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.impl.AtualizarEnderecoRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.impl.AtualizarNomeRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.impl.AtualizarTipoCulinariaRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.impl.BuscarRestaurantePorIdUseCase;
import br.com.fiapfood.core.usecases.restaurante.impl.BuscarTodosRestaurantesUseCase;
import br.com.fiapfood.core.usecases.restaurante.impl.CadastrarRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.impl.InativarRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.impl.ReativarRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IAtualizarDonoRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IAtualizarEnderecoRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IAtualizarNomeRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IAtualizarTipoCulinariaRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IBuscarRestaurantePorId;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IBuscarTodosRestaurantesUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.ICadastrarRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IInativarRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IReativarRestauranteUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.impl.AtualizarNomeTipoCulinariaUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.impl.BuscarTipoCulinariaPorIdUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.impl.BuscarTodosTiposCulinariaUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.impl.CadastrarTipoCulinariaUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.IAtualizarNomeTipoCulinariaUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.IBuscarTipoCulinariaPorIdUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.IBuscarTodosTiposCulinariaUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.ICadastrarTipoCulinariaUseCase;
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
import br.com.fiapfood.infraestructure.repositories.interfaces.IPerfilRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.IRestauranteRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.ITipoCulinariaRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.IUsuarioRepository;

@Configuration
public class AppConfig {
	//Controllers
		
	@Bean
	public IPerfilCoreController iPerfilCoreController(IBuscarTodosPerfisUseCase buscarTodosUseCase, IBuscarPerfilPorIdUseCase buscarPorIdUseCase, 
													   ICadastrarPerfilUseCase cadastrarPerfilUseCase, IAtualizarNomePerfilUseCase atualizarNomePerfilUseCase,
													   IInativarPerfilUseCase inativarPerfilUseCase, IReativarPerfilUseCase reativarPerfilUseCase) {
		return new PerfilCoreController(buscarTodosUseCase, buscarPorIdUseCase, cadastrarPerfilUseCase, atualizarNomePerfilUseCase, inativarPerfilUseCase, reativarPerfilUseCase);
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
														 IAtualizarEnderecoUsuarioUseCase atualizarEnderecoUsuarioUseCase,
														 IValidarAcessoUseCase validarLoginUseCase, 
														 IAtualizarSenhaUseCase atualizarSenhaUseCase, 
														 IAtualizarMatriculaUseCase atualizarMatriculaUseCase) {
		return new UsuarioCoreController(buscarUsuarioPorIdUseCase, 
										 buscarTodosUsuariosUseCase, 
										 cadastrarUsuarioUseCase, 
										 atualizarEmailUsuarioUseCase, 
										 atualizarNomeUsuarioUseCase, 
										 inativarUsuarioUseCase,
										 reativarUsuarioUseCase,
										 atualizarPerfilUsuarioUseCase,
										 atualizarEnderecoUsuarioUseCase,
										 validarLoginUseCase,
										 atualizarSenhaUseCase,
										 atualizarMatriculaUseCase);
	}
	
	@Bean
	public IRestauranteCoreController restauranteCoreController(IBuscarRestaurantePorId buscarRestaurantePorId, IBuscarTodosRestaurantesUseCase buscarTodosRestaurantesUseCase, 
																 ICadastrarRestauranteUseCase cadastrarRestauranteUseCase, IReativarRestauranteUseCase reativarRestauranteUseCase,
																 IInativarRestauranteUseCase inativarRestauranteUseCase, IAtualizarDonoRestauranteUseCase atualizarDonoRestauranteUseCase,
																 IAtualizarEnderecoRestauranteUseCase atualizarEnderecoRestauranteUseCase, IAtualizarNomeRestauranteUseCase atualizarNomeRestauranteUseCase,
																 IAtualizarTipoCulinariaRestauranteUseCase atualizarTipoCulinariaRestauranteUseCase,
																 IAtualizarAtendimentoUseCase atualizarAtendimentoUseCase,
																 IAdicionarAtendimentoUseCase adicionarAtendimentoUseCase, 
																 IExcluirAtendimentoUseCase excluirAtendimentoUseCase, IAtualizarDescricaoItemUseCase atualizarDescricaoItemUseCase,
																 IAtualizarNomeItemUseCase atualizarNomeItemUseCase, IAtualizarDisponibilidadeConsumoPresencialItemUseCase atualizarDisponibilidadeConsumoPresencialItemUseCase,
																 IAtualizarDisponibilidadeItemUseCase atualizarDisponibilidadeItemUseCase, IAtualizarImagemItemUseCase atualizarImagemItemUseCase,
																 IAtualizarPrecoItemUseCase atualizarPrecoItemUseCase, IBaixarImagemItemUseCase baixarImagemItemUseCase,
																 IBuscarItemPorIdUseCase buscarItemPorIdUseCase, IBuscarTodosItensUseCase buscarTodosItensUseCase,
																 ICadastrarItemUseCase cadastrarItemUseCase) {
		return new RestauranteCoreController(buscarRestaurantePorId, buscarTodosRestaurantesUseCase, cadastrarRestauranteUseCase, 
											 reativarRestauranteUseCase, inativarRestauranteUseCase, atualizarDonoRestauranteUseCase, atualizarEnderecoRestauranteUseCase,
											 atualizarNomeRestauranteUseCase, atualizarTipoCulinariaRestauranteUseCase,
											 atualizarAtendimentoUseCase, adicionarAtendimentoUseCase, excluirAtendimentoUseCase, atualizarDescricaoItemUseCase,
											 atualizarNomeItemUseCase, atualizarDisponibilidadeConsumoPresencialItemUseCase,
											 atualizarDisponibilidadeItemUseCase, atualizarImagemItemUseCase,
											 atualizarPrecoItemUseCase, baixarImagemItemUseCase,
											 buscarItemPorIdUseCase, buscarTodosItensUseCase, cadastrarItemUseCase);
	}
	
	@Bean
	public ITipoCulinariaCoreController tipoCulinariaCoreController(IBuscarTodosTiposCulinariaUseCase buscarTodosUseCase, 
																	 IBuscarTipoCulinariaPorIdUseCase buscarPorIdUseCase,
																	 ICadastrarTipoCulinariaUseCase cadastrarTipoCulinariaUseCase, 
																	 IAtualizarNomeTipoCulinariaUseCase atualizarNomeTipoCulinariaUseCase) {
		return new TipoCulinariaCoreController(buscarTodosUseCase, buscarPorIdUseCase, cadastrarTipoCulinariaUseCase, atualizarNomeTipoCulinariaUseCase);
	}
		
	// Usecases
	
	@Bean
	public IValidarAcessoUseCase iValidarLoginUseCase(IUsuarioGateway usuarioGateway) {
		return new ValidarLoginUseCase(usuarioGateway);
	}
	
	@Bean
	public IAtualizarMatriculaUseCase iAtualizarMatriculaUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		return new AtualizarMatriculaUseCase(usuarioGateway, perfilGateway);
	}
	
	@Bean
	public IAtualizarSenhaUseCase iAtualizarSenhaUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		return new AtualizarSenhaUseCase(usuarioGateway, perfilGateway);
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
	public IBuscarUsuarioPorIdUseCase iBuscarUsuarioPorIdUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		return new BuscarUsuarioPorIdUseCase(usuarioGateway, perfilGateway);
	}
	
	@Bean
	public IBuscarTodosUsuariosUseCase iBuscarTodosUsuariosUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		return new BuscarTodosUsuariosUseCase(usuarioGateway, perfilGateway);
	}
	
	@Bean
	public ICadastrarUsuarioUseCase iCadastrarUsuarioUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		return new CadastrarUsuarioUseCase(usuarioGateway, perfilGateway);
	}
	
	@Bean
	public IAtualizarPerfilUsuarioUseCase iAtualizarPerfilUsuarioUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		return new AtualizarPerfilUsuarioUseCase(usuarioGateway, perfilGateway);
	}

	@Bean
	public IAtualizarNomeUsuarioUseCase iAtualizarNomeUsuarioUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		return new AtualizarNomeUsuarioUseCase(usuarioGateway, perfilGateway);
	}
	
	@Bean
	public IAtualizarEmailUsuarioUseCase iAtualizarEmailUsuarioUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		return new AtualizarEmailUsuarioUseCase(usuarioGateway, perfilGateway);
	}
	
	@Bean
	public IAtualizarEnderecoUsuarioUseCase iAtualizarEnderecoUsuarioUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		return new AtualizarEnderecoUsuarioUseCase(usuarioGateway, perfilGateway);
	}
		
	@Bean
	public IInativarUsuarioUseCase iInativarUsuarioUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		return new InativarUsuarioUseCase(usuarioGateway, perfilGateway);
	}
	
	@Bean
	public IReativarUsuarioUseCase iReativarUsuarioUseCase(IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		return new ReativarUsuarioUseCase(usuarioGateway, perfilGateway);
	}
	
	@Bean
	public IBuscarRestaurantePorId iBuscarRestaurantePorId(IRestauranteGateway restauranteGateway, IUsuarioGateway usuarioGateway,
														   ITipoCulinariaGateway tipoCulinariaGateway) {
		return new BuscarRestaurantePorIdUseCase(restauranteGateway, usuarioGateway, tipoCulinariaGateway);
	}
	
	@Bean
	public IBuscarTodosRestaurantesUseCase iBuscarTodosRestaurantesUseCase(IRestauranteGateway restauranteGateway, IUsuarioGateway usuarioGateway, 
																		   ITipoCulinariaGateway tipoCulinariaGateway) {
		return new BuscarTodosRestaurantesUseCase(restauranteGateway, usuarioGateway, tipoCulinariaGateway);
	}
	
	@Bean
	public ICadastrarRestauranteUseCase iCadastrarRestauranteUseCase(IRestauranteGateway restauranteGateway, IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		return new CadastrarRestauranteUseCase(restauranteGateway, usuarioGateway, perfilGateway);
	}
	
	@Bean
	public IReativarRestauranteUseCase iReativarRestauranteUseCase(IRestauranteGateway restauranteGateway) {
		return new ReativarRestauranteUseCase(restauranteGateway);
	}
	
	@Bean
	public IInativarRestauranteUseCase iInativarRestauranteUseCase(IRestauranteGateway restauranteGateway) {
		return new InativarRestauranteUseCase(restauranteGateway);
	}
		
	@Bean
	public IAtualizarDonoRestauranteUseCase iAtualizarDonoRestauranteUseCase(IRestauranteGateway restauranteGateway, IUsuarioGateway usuarioGateway, IPerfilGateway perfilGateway) {
		return new AtualizarDonoRestauranteUseCase(restauranteGateway, usuarioGateway, perfilGateway);
	}	
	
	@Bean
	public IAtualizarEnderecoRestauranteUseCase iAtualizarEnderecoRestauranteUseCase(IRestauranteGateway restauranteGateway) {
		return new AtualizarEnderecoRestauranteUseCase(restauranteGateway);
	}
	
	@Bean
	public IAtualizarNomeRestauranteUseCase iAtualizarNomeRestauranteUseCase(IRestauranteGateway restauranteGateway) {
		return new AtualizarNomeRestauranteUseCase(restauranteGateway);
	}
	
	@Bean
	public IAtualizarTipoCulinariaRestauranteUseCase iAtualizarTipoCulinariaRestauranteUseCase(IRestauranteGateway restauranteGateway) {
		return new AtualizarTipoCulinariaRestauranteUseCase(restauranteGateway);
	}

	@Bean
	public ICadastrarPerfilUseCase iCadastrarPerfilUseCase(IPerfilGateway perfilGateway) {
		return new CadastrarPerfilUseCase(perfilGateway);
	}
	
	@Bean
	public IAtualizarNomePerfilUseCase iAtualizarNomePerfilUseCase(IPerfilGateway perfilGateway) {
		return new AtualizarNomePerfilUseCase(perfilGateway);
	}
	
	@Bean
	public IInativarPerfilUseCase iInativarPerfilUseCase(IPerfilGateway perfilGateway, IUsuarioGateway usuarioGateway) {
		return new InativarPerfilUseCase(perfilGateway, usuarioGateway);
	}

	@Bean
	public IReativarPerfilUseCase iReativarPerfilUseCase(IPerfilGateway perfilGateway) {
		return new ReativarPerfilUseCase(perfilGateway);
	}
	
	@Bean
	public IBuscarTipoCulinariaPorIdUseCase iBuscarTipoCulinariaPorIdUseCase(ITipoCulinariaGateway tipoCulinariaGateway) {
		return new BuscarTipoCulinariaPorIdUseCase(tipoCulinariaGateway);
	}
	
	@Bean
	public IBuscarTodosTiposCulinariaUseCase iBuscarTodosTiposCulinariaUseCase(ITipoCulinariaGateway tipoCulinariaGateway) {
		return new BuscarTodosTiposCulinariaUseCase(tipoCulinariaGateway);
	}
	
	@Bean
	public ICadastrarTipoCulinariaUseCase iCadastrarTipoCulinariaUseCase(ITipoCulinariaGateway tipoCulinariaGateway) {
		return new CadastrarTipoCulinariaUseCase(tipoCulinariaGateway);
	}
	
	@Bean
	public IAtualizarNomeTipoCulinariaUseCase iAtualizarNomeTipoCulinariaUseCase(ITipoCulinariaGateway tipoCulinariaGateway) {
		return new AtualizarNomeTipoCulinariaUseCase(tipoCulinariaGateway);
	}
	
	@Bean
	public IBuscarItemPorIdUseCase iBuscarItemPorIdUseCase(IRestauranteGateway restauranteGateway) {
		return new BuscarItemPorIdUseCase(restauranteGateway);
	}
	
	@Bean
	public IBuscarTodosItensUseCase iBuscarTodosItensUseCase(IRestauranteGateway restauranteGateway) {
		return new BuscarTodosItensUseCase(restauranteGateway);
	}
	
	@Bean
	public ICadastrarItemUseCase iCadastrarItemUseCase(IRestauranteGateway restauranteGateway) {
		return new CadastrarItemUseCase(restauranteGateway);
	}
	
	@Bean
	public IAtualizarDescricaoItemUseCase iAtualizarDescricaoItemUseCase(IRestauranteGateway restauranteGateway) {
		return new AtualizarDescricaoItemUseCase(restauranteGateway);
	}
	
	@Bean
	public IAtualizarDisponibilidadeConsumoPresencialItemUseCase iAtualizarDisponibilidadeConsumoPresencialItemUseCase(IRestauranteGateway restauranteGateway) {
		return new AtualizarDisponibilidadeConsumoPresencialItemUseCase(restauranteGateway);
	}
	
	@Bean
	public IAtualizarDisponibilidadeItemUseCase iAtualizarDisponibilidadeItemUseCase(IRestauranteGateway restauranteGateway) {
		return new AtualizarDisponibilidadeItemUseCase(restauranteGateway);
	}
	
	@Bean
	public IAtualizarImagemItemUseCase iAtualizarImagemItemUseCase(IRestauranteGateway restauranteGateway) {
		return new AtualizarImagemItemUseCase(restauranteGateway);
	}
	
	@Bean
	public IAtualizarPrecoItemUseCase iAtualizarPrecoItemUseCase(IRestauranteGateway restauranteGateway) {
		return new AtualizarPrecoItemUseCase(restauranteGateway);
	}
	
	@Bean
	public IAtualizarNomeItemUseCase iAtualizarNomeItemUseCase(IRestauranteGateway restauranteGateway) {
		return new AtualizarNomeItemUseCase(restauranteGateway);
	}
	
	@Bean
	public IBaixarImagemItemUseCase iBaixarImagemItemUseCase(IRestauranteGateway restauranteGateway) {
		return new BaixarImagemItemUseCase(restauranteGateway);
	}
	
	@Bean
	public IAtualizarAtendimentoUseCase iAtualizarAtendimentoUseCase(IRestauranteGateway restauranteGateway) {
		return new AtualizarAtendimentoUseCase(restauranteGateway);
	}

	@Bean
	public IAdicionarAtendimentoUseCase iAdicionarAtendimentoUseCase(IRestauranteGateway restauranteGateway) {
		return new AdicionarAtendimentoUseCase(restauranteGateway);
	}
	
	@Bean
	public IExcluirAtendimentoUseCase iExcluirAtendimentoUseCase(IRestauranteGateway restauranteGateway) {
		return new ExcluirAtendimentoUseCase(restauranteGateway);
	}
	
	// Gateways
	
	@Bean
	public IUsuarioGateway iUsuarioGateway(IUsuarioRepository usuarioRepository) {
		return new UsuarioGateway(usuarioRepository);
	}
	
	@Bean
	public IPerfilGateway iPerfilGateway(IPerfilRepository perfilRepository) {
		return new PerfilGateway(perfilRepository);
	}
	
	@Bean
	public IRestauranteGateway iRestauranteGateway(IRestauranteRepository restauranteRepository) {
		return new RestauranteGateway(restauranteRepository);
	}
	
	@Bean
	public ITipoCulinariaGateway iTipoCulinariaGateway(ITipoCulinariaRepository tipoCulinariaRepository) {
		return new TipoCulinariaGateway(tipoCulinariaRepository);
	}
}