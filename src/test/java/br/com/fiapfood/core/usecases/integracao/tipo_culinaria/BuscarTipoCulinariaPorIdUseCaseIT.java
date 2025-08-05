package br.com.fiapfood.core.usecases.integracao.tipo_culinaria;

import br.com.fiapfood.core.exceptions.tipo_culinaria.TipoCulinariaInvalidoException;
import br.com.fiapfood.core.gateways.impl.TipoCulinariaGateway;
import br.com.fiapfood.core.gateways.interfaces.ITipoCulinariaGateway;
import br.com.fiapfood.core.usecases.tipo_culinaria.impl.BuscarTipoCulinariaPorIdUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.IBuscarTipoCulinariaPorIdUseCase;
import br.com.fiapfood.infraestructure.repositories.interfaces.ITipoCulinariaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BuscarTipoCulinariaPorIdUseCaseIT {
    private final String TIPO_CULINARIA_NAO_ENCONTRADO = "Não foi encontrado nenhum tipo de culinária com o id informado.";

    private IBuscarTipoCulinariaPorIdUseCase buscarTipoCulinariaPorIdUseCase;
    private ITipoCulinariaGateway tipoCulinariaGateway;

    @Autowired
    private ITipoCulinariaRepository tipoCulinariaRepository;

    @BeforeEach
    public void setUp() {
        tipoCulinariaGateway = new TipoCulinariaGateway(tipoCulinariaRepository);

        buscarTipoCulinariaPorIdUseCase = new BuscarTipoCulinariaPorIdUseCase(tipoCulinariaGateway);
    }

    @DisplayName("Deve buscar tipo culinária por ID com sucesso.")
    @Test
    void deveBuscarTipoCulinariaPorIdComSucesso() {
        // Arrange
        List<String> tiposCulinariaRegistradosEmOrdemDeId = List.of(
                "Brasileira",
                "Italiana",
                "Japonesa",
                "Churrasco",
                "Vegetariana"
        );

        for (int i = 0; i < tiposCulinariaRegistradosEmOrdemDeId.size(); i++) {
            // Act
            var tipoCulinaria = buscarTipoCulinariaPorIdUseCase.buscar(i + 1);

            // Assert
            assertThat(tipoCulinaria.nome()).isEqualTo(tiposCulinariaRegistradosEmOrdemDeId.get(i));
            assertThat(tipoCulinaria.id()).isEqualTo(i + 1);
        }
    }

    @DisplayName("Deve buscar tipo culinária por ID com erro. Tipo culinária não encontrado através do ID.")
    @Test
    void deveLancarExcecaoSeTiPoCulinariaNaoEncontradoAtravesDoID() {
        // Arrange
        int tipoCulinariaId = 10;

        // Act & Assert
        assertThatThrownBy(() -> buscarTipoCulinariaPorIdUseCase.buscar(tipoCulinariaId))
                .isInstanceOf(TipoCulinariaInvalidoException.class)
                .hasMessage(TIPO_CULINARIA_NAO_ENCONTRADO);
    }
}
