package br.com.fiapfood.core.usecases.unitarios.perfil;

import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.exceptions.perfil.PerfilNaoEncontradoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.usecases.perfil.impl.BuscarTodosPerfisUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.IBuscarTodosPerfisUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static br.com.fiapfood.utils.CoreEntityDataGenerator.corePerfilEntityValido;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class BuscarTodosPerfisUseCaseTest {
    private final String PERFIL_NAO_ENCONTRADO = "Não foi encontrado nenhum perfil na base de dados.";

    @Mock
    private IPerfilGateway perfilGateway;
    private IBuscarTodosPerfisUseCase buscarTodosPerfisUseCase;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        buscarTodosPerfisUseCase = new BuscarTodosPerfisUseCase(perfilGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @DisplayName("Deve buscar todos perfis com sucesso.")
    @Test
    void deveBuscarTodosPerfisComSucesso() {
        // Arrange
        List<Perfil> perfisRetorandos = List.of(
                corePerfilEntityValido(),
                corePerfilEntityValido(),
                corePerfilEntityValido()
        );

        when(perfilGateway.buscarTodos()).thenReturn(perfisRetorandos);

        // Act
        var perfis = buscarTodosPerfisUseCase.buscar();

        // Assert
        assertThat(perfis.size()).isEqualTo(perfisRetorandos.size());
        verify(perfilGateway, times(1)).buscarTodos();
    }

    @DisplayName("Deve buscar todos perfis com erro. Nenhum perfil encontrado no banco de dados.")
    @Test
    void deveLancarExcecaoSeNenhumPerfilEncontrado() {
        // Arrange
        when(perfilGateway.buscarTodos()).thenThrow(new PerfilNaoEncontradoException(PERFIL_NAO_ENCONTRADO));

        //Act & Assert
        assertThatThrownBy(() -> buscarTodosPerfisUseCase.buscar())
                .isInstanceOf(PerfilNaoEncontradoException.class)
                .hasMessage(PERFIL_NAO_ENCONTRADO);
        verify(perfilGateway, times(1)).buscarTodos();
    }
}
