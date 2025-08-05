package br.com.fiapfood.core.usecases.integracao.restaurante;

import br.com.fiapfood.core.exceptions.restaurante.AtualizacaoTipoCulinariaRestauranteNaoPermitidaException;
import br.com.fiapfood.core.gateways.impl.RestauranteGateway;
import br.com.fiapfood.core.gateways.interfaces.IRestauranteGateway;
import br.com.fiapfood.core.usecases.restaurante.impl.AtualizarTipoCulinariaRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IAtualizarTipoCulinariaRestauranteUseCase;
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
public class AtualizarTipoCulinariaRestauranteUseCaseIT {

    private final String RESTAURANTE_INATIVO = "Não é possível alterar o tipo de culinária do restaurante, pois ele se encontra inativo.";
    private final String TIPO_CULINARIA_DUPLICADO = "Não é possível atualizar o tipo de culínaia do restaurante, pois a identificação informada é igual a identificação atual do tipo de culinária do restaurante.";


    private IAtualizarTipoCulinariaRestauranteUseCase atualizarTipoCulinariaRestauranteUseCase;
    private IRestauranteGateway restauranteGateway;

    @Autowired
    private IRestauranteRepository restauranteRepository;

    @BeforeEach
    void setUp() {
        restauranteGateway = new RestauranteGateway(restauranteRepository);
        atualizarTipoCulinariaRestauranteUseCase = new AtualizarTipoCulinariaRestauranteUseCase(restauranteGateway);
    }

    @Test
    void deveAtualizarTipoCulinariaDoRestauranteComSucesso() {
        // Arrange
        UUID idRestaurante = UUID.fromString("40d5955e-c0bd-41da-b434-e46fa69bda14");
        Integer novoTipoCulinaria = 2;

        // Act
        atualizarTipoCulinariaRestauranteUseCase.atualizar(idRestaurante, novoTipoCulinaria);
        var restauranteAtualizado = restauranteGateway.buscarPorId(idRestaurante);

        // Assert
        assertThat(restauranteAtualizado).isNotNull();
        assertThat(restauranteAtualizado.getIdTipoCulinaria()).isEqualTo(novoTipoCulinaria);
    }

    @Test
    void deveLancarExcecaoQuandoRestauranteEstiverInativo() {
        // Arrange
        UUID idRestauranteInativo = UUID.fromString("fc8a9535-d6be-465f-8bf1-d9885e91c91d");
        Integer novoTipoCulinaria = 1;

        // Act + Assert
        assertThatThrownBy(() -> atualizarTipoCulinariaRestauranteUseCase.atualizar(idRestauranteInativo, novoTipoCulinaria))
                .isInstanceOf(AtualizacaoTipoCulinariaRestauranteNaoPermitidaException.class)
                .hasMessage(RESTAURANTE_INATIVO);
    }

    @Test
    void deveLancarExcecaoQuandoTipoCulinariaInformadoForIgualAoAtual() {
        // Arrange
        UUID idRestaurante = UUID.fromString("40d5955e-c0bd-41da-b434-e46fa69bda14");
        Integer tipoCulinariaAtual = 1;

        // Act + Assert
        assertThatThrownBy(() -> atualizarTipoCulinariaRestauranteUseCase.atualizar(idRestaurante, tipoCulinariaAtual))
                .isInstanceOf(AtualizacaoTipoCulinariaRestauranteNaoPermitidaException.class)
                .hasMessage(TIPO_CULINARIA_DUPLICADO);
    }
}
