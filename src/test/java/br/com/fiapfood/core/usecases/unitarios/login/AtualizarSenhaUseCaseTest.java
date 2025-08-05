package br.com.fiapfood.core.usecases.unitarios.login;

import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioCoreDto;
import br.com.fiapfood.core.exceptions.usuario.UsuarioInativoException;
import br.com.fiapfood.core.exceptions.usuario.UsuarioNaoEncontradoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.login.impl.AtualizarSenhaUseCase;
import br.com.fiapfood.core.usecases.login.interfaces.IAtualizarSenhaUseCase;
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

public class AtualizarSenhaUseCaseTest {

    private final String USUARIO_INATIVO = "Não é possível alterar a senha, pois o usuário está inativo.";
    private final String USUARIO_NAO_ENCONTRADO = "Não foi encontrado nenhum usuário.";

    @Mock
    private IUsuarioGateway usuarioGateway;
    @Mock
    private IPerfilGateway perfilGateway;

    IAtualizarSenhaUseCase atualizarSenhaUseCase;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        atualizarSenhaUseCase = new AtualizarSenhaUseCase(usuarioGateway, perfilGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @DisplayName("Deve atualizar a senha com sucesso.")
    @Test
    void deveAtualizarSenhaComSucesso() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        String novaSenha = "321";

        Usuario usuarioRetornado = coreUsuarioEntityAtivoValido();

        when(usuarioGateway.buscarPorId(any(UUID.class))).thenReturn(usuarioRetornado);
        when(perfilGateway.buscarPorId(anyInt())).thenReturn(corePerfilEntityValido());
        doNothing().when(usuarioGateway).salvar(any(DadosUsuarioCoreDto.class));

        // Act
        atualizarSenhaUseCase.atualizar(usuarioId, novaSenha);

        // Assert
        verify(usuarioGateway, times(1)).buscarPorId(any(UUID.class));
        verify(perfilGateway, times(1)).buscarPorId(anyInt());
        verify(usuarioGateway, times(1)).salvar(any(DadosUsuarioCoreDto.class));
        assertThat(usuarioRetornado.getLogin().getSenha()).isEqualTo(novaSenha);
    }

    @DisplayName("Deve atualizar a senha com erro. Usuário não encontrado através do ID.")
    @Test
    void deveLancarExcecaoSeUsuarioNaoEncontradoAtravesDoId(){
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        String novaSenha = "321";

        when(usuarioGateway.buscarPorId(any(UUID.class))).thenThrow(new UsuarioNaoEncontradoException(USUARIO_NAO_ENCONTRADO));
        when(perfilGateway.buscarPorId(anyInt())).thenReturn(corePerfilEntityValido());
        doNothing().when(usuarioGateway).salvar(any(DadosUsuarioCoreDto.class));

        // Act & Assert
        assertThatThrownBy(() -> atualizarSenhaUseCase.atualizar(usuarioId, novaSenha))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(USUARIO_NAO_ENCONTRADO);
        verify(usuarioGateway, times(1)).buscarPorId(any(UUID.class));
        verify(perfilGateway, times(0)).buscarPorId(anyInt());
        verify(usuarioGateway, times(0)).salvar(any(DadosUsuarioCoreDto.class));
    }

    @DisplayName("Deve atualizar a senha com erro. Usuário encontrado está inativo.")
    @Test
    void deveLancarExcecaoSeUsuarioEstiverInativo(){
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        String novaSenha = "321";
        Usuario usuarioRetornado = coreUsuarioEntityInativoValido();

        when(usuarioGateway.buscarPorId(any(UUID.class))).thenReturn(usuarioRetornado);
        when(perfilGateway.buscarPorId(anyInt())).thenReturn(corePerfilEntityValido());
        doNothing().when(usuarioGateway).salvar(any(DadosUsuarioCoreDto.class));

        // Act & Assert
        assertThatThrownBy(() -> atualizarSenhaUseCase.atualizar(usuarioId, novaSenha))
                .isInstanceOf(UsuarioInativoException.class)
                .hasMessage(USUARIO_INATIVO);
        verify(usuarioGateway, times(1)).buscarPorId(any(UUID.class));
        verify(perfilGateway, times(0)).buscarPorId(anyInt());
        verify(usuarioGateway, times(0)).salvar(any(DadosUsuarioCoreDto.class));
        assertThat(usuarioRetornado.getLogin().getSenha()).isNotEqualTo(novaSenha);
    }
}
