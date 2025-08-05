package br.com.fiapfood.core.entities.unitarios;

import br.com.fiapfood.core.entities.TipoCulinaria;
import br.com.fiapfood.core.exceptions.tipo_culinaria.NomeTipoCulinariaInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static br.com.fiapfood.utils.CoreEntityDataGenerator.coreTipoCulinariaEntityValido;

public class TipoCulinariaTest {

    private TipoCulinaria tipoCulinaria;

    @BeforeEach
    void setUp() {
        tipoCulinaria = coreTipoCulinariaEntityValido();
    }

    @Nested
    class CriarTipoCulinaria {

        @Test
        void deveCriarTipoCulinariaComSucesso() {
            // Arrange
            Integer id = 2;
            String nome = "Japonesa";

            // Act
            TipoCulinaria tipoCulinaria = TipoCulinaria.criar(id, nome);

            // Assert
            assertThat(tipoCulinaria).isNotNull();
            assertThat(tipoCulinaria).isInstanceOf(TipoCulinaria.class);
            assertThat(tipoCulinaria.getId()).isEqualTo(id);
            assertThat(tipoCulinaria.getNome()).isEqualTo(nome);
        }
    }

    @Nested
    class ValidarTipoCulinaria {

        @Test
        void deveValidarNomeValido() throws Exception {
            // Arrange
            String nomeValido = "Francesa";
            var method = TipoCulinaria.class.getDeclaredMethod("validarNome", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatCode(() -> method.invoke(null, nomeValido)).doesNotThrowAnyException();
        }

        @Test
        void deveLancarExcecaoNomeInvalidoAoValidarNomeNulo() throws Exception {
            // Arrange
            String nomeInvalido = null;
            var method = TipoCulinaria.class.getDeclaredMethod("validarNome", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatThrownBy(() -> method.invoke(null, nomeInvalido))
                    .hasCauseInstanceOf(NomeTipoCulinariaInvalidoException.class)
                    .hasRootCauseMessage("O nome do tipo de culinária é inválido.");
        }

        @Test
        void deveLancarExcecaoNomeInvalidoAoValidarNomeVazio() throws Exception {
            // Arrange
            String nomeInvalido = "";
            var method = TipoCulinaria.class.getDeclaredMethod("validarNome", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatThrownBy(() -> method.invoke(null, nomeInvalido))
                    .hasCauseInstanceOf(NomeTipoCulinariaInvalidoException.class)
                    .hasRootCauseMessage("O nome do tipo de culinária é inválido.");
        }
    }

    @Nested
    class AtualizarTipoCulinaria {

        @Test
        void deveAtualizarNomeComSucesso() {
            // Arrange
            String novoNome = "Chinesa";

            // Act
            tipoCulinaria.atualizarNome(novoNome);

            // Assert
            assertThat(tipoCulinaria.getNome()).isEqualTo(novoNome);
        }
    }
}
