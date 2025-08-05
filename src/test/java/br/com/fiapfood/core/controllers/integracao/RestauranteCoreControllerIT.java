package br.com.fiapfood.core.controllers.integracao;

import br.com.fiapfood.core.controllers.impl.RestauranteCoreController;
import br.com.fiapfood.core.controllers.interfaces.IRestauranteCoreController;
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
import br.com.fiapfood.core.usecases.item.impl.*;
import br.com.fiapfood.core.usecases.item.interfaces.*;
import br.com.fiapfood.core.usecases.restaurante.impl.*;
import br.com.fiapfood.core.usecases.restaurante.interfaces.*;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.CadastrarRestauranteDto;
import br.com.fiapfood.infraestructure.repositories.interfaces.IPerfilRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.IRestauranteRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.ITipoCulinariaRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.IUsuarioRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static br.com.fiapfood.utils.DtoDataGenerator.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RestauranteCoreControllerIT {

    private IRestauranteCoreController restauranteCoreController;

    @Autowired
    IRestauranteRepository restauranteRepository;

    @Autowired
    IUsuarioRepository usuarioRepository;

    @Autowired
    ITipoCulinariaRepository tipoCulinariaRepository;

    @Autowired
    IPerfilRepository perfilRepository;


    @BeforeEach
    void setUp() {
        IRestauranteGateway restauranteGateway = new RestauranteGateway(restauranteRepository);
        IUsuarioGateway usuarioGateway = new UsuarioGateway(usuarioRepository);
        ITipoCulinariaGateway tipoCulinariaGateway = new TipoCulinariaGateway(tipoCulinariaRepository);
        IPerfilGateway perfilGateway = new PerfilGateway(perfilRepository);

        IBuscarRestaurantePorId buscarRestaurantePorId =
                new BuscarRestaurantePorIdUseCase(restauranteGateway, usuarioGateway, tipoCulinariaGateway);
        
        IBuscarTodosRestaurantesUseCase buscarTodosRestaurantesUseCase =
                new BuscarTodosRestaurantesUseCase(restauranteGateway, usuarioGateway, tipoCulinariaGateway);
        
        ICadastrarRestauranteUseCase cadastrarRestauranteUseCase =
                new CadastrarRestauranteUseCase(restauranteGateway, usuarioGateway, perfilGateway);

        IReativarRestauranteUseCase reativarRestauranteUseCase =
                new ReativarRestauranteUseCase(restauranteGateway);
        
        IInativarRestauranteUseCase inativarRestauranteUseCase =
                new InativarRestauranteUseCase(restauranteGateway);

        IAtualizarDonoRestauranteUseCase atualizarDonoRestauranteUseCase =
                new AtualizarDonoRestauranteUseCase(restauranteGateway, usuarioGateway, perfilGateway);
        
        IAtualizarEnderecoRestauranteUseCase atualizarEnderecoRestauranteUseCase =
                new AtualizarEnderecoRestauranteUseCase(restauranteGateway);
        
        IAtualizarNomeRestauranteUseCase atualizarNomeRestauranteUseCase =
                new AtualizarNomeRestauranteUseCase(restauranteGateway);

        IAtualizarTipoCulinariaRestauranteUseCase atualizarTipoCulinariaRestauranteUseCase =
                new AtualizarTipoCulinariaRestauranteUseCase(restauranteGateway);
        
        IAtualizarAtendimentoUseCase atualizarAtendimentoUseCase =
                new AtualizarAtendimentoUseCase(restauranteGateway);
        
        IAdicionarAtendimentoUseCase adicionarAtendimentoUseCase =
                new AdicionarAtendimentoUseCase(restauranteGateway);
        
        IExcluirAtendimentoUseCase excluirAtendimentoUseCase =
                new ExcluirAtendimentoUseCase(restauranteGateway);
        
        IAtualizarDescricaoItemUseCase atualizarDescricaoItemUseCase =
                new AtualizarDescricaoItemUseCase(restauranteGateway);
        
        IAtualizarNomeItemUseCase atualizarNomeItemUseCase =
                new AtualizarNomeItemUseCase(restauranteGateway);
        
        IAtualizarDisponibilidadeConsumoPresencialItemUseCase atualizarDisponibilidadeConsumoPresencialItemUseCase =
                new AtualizarDisponibilidadeConsumoPresencialItemUseCase(restauranteGateway);
        
        IAtualizarDisponibilidadeItemUseCase atualizarDisponibilidadeItemUseCase =
                new AtualizarDisponibilidadeItemUseCase(restauranteGateway);
        
        IAtualizarImagemItemUseCase atualizarImagemItemUseCase =
                new AtualizarImagemItemUseCase(restauranteGateway);
        
        IAtualizarPrecoItemUseCase atualizarPrecoItemUseCase =
                new AtualizarPrecoItemUseCase(restauranteGateway);
        
        IBaixarImagemItemUseCase baixarImagemItemUseCase =
                new BaixarImagemItemUseCase(restauranteGateway);
        
        IBuscarItemPorIdUseCase buscarItemPorIdUseCase =
                new BuscarItemPorIdUseCase(restauranteGateway);
        
        IBuscarTodosItensUseCase buscarTodosItensUseCase =
                new BuscarTodosItensUseCase(restauranteGateway);
        
        ICadastrarItemUseCase cadastrarItemUseCase =
                new CadastrarItemUseCase(restauranteGateway);

        restauranteCoreController = new RestauranteCoreController(
                buscarRestaurantePorId,
                buscarTodosRestaurantesUseCase,
                cadastrarRestauranteUseCase,
                reativarRestauranteUseCase,
                inativarRestauranteUseCase,
                atualizarDonoRestauranteUseCase,
                atualizarEnderecoRestauranteUseCase,
                atualizarNomeRestauranteUseCase,
                atualizarTipoCulinariaRestauranteUseCase,
                atualizarAtendimentoUseCase,
                adicionarAtendimentoUseCase,
                excluirAtendimentoUseCase,
                atualizarDescricaoItemUseCase,
                atualizarNomeItemUseCase,
                atualizarDisponibilidadeConsumoPresencialItemUseCase,
                atualizarDisponibilidadeItemUseCase,
                atualizarImagemItemUseCase,
                atualizarPrecoItemUseCase,
                baixarImagemItemUseCase,
                buscarItemPorIdUseCase,
                buscarTodosItensUseCase,
                cadastrarItemUseCase
        );
    }

    @Nested
    @Transactional
    class GerenciarRestauranteRequest {

        @Test
        @DisplayName("Deve buscar restaurantes com paginação")
        void deveBuscarRestaurantesComSucesso() throws Exception {
            // Arrange

            // Act
            var restaurantes = restauranteCoreController.buscarTodos(1);

            // Assert
            assertThat(restaurantes).isNotNull();
            assertThat(restaurantes.restaurantes()).isNotNull();
            assertThat(restaurantes.paginacao()).isNotNull();
        }

        @Test
        @DisplayName("Deve buscar restaurante por id")
        void deveBuscarRestaurantePorIdComSucesso() throws Exception {
            // Arrange
            UUID id = UUID.fromString("a72181a6-7699-4686-a5ec-1a0431764e62");

            // Act
            var restaurantes = restauranteCoreController.buscarPorId(id);

            // Assert
            assertThat(restaurantes).isNotNull();
            assertThat(restaurantes.id()).isEqualTo(id);
        }

//        @Test
//        @DisplayName("Deve cadastrar restaurante")
//        void deveCadastrarRestauranteComSucesso() throws Exception {
//            // Arrange
//            CadastrarRestauranteDto cadastrarRestauranteDto = cadastrarRestauranteDtoValido();
//
//            // Act
//            var restaurantesBefore = restauranteCoreController.buscarTodos(1);
//            restauranteCoreController.cadastrar(cadastrarRestauranteDto);
//            var restaurantesAfter = restauranteCoreController.buscarTodos(1);
//
//            // Assert
//            assertThat(restaurantesBefore.restaurantes().size()).isLessThan(restaurantesAfter.restaurantes().size());
//        }

        @Test
        @DisplayName("Deve inativar restaurante")
        void deveInativarRestauranteComSucesso() throws Exception {
            // Arrange
            UUID idRestauranteAtivo = UUID.fromString("a72181a6-7699-4686-a5ec-1a0431764e62");

            // Act
            restauranteCoreController.inativar(idRestauranteAtivo);
            var restaurantes = restauranteCoreController.buscarPorId(idRestauranteAtivo);

            // Assert
            assertThat(restaurantes.isAtivo()).isEqualTo(false);
        }

        @Test
        @DisplayName("Deve reativar restaurante")
        void deveReativarRestauranteComSucesso() throws Exception {
            // Arrange
            UUID idRestauranteInativo = UUID.fromString("fc8a9535-d6be-465f-8bf1-d9885e91c91d");

            // Act
            restauranteCoreController.reativar(idRestauranteInativo);
            var restaurantes = restauranteCoreController.buscarPorId(idRestauranteInativo);

            // Assert
            assertThat(restaurantes.isAtivo()).isEqualTo(true);
        }

    }
}
