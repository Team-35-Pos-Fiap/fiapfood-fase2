package br.com.fiapfood.core.entities.unitarios;

import br.com.fiapfood.core.entities.Login;
import br.com.fiapfood.core.exceptions.usuario.MatriculaInvalidaException;
import br.com.fiapfood.core.exceptions.usuario.SenhaUsuarioInvalidaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static br.com.fiapfood.utils.CoreEntityDataGenerator.coreLoginEntityValido;
import static org.assertj.core.api.Assertions.*;

public class LoginTest {

    private Login login;

    @BeforeEach
    void setUp() {
        login = coreLoginEntityValido();
    }

    @Nested
    class CriarLogin {

        @Test
        void deveCriarLoginComSucesso() {
            // Arrange
            UUID id = UUID.randomUUID();
            String matricula = "us0010";
            String senha = "123456";

            // Act
            Login login = Login.criar(id, matricula, senha);

            // Assert
            assertThat(login).isNotNull();
            assertThat(login.getMatricula()).isEqualTo(matricula);
            assertThat(login.getSenha()).isEqualTo(senha);
        }
    }

    @Nested
    class ValidarLogin {

        @Test
        void deveValidarMatriculaValida() throws Exception {
            // Arrange
            var method = Login.class.getDeclaredMethod("validarMatricula", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatCode(() -> method.invoke(null, "us0010")).doesNotThrowAnyException();
        }

        @Test
        void deveLancarExcecaoMatriculaInvalidaAoValidarMatriculaNula() throws Exception {
            // Arrange
            var method = Login.class.getDeclaredMethod("validarMatricula", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatThrownBy(() -> method.invoke(null, (Object) null))
                    .hasCauseInstanceOf(MatriculaInvalidaException.class)
                    .hasRootCauseMessage("A informação da matrícula não é válida.");
        }

        @Test
        void deveLancarExcecaoMatriculaInvalidaAoValidarMatriculaVazia() throws Exception {
            // Arrange
            var method = Login.class.getDeclaredMethod("validarMatricula", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatThrownBy(() -> method.invoke(null, ""))
                    .hasCauseInstanceOf(MatriculaInvalidaException.class)
                    .hasRootCauseMessage("A informação da matrícula não é válida.");
        }

        @Test
        void deveValidarSenhaValida() throws Exception {
            // Arrange
            var method = Login.class.getDeclaredMethod("validarSenha", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatCode(() -> method.invoke(null, "123456")).doesNotThrowAnyException();
        }

        @Test
        void deveLancarExcecaoSenhaInvalidaAoValidarSenhaNula() throws Exception {
            // Arrange
            var method = Login.class.getDeclaredMethod("validarSenha", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatThrownBy(() -> method.invoke(null, (Object) null))
                    .hasCauseInstanceOf(SenhaUsuarioInvalidaException.class)
                    .hasRootCauseMessage("A informação da senha não é válida.");
        }

        @Test
        void deveLancarExcecaoSenhaInvalidaAoValidarSenhaVazia() throws Exception {
            // Arrange
            var method = Login.class.getDeclaredMethod("validarSenha", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatThrownBy(() -> method.invoke(null, ""))
                    .hasCauseInstanceOf(SenhaUsuarioInvalidaException.class)
                    .hasRootCauseMessage("A informação da senha não é válida.");
        }
    }

    @Nested
    class AtualizarLogin {

        @Test
        void deveAtualizarSenhaComSucesso() {
            // Arrange
            String novaSenha = "novaSenha123";

            // Act
            login.atualizarSenha(novaSenha);

            // Assert
            assertThat(login.getSenha()).isEqualTo(novaSenha);
        }

        @Test
        void deveAtualizarMatriculaComSucesso() {
            // Arrange
            String novaMatricula = "us12345";

            // Act
            login.atualizarMatricula(novaMatricula);

            // Assert
            assertThat(login.getMatricula()).isEqualTo(novaMatricula);
        }
    }
}
