package br.com.fiapfood.core.usecases.unitarios.login;

import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.exceptions.usuario.UsuarioNaoEncontradoException;
import br.com.fiapfood.core.exceptions.usuario.UsuarioSemAcessoException;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.login.impl.ValidarLoginUseCase;
import br.com.fiapfood.core.usecases.login.interfaces.IValidarAcessoUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static br.com.fiapfood.utils.CoreEntityDataGenerator.coreUsuarioEntityAtivoValido;
import static br.com.fiapfood.utils.CoreEntityDataGenerator.coreUsuarioEntityInativoValido;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class ValidarLoginUseCaseTest {

    private final String USUARIO_INATIVO = "Não é possível realizar o login para usuários inativos.";
    private final String USUARIO_NAO_ENCONTRADO = "Não foi encontrado nenhum usuário.";
    private final String ACESSO_LIBERADO = "Acesso liberado para o usuário ";

    @Mock
    private IUsuarioGateway usuarioGateway;

    IValidarAcessoUseCase validarLoginUseCase;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        validarLoginUseCase = new ValidarLoginUseCase(usuarioGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @DisplayName("Deve validar login com sucesso.")
    @Test
    void deveValidarUsuarioSucesso() {
        // Arrange
        String matricula = "us0001";
        String senha = "us0001";

        Usuario usuarioRetornado = coreUsuarioEntityAtivoValido();

        when(usuarioGateway.buscarPorMatriculaSenha(anyString(), anyString())).thenReturn(usuarioRetornado);

        // Act
        var mensagem = validarLoginUseCase.validar(matricula, senha);

        // Assert
        verify(usuarioGateway, times(1)).buscarPorMatriculaSenha(anyString(), anyString());
        assertThat(mensagem).isEqualTo(ACESSO_LIBERADO + usuarioRetornado.getNome());
    }

    @DisplayName("Deve validar login com erro. Usuário não encontrado através da matricula e senha.")
    @Test
    void deveLancarExcecaoSeUsuarioNaoEncontradoAtravesDaMatriculaESenha(){
        // Arrange
        String matricula = "us0001";
        String senha = "us0001";

        when(usuarioGateway.buscarPorMatriculaSenha(anyString(), anyString())).thenThrow(new UsuarioNaoEncontradoException(USUARIO_NAO_ENCONTRADO));

        // Act & Assert
        assertThatThrownBy(() -> validarLoginUseCase.validar(matricula, senha))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(USUARIO_NAO_ENCONTRADO);
        verify(usuarioGateway, times(1)).buscarPorMatriculaSenha(anyString(), anyString());
    }

    @DisplayName("Deve validar login com erro. Usuário encontrado está inativo.")
    @Test
    void deveLancarExcecaoSeUsuarioEstiverInativo(){
        // Arrange
        String matricula = "us0001";
        String senha = "us0001";
        Usuario usuarioRetornado = coreUsuarioEntityInativoValido();

        when(usuarioGateway.buscarPorMatriculaSenha(anyString(), anyString())).thenReturn(usuarioRetornado);

        // Act & Assert
        assertThatThrownBy(() -> validarLoginUseCase.validar(matricula, senha))
                .isInstanceOf(UsuarioSemAcessoException.class)
                .hasMessage(USUARIO_INATIVO);
        verify(usuarioGateway, times(1)).buscarPorMatriculaSenha(anyString(), anyString());
    }
}
