package br.com.fiapfood.core.usecases.integracao.restaurante;

import br.com.fiapfood.core.exceptions.restaurante.AtualizacaoNomeRestauranteNaoPermitidaException;
import br.com.fiapfood.core.gateways.impl.RestauranteGateway;
import br.com.fiapfood.core.gateways.interfaces.IRestauranteGateway;
import br.com.fiapfood.core.usecases.restaurante.impl.AtualizarNomeRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IAtualizarNomeRestauranteUseCase;
import br.com.fiapfood.infraestructure.repositories.interfaces.IRestauranteRepository;
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
public class AtualizarNomeRestauranteUseCaseIT {

    private final String RESTAURANTE_INATIVO = "Não é possível alterar o nome do restaurante, pois ele se encontra inativo.";
    private final String NOME_DUPLICADO = "Não é possível atualizar o nome do restaurante, pois o nome informado é igual ao nome atual do restaurante.";

    private IAtualizarNomeRestauranteUseCase atualizarNomeRestauranteUseCase;
    private IRestauranteGateway restauranteGateway;

    @Autowired
    private IRestauranteRepository restauranteRepository;

    @BeforeEach
    void setUp() {
        restauranteGateway = new RestauranteGateway(restauranteRepository);
        atualizarNomeRestauranteUseCase = new AtualizarNomeRestauranteUseCase(restauranteGateway);
    }

    @Test
    void deveAtualizarNomeDoRestauranteComSucesso() {
        // Arrange
        UUID idRestaurante = UUID.fromString("40d5955e-c0bd-41da-b434-e46fa69bda14");
        String novoNome = "Restaurante Atualizado";

        // Act
        atualizarNomeRestauranteUseCase.atualizar(idRestaurante, novoNome);
        var restauranteAtualizado = restauranteGateway.buscarPorId(idRestaurante);

        // Assert
        assertThat(restauranteAtualizado).isNotNull();
        assertThat(restauranteAtualizado.getNome()).isEqualTo(novoNome);
    }

    @Test
    void deveFalharQuandoRestauranteEstiverInativo() {
        // Arrange
        UUID idRestauranteInativo = UUID.fromString("fc8a9535-d6be-465f-8bf1-d9885e91c91d"); // restaurante inativo
        String novoNome = "Restaurante Atualizado";

        // Act + Assert
        assertThatThrownBy(() -> atualizarNomeRestauranteUseCase.atualizar(idRestauranteInativo, novoNome))
                .isInstanceOf(AtualizacaoNomeRestauranteNaoPermitidaException.class)
                .hasMessage(RESTAURANTE_INATIVO);
    }

    @Test
    void deveFalharQuandoNomeInformadoForIgualAoAtual() {
        // Arrange
        UUID idRestaurante = UUID.fromString("40d5955e-c0bd-41da-b434-e46fa69bda14");
        String nomeAtual = "Restaurante Sabor Brasil";

        // Act + Assert
        assertThatThrownBy(() -> atualizarNomeRestauranteUseCase.atualizar(idRestaurante, nomeAtual))
                .isInstanceOf(AtualizacaoNomeRestauranteNaoPermitidaException.class)
                .hasMessage(NOME_DUPLICADO);
    }
}
