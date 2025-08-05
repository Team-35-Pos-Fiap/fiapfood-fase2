package br.com.fiapfood.core.entities.unitarios;

import br.com.fiapfood.core.entities.Endereco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static br.com.fiapfood.utils.CoreEntityDataGenerator.coreEnderecoEntityValido;


public class EnderecoTest {

    private Endereco endereco;

    @BeforeEach
    void setUp() {
        endereco = coreEnderecoEntityValido();
    }

    @Nested
    class AtualizarDados {

        @Test
        void deveAtualizarTodosOsCamposComSucesso() {
            // Arrange
            String novoEndereco = "Av. Paulista";
            String novaCidade = "São Paulo";
            String novoBairro = "Bela Vista";
            String novoEstado = "SP";
            Integer novoNumero = 1000;
            String novoCep = "01310-000";
            String novoComplemento = "Bloco B";

            // Act
            endereco.atualizarDados(novoEndereco, novaCidade, novoBairro, novoEstado, novoNumero, novoCep, novoComplemento);

            // Assert
            assertThat(endereco.getEndereco()).isEqualTo(novoEndereco);
            assertThat(endereco.getCidade()).isEqualTo(novaCidade);
            assertThat(endereco.getBairro()).isEqualTo(novoBairro);
            assertThat(endereco.getEstado()).isEqualTo(novoEstado);
            assertThat(endereco.getNumero()).isEqualTo(novoNumero);
            assertThat(endereco.getCep()).isEqualTo(novoCep);
            assertThat(endereco.getComplemento()).isEqualTo(novoComplemento);
        }
    }
}
