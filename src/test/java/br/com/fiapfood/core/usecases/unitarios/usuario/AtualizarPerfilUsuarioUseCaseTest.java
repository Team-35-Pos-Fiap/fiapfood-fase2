package br.com.fiapfood.core.usecases.unitarios.usuario;

import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioCoreDto;
import br.com.fiapfood.core.exceptions.usuario.AtualizacaoPerfilNaoPermitidaException;
import br.com.fiapfood.core.exceptions.usuario.UsuarioNaoEncontradoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.usuario.impl.AtualizarPerfilUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarPerfilUsuarioUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static br.com.fiapfood.utils.CoreEntityDataGenerator.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class AtualizarPerfilUsuarioUseCaseTest {

    private final String USUARIO_INATIVO = "Não é possível alterar o perfil de um usuário inativo.";
    private final String PERFIL_DUPLICADO = "O perfil selecionado é o mesmo que o usuário já possui.";
    private final String USUARIO_NAO_ENCONTRADO = "Não foi encontrado nenhum usuário.";

    @Mock
    private IUsuarioGateway usuarioGateway;
    @Mock
    private IPerfilGateway perfilGateway;

    private IAtualizarPerfilUsuarioUseCase atualizarPerfilUsuarioUseCase;

    AutoCloseable mock;

    @BeforeEach
    public void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        atualizarPerfilUsuarioUseCase = new AtualizarPerfilUsuarioUseCase(usuarioGateway, perfilGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @DisplayName("Deve atualizar o perfil do usuário com sucesso.")
    @Test
    void deveAtualizarPerfilUsuarioComSucesso() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        int idPerfil = 3;

        var usuarioExistente = coreUsuarioEntityAtivoValido();

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(usuarioExistente);
        when(perfilGateway.buscarPorId(anyInt())).thenReturn(corePerfilEntityValido());

        // Act
        atualizarPerfilUsuarioUseCase.atualizar(usuarioId, idPerfil);

        // Assert
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(perfilGateway).buscarPorId(anyInt());
        verify(usuarioGateway).salvar(any(DadosUsuarioCoreDto.class));
        assertThat(usuarioExistente.getIdPerfil()).isEqualTo(idPerfil);
    }

    @DisplayName("Deve atualizar o perfil do usuário com erro. Usuário não encontrado através do ID.")
    @Test
    void deveLancarExcecaoSeUsuairoNaoEncontradoAtravesDoID() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        int idPerfil = 3;

        when(usuarioGateway.buscarPorId(usuarioId)).thenThrow(new UsuarioNaoEncontradoException(USUARIO_NAO_ENCONTRADO));

        // Act & Assert
        assertThatThrownBy(() -> atualizarPerfilUsuarioUseCase.atualizar(usuarioId, idPerfil))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(USUARIO_NAO_ENCONTRADO);
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(perfilGateway, never()).buscarPorId(anyInt());
        verify(usuarioGateway, never()).salvar(any(DadosUsuarioCoreDto.class));
    }

    @DisplayName("Deve atualizar o perfil do usuário com erro. Usuário encontrado está inativo.")
    @Test
    void deveLancarExcecaoSeUsuairoEstiverInativo() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        int idPerfil = 3;

        var usuarioExistente = coreUsuarioEntityInativoValido();

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(usuarioExistente);

        // Act & Assert
        assertThatThrownBy(() -> atualizarPerfilUsuarioUseCase.atualizar(usuarioId, idPerfil))
                .isInstanceOf(AtualizacaoPerfilNaoPermitidaException.class)
                .hasMessage(USUARIO_INATIVO);
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(perfilGateway, never()).buscarPorId(anyInt());
        verify(usuarioGateway, never()).salvar(any(DadosUsuarioCoreDto.class));
    }

    @DisplayName("Deve atualizar o perfil do usuário com erro. Novo perfil igual ao atual")
    @Test
    void deveLancarExcecaoSeNovoPerfilForIgualAoAtual() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        int idPerfil = 1;

        var usuarioExistente = coreUsuarioEntityAtivoValido();

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(usuarioExistente);

        // Act & Assert
        assertThatThrownBy(() -> atualizarPerfilUsuarioUseCase.atualizar(usuarioId, idPerfil))
                .isInstanceOf(AtualizacaoPerfilNaoPermitidaException.class)
                .hasMessage(PERFIL_DUPLICADO);
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(perfilGateway, never()).buscarPorId(anyInt());
        verify(usuarioGateway, never()).salvar(any(DadosUsuarioCoreDto.class));
    }
}
