package br.com.fiapfood.core.entities.unitarios;

import br.com.fiapfood.core.entities.Imagem;
import br.com.fiapfood.core.exceptions.item.NomeImagemItemInvalidoException;
import br.com.fiapfood.core.exceptions.item.TamanhoNomeImagemItemInvalidoException;
import br.com.fiapfood.core.exceptions.item.TipoImagemItemInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import static org.assertj.core.api.Assertions.*;
import static br.com.fiapfood.utils.CoreEntityDataGenerator.coreImagemEntityValido;

public class ImagemTest {

    private Imagem imagem;

    @BeforeEach
    void setUp() {
        imagem = coreImagemEntityValido();
    }

    @Nested
    class CriarImagem {

        @Test
        void deveCriarImagemComSucesso() {
            // Arrange
            UUID id = UUID.randomUUID();
            String nome = "logo.png";
            byte[] conteudo = new byte[]{10, 20, 30};
            String tipo = "image/png";

            // Act
            Imagem imagem = Imagem.criar(id, nome, conteudo, tipo);

            // Assert
            assertThat(imagem).isNotNull();
            assertThat(imagem.getNome()).isEqualTo(nome);
            assertThat(imagem.getConteudo()).isEqualTo(conteudo);
            assertThat(imagem.getTipo()).isEqualTo(tipo);
        }
    }

    @Nested
    class ValidarImagem {

        @Test
        void deveValidarNomeValido() throws Exception {
            // Arrange
            var method = Imagem.class.getDeclaredMethod("validarNome", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatCode(() -> method.invoke(null, "foto.png")).doesNotThrowAnyException();
        }

        @Test
        void deveLancarExcecaoAoValidarNomeNulo() throws Exception {
            // Arrange
            var method = Imagem.class.getDeclaredMethod("validarNome", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatThrownBy(() -> method.invoke(null, (Object) null))
                    .hasCauseInstanceOf(NomeImagemItemInvalidoException.class)
                    .hasRootCauseMessage("O nome da imagem informado é inválido.");
        }

        @Test
        void deveLancarExcecaoAoValidarNomeVazio() throws Exception {
            // Arrange
            var method = Imagem.class.getDeclaredMethod("validarNome", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatThrownBy(() -> method.invoke(null, ""))
                    .hasCauseInstanceOf(NomeImagemItemInvalidoException.class)
                    .hasRootCauseMessage("O nome da imagem informado é inválido.");
        }

        @Test
        void deveValidarTamanhoNomeValido() throws Exception {
            // Arrange
            var method = Imagem.class.getDeclaredMethod("validarTamanhoNome", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatCode(() -> method.invoke(null, "arquivo_de_teste.png")).doesNotThrowAnyException();
        }

        @Test
        void deveLancarExcecaoQuandoNomeForMaiorQue50Caracteres() throws Exception {
            // Arrange
            var method = Imagem.class.getDeclaredMethod("validarTamanhoNome", String.class);
            method.setAccessible(true);
            String nomeLongo = "a".repeat(51);

            // Act + Assert
            assertThatThrownBy(() -> method.invoke(null, nomeLongo))
                    .hasCauseInstanceOf(TamanhoNomeImagemItemInvalidoException.class)
                    .hasRootCauseMessage("O tamanho da imagem excede a quantidade de 50 caracteres.");
        }

        @Test
        void deveValidarTipoValido() throws Exception {
            // Arrange
            var method = Imagem.class.getDeclaredMethod("validarTipo", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatCode(() -> method.invoke(null, "image/jpeg")).doesNotThrowAnyException();
        }

        @Test
        void deveLancarExcecaoParaTipoNaoPermitido() throws Exception {
            // Arrange
            var method = Imagem.class.getDeclaredMethod("validarTipo", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatThrownBy(() -> method.invoke(null, "application/pdf"))
                    .hasCauseInstanceOf(TipoImagemItemInvalidoException.class)
                    .hasRootCauseMessage("O tipo do documento não é suportado. Os tipos suportados são jpg, jpeg e png.");
        }
    }

    @Nested
    class AtualizarImagem {

        @Test
        void deveAtualizarNomeComSucesso() {
            // Arrange
            String novoNome = "nova_foto/jpg";

            // Act
            imagem.atualizarNome(novoNome);

            // Assert
            assertThat(imagem.getNome()).isEqualTo(novoNome);
        }

        @Test
        void deveAtualizarTipoComSucesso() {
            // Arrange
            String novoTipo = "image/jpeg";

            // Act
            imagem.atualizarTipo(novoTipo);

            // Assert
            assertThat(imagem.getTipo()).isEqualTo(novoTipo);
        }

        @Test
        void deveAtualizarConteudoComSucesso() {
            // Arrange
            byte[] novoConteudo = new byte[]{9, 8, 7};

            // Act
            imagem.atualizarConteudo(novoConteudo);

            // Assert
            assertThat(imagem.getConteudo()).isEqualTo(novoConteudo);
        }
    }
}
