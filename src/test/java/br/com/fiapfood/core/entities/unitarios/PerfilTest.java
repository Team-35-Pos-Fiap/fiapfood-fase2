package br.com.fiapfood.core.entities.unitarios;

import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.exceptions.perfil.NomePerfilInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static br.com.fiapfood.utils.CoreEntityDataGenerator.corePerfilEntityValido;

public class PerfilTest {

    private Perfil perfil;

    @BeforeEach
    void setUp() {
        perfil = corePerfilEntityValido();
    }

    @Nested
    class CriarPerfil {

        @Test
        void deveCriarPerfilComSucesso() {
            // Arrange
            Integer id = 1;
            String nome = "Dono";
            LocalDate dataCriacao = LocalDate.now();

            // Act
            Perfil perfil = Perfil.criar(id, nome, dataCriacao, null);

            // Assert
            assertThat(perfil).isNotNull();
            assertThat(perfil.getId()).isEqualTo(id);
            assertThat(perfil.getNome()).isEqualTo(nome);
            assertThat(perfil.getDataCriacao()).isEqualTo(dataCriacao);
            assertThat(perfil.getDataInativacao()).isNull();
        }
    }

    @Nested
    class ValidarPerfil {

        @Test
        void deveValidarNomeValido() throws Exception {
            // Arrange
            var method = Perfil.class.getDeclaredMethod("validarNome", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatCode(() -> method.invoke(null, "Cliente")).doesNotThrowAnyException();
        }

        @Test
        void deveLancarExcecaoNomePerfilInvalidoAoValidarNomeNulo() throws Exception {
            // Arrange
            var method = Perfil.class.getDeclaredMethod("validarNome", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatThrownBy(() -> method.invoke(null, (Object) null))
                    .hasCauseInstanceOf(NomePerfilInvalidoException.class)
                    .hasRootCauseMessage("O nome do perfil é inválido.");
        }

        @Test
        void deveLancarExcecaoNomePerfilInvalidoAoValidarNomeVazio() throws Exception {
            // Arrange
            var method = Perfil.class.getDeclaredMethod("validarNome", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatThrownBy(() -> method.invoke(null, ""))
                    .hasCauseInstanceOf(NomePerfilInvalidoException.class)
                    .hasRootCauseMessage("O nome do perfil é inválido.");
        }
    }

    @Nested
    class AtualizarPerfil {

        @Test
        void deveAtualizarNomeComSucesso() {
            // Arrange
            String novoNome = "Gerente";

            // Act
            perfil.atualizarNome(novoNome);

            // Assert
            assertThat(perfil.getNome()).isEqualTo(novoNome);
        }

        @Test
        void deveInativarPerfilComSucesso() {
            // Act
            perfil.inativar();

            // Assert
            assertThat(perfil.getDataInativacao()).isEqualTo(LocalDate.now());
        }

        @Test
        void deveReativarPerfilComSucesso() {
            // Arrange
            perfil.inativar();

            // Act
            perfil.reativar();

            // Assert
            assertThat(perfil.getDataInativacao()).isNull();
        }
    }
}
