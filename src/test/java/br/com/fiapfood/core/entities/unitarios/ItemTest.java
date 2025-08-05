package br.com.fiapfood.core.entities.unitarios;

import br.com.fiapfood.core.entities.Imagem;
import br.com.fiapfood.core.entities.Item;
import br.com.fiapfood.core.exceptions.item.NomeItemInvalidoException;
import br.com.fiapfood.core.exceptions.item.ValorItemInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;
import static br.com.fiapfood.utils.CoreEntityDataGenerator.coreItemEntityValido;
import static br.com.fiapfood.utils.CoreEntityDataGenerator.coreImagemEntityValido;

public class ItemTest {

    private Item item;

    private Imagem imagem;

    @BeforeEach
    void setUp() {
        item = coreItemEntityValido();
        imagem = coreImagemEntityValido();
    }

    @Nested
    class CriarItem {

        @Test
        void deveCriarItemComSucesso() {
            // Arrange
            UUID id = UUID.randomUUID();
            String nome = "Coca-Cola";
            String descricao = "Refrigerante lata 350ml";
            BigDecimal preco = BigDecimal.valueOf(6.90);
            boolean disponivelPresencial = true;
            boolean disponivel = true;

            // Act
            Item item = Item.criar(id, nome, descricao, preco, disponivelPresencial, disponivel, imagem);

            // Assert
            assertThat(item).isNotNull();
            assertThat(item.getNome()).isEqualTo(nome);
            assertThat(item.getDescricao()).isEqualTo(descricao);
            assertThat(item.getPreco()).isEqualByComparingTo(preco);
            assertThat(item.getIsDisponivelConsumoPresencial()).isTrue();
            assertThat(item.getIsDisponivel()).isTrue();
            assertThat(item.getImagem()).isEqualTo(imagem);
        }
    }

    @Nested
    class ValidarItem {

        @Test
        void deveValidarNomeValido() throws Exception {
            // Arrange
            var method = Item.class.getDeclaredMethod("validarNome", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatCode(() -> method.invoke(null, "Pizza")).doesNotThrowAnyException();
        }

        @Test
        void deveLancarExcecaoQuandoNomeForNulo() throws Exception {
            // Arrange
            var method = Item.class.getDeclaredMethod("validarNome", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatThrownBy(() -> method.invoke(null, (Object) null))
                    .hasCauseInstanceOf(NomeItemInvalidoException.class)
                    .hasRootCauseMessage("O nome do item informado é inválido.");
        }

        @Test
        void deveLancarExcecaoQuandoNomeForVazio() throws Exception {
            // Arrange
            var method = Item.class.getDeclaredMethod("validarNome", String.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatThrownBy(() -> method.invoke(null, ""))
                    .hasCauseInstanceOf(NomeItemInvalidoException.class)
                    .hasRootCauseMessage("O nome do item informado é inválido.");
        }

        @Test
        void deveValidarPrecoValido() throws Exception {
            // Arrange
            var method = Item.class.getDeclaredMethod("validarPreco", BigDecimal.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatCode(() -> method.invoke(null, BigDecimal.TEN)).doesNotThrowAnyException();
        }

        @Test
        void deveLancarExcecaoQuandoPrecoForNulo() throws Exception {
            // Arrange
            var method = Item.class.getDeclaredMethod("validarPreco", BigDecimal.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatThrownBy(() -> method.invoke(null, (Object) null))
                    .hasCauseInstanceOf(ValorItemInvalidoException.class)
                    .hasRootCauseMessage("O valor do item informado é inválido.");
        }

        @Test
        void deveLancarExcecaoQuandoPrecoForNegativo() throws Exception {
            // Arrange
            var method = Item.class.getDeclaredMethod("validarPreco", BigDecimal.class);
            method.setAccessible(true);

            // Act + Assert
            assertThatThrownBy(() -> method.invoke(null, BigDecimal.valueOf(-1)))
                    .hasCauseInstanceOf(ValorItemInvalidoException.class)
                    .hasRootCauseMessage("O valor do item não pode ser menor do que 0.");
        }
    }

    @Nested
    class AtualizarItem {

        @Test
        void deveAtualizarNome() {
            // Arrange
            String novoNome = "X-Tudo";

            // Act
            item.atualizarNome(novoNome);

            // Assert
            assertThat(item.getNome()).isEqualTo(novoNome);
        }

        @Test
        void deveAtualizarPreco() {
            // Arrange
            BigDecimal novoPreco = BigDecimal.valueOf(29.90);

            // Act
            item.atualizarPreco(novoPreco);

            // Assert
            assertThat(item.getPreco()).isEqualByComparingTo(novoPreco);
        }

        @Test
        void deveAtualizarDescricao() {
            // Arrange
            String novaDescricao = "Hambúrguer com tudo dentro";

            // Act
            item.atualizarDescricao(novaDescricao);

            // Assert
            assertThat(item.getDescricao()).isEqualTo(novaDescricao);
        }

        @Test
        void deveAtualizarDisponibilidadeConsumoPresencial() {
            // Arrange
            boolean isDisponivelConsumoPresencial = false;

            // Act
            item.atualizarDisponibilidadeConsumoPresencial(isDisponivelConsumoPresencial);

            // Assert
            assertThat(item.getIsDisponivelConsumoPresencial()).isFalse();
        }

        @Test
        void deveAtualizarDisponibilidade() {
            // Arrange
            boolean isDisponivel = false;

            // Act
            item.atualizarDisponibilidade(isDisponivel);

            // Assert
            assertThat(item.getIsDisponivel()).isFalse();
        }

        @Test
        void deveAtualizarImagem() {
            // Arrange
            Imagem novaImagem = Imagem.criar(UUID.randomUUID(), "novo.png", new byte[]{9}, "image/png");

            // Act
            item.atualizarImagem(novaImagem);

            // Assert
            assertThat(item.getImagem()).isEqualTo(novaImagem);
        }
    }

    @Nested
    class ComparacaoItem {

        @Test
        void deveRetornarTrueQuandoIdsForemIguais() {
            // Arrange
            UUID id = UUID.randomUUID();
            Item item1 = Item.criar(id, "A", "", BigDecimal.ONE, true, true, null);
            Item item2 = Item.criar(id, "B", "", BigDecimal.ONE, true, true, null);

            // Assert
            assertThat(item1).isEqualTo(item2);
            assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
        }

        @Test
        void deveRetornarFalseQuandoIdsForemDiferentes() {
            // Arrange
            Item item1 = Item.criar(UUID.randomUUID(), "A", "", BigDecimal.ONE, true, true, null);
            Item item2 = Item.criar(UUID.randomUUID(), "A", "", BigDecimal.ONE, true, true, null);

            // Assert
            assertThat(item1).isNotEqualTo(item2);
        }
    }
}
