package br.com.fiapfood.core.entities.unitarios;

import br.com.fiapfood.core.entities.Endereco;
import br.com.fiapfood.core.entities.Login;
import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.exceptions.perfil.PerfilInvalidoException;
import br.com.fiapfood.core.exceptions.usuario.EmailUsuarioInvalidoException;
import br.com.fiapfood.core.exceptions.usuario.NomeUsuarioInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static br.com.fiapfood.utils.CoreEntityDataGenerator.coreUsuarioEntityAtivoValido;
import static br.com.fiapfood.utils.CoreEntityDataGenerator.coreEnderecoEntityValido;
import static br.com.fiapfood.utils.CoreEntityDataGenerator.coreLoginEntityValido;
import static org.assertj.core.api.Assertions.*;

public class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = coreUsuarioEntityAtivoValido();
    }

    private void assertDataAtualizacao(Usuario usuario, LocalDateTime dataAntes) {
        assertThat(usuario.getDataAtualizacao())
                .isNotNull()
                .isAfterOrEqualTo(dataAntes)
                .isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Nested
    class CriarUsuario {

        @Test
        void deveCriarUsuarioComSucesso() {
            // Arrange
            Endereco endereco = coreEnderecoEntityValido();
            Login login = coreLoginEntityValido();

            // Act
            Usuario usuario = Usuario.criar(
                    UUID.randomUUID(),
                    "João da Silva",
                    1,
                    login,
                    true,
                    "test@email.com",
                    LocalDateTime.now(),
                    null,
                    endereco
            );

            // Assert
            assertThat(usuario).isNotNull();
            assertThat(usuario).isInstanceOf(Usuario.class);
        }
    }

    @Nested
    class ValidarUsuario {

        @Test
        void deveValidarNomeValido() throws Exception {
            // Arrange
            String nomeValido = ("João da Silva");
            var method =  Usuario.class.getDeclaredMethod("validarNome", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatCode(() -> method.invoke(null, nomeValido)).doesNotThrowAnyException();
        }

        @Test
        void deveLancarExcecaoNomeUsuarioInvalidoAoValidarNomeNulo() throws Exception {
            // Arrange
            String nomeInvalido = null;
            var method =  Usuario.class.getDeclaredMethod("validarNome", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatThrownBy(()-> method.invoke(null, nomeInvalido))
                    .hasCauseInstanceOf(NomeUsuarioInvalidoException.class)
                    .hasRootCauseMessage("O nome do usuário informado é inválido.");
        }

        @Test
        void deveLancarExcecaoNomeUsuarioInvalidoAoValidarNomeVazio() throws Exception {
            // Arrange
            String nomeInvalido = "";
            var method =  Usuario.class.getDeclaredMethod("validarNome", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatThrownBy(()-> method.invoke(null, nomeInvalido))
                    .hasCauseInstanceOf(NomeUsuarioInvalidoException.class)
                    .hasRootCauseMessage("O nome do usuário informado é inválido.");
        }

        @Test
        void deveValidarEmailValido() throws Exception {
            // Arrange
            String emailValido = "usuario@email.com";
            var method =  Usuario.class.getDeclaredMethod("validarEmail", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatCode(() -> method.invoke(null, emailValido)).doesNotThrowAnyException();
        }

        @Test
        void deveLancarExcecaoEmailUsuarioInvalidoAoValidarEmailNulo() throws Exception {
            // Arrange
            String emailInvalido = null;
            var method =  Usuario.class.getDeclaredMethod("validarEmail", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatThrownBy(()-> method.invoke(null, emailInvalido))
                    .hasCauseInstanceOf(EmailUsuarioInvalidoException.class)
                    .hasRootCauseMessage("O email do usuário informado é inválido.");
        }

        @Test
        void deveLancarExcecaoEmailUsuarioInvalidoAoValidarEmailVazio() throws Exception {
            // Arrange
            String emailInvalido = "";
            var method =  Usuario.class.getDeclaredMethod("validarEmail", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatThrownBy(()-> method.invoke(null, emailInvalido))
                    .hasCauseInstanceOf(EmailUsuarioInvalidoException.class)
                    .hasRootCauseMessage("O email do usuário informado é inválido.");
        }

        @Test
        void deveValidarPerfilValido() throws Exception {
            // Arrange
            Integer perfilValido = 1;
            var method =  Usuario.class.getDeclaredMethod("validarPerfil", Integer.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatCode(() -> method.invoke(null, perfilValido)).doesNotThrowAnyException();
        }

        @Test
        void deveLancarExcecaoPerfilInvalidoAoValidarPerfilNulo() throws Exception {
            // Arrange
            Integer perfilInvalido = null;
            var method =  Usuario.class.getDeclaredMethod("validarPerfil", Integer.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatThrownBy(()-> method.invoke(null, perfilInvalido))
                    .hasCauseInstanceOf(PerfilInvalidoException.class)
                    .hasRootCauseMessage("O perfil informado é inválido.");
        }

        @Test
        void deveLancarExcecaoPerfilInvalidoAoValidarPerfilComIdInvalido() throws Exception {
            // Arrange
            Integer perfilInvalido = -1;
            var method =  Usuario.class.getDeclaredMethod("validarPerfil", Integer.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatThrownBy(()-> method.invoke(null, perfilInvalido))
                    .hasCauseInstanceOf(PerfilInvalidoException.class)
                    .hasRootCauseMessage("O perfil informado é inválido.");
        }
    }

    @Nested
    class AtualizarUsuario {

        @Test
        void deveAtualizarIsAtivoParaFalse(){
            // Arrange
            LocalDateTime dataAntes = LocalDateTime.now();

            // Act
            usuario.inativar();

            // Assert
            assertThat(usuario.getIsAtivo()).isFalse();
            assertDataAtualizacao(usuario, dataAntes);
        }

        @Test
        void deveAtualizarIsAtivoParaTrue(){
            // Arrange
            LocalDateTime dataAntes = LocalDateTime.now();
            usuario.inativar();

            // Act
            usuario.reativar();

            // Assert
            assertThat(usuario.getIsAtivo()).isTrue();
            assertDataAtualizacao(usuario, dataAntes);
        }

        @Test
        void deveAtualizarPerfil() {
            // Arrange
            LocalDateTime dataAntes = LocalDateTime.now();
            Integer perfilId = 2;

            // Act
            usuario.atualizarPerfil(perfilId);

            // Assert
            assertThat(usuario.getIdPerfil()).isEqualTo(perfilId);
            assertDataAtualizacao(usuario, dataAntes);
        }

        @Test
        void deveAtualizarEmail() {
            // Arrange
            LocalDateTime dataAntes = LocalDateTime.now();
            String novoEmail = "novo@email.com";

            // Act
            usuario.atualizarEmail(novoEmail);

            // Assert
            assertThat(usuario.getEmail()).isEqualTo(novoEmail);
            assertDataAtualizacao(usuario, dataAntes);
        }

        @Test
        void deveAtualizarEndereco() {
            // Arrange
            LocalDateTime dataAntes = LocalDateTime.now();
            Endereco endereco = coreEnderecoEntityValido();
            String novoEndereco = endereco.getEndereco();
            String novoBairro = endereco.getBairro();
            String novaCidade = endereco.getCidade();
            String novoEstado = endereco.getEstado();
            String novoCep = endereco.getCep();
            String novoComplemento = endereco.getComplemento();
            Integer novoNumero = endereco.getNumero();

            // Act
            usuario.atualizarEndereco(endereco);

            // Assert
            assertThat(usuario.getDadosEndereco()).isNotNull();
            assertThat(usuario.getDadosEndereco().getEndereco()).isEqualTo(novoEndereco);
            assertThat(usuario.getDadosEndereco().getCidade()).isEqualTo(novaCidade);
            assertThat(usuario.getDadosEndereco().getBairro()).isEqualTo(novoBairro);
            assertThat(usuario.getDadosEndereco().getEstado()).isEqualTo(novoEstado);
            assertThat(usuario.getDadosEndereco().getNumero()).isEqualTo(novoNumero);
            assertThat(usuario.getDadosEndereco().getCep()).isEqualTo(novoCep);
            assertThat(usuario.getDadosEndereco().getComplemento()).isEqualTo(novoComplemento);
            assertDataAtualizacao(usuario, dataAntes);
        }

        @Test
        void deveAtualizarLogin() {
            // Arrange
            LocalDateTime dataAntes = LocalDateTime.now();
            Login login = coreLoginEntityValido();
            String novaMatricula = login.getMatricula();
            String novaSenha = login.getSenha();

            // Act
            usuario.atualizarLogin(login);

            // Assert
            assertThat(usuario.getLogin()).isNotNull();
            assertThat(usuario.getLogin().getMatricula()).isEqualTo(novaMatricula);
            assertThat(usuario.getLogin().getSenha()).isEqualTo(novaSenha);
            assertDataAtualizacao(usuario, dataAntes);
        }

        @Test
        void deveAtualizarNome() {
            // Arrange
            LocalDateTime dataAntes = LocalDateTime.now();
            String nome = "novoNome";

            // Act
            usuario.atualizarNome(nome);

            // Assert
            assertThat(usuario.getNome()).isNotNull();
            assertThat(usuario.getNome()).isEqualTo(nome);
            assertDataAtualizacao(usuario, dataAntes);
        }

        @Test
        void deveAtualizarDataAtualizacao() {
            // Arrange
            LocalDateTime dataAntes = LocalDateTime.now();

            // Act
            usuario.atualizarNome("Teste");

            // Assert
            assertDataAtualizacao(usuario, dataAntes);
        }
    }
}
