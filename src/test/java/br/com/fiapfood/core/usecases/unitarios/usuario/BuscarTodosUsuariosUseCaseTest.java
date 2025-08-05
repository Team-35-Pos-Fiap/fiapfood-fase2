package br.com.fiapfood.core.usecases.unitarios.usuario;

import br.com.fiapfood.core.entities.dto.paginacao.PaginacaoCoreDto;
import br.com.fiapfood.core.exceptions.usuario.UsuarioNaoEncontradoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.usuario.impl.BuscarTodosUsuariosUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IBuscarTodosUsuariosUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static br.com.fiapfood.utils.CoreEntityDataGenerator.corePerfilEntityValido;
import static br.com.fiapfood.utils.DtoDataGenerator.usuarioPaginacaoInputDtoValido;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BuscarTodosUsuariosUseCaseTest {
    private final String USUARIOS_NAO_ENCONTRADOS = "Não foram encontrados usuários na base de dados para a página informada.";

    @Mock
    private IUsuarioGateway usuarioGateway;
    @Mock
    private IPerfilGateway perfilGateway;

    private IBuscarTodosUsuariosUseCase buscarTodosUsuariosUseCase;

    AutoCloseable mock;

    @BeforeEach
    public void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        buscarTodosUsuariosUseCase = new BuscarTodosUsuariosUseCase(usuarioGateway, perfilGateway);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mock.close();
    }

    @DisplayName("Deve buscar todos os usuários com sucesso.")
    @Test
    void deveBuscarTodosUsuariosComSucesso() {
        // Arrange
        int pagina = 1;

        var dadosRetornadosDoGateway = usuarioPaginacaoInputDtoValido();

        when(usuarioGateway.buscarTodos(anyInt())).thenReturn(dadosRetornadosDoGateway);
        when(perfilGateway.buscarPorId(anyInt())).thenReturn(corePerfilEntityValido());

        // Act
        var todosUsuarios = buscarTodosUsuariosUseCase.buscar(pagina);

        // Assert
        verify(usuarioGateway).buscarTodos(anyInt());
        assertThat(todosUsuarios).isNotNull();
        assertThat(todosUsuarios.usuarios()).isNotNull();
        assertThat(todosUsuarios.usuarios()).isInstanceOf(List.class);
        assertThat(todosUsuarios.paginacao()).isNotNull();
        assertThat(todosUsuarios.paginacao()).isInstanceOf(PaginacaoCoreDto.class);
    }

    @DisplayName("Deve buscar todos os usuários com erro. Nenhum usuário encontrado na pagina.")
    @Test
    void deveLancarExcecaoSeNenhumUsuarioEncontradoNaPagina() {
        // Arrange
        int pagina = 100;
        when(usuarioGateway.buscarTodos(anyInt())).thenThrow(new UsuarioNaoEncontradoException(USUARIOS_NAO_ENCONTRADOS));

        // Act & Assert
        assertThatThrownBy(() -> buscarTodosUsuariosUseCase.buscar(pagina))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(USUARIOS_NAO_ENCONTRADOS);
        verify(usuarioGateway).buscarTodos(anyInt());
    }
}
