package br.com.fiapfood.core.usecases.unitarios.usuario;

import br.com.fiapfood.core.exceptions.usuario.UsuarioNaoEncontradoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.usuario.impl.BuscarUsuarioPorIdUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IBuscarUsuarioPorIdUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static br.com.fiapfood.utils.CoreEntityDataGenerator.corePerfilEntityValido;
import static br.com.fiapfood.utils.CoreEntityDataGenerator.coreUsuarioEntityInativoValido;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BuscarUsuarioPorIdUseCaseTest {
    private final String USUARIO_NAO_ENCONTRADO = "Não foi encontrado nenhum usuário.";

    @Mock
    private IUsuarioGateway usuarioGateway;
    @Mock
    private IPerfilGateway perfilGateway;

    private IBuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;

    AutoCloseable mock;

    @BeforeEach
    public void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        buscarUsuarioPorIdUseCase = new BuscarUsuarioPorIdUseCase(usuarioGateway, perfilGateway);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mock.close();
    }

    @DisplayName("Deve buscar usuário por ID com sucesso.")
    @Test
    void deveBuscarUsuarioPorIdComSucesso() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();

        var usuarioExistente = coreUsuarioEntityInativoValido();

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(usuarioExistente);
        when(perfilGateway.buscarPorId(anyInt())).thenReturn(corePerfilEntityValido());

        // Act
        var usuario = buscarUsuarioPorIdUseCase.buscar(usuarioId);

        // Assert
        verify(usuarioGateway).buscarPorId(usuarioId);
        assertThat(usuario).isNotNull();
        assertThat(usuario.id()).isEqualTo(coreUsuarioEntityInativoValido().getId());
    }

    @DisplayName("Deve buscar usuário por ID com erro. Usuário não encontrado através do ID.")
    @Test
    void deveLancarExcecaoSeUsuarioNaoEncontradoAtravesDoID() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();

        when(usuarioGateway.buscarPorId(usuarioId)).thenThrow(new UsuarioNaoEncontradoException(USUARIO_NAO_ENCONTRADO));

        // Act & Assert
        assertThatThrownBy(() -> buscarUsuarioPorIdUseCase.buscar(usuarioId))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(USUARIO_NAO_ENCONTRADO);
        verify(usuarioGateway).buscarPorId(usuarioId);
    }
}
