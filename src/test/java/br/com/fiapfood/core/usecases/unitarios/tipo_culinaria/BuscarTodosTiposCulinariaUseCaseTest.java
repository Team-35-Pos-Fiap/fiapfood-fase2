package br.com.fiapfood.core.usecases.unitarios.tipo_culinaria;

import br.com.fiapfood.core.exceptions.tipo_culinaria.TipoCulinariaNaoEncontradoException;
import br.com.fiapfood.core.gateways.interfaces.ITipoCulinariaGateway;
import br.com.fiapfood.core.usecases.tipo_culinaria.impl.BuscarTodosTiposCulinariaUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.IBuscarTodosTiposCulinariaUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static br.com.fiapfood.utils.CoreEntityDataGenerator.coreTipoCulinariaEntityValido;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class BuscarTodosTiposCulinariaUseCaseTest {
    private final String TIPOS_CULINARIA_NAO_ENCONTRADOS = "Não foi encontrado nenhum tipo de culinária na base de dados.";

    @Mock
    private ITipoCulinariaGateway tipoCulinariaGateway;
    private IBuscarTodosTiposCulinariaUseCase buscarTodosTiposCulinariaUseCase;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        buscarTodosTiposCulinariaUseCase = new BuscarTodosTiposCulinariaUseCase(tipoCulinariaGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @DisplayName("Deve buscar todos tipo culinária com sucesso.")
    @Test
    void deveBuscarTodosTipoCulinariaComSucesso() {
        // Arrange
        var tipoCulinariaRetornadoDoGateway = List.of(
                coreTipoCulinariaEntityValido(),
                coreTipoCulinariaEntityValido(),
                coreTipoCulinariaEntityValido(),
                coreTipoCulinariaEntityValido()
                );

        when(tipoCulinariaGateway.buscarTodos()).thenReturn(tipoCulinariaRetornadoDoGateway);

        // Act
        var todosTipoCulinaria = buscarTodosTiposCulinariaUseCase.buscar();

        // Assert
        verify(tipoCulinariaGateway, times(1)).buscarTodos();
        assertThat(todosTipoCulinaria.size()).isEqualTo(tipoCulinariaRetornadoDoGateway.size());
    }

    @DisplayName("Deve buscar todos tipo culinária com erro. Nenhum tipo culinária encontrado.")
    @Test
    void deveLancarExcecaoSeTiPoCulinariaNaoEncontradoAtravesDoID() {
        // Arrange

        when(tipoCulinariaGateway.buscarTodos()).thenThrow(new TipoCulinariaNaoEncontradoException(TIPOS_CULINARIA_NAO_ENCONTRADOS));

        // Act & Assert
        assertThatThrownBy(() -> buscarTodosTiposCulinariaUseCase.buscar())
                .isInstanceOf(TipoCulinariaNaoEncontradoException.class)
                .hasMessage(TIPOS_CULINARIA_NAO_ENCONTRADOS);
        verify(tipoCulinariaGateway, times(1)).buscarTodos();
    }
}
