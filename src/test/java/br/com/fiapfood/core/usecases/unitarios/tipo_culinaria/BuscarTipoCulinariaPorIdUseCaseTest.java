package br.com.fiapfood.core.usecases.unitarios.tipo_culinaria;

import br.com.fiapfood.core.exceptions.tipo_culinaria.TipoCulinariaInvalidoException;
import br.com.fiapfood.core.gateways.interfaces.ITipoCulinariaGateway;
import br.com.fiapfood.core.usecases.tipo_culinaria.impl.BuscarTipoCulinariaPorIdUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.IBuscarTipoCulinariaPorIdUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static br.com.fiapfood.utils.CoreEntityDataGenerator.coreTipoCulinariaEntityValido;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class BuscarTipoCulinariaPorIdUseCaseTest {
    private final String TIPO_CULINARIA_NAO_ENCONTRADO = "Não foi encontrado nenhum tipo de culinária com o id informado.";

    @Mock
    private ITipoCulinariaGateway tipoCulinariaGateway;
    private IBuscarTipoCulinariaPorIdUseCase buscarTipoCulinariaPorIdUseCase;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        buscarTipoCulinariaPorIdUseCase = new BuscarTipoCulinariaPorIdUseCase(tipoCulinariaGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @DisplayName("Deve buscar tipo culinária por ID com sucesso.")
    @Test
    void deveBuscarTipoCulinariaPorIdComSucesso() {
        // Arrange
        int tipoCulinariaId = 1;
        var tipoCulinariaRetornadoDoGateway = coreTipoCulinariaEntityValido();

        when(tipoCulinariaGateway.buscarPorId(anyInt())).thenReturn(tipoCulinariaRetornadoDoGateway);

        // Act
        var tipoCulinaria = buscarTipoCulinariaPorIdUseCase.buscar(tipoCulinariaId);

        // Assert
        verify(tipoCulinariaGateway, times(1)).buscarPorId(anyInt());
        assertThat(tipoCulinariaRetornadoDoGateway.getNome()).isEqualTo(tipoCulinaria.nome());
        assertThat(tipoCulinariaRetornadoDoGateway.getId()).isEqualTo(tipoCulinaria.id());
    }

    @DisplayName("Deve buscar tipo culinária por ID com erro. Tipo culinária não encontrado através do ID.")
    @Test
    void deveLancarExcecaoSeTiPoCulinariaNaoEncontradoAtravesDoID() {
        // Arrange
        int tipoCulinariaId = 1;

        when(tipoCulinariaGateway.buscarPorId(anyInt())).thenThrow(new TipoCulinariaInvalidoException(TIPO_CULINARIA_NAO_ENCONTRADO));

        // Act & Assert
        assertThatThrownBy(() -> buscarTipoCulinariaPorIdUseCase.buscar(tipoCulinariaId))
                .isInstanceOf(TipoCulinariaInvalidoException.class)
                .hasMessage(TIPO_CULINARIA_NAO_ENCONTRADO);
        verify(tipoCulinariaGateway, times(1)).buscarPorId(anyInt());
    }
}
