package br.com.fiapfood.core.usecases.integracao.restaurante;

import br.com.fiapfood.core.exceptions.restaurante.AtualizacaoDonoRestauranteNaoPermitidaException;
import br.com.fiapfood.core.exceptions.restaurante.AtualizacaoEnderecoRestauranteNaoPermitidaException;
import br.com.fiapfood.core.exceptions.restaurante.AtualizacaoNomeRestauranteNaoPermitidaException;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.impl.RestauranteGateway;
import br.com.fiapfood.core.gateways.impl.UsuarioGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IRestauranteGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.restaurante.impl.AtualizarDonoRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IAtualizarDonoRestauranteUseCase;
import br.com.fiapfood.infraestructure.repositories.interfaces.IPerfilRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.IRestauranteRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.IUsuarioRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class AtualizarDonoRestauranteUseCaseIT {

    private final String RESTAURANTE_INATIVO = "Não é possível alterar o dono do restaurante, pois ele se encontra inativo.";
    private final String USUARIO_DONO = "Não é possível cadastrar o restaurante, pois o responsável não possui o perfil de dono.";
    private final String USUARIO_INATIVO = "Não é possível cadastrar o restaurante, pois o responsável se encontra inativo.";
    private final String USUARIO_DUPLICADO = "Não é possível atualizar o dono do restaurante, pois o identificação informada é igual a identificação atual do dono do restaurante.";

    private IAtualizarDonoRestauranteUseCase atualizarDonoRestauranteUseCase;
    private IRestauranteGateway restauranteGateway;
    private IUsuarioGateway usuarioGateway;
    private IPerfilGateway perfilGateway;

    @Autowired
    private IRestauranteRepository restauranteRepository;
    @Autowired
    private IUsuarioRepository usuarioRepository;
    @Autowired
    private IPerfilRepository perfilRepository;

    @BeforeEach
    void setUp() {
        restauranteGateway = new RestauranteGateway(restauranteRepository);
        usuarioGateway = new UsuarioGateway(usuarioRepository);
        perfilGateway = new PerfilGateway(perfilRepository);

        atualizarDonoRestauranteUseCase = new AtualizarDonoRestauranteUseCase(restauranteGateway, usuarioGateway, perfilGateway);
    }

    @Test
    void deveAtualizarDonoRestauranteComSucesso() {
        // Arrange
        UUID idRestaurante = UUID.fromString("40d5955e-c0bd-41da-b434-e46fa69bda14");
        UUID idDono = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");

        // Act
        atualizarDonoRestauranteUseCase.atualizar(idRestaurante, idDono);
        var restauranteAposAtualizar = restauranteGateway.buscarPorId(idRestaurante);

        // Assert
        assertThat(restauranteAposAtualizar).isNotNull();
        assertThat(restauranteAposAtualizar.getId()).isEqualTo(idRestaurante);
        assertThat(restauranteAposAtualizar.getIdDonoRestaurante()).isEqualTo(idDono);
    }

    @Test
    void deveLancarExcecaoSeRestauranteEstiverInativo() {
        // Arrange
        UUID idRestauranteInativo = UUID.fromString("fc8a9535-d6be-465f-8bf1-d9885e91c91d");
        UUID idDono = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");

        // Act + Assert
        assertThatThrownBy(() -> atualizarDonoRestauranteUseCase.atualizar(idRestauranteInativo, idDono))
                .isInstanceOf(AtualizacaoEnderecoRestauranteNaoPermitidaException.class)
                .hasMessage(RESTAURANTE_INATIVO);
    }

    @Test
    void deveFalharQuandoUsuarioNaoPossuirPerfilDono() {
        // Arrange
        UUID idRestaurante = UUID.fromString("40d5955e-c0bd-41da-b434-e46fa69bda14");
        UUID idUsuarioCliente = UUID.fromString("b48bc2dc-fd87-462d-a8a6-6e74674d0338");

        // Act + Assert
        assertThatThrownBy(() -> atualizarDonoRestauranteUseCase.atualizar(idRestaurante, idUsuarioCliente))
                .isInstanceOf(AtualizacaoDonoRestauranteNaoPermitidaException.class)
                .hasMessage(USUARIO_DONO);
    }

    @Test
    void deveFalharQuandoUsuarioEstiverInativo() {
        // Arrange
        UUID idRestaurante = UUID.fromString("40d5955e-c0bd-41da-b434-e46fa69bda14");
        UUID idUsuarioInativo = UUID.fromString("602a4056-68d0-44f0-8284-14b0cf7a75b6");

        // Act + Assert
        assertThatThrownBy(() -> atualizarDonoRestauranteUseCase.atualizar(idRestaurante, idUsuarioInativo))
                .isInstanceOf(AtualizacaoDonoRestauranteNaoPermitidaException.class)
                .hasMessage(USUARIO_INATIVO);
    }

    @Test
    void deveFalharQuandoNovoDonoEhIgualAoDonoAtual() {
        // Arrange
        UUID idRestaurante = UUID.fromString("a72181a6-7699-4686-a5ec-1a0431764e62");
        UUID idDonoAtual = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");

        // Act + Assert
        assertThatThrownBy(() -> atualizarDonoRestauranteUseCase.atualizar(idRestaurante, idDonoAtual))
                .isInstanceOf(AtualizacaoNomeRestauranteNaoPermitidaException.class)
                .hasMessage(USUARIO_DUPLICADO);
    }
}
