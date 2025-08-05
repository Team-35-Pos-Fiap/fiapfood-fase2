package br.com.fiapfood.core.usecases.integracao.usuario;

import br.com.fiapfood.core.exceptions.usuario.AtualizacaoStatusUsuarioNaoPermitidaException;
import br.com.fiapfood.core.exceptions.usuario.UsuarioNaoEncontradoException;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.impl.UsuarioGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.usuario.impl.ReativarUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IReativarUsuarioUseCase;
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
public class ReativarUsuarioUseCaseIT {

    private final String REATIVACAO_NAO_PERMITIDA = "Não é possível reativar um usuário, pois ele já se encontra ativo.";
    private final String USUARIO_NAO_ENCONTRADO = "Não foi encontrado nenhum usuário.";

    private IReativarUsuarioUseCase reativarUsuarioUseCase;
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

        reativarUsuarioUseCase = new ReativarUsuarioUseCase(usuarioGateway, perfilGateway);
    }

    @DisplayName("Deve reativar usuário com sucesso.")
    @Test
    void deveReativarUsuarioComSucesso(){
        // Arrange
        UUID usuarioId = UUID.fromString("602a4056-68d0-44f0-8284-14b0cf7a75b6");

        // Act
        reativarUsuarioUseCase.reativar(usuarioId);

        var usuarioAposAtualizacao = usuarioGateway.buscarPorId(usuarioId);

        // Assert
        assertThat(usuarioAposAtualizacao).isNotNull();
        assertThat(usuarioAposAtualizacao.getId()).isEqualTo(usuarioId);
        assertThat(usuarioAposAtualizacao.getIsAtivo()).isTrue();
    }

    @DisplayName("Deve reativar usuário com erro. Usuário não encontrado através do ID")
    @Test
    void deveLancarExcecaoSeNaoEncontrarUsuarioAtravesDoID(){
        // Arrange
        UUID usuarioId = UUID.fromString("0fc5db14-7993-4564-bff9-c258b5c73c78");

        // Act & Assert
        assertThatThrownBy(() -> reativarUsuarioUseCase.reativar(usuarioId))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(USUARIO_NAO_ENCONTRADO);
    }

    @DisplayName("Deve reativar usuário com erro. Usuário encontrado já está ativo")
    @Test
    void deveLancarExcecaoSeNaoEncontrarUsuarioEncontradoEstiverInativo(){
        // Arrange
        UUID usuarioId = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");

        // Act & Assert
        assertThatThrownBy(() -> reativarUsuarioUseCase.reativar(usuarioId))
                .isInstanceOf(AtualizacaoStatusUsuarioNaoPermitidaException.class)
                .hasMessage(REATIVACAO_NAO_PERMITIDA);
    }
}
