package br.com.fiapfood.core.usecases.unitarios.usuario;

import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioCoreDto;
import br.com.fiapfood.core.exceptions.usuario.AtualizacaoNomeUsuarioNaoPermitidoException;
import br.com.fiapfood.core.exceptions.usuario.NomeUsuarioInvalidoException;
import br.com.fiapfood.core.exceptions.usuario.UsuarioInativoException;
import br.com.fiapfood.core.exceptions.usuario.UsuarioNaoEncontradoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.usuario.impl.AtualizarNomeUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarNomeUsuarioUseCase;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class AtualizarNomeUsuarioUseCaseTest {

    private final String USUARIO_DUPLICADO = "Não é possível alterar o nome do usuário, pois ele é igual ao nome do usuário.";
    private final String USUARIO_INATIVO = "Não é possível alterar o nome de um usuário inativo.";
    private final String USUARIO_NAO_ENCONTRADO = "Não foi encontrado nenhum usuário.";

    @Mock
    private IUsuarioGateway usuarioGateway;
    @Mock
    private IPerfilGateway perfilGateway;

    private IAtualizarNomeUsuarioUseCase atualizarNomeUsuarioUseCase;

    AutoCloseable mock;

    @BeforeEach
    public void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        atualizarNomeUsuarioUseCase = new AtualizarNomeUsuarioUseCase(usuarioGateway, perfilGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @DisplayName("Deve atualizar nome do usuário com sucesso.")
    @Test
    void deveAtualizarNomeDoUsuarioComSucesso() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        String novoNome = "Jane Doe";

        var usuarioExistente = coreUsuarioEntityAtivoValido();

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(usuarioExistente);
        when(perfilGateway.buscarPorId(anyInt())).thenReturn(corePerfilEntityValido());

        ArgumentCaptor<DadosUsuarioCoreDto> captor = ArgumentCaptor.forClass(DadosUsuarioCoreDto.class);

        // Act
        atualizarNomeUsuarioUseCase.atualizar(usuarioId, novoNome);

        // Assert
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(perfilGateway).buscarPorId(anyInt());
        verify(usuarioGateway).salvar(captor.capture());

        DadosUsuarioCoreDto usuarioSalvo = captor.getValue();

        assertThat(usuarioSalvo).isNotNull();
        assertThat(usuarioSalvo.id()).isEqualTo(usuarioExistente.getId());
        assertThat(usuarioSalvo.nome()).isEqualTo(novoNome);
    }

    @DisplayName("Deve atualizar nome do usuário com erro. Usuário não encontrado através do ID")
    @Test
    void deveLancarExcecaoSeNaoEncontrarUsuarioAtravesDoID() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        String novoNome = "Jane Doe";

        when(usuarioGateway.buscarPorId(usuarioId)).thenThrow(new UsuarioNaoEncontradoException(USUARIO_NAO_ENCONTRADO));

        // Act & Assert
        assertThatThrownBy(() -> atualizarNomeUsuarioUseCase.atualizar(usuarioId, novoNome))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(USUARIO_NAO_ENCONTRADO);
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(perfilGateway, never()).buscarPorId(anyInt());
        verify(usuarioGateway, never()).salvar(any(DadosUsuarioCoreDto.class));
    }

    @DisplayName("Deve atualizar nome do usuário com erro. Usuário está inativo.")
    @Test
    void deveLancarExcecaoSeUsuarioEstiverInativo() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        String novoNome = "Jane Doe";

        var usuarioExistente = coreUsuarioEntityInativoValido();

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(usuarioExistente);

        // Act & Assert
        assertThatThrownBy(() -> atualizarNomeUsuarioUseCase.atualizar(usuarioId, novoNome))
                .isInstanceOf(UsuarioInativoException.class)
                .hasMessage(USUARIO_INATIVO);
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(perfilGateway, never()).buscarPorId(anyInt());
        verify(usuarioGateway, never()).salvar(any(DadosUsuarioCoreDto.class));
    }

    @DisplayName("Deve atualizar nome do usuário com erro. Novo nome igual ao antigo.")
    @Test
    void deveLancarExcecaoSeNovoNomeForIgualAoAntigo() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        String novoNome = "John Doe";

        var usuarioExistente = coreUsuarioEntityAtivoValido();

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(usuarioExistente);

        // Act & Assert
        assertThatThrownBy(() -> atualizarNomeUsuarioUseCase.atualizar(usuarioId, novoNome))
                .isInstanceOf(AtualizacaoNomeUsuarioNaoPermitidoException.class)
                .hasMessage(USUARIO_DUPLICADO);
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(perfilGateway, never()).buscarPorId(anyInt());
        verify(usuarioGateway, never()).salvar(any(DadosUsuarioCoreDto.class));
    }

    @DisplayName("Deve atualizar nome do usuário com erro. Novo nome inválido.")
    @Test
    void deveLancarExcecaoSeNovoNomeForInvalido() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        String novoNome = "";

        var usuarioExistente = coreUsuarioEntityAtivoValido();

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(usuarioExistente);

        // Act & Assert
        assertThatThrownBy(() -> atualizarNomeUsuarioUseCase.atualizar(usuarioId, novoNome))
                .isInstanceOf(NomeUsuarioInvalidoException.class)
                .hasMessage("O nome do usuário informado é inválido.");
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(perfilGateway, never()).buscarPorId(anyInt());
        verify(usuarioGateway, never()).salvar(any(DadosUsuarioCoreDto.class));
    }
}
