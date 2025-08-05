package br.com.fiapfood.core.usecases.unitarios.usuario;

import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioCoreDto;
import br.com.fiapfood.core.exceptions.usuario.AtualizacaoStatusUsuarioNaoPermitidaException;
import br.com.fiapfood.core.exceptions.usuario.UsuarioNaoEncontradoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.usuario.impl.InativarUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IInativarUsuarioUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static br.com.fiapfood.utils.CoreEntityDataGenerator.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class InativarUsuarioUseCaseTest {
    private final String INATIVACAO_NAO_PERMITIDA = "Não é possível inativar o usuário, pois ele já se encontra inativo.";
    private final String USUARIO_NAO_ENCONTRADO = "Não foi encontrado nenhum usuário.";

    @Mock
    private IUsuarioGateway usuarioGateway;
    @Mock
    private IPerfilGateway perfilGateway;

    private IInativarUsuarioUseCase inativarUsuarioUseCase;

    AutoCloseable mock;

    @BeforeEach
    public void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        inativarUsuarioUseCase = new InativarUsuarioUseCase(usuarioGateway, perfilGateway);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mock.close();
    }

    @DisplayName("Deve inativar usuário com sucesso.")
    @Test
    void deveInativarUsuarioComSucesso(){
        // Arrange
        UUID usuarioId = UUID.randomUUID();

        var usuarioExistente = coreUsuarioEntityAtivoValido();

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(usuarioExistente);
        when(perfilGateway.buscarPorId(anyInt())).thenReturn(corePerfilEntityValido());

        ArgumentCaptor<DadosUsuarioCoreDto> captor = ArgumentCaptor.forClass(DadosUsuarioCoreDto.class);

        // Act
        inativarUsuarioUseCase.inativar(usuarioId);

        // Assert
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(perfilGateway).buscarPorId(anyInt());
        verify(usuarioGateway).salvar(captor.capture());

        DadosUsuarioCoreDto usuarioSalvo = captor.getValue();

        assertThat(usuarioSalvo).isNotNull();
        assertThat(usuarioSalvo.id()).isEqualTo(usuarioExistente.getId());
        assertThat(usuarioSalvo.isAtivo()).isFalse();
    }

    @DisplayName("Deve inativar usuário com erro. Usuário não encontrado através do ID")
    @Test
    void deveLancarExcecaoSeNaoEncontrarUsuarioAtravesDoID(){
        // Arrange
        UUID usuarioId = UUID.randomUUID();

        when(usuarioGateway.buscarPorId(usuarioId)).thenThrow(new UsuarioNaoEncontradoException(USUARIO_NAO_ENCONTRADO));

        // Act & Assert
        assertThatThrownBy(() -> inativarUsuarioUseCase.inativar(usuarioId))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(USUARIO_NAO_ENCONTRADO);
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(perfilGateway, never()).buscarPorId(anyInt());
        verify(usuarioGateway, never()).salvar(any(DadosUsuarioCoreDto.class));
    }

    @DisplayName("Deve inativar usuário com erro. Usuário encontrado já está inativo")
    @Test
    void deveLancarExcecaoSeNaoEncontrarUsuarioEncontradoEstiverInativo(){
        // Arrange
        UUID usuarioId = UUID.randomUUID();

        var usuarioExistente = coreUsuarioEntityInativoValido();

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(usuarioExistente);

        // Act & Assert
        assertThatThrownBy(() -> inativarUsuarioUseCase.inativar(usuarioId))
                .isInstanceOf(AtualizacaoStatusUsuarioNaoPermitidaException.class)
                .hasMessage(INATIVACAO_NAO_PERMITIDA);
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(perfilGateway, never()).buscarPorId(anyInt());
        verify(usuarioGateway, never()).salvar(any(DadosUsuarioCoreDto.class));
    }
}
