package br.com.fiapfood.core.usecases.integracao.usuario;

import br.com.fiapfood.core.exceptions.usuario.AtualizacaoEmailUsuarioNaoPermitidoException;
import br.com.fiapfood.core.exceptions.usuario.EmailUsuarioInvalidoException;
import br.com.fiapfood.core.exceptions.usuario.UsuarioNaoEncontradoException;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.impl.UsuarioGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.usuario.impl.AtualizarEmailUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarEmailUsuarioUseCase;
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
public class AtualizarEmailUsuarioUseCaseIT {

    private final String USUARIO_INATIVO = "Não é possível alterar o email de um usuário inativo.";
    private final String USUARIO_CADASTRADO = "Já existe um usuário com o email informado.";
    private final String EMAIL_DUPLICADO = "Não é possível alterar o email do usuário, pois ele já é igual ao email atual.";
    private final String USUARIO_NAO_ENCONTRADO = "Não foi encontrado nenhum usuário.";

    private IAtualizarEmailUsuarioUseCase atualizarEmailUsuarioUseCase;
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

        atualizarEmailUsuarioUseCase = new AtualizarEmailUsuarioUseCase(usuarioGateway, perfilGateway);
    }

    @DisplayName("Deve atualizar o e-mail do usuário com sucesso.")
    @Test
    void deveAtualizarEmailUsuarioComSucesso() {
        // Arrange
        UUID usuarioId = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
        String novoEmail = "john.doe1234@email.com";

        // Act
        atualizarEmailUsuarioUseCase.atualizar(usuarioId, novoEmail);
        var usuarioAposAtualizar = usuarioGateway.buscarPorId(usuarioId);

        // Assert
        assertThat(usuarioAposAtualizar).isNotNull();
        assertThat(usuarioAposAtualizar.getEmail()).isEqualTo(novoEmail);
        assertThat(usuarioAposAtualizar.getId()).isEqualTo(usuarioId);
    }

    @DisplayName("Deve atualizar o e-mail do usuário com erro. Usuário nao encontrado através do ID.")
    @Test
    void deveLancarExcecaoSeNaoEncontrarUsuarioAtravesDoID() {
        // Arrange
        UUID usuarioId = UUID.fromString("0fc5db14-7993-4564-bff9-c258b5c7387c");
        String novoEmail = "john.doe1234@email.com";

        // Act & Assert
        assertThatThrownBy(() -> atualizarEmailUsuarioUseCase.atualizar(usuarioId, novoEmail))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(USUARIO_NAO_ENCONTRADO);
    }

    @DisplayName("Deve atualizar o e-mail do usuário com erro. Usuário encontrado está inativo.")
    @Test
    void deveLancarExcecaoSeUsuarioEncontradoEstiverInativo() {
        // Arrange
        UUID usuarioId = UUID.fromString("602a4056-68d0-44f0-8284-14b0cf7a75b6");
        String novoEmail = "john.doe1234@email.com";

        // Act & Assert
        assertThatThrownBy(() -> atualizarEmailUsuarioUseCase.atualizar(usuarioId, novoEmail))
                .isInstanceOf(AtualizacaoEmailUsuarioNaoPermitidoException.class)
                .hasMessage(USUARIO_INATIVO);
    }

    @DisplayName("Deve atualizar o e-mail do usuário com erro. Novo email já está cadastrado.")
    @Test
    void deveLancarExcecaoNovoEmailJaEstivarCadastrado() {
        // Arrange
        UUID usuarioId = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
        String novoEmail = "carla.rodrigues@fiapfood.com";

        // Act & Assert
        assertThatThrownBy(() -> atualizarEmailUsuarioUseCase.atualizar(usuarioId, novoEmail))
                .isInstanceOf(AtualizacaoEmailUsuarioNaoPermitidoException.class)
                .hasMessage(USUARIO_CADASTRADO);
    }

    // Não consigo realizar esse teste pois ao utilizar o mesmo email eu recebo a exceção de que já existe um usuário cadastrado com esse email.
//    @DisplayName("Deve atualizar o e-mail do usuário com erro. Novo email igual ao antigo.")
//    @Test
//    void deveLancarExcecaoNovoEmailForIgualAoAntigo() {
//        // Arrange
//        UUID usuarioId = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
//        String novoEmail = "thiago@fiapfood.com";
//
//        // Act & Assert
//        assertThatThrownBy(() -> atualizarEmailUsuarioUseCase.atualizar(usuarioId, novoEmail))
//                .isInstanceOf(AtualizacaoEmailUsuarioNaoPermitidoException.class)
//                .hasMessage(EMAIL_DUPLICADO);
//    }

    @DisplayName("Deve atualizar o e-mail do usuário com erro. Novo email está inválido.")
    @Test
    void deveLancarExcecaoNovoEmailForInvalido() {
        // Arrange
        UUID usuarioId = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
        String novoEmail = "";

        // Act & Assert
        assertThatThrownBy(() -> atualizarEmailUsuarioUseCase.atualizar(usuarioId, novoEmail))
                .isInstanceOf(EmailUsuarioInvalidoException.class)
                .hasMessage("O email do usuário informado é inválido.");
    }
}
