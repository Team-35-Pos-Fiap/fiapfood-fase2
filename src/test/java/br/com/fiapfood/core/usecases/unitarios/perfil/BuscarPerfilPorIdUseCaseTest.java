package br.com.fiapfood.core.usecases.unitarios.perfil;

import br.com.fiapfood.core.exceptions.perfil.PerfilInvalidoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.usecases.perfil.impl.BuscarPerfilPorIdUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.IBuscarPerfilPorIdUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static br.com.fiapfood.utils.CoreEntityDataGenerator.corePerfilEntityValido;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class BuscarPerfilPorIdUseCaseTest {

    private final String PERFIS_NAO_ENCONTRADOS = "Não foi encontrado nenhum perfil com o id informado.";

    @Mock
    private IPerfilGateway perfilGateway;
    private IBuscarPerfilPorIdUseCase buscarPerfilPorIdUseCase;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        buscarPerfilPorIdUseCase = new BuscarPerfilPorIdUseCase(perfilGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @DisplayName("Deve buscar perfil por id com sucesso.")
    @Test
    void deveBuscarPerfilPorIdComSucesso() {
        // Arrange
        int perfilId = 1;
        var perfilRetornado = corePerfilEntityValido();

        when(perfilGateway.buscarPorId(anyInt())).thenReturn(perfilRetornado);

        // Act
        var perfil = buscarPerfilPorIdUseCase.buscar(perfilId);

        // Assert
        assertThat(perfil.id()).isEqualTo(perfilId);
        verify(perfilGateway, times(1)).buscarPorId(anyInt());
    }

    @DisplayName("Deve buscar perfil por id com erro. Nenhum perfil encontrado através do ID.")
    @Test
    void deveLancarExcecaoSeNenhumPerfilEncontradoPorId() {
        // Arrange
        int perfilId = 1;

        when(perfilGateway.buscarPorId(anyInt())).thenThrow(new PerfilInvalidoException(PERFIS_NAO_ENCONTRADOS));

        //Act & Assert
        assertThatThrownBy(() -> buscarPerfilPorIdUseCase.buscar(perfilId))
                .isInstanceOf(PerfilInvalidoException.class)
                .hasMessage(PERFIS_NAO_ENCONTRADOS);
        verify(perfilGateway, times(1)).buscarPorId(anyInt());
    }
}
