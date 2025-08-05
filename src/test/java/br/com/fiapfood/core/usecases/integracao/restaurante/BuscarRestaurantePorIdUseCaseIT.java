package br.com.fiapfood.core.usecases.integracao.restaurante;

import br.com.fiapfood.core.exceptions.restaurante.RestauranteNaoEncontradoException;
import br.com.fiapfood.core.gateways.impl.RestauranteGateway;
import br.com.fiapfood.core.gateways.impl.TipoCulinariaGateway;
import br.com.fiapfood.core.gateways.impl.UsuarioGateway;
import br.com.fiapfood.core.gateways.interfaces.IRestauranteGateway;
import br.com.fiapfood.core.gateways.interfaces.ITipoCulinariaGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.restaurante.impl.BuscarRestaurantePorIdUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IBuscarRestaurantePorId;
import br.com.fiapfood.infraestructure.repositories.interfaces.IRestauranteRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.ITipoCulinariaRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.IUsuarioRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BuscarRestaurantePorIdUseCaseIT {

    private final String RESTAURANTE_NAO_ENCONTRADO = "Não foi encontrado nenhum restaurante com o id informado.";

    private IBuscarRestaurantePorId buscarRestaurantePorId;
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
        buscarRestaurantePorId = new BuscarRestaurantePorIdUseCase(restauranteGateway, usuarioGateway, tipoCulinariaGateway);
    }

    @Test
    void deveBuscarRestaurantePorIdComSucesso() {
        // Arrange
        UUID idRestaurante = UUID.fromString("40d5955e-c0bd-41da-b434-e46fa69bda14");

        // Act
        var restaurante = buscarRestaurantePorId.buscar(idRestaurante);

        // Assert
        assertThat(restaurante).isNotNull();
        assertThat(restaurante.id()).isEqualTo(idRestaurante);
        assertThat(restaurante.nome()).isEqualTo("Restaurante Sabor Brasil");
        assertThat(restaurante.dono().nome()).isEqualTo("Rafael Santos");
        assertThat(restaurante.tipoCulinaria().nome()).isEqualTo("Brasileira");
    }

    @Test
    void deveLancarExcecaoQuandoRestauranteNaoForEncontrado() {
        // Arrange
        UUID idInexistente = UUID.fromString("11111111-1111-1111-1111-111111111111");

        // Act + Assert
        assertThatThrownBy(() -> buscarRestaurantePorId.buscar(idInexistente))
                .isInstanceOf(RestauranteNaoEncontradoException.class)
                .hasMessage(RESTAURANTE_NAO_ENCONTRADO);
    }
}
