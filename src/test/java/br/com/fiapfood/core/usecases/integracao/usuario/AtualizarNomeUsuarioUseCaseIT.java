package br.com.fiapfood.core.usecases.integracao.usuario;

import br.com.fiapfood.core.exceptions.usuario.AtualizacaoNomeUsuarioNaoPermitidoException;
import br.com.fiapfood.core.exceptions.usuario.NomeUsuarioInvalidoException;
import br.com.fiapfood.core.exceptions.usuario.UsuarioInativoException;
import br.com.fiapfood.core.exceptions.usuario.UsuarioNaoEncontradoException;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.impl.UsuarioGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.usuario.impl.AtualizarNomeUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarNomeUsuarioUseCase;
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
public class AtualizarNomeUsuarioUseCaseIT {

    private final String USUARIO_DUPLICADO = "Não é possível alterar o nome do usuário, pois ele é igual ao nome do usuário.";
    private final String USUARIO_INATIVO = "Não é possível alterar o nome de um usuário inativo.";
    private final String USUARIO_NAO_ENCONTRADO = "Não foi encontrado nenhum usuário.";

    private IAtualizarNomeUsuarioUseCase atualizarNomeUsuarioUseCase;
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

        atualizarNomeUsuarioUseCase = new AtualizarNomeUsuarioUseCase(usuarioGateway, perfilGateway);
    }

    @DisplayName("Deve atualizar nome do usuário com sucesso.")
    @Test
    void deveAtualizarNomeDoUsuarioComSucesso() {
        // Arrange
        UUID usuarioId = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
        String novoNome = "Jane Doe";

        // Act
        atualizarNomeUsuarioUseCase.atualizar(usuarioId, novoNome);
        var usuarioDepoisDeAtualizar = usuarioGateway.buscarPorId(usuarioId);

        // Assert
        assertThat(usuarioDepoisDeAtualizar).isNotNull();
        assertThat(usuarioDepoisDeAtualizar.getId()).isEqualTo(usuarioId);
        assertThat(usuarioDepoisDeAtualizar.getNome()).isEqualTo(novoNome);
    }

    @DisplayName("Deve atualizar nome do usuário com erro. Usuário não encontrado através do ID")
    @Test
    void deveLancarExcecaoSeNaoEncontrarUsuarioAtravesDoID() {
        // Arrange
        UUID usuarioId = UUID.fromString("0fc5db14-7993-4564-bff9-c258b5c73c78");
        String novoNome = "Jane Doe";

        // Act & Assert
        assertThatThrownBy(() -> atualizarNomeUsuarioUseCase.atualizar(usuarioId, novoNome))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(USUARIO_NAO_ENCONTRADO);
    }

    @DisplayName("Deve atualizar nome do usuário com erro. Usuário está inativo.")
    @Test
    void deveLancarExcecaoSeUsuarioEstiverInativo() {
        // Arrange
        UUID usuarioId = UUID.fromString("602a4056-68d0-44f0-8284-14b0cf7a75b6");
        String novoNome = "Jane Doe";

        // Act & Assert
        assertThatThrownBy(() -> atualizarNomeUsuarioUseCase.atualizar(usuarioId, novoNome))
                .isInstanceOf(UsuarioInativoException.class)
                .hasMessage(USUARIO_INATIVO);
    }

    @DisplayName("Deve atualizar nome do usuário com erro. Novo nome igual ao antigo.")
    @Test
    void deveLancarExcecaoSeNovoNomeForIgualAoAntigo() {
        // Arrange
        UUID usuarioId = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
        String novoNome = "Thiago Motta";

        // Act & Assert
        assertThatThrownBy(() -> atualizarNomeUsuarioUseCase.atualizar(usuarioId, novoNome))
                .isInstanceOf(AtualizacaoNomeUsuarioNaoPermitidoException.class)
                .hasMessage(USUARIO_DUPLICADO);
    }

    @DisplayName("Deve atualizar nome do usuário com erro. Novo nome inválido.")
    @Test
    void deveLancarExcecaoSeNovoNomeForInvalido() {
        // Arrange
        UUID usuarioId = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
        String novoNome = "";

        // Act & Assert
        assertThatThrownBy(() -> atualizarNomeUsuarioUseCase.atualizar(usuarioId, novoNome))
                .isInstanceOf(NomeUsuarioInvalidoException.class)
                .hasMessage("O nome do usuário informado é inválido.");
    }
}
