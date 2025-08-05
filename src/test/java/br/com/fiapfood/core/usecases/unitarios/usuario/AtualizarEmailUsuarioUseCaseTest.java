package br.com.fiapfood.core.usecases.unitarios.usuario;

import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioCoreDto;
import br.com.fiapfood.core.exceptions.usuario.AtualizacaoEmailUsuarioNaoPermitidoException;
import br.com.fiapfood.core.exceptions.usuario.EmailUsuarioInvalidoException;
import br.com.fiapfood.core.exceptions.usuario.UsuarioNaoEncontradoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.usuario.impl.AtualizarEmailUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarEmailUsuarioUseCase;
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
import static org.mockito.Mockito.*;

public class AtualizarEmailUsuarioUseCaseTest {

    private final String USUARIO_INATIVO = "Não é possível alterar o email de um usuário inativo.";
    private final String USUARIO_CADASTRADO = "Já existe um usuário com o email informado.";
    private final String EMAIL_DUPLICADO = "Não é possível alterar o email do usuário, pois ele já é igual ao email atual.";
    private final String USUARIO_NAO_ENCONTRADO = "Não foi encontrado nenhum usuário.";

    @Mock
    private IUsuarioGateway usuarioGateway;
    @Mock
    private IPerfilGateway perfilGateway;

    private IAtualizarEmailUsuarioUseCase atualizarEmailUsuarioUseCase;

    AutoCloseable mock;

    @BeforeEach
    public void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        atualizarEmailUsuarioUseCase = new AtualizarEmailUsuarioUseCase(usuarioGateway, perfilGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @DisplayName("Deve atualizar o e-mail do usuário com sucesso.")
    @Test
    void deveAtualizarEmailUsuarioComSucesso() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        String novoEmail = "john.doe1234@email.com";

        var usuarioExistente = coreUsuarioEntityAtivoValido();

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(usuarioExistente);
        when(usuarioGateway.emailJaCadastrado(novoEmail)).thenReturn(false);
        when(perfilGateway.buscarPorId(anyInt())).thenReturn(corePerfilEntityValido());

        ArgumentCaptor<DadosUsuarioCoreDto> captor = ArgumentCaptor.forClass(DadosUsuarioCoreDto.class);

        // Act
        atualizarEmailUsuarioUseCase.atualizar(usuarioId, novoEmail);

        // Assert
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(usuarioGateway).emailJaCadastrado(novoEmail);
        verify(usuarioGateway).salvar(captor.capture());

        DadosUsuarioCoreDto usuarioSalvo = captor.getValue();

        assertThat(usuarioSalvo).isNotNull();
        assertThat(usuarioSalvo.email()).isEqualTo(novoEmail);
        assertThat(usuarioSalvo.id()).isEqualTo(usuarioExistente.getId());
    }

    @DisplayName("Deve atualizar o e-mail do usuário com erro. Usuário nao encontrado através do ID.")
    @Test
    void deveLancarExcecaoSeNaoEncontrarUsuarioAtravesDoID() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        String novoEmail = "john.doe1234@email.com";

        when(usuarioGateway.buscarPorId(usuarioId)).thenThrow(new UsuarioNaoEncontradoException(USUARIO_NAO_ENCONTRADO));

        // Act & Assert
        assertThatThrownBy(() -> atualizarEmailUsuarioUseCase.atualizar(usuarioId, novoEmail))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(USUARIO_NAO_ENCONTRADO);
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(usuarioGateway, times(0)).emailJaCadastrado(novoEmail);
        verify(usuarioGateway, times(0)).salvar(any(DadosUsuarioCoreDto.class));
    }

    @DisplayName("Deve atualizar o e-mail do usuário com erro. Usuário encontrado está inativo.")
    @Test
    void deveLancarExcecaoSeUsuarioEncontradoEstiverInativo() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        String novoEmail = "john.doe1234@email.com";

        var usuarioExistente = coreUsuarioEntityInativoValido();

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(usuarioExistente);
        when(usuarioGateway.emailJaCadastrado(novoEmail)).thenReturn(false);
        when(perfilGateway.buscarPorId(anyInt())).thenReturn(corePerfilEntityValido());

        // Act & Assert
        assertThatThrownBy(() -> atualizarEmailUsuarioUseCase.atualizar(usuarioId, novoEmail))
                .isInstanceOf(AtualizacaoEmailUsuarioNaoPermitidoException.class)
                .hasMessage(USUARIO_INATIVO);
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(usuarioGateway, times(0)).emailJaCadastrado(novoEmail);
        verify(usuarioGateway, times(0)).salvar(any(DadosUsuarioCoreDto.class));
    }

    @DisplayName("Deve atualizar o e-mail do usuário com erro. Novo email já está cadastrado.")
    @Test
    void deveLancarExcecaoNovoEmailJaEstivarCadastrado() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        String novoEmail = "john.doe1234@email.com";

        var usuarioExistente = coreUsuarioEntityAtivoValido();

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(usuarioExistente);
        when(usuarioGateway.emailJaCadastrado(novoEmail)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> atualizarEmailUsuarioUseCase.atualizar(usuarioId, novoEmail))
                .isInstanceOf(AtualizacaoEmailUsuarioNaoPermitidoException.class)
                .hasMessage(USUARIO_CADASTRADO);
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(usuarioGateway).emailJaCadastrado(novoEmail);
        verify(usuarioGateway, times(0)).salvar(any(DadosUsuarioCoreDto.class));

    }

    @DisplayName("Deve atualizar o e-mail do usuário com erro. Novo email igual ao antigo.")
    @Test
    void deveLancarExcecaoNovoEmailForIgualAoAntigo() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        String novoEmail = "john.doe@email.com";

        var usuarioExistente = coreUsuarioEntityAtivoValido();

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(usuarioExistente);
        when(usuarioGateway.emailJaCadastrado(novoEmail)).thenReturn(false);
        when(perfilGateway.buscarPorId(anyInt())).thenReturn(corePerfilEntityValido());

        ArgumentCaptor<DadosUsuarioCoreDto> captor = ArgumentCaptor.forClass(DadosUsuarioCoreDto.class);

        // Act & Assert
        assertThatThrownBy(() -> atualizarEmailUsuarioUseCase.atualizar(usuarioId, novoEmail))
                .isInstanceOf(AtualizacaoEmailUsuarioNaoPermitidoException.class)
                .hasMessage(EMAIL_DUPLICADO);
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(usuarioGateway).emailJaCadastrado(novoEmail);
        verify(usuarioGateway, times(0)).salvar(any(DadosUsuarioCoreDto.class));
    }

    @DisplayName("Deve atualizar o e-mail do usuário com erro. Novo email está inválido.")
    @Test
    void deveLancarExcecaoNovoEmailForInvalido() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        String novoEmail = "";

        var usuarioExistente = coreUsuarioEntityAtivoValido();

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(usuarioExistente);
        when(usuarioGateway.emailJaCadastrado(novoEmail)).thenReturn(false);
        when(perfilGateway.buscarPorId(anyInt())).thenReturn(corePerfilEntityValido());

        // Act & Assert
        assertThatThrownBy(() -> atualizarEmailUsuarioUseCase.atualizar(usuarioId, novoEmail))
                .isInstanceOf(EmailUsuarioInvalidoException.class)
                .hasMessage("O email do usuário informado é inválido.");
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(usuarioGateway).emailJaCadastrado(novoEmail);
        verify(usuarioGateway, times(0)).salvar(any(DadosUsuarioCoreDto.class));
    }


}
