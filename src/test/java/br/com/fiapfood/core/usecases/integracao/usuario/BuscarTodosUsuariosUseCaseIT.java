package br.com.fiapfood.core.usecases.integracao.usuario;

import br.com.fiapfood.core.entities.dto.paginacao.PaginacaoCoreDto;
import br.com.fiapfood.core.exceptions.usuario.UsuarioNaoEncontradoException;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.impl.UsuarioGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.usuario.impl.BuscarTodosUsuariosUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IBuscarTodosUsuariosUseCase;
import br.com.fiapfood.infraestructure.repositories.interfaces.IPerfilRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.IUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BuscarTodosUsuariosUseCaseIT {
    private final String USUARIOS_NAO_ENCONTRADOS = "Não foram encontrados usuários na base de dados para a página informada.";

    private IBuscarTodosUsuariosUseCase buscarTodosUsuariosUseCase;
    private IUsuarioGateway usuarioGateway;
    private IPerfilGateway perfilGateway;

    @Autowired
    IPerfilRepository perfilRepository;
    @Autowired
    IUsuarioRepository usuarioRepository;

    @BeforeEach
    public void setUp() {
        perfilGateway = new PerfilGateway(perfilRepository);
        usuarioGateway = new UsuarioGateway(usuarioRepository);

        buscarTodosUsuariosUseCase = new BuscarTodosUsuariosUseCase(usuarioGateway, perfilGateway);
    }

    @DisplayName("Deve buscar todos os usuários com sucesso.")
    @Test
    void deveBuscarTodosUsuariosComSucesso() {
        // Arrange
        int pagina = 1;

        // Act
        var todosUsuarios = buscarTodosUsuariosUseCase.buscar(pagina);

        // Assert
        assertThat(todosUsuarios).isNotNull();
        assertThat(todosUsuarios.usuarios()).isNotNull();
        assertThat(todosUsuarios.usuarios()).isInstanceOf(List.class);
        assertThat(todosUsuarios.paginacao()).isNotNull();
        assertThat(todosUsuarios.paginacao()).isInstanceOf(PaginacaoCoreDto.class);
    }

    @DisplayName("Deve buscar todos os usuários com erro. Nenhum usuário encontrado na pagina.")
    @Test
    @Sql(scripts = "/db_clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // Limpando o BD para retornar o erro
    void deveLancarExcecaoSeNenhumUsuarioEncontradoNaPagina() {
        // Arrange
        int pagina = 100;

        // Act & Assert
        assertThatThrownBy(() -> buscarTodosUsuariosUseCase.buscar(pagina))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(USUARIOS_NAO_ENCONTRADOS);
    }
}
