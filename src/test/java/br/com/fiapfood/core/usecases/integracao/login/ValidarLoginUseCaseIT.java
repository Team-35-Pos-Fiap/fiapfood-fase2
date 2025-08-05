package br.com.fiapfood.core.usecases.integracao.login;

import br.com.fiapfood.core.exceptions.usuario.UsuarioNaoEncontradoException;
import br.com.fiapfood.core.exceptions.usuario.UsuarioSemAcessoException;
import br.com.fiapfood.core.gateways.impl.UsuarioGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.login.impl.ValidarLoginUseCase;
import br.com.fiapfood.core.usecases.login.interfaces.IValidarAcessoUseCase;
import br.com.fiapfood.infraestructure.repositories.interfaces.IUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ValidarLoginUseCaseIT {

    private final String USUARIO_INATIVO = "Não é possível realizar o login para usuários inativos.";
    private final String USUARIO_NAO_ENCONTRADO = "Não foi encontrado nenhum usuário.";
    private final String ACESSO_LIBERADO = "Acesso liberado para o usuário ";

    private IValidarAcessoUseCase validarLoginUseCase;
    private IUsuarioGateway usuarioGateway;

    @Autowired
    IUsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        usuarioGateway = new UsuarioGateway(usuarioRepository);

        validarLoginUseCase = new ValidarLoginUseCase(usuarioGateway);
    }

    @DisplayName("Deve validar login com sucesso.")
    @Test
    void deveValidarUsuarioSucesso() {
        // Arrange
        String matricula = "us0001";
        String senha = "123";

        var usuario = usuarioGateway.buscarPorMatriculaSenha(matricula, senha);

        // Act
        var mensagem = validarLoginUseCase.validar(matricula, senha);

        // Assert
        assertThat(mensagem).isEqualTo(ACESSO_LIBERADO + usuario.getNome());
    }

    @DisplayName("Deve validar login com erro. Usuário não encontrado através da matricula e senha.")
    @Test
    void deveLancarExcecaoSeUsuarioNaoEncontradoAtravesDaMatriculaESenha(){
        // Arrange
        String matricula = "us0010";
        String senha = "us0002";

        // Act & Assert
        assertThatThrownBy(() -> validarLoginUseCase.validar(matricula, senha))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(USUARIO_NAO_ENCONTRADO);
    }

    @DisplayName("Deve validar login com erro. Usuário encontrado está inativo.")
    @Test
    void deveLancarExcecaoSeUsuarioEstiverInativo(){
        // Arrange
        String matricula = "us0003";
        String senha = "123";

        // Act & Assert
        assertThatThrownBy(() -> validarLoginUseCase.validar(matricula, senha))
                .isInstanceOf(UsuarioSemAcessoException.class)
                .hasMessage(USUARIO_INATIVO);
    }

    @DisplayName("Deve lançar exceção se a senha estiver incorreta")
    @Test
    void deveLancarExcecaoSeSenhaEstiverIncorreta() {
        String matricula = "us0001";
        String senhaErrada = "wrong";

        assertThatThrownBy(() -> validarLoginUseCase.validar(matricula, senhaErrada))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(USUARIO_NAO_ENCONTRADO);
    }
}
