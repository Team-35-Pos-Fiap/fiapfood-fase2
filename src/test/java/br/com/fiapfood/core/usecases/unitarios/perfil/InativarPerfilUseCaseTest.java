package br.com.fiapfood.core.usecases.unitarios.perfil;

import br.com.fiapfood.core.entities.dto.perfil.PerfilCoreDto;
import br.com.fiapfood.core.exceptions.perfil.ExclusaoPerfilNaoPermitidaException;
import br.com.fiapfood.core.exceptions.perfil.PerfilInvalidoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.perfil.impl.InativarPerfilUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.IInativarPerfilUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static br.com.fiapfood.utils.CoreEntityDataGenerator.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class InativarPerfilUseCaseTest {
    private final String USUARIOS_ATIVOS = "Não é possível inativar o perfil pois há usuário ativo associado ao perfil.";
    private final String PERFIS_NAO_ENCONTRADOS = "Não foi encontrado nenhum perfil com o id informado.";

    @Mock
    private IPerfilGateway perfilGateway;
    @Mock
    IUsuarioGateway usuarioGateway;
    private IInativarPerfilUseCase inativarPerfilUseCase;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        inativarPerfilUseCase = new InativarPerfilUseCase(perfilGateway,  usuarioGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @DisplayName("Deve inativar perfil com sucesso.")
    @Test
    void deveInativarPerfilComSucesso() {
        // Arrange
        int perfilId = 1;
        var perfilRetornado = corePerfilEntityValido();

        when(perfilGateway.buscarPorId(anyInt())).thenReturn(perfilRetornado);
        when(usuarioGateway.buscarPorIdPerfil(anyInt())).thenReturn(null);

        ArgumentCaptor<PerfilCoreDto> captor = ArgumentCaptor.forClass(PerfilCoreDto.class);

        // Act
        inativarPerfilUseCase.inativar(perfilId);

        // Assert
        verify(perfilGateway, times(1)).buscarPorId(anyInt());
        verify(usuarioGateway, times(1)).buscarPorIdPerfil(anyInt());
        verify(perfilGateway, times(1)).salvar(captor.capture());
        assertThat(captor.getValue()).isNotNull();
        assertThat(captor.getValue().id()).isEqualTo(perfilId);
        assertThat(captor.getValue().dataInativacao()).isNotNull();
    }

    @DisplayName("Deve inativar perfil com erro. Perfil não encontrado através do ID.")
    @Test
    void deveLancarExcecaoSeNaoEncontrarPerfilPorId() {
        // Arrange
        int perfilId = 1;

        when(perfilGateway.buscarPorId(anyInt()))
                .thenThrow(new PerfilInvalidoException(PERFIS_NAO_ENCONTRADOS));

        // Act & Assert
        assertThatThrownBy(() -> inativarPerfilUseCase.inativar(perfilId))
                .isInstanceOf(PerfilInvalidoException.class)
                .hasMessage(PERFIS_NAO_ENCONTRADOS);

        verify(perfilGateway, times(1)).buscarPorId(anyInt());
        verify(usuarioGateway, times(0)).buscarPorIdPerfil(anyInt());
        verify(perfilGateway, times(0)).salvar(any(PerfilCoreDto.class));
    }

    @DisplayName("Deve inativar perfil com erro. Usuários ativos cadastrados com perfil.")
    @Test
    void deveLancarExcecaoSeUsuariosAtivosEstiveremRegistradosComPerfil() {
        // Arrange
        int perfilId = 1;
        var perfilRetornado = corePerfilEntityValido();
        var listaUsuarios = List.of(coreUsuarioEntityInativoValido(),coreUsuarioEntityInativoValido(),coreUsuarioEntityAtivoValido(),coreUsuarioEntityInativoValido());

        when(perfilGateway.buscarPorId(anyInt())).thenReturn(perfilRetornado);
        when(usuarioGateway.buscarPorIdPerfil(anyInt())).thenReturn(listaUsuarios);

        // Act & Assert
        assertThatThrownBy(() -> inativarPerfilUseCase.inativar(perfilId))
                .isInstanceOf(ExclusaoPerfilNaoPermitidaException.class)
                .hasMessage(USUARIOS_ATIVOS);

        verify(perfilGateway, times(1)).buscarPorId(anyInt());
        verify(usuarioGateway, times(1)).buscarPorIdPerfil(anyInt());
        verify(perfilGateway, times(0)).salvar(any(PerfilCoreDto.class));
    }
}
