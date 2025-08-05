package br.com.fiapfood.core.usecases.integracao.usuario;

import br.com.fiapfood.core.exceptions.usuario.UsuarioNaoEncontradoException;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.impl.UsuarioGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.usuario.impl.BuscarUsuarioPorIdUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IBuscarUsuarioPorIdUseCase;
import br.com.fiapfood.infraestructure.repositories.interfaces.IPerfilRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.IUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BuscarUsuarioPorIdUseCaseIT {
    private final String USUARIO_NAO_ENCONTRADO = "Não foi encontrado nenhum usuário.";

    private IBuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;
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

        buscarUsuarioPorIdUseCase = new BuscarUsuarioPorIdUseCase(usuarioGateway, perfilGateway);
    }

    @DisplayName("Deve buscar usuário por ID com sucesso.")
    @Test
    void deveBuscarUsuarioPorIdComSucesso() {
        // Arrange
        UUID usuarioId = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");

        // Act
        var usuario = buscarUsuarioPorIdUseCase.buscar(usuarioId);

        // Assert
        assertThat(usuario).isNotNull();
        assertThat(usuario.id()).isEqualTo(usuarioId);
    }

    @DisplayName("Deve buscar usuário por ID com erro. Usuário não encontrado através do ID.")
    @Test
    void deveLancarExcecaoSeUsuarioNaoEncontradoAtravesDoID() {
        // Arrange
        UUID usuarioId = UUID.fromString("0fc5db14-7993-4564-bff9-c258b5c73c78");

        // Act & Assert
        assertThatThrownBy(() -> buscarUsuarioPorIdUseCase.buscar(usuarioId))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(USUARIO_NAO_ENCONTRADO);
    }
}
