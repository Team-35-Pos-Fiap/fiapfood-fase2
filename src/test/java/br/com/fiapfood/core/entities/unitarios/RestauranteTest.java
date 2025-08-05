package br.com.fiapfood.core.entities.unitarios;

import br.com.fiapfood.core.entities.Atendimento;
import br.com.fiapfood.core.entities.Endereco;
import br.com.fiapfood.core.entities.Item;
import br.com.fiapfood.core.entities.Restaurante;
import br.com.fiapfood.core.exceptions.restaurante.DonoRestauranteInvalidoException;
import br.com.fiapfood.core.exceptions.restaurante.NomeRestauranteInvalidoException;
import br.com.fiapfood.core.exceptions.tipo_culinaria.TipoCulinariaInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static br.com.fiapfood.utils.CoreEntityDataGenerator.coreEnderecoEntityValido;
import static br.com.fiapfood.utils.CoreEntityDataGenerator.coreRestauranteEntityValido;
import static org.assertj.core.api.Assertions.*;

public class RestauranteTest {

    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        restaurante = coreRestauranteEntityValido();
    }

    @Nested
    class CriarRestaurante {

        @Test
        void deveCriarRestauranteComSucesso() {
            // Arrange
            UUID id = UUID.randomUUID();
            String nome = "Churrascaria Fogo";
            Endereco endereco = coreEnderecoEntityValido();
            UUID idDono = UUID.randomUUID();
            Integer idTipoCulinaria = 2;
            Boolean isAtivo = true;
            List<Atendimento> atendimentos = new ArrayList<>();
            List<Item> itens = new ArrayList<>();

            // Act
            Restaurante restaurante = Restaurante.criar(id, nome, endereco, idDono, idTipoCulinaria, isAtivo, atendimentos, itens);

            // Assert
            assertThat(restaurante).isNotNull();
            assertThat(restaurante.getNome()).isEqualTo(nome);
            assertThat(restaurante.getIdDonoRestaurante()).isEqualTo(idDono);
            assertThat(restaurante.getIdTipoCulinaria()).isEqualTo(idTipoCulinaria);
            assertThat(restaurante.getDadosEndereco()).isEqualTo(endereco);
            assertThat(restaurante.getAtendimentos()).isEmpty();
            assertThat(restaurante.getItens()).isEmpty();
        }
    }

    @Nested
    class ValidarRestaurante {

        @Test
        void deveValidarNomeValido() throws Exception {
            var method = Restaurante.class.getDeclaredMethod("validarNome", String.class);
            method.setAccessible(true);
            assertThatCode(() -> method.invoke(null, "Restaurante da Esquina")).doesNotThrowAnyException();
        }

        @Test
        void deveLancarExcecaoNomeInvalido() throws Exception {
            var method = Restaurante.class.getDeclaredMethod("validarNome", String.class);
            method.setAccessible(true);
            assertThatThrownBy(() -> method.invoke(null, ""))
                    .hasCauseInstanceOf(NomeRestauranteInvalidoException.class)
                    .hasRootCauseMessage("O nome do restaurante informado é inválido.");
        }

        @Test
        void deveValidarDonoValido() throws Exception {
            var method = Restaurante.class.getDeclaredMethod("validarUsuarioDono", UUID.class);
            method.setAccessible(true);
            assertThatCode(() -> method.invoke(null, UUID.randomUUID())).doesNotThrowAnyException();
        }

        @Test
        void deveLancarExcecaoDonoInvalido() throws Exception {
            var method = Restaurante.class.getDeclaredMethod("validarUsuarioDono", UUID.class);
            method.setAccessible(true);
            assertThatThrownBy(() -> method.invoke(null, new Object[]{null}))
                    .hasCauseInstanceOf(DonoRestauranteInvalidoException.class)
                    .hasRootCauseMessage("A identificação do dono do restaurante é inválida.");
        }

        @Test
        void deveValidarTipoCulinariaValido() throws Exception {
            var method = Restaurante.class.getDeclaredMethod("validarTipoCulinaria", Integer.class);
            method.setAccessible(true);
            assertThatCode(() -> method.invoke(null, 1)).doesNotThrowAnyException();
        }

        @Test
        void deveLancarExcecaoTipoCulinariaInvalido() throws Exception {
            var method = Restaurante.class.getDeclaredMethod("validarTipoCulinaria", Integer.class);
            method.setAccessible(true);
            assertThatThrownBy(() -> method.invoke(null, new Object[]{null}))
                    .hasCauseInstanceOf(TipoCulinariaInvalidoException.class)
                    .hasRootCauseMessage("A identificação do tipo de culinária é inválida.");
        }
    }

    @Nested
    class AtualizarRestaurante {

        @Test
        void deveInativarRestaurante() {
            // Act
            restaurante.inativar();

            // Assert
            assertThat(restaurante.getIsAtivo()).isFalse();
        }

        @Test
        void deveReativarRestaurante() {
            // Arrange
            restaurante.inativar();

            // Act
            restaurante.reativar();

            // Assert
            assertThat(restaurante.getIsAtivo()).isTrue();
        }

        @Test
        void deveAtualizarNome() {
            // Act
            restaurante.atualizarNome("Novo Nome");

            // Assert
            assertThat(restaurante.getNome()).isEqualTo("Novo Nome");
        }

        @Test
        void deveAtualizarDono() {
            // Act
            UUID novoDono = UUID.randomUUID();
            restaurante.atualizarDono(novoDono);

            // Assert
            assertThat(restaurante.getIdDonoRestaurante()).isEqualTo(novoDono);
        }

        @Test
        void deveAtualizarTipoCulinaria() {
            // Act
            restaurante.atualizarTipoCulinaria(5);

            // Assert
            assertThat(restaurante.getIdTipoCulinaria()).isEqualTo(5);
        }

        @Test
        void deveAtualizarEndereco() {
            // Act
            Endereco novoEndereco = coreEnderecoEntityValido();
            restaurante.atualizarEndereco(novoEndereco);

            // Assert
            assertThat(restaurante.getDadosEndereco()).isEqualTo(novoEndereco);
        }

        @Test
        void deveLimparListaDeAtendimentos() {
            // Arrange
            assertThat(restaurante.getAtendimentos()).isNotEmpty();
            assertThat(restaurante.getAtendimentos().size()).isEqualTo(1);

            // Act
            restaurante.limparAtendimentos();

            // Assert
            assertThat(restaurante.getAtendimentos()).isEmpty();
        }

        @Test
        void deveLimparListaDeItens() {
            // Assert
            assertThat(restaurante.getItens()).isNotEmpty();
            assertThat(restaurante.getItens().size()).isEqualTo(1);

            // Act
            restaurante.limparItens();

            // Assert
            assertThat(restaurante.getItens()).isEmpty();
        }
    }
}
