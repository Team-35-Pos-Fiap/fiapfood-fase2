package br.com.fiapfood.core.usecases.integracao.usuario;

import br.com.fiapfood.core.exceptions.usuario.AtualizacaoPerfilNaoPermitidaException;
import br.com.fiapfood.core.exceptions.usuario.UsuarioNaoEncontradoException;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.impl.UsuarioGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.usuario.impl.AtualizarPerfilUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarPerfilUsuarioUseCase;
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
public class AtualizarPerfilUsuarioUseCaseIT {

    private final String USUARIO_INATIVO = "Não é possível alterar o perfil de um usuário inativo.";
    private final String PERFIL_DUPLICADO = "O perfil selecionado é o mesmo que o usuário já possui.";
    private final String USUARIO_NAO_ENCONTRADO = "Não foi encontrado nenhum usuário.";

    private IAtualizarPerfilUsuarioUseCase atualizarPerfilUsuarioUseCase;
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

        atualizarPerfilUsuarioUseCase = new AtualizarPerfilUsuarioUseCase(usuarioGateway, perfilGateway);
    }

    @DisplayName("Deve atualizar o perfil do usuário com sucesso.")
    @Test
    void deveAtualizarPerfilUsuarioComSucesso() {
        // Arrange
        UUID usuarioId = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
        int idPerfil = 3;

        // Act
        atualizarPerfilUsuarioUseCase.atualizar(usuarioId, idPerfil);

        var usuarioAposAtualizacao = usuarioGateway.buscarPorId(usuarioId);

        // Assert
        assertThat(usuarioAposAtualizacao).isNotNull();
        assertThat(usuarioAposAtualizacao.getIdPerfil()).isEqualTo(idPerfil);
    }

    @DisplayName("Deve atualizar o perfil do usuário com erro. Usuário não encontrado através do ID.")
    @Test
    void deveLancarExcecaoSeUsuairoNaoEncontradoAtravesDoID() {
        // Arrange
        UUID usuarioId = UUID.fromString("0fc5db14-7993-4564-bff9-c258b5c73c78");
        int idPerfil = 3;

        // Act & Assert
        assertThatThrownBy(() -> atualizarPerfilUsuarioUseCase.atualizar(usuarioId, idPerfil))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(USUARIO_NAO_ENCONTRADO);
    }

    @DisplayName("Deve atualizar o perfil do usuário com erro. Usuário encontrado está inativo.")
    @Test
    void deveLancarExcecaoSeUsuairoEstiverInativo() {
        // Arrange
        UUID usuarioId = UUID.fromString("602a4056-68d0-44f0-8284-14b0cf7a75b6");
        int idPerfil = 3;

        // Act & Assert
        assertThatThrownBy(() -> atualizarPerfilUsuarioUseCase.atualizar(usuarioId, idPerfil))
                .isInstanceOf(AtualizacaoPerfilNaoPermitidaException.class)
                .hasMessage(USUARIO_INATIVO);
    }

    @DisplayName("Deve atualizar o perfil do usuário com erro. Novo perfil igual ao atual")
    @Test
    void deveLancarExcecaoSeNovoPerfilForIgualAoAtual() {
        // Arrange
        UUID usuarioId = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
        int idPerfil = 1;

        // Act & Assert
        assertThatThrownBy(() -> atualizarPerfilUsuarioUseCase.atualizar(usuarioId, idPerfil))
                .isInstanceOf(AtualizacaoPerfilNaoPermitidaException.class)
                .hasMessage(PERFIL_DUPLICADO);
    }
}
