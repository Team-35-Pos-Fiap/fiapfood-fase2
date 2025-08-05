package br.com.fiapfood.core.usecases.unitarios.perfil;

import br.com.fiapfood.core.entities.dto.perfil.PerfilCoreDto;
import br.com.fiapfood.core.exceptions.perfil.PerfilInvalidoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.usecases.perfil.impl.ReativarPerfilUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.IReativarPerfilUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static br.com.fiapfood.utils.CoreEntityDataGenerator.corePerfilEntityValido;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class ReativarPerfilUseCaseTest {

    private final String PERFIS_NAO_ENCONTRADOS = "Não foi encontrado nenhum perfil com o id informado.";

    @Mock
    private IPerfilGateway perfilGateway;

    private IReativarPerfilUseCase reativarPerfilUseCase;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        reativarPerfilUseCase = new ReativarPerfilUseCase(perfilGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @DisplayName("Deve reativar perfil com sucesso.")
    @Test
    void deveReativarPerfilComSucesso() {
        // Arrange
        int perfilId = 1;
        var perfilRetornado = corePerfilEntityValido();

        when(perfilGateway.buscarPorId(anyInt())).thenReturn(perfilRetornado);

        ArgumentCaptor<PerfilCoreDto> captor = ArgumentCaptor.forClass(PerfilCoreDto.class);

        // Act
        reativarPerfilUseCase.reativar(perfilId);

        // Assert
        verify(perfilGateway, times(1)).buscarPorId(anyInt());
        verify(perfilGateway, times(1)).salvar(captor.capture());
        assertThat(captor.getValue()).isNotNull();
        assertThat(captor.getValue().id()).isEqualTo(perfilId);
        assertThat(captor.getValue().dataInativacao()).isNull();
    }

    @DisplayName("Deve reativar perfil com erro. Perfil não encontrado através do ID.")
    @Test
    void deveLancarExcecaoSeNaoEncontrarPerfilPorId() {
        // Arrange
        int perfilId = 1;

        when(perfilGateway.buscarPorId(anyInt()))
                .thenThrow(new PerfilInvalidoException(PERFIS_NAO_ENCONTRADOS));

        // Act & Assert
        assertThatThrownBy(() -> reativarPerfilUseCase.reativar(perfilId))
                .isInstanceOf(PerfilInvalidoException.class)
                .hasMessage(PERFIS_NAO_ENCONTRADOS);

        verify(perfilGateway, times(1)).buscarPorId(anyInt());
        verify(perfilGateway, times(0)).salvar(any(PerfilCoreDto.class));
    }
}
