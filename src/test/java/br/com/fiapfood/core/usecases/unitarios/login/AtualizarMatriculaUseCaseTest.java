package br.com.fiapfood.core.usecases.unitarios.login;

import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioCoreDto;
import br.com.fiapfood.core.exceptions.perfil.PerfilInvalidoException;
import br.com.fiapfood.core.exceptions.usuario.MatriculaDuplicadaException;
import br.com.fiapfood.core.exceptions.usuario.UsuarioInativoException;
import br.com.fiapfood.core.exceptions.usuario.UsuarioNaoEncontradoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.login.impl.AtualizarMatriculaUseCase;
import br.com.fiapfood.core.usecases.login.interfaces.IAtualizarMatriculaUseCase;
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
import static org.mockito.Mockito.*;

public class AtualizarMatriculaUseCaseTest {

    private final String USUARIO_INATIVO = "Não é possível alterar a matrícula, pois o usuário está inativo.";
    private final String MATRICULA_DUPLICADA = "Já existe um usuário com a matrícula informada.";
    private final String USUARIO_NAO_ENCONTRADO = "Não foi encontrado nenhum usuário.";
    private final String PERFIS_NAO_ENCONTRADOS = "Não foi encontrado nenhum perfil com o id informado.";


    @Mock
    private IUsuarioGateway usuarioGateway;
    @Mock
    private IPerfilGateway perfilGateway;

    IAtualizarMatriculaUseCase atualizarMatriculaUseCase;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        atualizarMatriculaUseCase = new AtualizarMatriculaUseCase(usuarioGateway, perfilGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @DisplayName("Deve atualizar a matricula com sucesso")
    @Test
    void deveAtualizarMatriculaComSucesso(){
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        String novaMatricula = "us0010";
        Usuario usuarioRetornado = coreUsuarioEntityAtivoValido();

        when(usuarioGateway.buscarPorId(any(UUID.class))).thenReturn(usuarioRetornado);
        when(usuarioGateway.matriculaJaCadastrada(anyString())).thenReturn(false);
        when(perfilGateway.buscarPorId(anyInt())).thenReturn(corePerfilEntityValido());
        doNothing().when(usuarioGateway).salvar(any(DadosUsuarioCoreDto.class));

        // Act
        atualizarMatriculaUseCase.atualizar(usuarioId, novaMatricula);

        // Assert
        verify(usuarioGateway, times(1)).buscarPorId(any(UUID.class));
        verify(usuarioGateway, times(1)).matriculaJaCadastrada(anyString());
        verify(perfilGateway, times(1)).buscarPorId(anyInt());
        verify(usuarioGateway, times(1)).salvar(any(DadosUsuarioCoreDto.class));
        assertThat(usuarioRetornado.getLogin().getMatricula()).isEqualTo(novaMatricula);
    }

    @DisplayName("Deve atualizar a matricula com erro. Usuário não encontrado através do ID")
    @Test
    void deveLancarExcecaoSeUsuarioNaoEncontradoAtravesDoId() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        String novaMatricula = "us0010";

        when(usuarioGateway.buscarPorId(any(UUID.class))).thenThrow(new UsuarioNaoEncontradoException(USUARIO_NAO_ENCONTRADO));
        when(usuarioGateway.matriculaJaCadastrada(anyString())).thenReturn(false);
        when(perfilGateway.buscarPorId(anyInt())).thenReturn(corePerfilEntityValido());
        doNothing().when(usuarioGateway).salvar(any(DadosUsuarioCoreDto.class));

        // Act & Assert
        assertThatThrownBy(() -> atualizarMatriculaUseCase.atualizar(usuarioId, novaMatricula))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(USUARIO_NAO_ENCONTRADO);
        verify(usuarioGateway, times(1)).buscarPorId(any(UUID.class));
        verify(usuarioGateway, times(0)).matriculaJaCadastrada(anyString());
        verify(perfilGateway, times(0)).buscarPorId(anyInt());
        verify(usuarioGateway, times(0)).salvar(any(DadosUsuarioCoreDto.class));
    }

    @DisplayName("Deve atualizar a matricula com erro. Nova matricula já cadastrada.")
    @Test
    void deveLancarExcecaoSeNovaMatriculaForDuplicada() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        String novaMatricula = "us0010";
        Usuario usuarioRetornado = coreUsuarioEntityAtivoValido();

        when(usuarioGateway.buscarPorId(any(UUID.class))).thenReturn(usuarioRetornado);
        when(usuarioGateway.matriculaJaCadastrada(anyString())).thenReturn(true);
        when(perfilGateway.buscarPorId(anyInt())).thenReturn(corePerfilEntityValido());
        doNothing().when(usuarioGateway).salvar(any(DadosUsuarioCoreDto.class));

        // Act & Assert
        assertThatThrownBy(() -> atualizarMatriculaUseCase.atualizar(usuarioId, novaMatricula))
                .isInstanceOf(MatriculaDuplicadaException.class)
                .hasMessage(MATRICULA_DUPLICADA);
        verify(usuarioGateway, times(1)).buscarPorId(any(UUID.class));
        verify(usuarioGateway, times(1)).matriculaJaCadastrada(anyString());
        verify(perfilGateway, times(0)).buscarPorId(anyInt());
        verify(usuarioGateway, times(0)).salvar(any(DadosUsuarioCoreDto.class));
    }

    @DisplayName("Deve atualizar a matricula com erro. Usuário encontrado está inativo.")
    @Test
    void deveLancarExcecaoSeUsuarioEstiverInativo() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        String novaMatricula = "us0010";
        Usuario usuarioRetornado = coreUsuarioEntityInativoValido();

        when(usuarioGateway.buscarPorId(any(UUID.class))).thenReturn(usuarioRetornado);
        when(usuarioGateway.matriculaJaCadastrada(anyString())).thenReturn(false);
        when(perfilGateway.buscarPorId(anyInt())).thenReturn(corePerfilEntityValido());
        doNothing().when(usuarioGateway).salvar(any(DadosUsuarioCoreDto.class));

        // Act & Assert
        assertThatThrownBy(() -> atualizarMatriculaUseCase.atualizar(usuarioId, novaMatricula))
                .isInstanceOf(UsuarioInativoException.class)
                .hasMessage(USUARIO_INATIVO);
        verify(usuarioGateway, times(1)).buscarPorId(any(UUID.class));
        verify(usuarioGateway, times(1)).matriculaJaCadastrada(anyString());
        verify(perfilGateway, times(0)).buscarPorId(anyInt());
        verify(usuarioGateway, times(0)).salvar(any(DadosUsuarioCoreDto.class));
        assertThat(usuarioRetornado.getLogin().getMatricula()).isNotEqualTo(novaMatricula);
    }

    @DisplayName("Deve atualizar a matricula com erro. Perfil não encontrado através do ID")
    @Test
    void deveLancarExcecaoSePerfilNaoEncontradoAtravesDoId() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        String novaMatricula = "us0010";
        Usuario usuarioRetornado = coreUsuarioEntityAtivoValido();

        when(usuarioGateway.buscarPorId(any(UUID.class))).thenReturn(usuarioRetornado);
        when(usuarioGateway.matriculaJaCadastrada(anyString())).thenReturn(false);
        when(perfilGateway.buscarPorId(anyInt())).thenThrow(new PerfilInvalidoException(PERFIS_NAO_ENCONTRADOS));
        doNothing().when(usuarioGateway).salvar(any(DadosUsuarioCoreDto.class));

        // Act & Assert
        assertThatThrownBy(() -> atualizarMatriculaUseCase.atualizar(usuarioId, novaMatricula))
                .isInstanceOf(PerfilInvalidoException.class)
                .hasMessage(PERFIS_NAO_ENCONTRADOS);
        verify(usuarioGateway, times(1)).buscarPorId(any(UUID.class));
        verify(usuarioGateway, times(1)).matriculaJaCadastrada(anyString());
        verify(perfilGateway, times(1)).buscarPorId(anyInt());
        verify(usuarioGateway, times(0)).salvar(any(DadosUsuarioCoreDto.class));
    }

}
