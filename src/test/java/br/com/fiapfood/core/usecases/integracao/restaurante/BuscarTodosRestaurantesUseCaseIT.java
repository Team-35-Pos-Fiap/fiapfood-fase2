package br.com.fiapfood.core.usecases.integracao.restaurante;

import br.com.fiapfood.core.entities.Restaurante;
import br.com.fiapfood.core.entities.dto.restaurante.DadosRestauranteCoreDto;
import br.com.fiapfood.core.entities.dto.restaurante.RestaurantePaginacaoCoreDto;
import br.com.fiapfood.core.exceptions.restaurante.RestauranteNaoEncontradoException;
import br.com.fiapfood.core.gateways.impl.RestauranteGateway;
import br.com.fiapfood.core.gateways.impl.TipoCulinariaGateway;
import br.com.fiapfood.core.gateways.impl.UsuarioGateway;
import br.com.fiapfood.core.gateways.interfaces.IRestauranteGateway;
import br.com.fiapfood.core.gateways.interfaces.ITipoCulinariaGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.restaurante.impl.BuscarTodosRestaurantesUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IBuscarTodosRestaurantesUseCase;
import br.com.fiapfood.infraestructure.repositories.interfaces.IRestauranteRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.ITipoCulinariaRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.IUsuarioRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BuscarTodosRestaurantesUseCaseIT {

    private final String RESTAURANTES_NAO_ENCONTRADOS = "Não foram encontrados restaurantes na base de dados para a página informada.";

    private IBuscarTodosRestaurantesUseCase buscarTodosRestaurantesUseCase;
    private IRestauranteGateway restauranteGateway;
    private IUsuarioGateway usuarioGateway;
    private ITipoCulinariaGateway tipoCulinariaGateway;

    @Autowired
    private IRestauranteRepository restauranteRepository;
    @Autowired
    private IUsuarioRepository usuarioRepository;
    @Autowired
    private ITipoCulinariaRepository tipoCulinariaRepository;

    @BeforeEach
    void setUp() {
        restauranteGateway = new RestauranteGateway(restauranteRepository);
        usuarioGateway = new UsuarioGateway(usuarioRepository);
        tipoCulinariaGateway = new TipoCulinariaGateway(tipoCulinariaRepository);

        buscarTodosRestaurantesUseCase = new BuscarTodosRestaurantesUseCase(restauranteGateway, usuarioGateway, tipoCulinariaGateway);
    }

    @Test
    void deveBuscarTodosOsRestaurantesComSucesso() {
        // Arrange
        Integer pagina = 1;

        // Act
        RestaurantePaginacaoCoreDto resultado = buscarTodosRestaurantesUseCase.buscar(pagina);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.restaurantes().isEmpty()).isFalse();
        assertThat(resultado.paginacao().paginaAtual()).isEqualTo(pagina);

        DadosRestauranteCoreDto restaurante = resultado.restaurantes().getFirst();
        assertThat(restaurante.nome()).isNotBlank();
        assertThat(restaurante.dono()).isNotNull();
        assertThat(restaurante.tipoCulinaria()).isNotNull();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremRestaurantesNaPagina() {
        // Arrange
        Integer pagina = 99;

        // Act + Assert
        assertThatThrownBy(() -> buscarTodosRestaurantesUseCase.buscar(pagina))
                .isInstanceOf(RestauranteNaoEncontradoException.class)
                .hasMessage(RESTAURANTES_NAO_ENCONTRADOS);
    }
}
