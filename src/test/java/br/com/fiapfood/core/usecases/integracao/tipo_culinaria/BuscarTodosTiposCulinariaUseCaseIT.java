package br.com.fiapfood.core.usecases.integracao.tipo_culinaria;

import br.com.fiapfood.core.exceptions.tipo_culinaria.TipoCulinariaNaoEncontradoException;
import br.com.fiapfood.core.gateways.impl.TipoCulinariaGateway;
import br.com.fiapfood.core.gateways.interfaces.ITipoCulinariaGateway;
import br.com.fiapfood.core.usecases.tipo_culinaria.impl.BuscarTodosTiposCulinariaUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.IBuscarTodosTiposCulinariaUseCase;
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
public class BuscarTodosTiposCulinariaUseCaseIT {
    private final String TIPOS_CULINARIA_NAO_ENCONTRADOS = "Não foi encontrado nenhum tipo de culinária na base de dados.";

    private IBuscarTodosTiposCulinariaUseCase buscarTodosTiposCulinariaUseCase;
    private ITipoCulinariaGateway tipoCulinariaGateway;

    @Autowired
    private ITipoCulinariaRepository tipoCulinariaRepository;

    @BeforeEach
    public void setUp() {
        tipoCulinariaGateway = new TipoCulinariaGateway(tipoCulinariaRepository);

        buscarTodosTiposCulinariaUseCase = new BuscarTodosTiposCulinariaUseCase(tipoCulinariaGateway);
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

        // Act
        var tiposCulinaria = buscarTodosTiposCulinariaUseCase.buscar();

        for (int i = 0; i < tiposCulinariaRegistradosEmOrdemDeId.size(); i++) {
            // Assert
            assertThat(tiposCulinaria.get(i).nome()).isEqualTo(tiposCulinariaRegistradosEmOrdemDeId.get(i));
        }
    }

    @DisplayName("Deve buscar tipo culinária por ID com erro. Tipo culinária não encontrado através do ID.")
    @Test
    @Sql(scripts = "/db_clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // Limpando o BD para retornar o erro
    void deveLancarExcecaoSeTiPoCulinariaNaoEncontradoAtravesDoID() {
        // Arrange

        // Act & Assert
        assertThatThrownBy(() -> buscarTodosTiposCulinariaUseCase.buscar())
                .isInstanceOf(TipoCulinariaNaoEncontradoException.class)
                .hasMessage(TIPOS_CULINARIA_NAO_ENCONTRADOS);
    }
}
