package br.com.fiapfood.core.entities.unitarios;

import br.com.fiapfood.core.entities.Atendimento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static br.com.fiapfood.utils.CoreEntityDataGenerator.coreAtendimentoEntityValido;

public class AtendimentoTest {

    private Atendimento atendimento;

    @BeforeEach
    void setUp() {
        atendimento = coreAtendimentoEntityValido();
    }

    @Nested
    class CriarAtendimento {

        @Test
        void deveCriarAtendimentoComSucesso() {
            // Arrange
            UUID id = UUID.randomUUID();
            String dia = "Terça-feira";
            LocalTime inicio = LocalTime.of(9, 0);
            LocalTime termino = LocalTime.of(18, 0);

            // Act
            Atendimento atendimento = Atendimento.criar(id, dia, inicio, termino);

            // Assert
            assertThat(atendimento).isNotNull();
            assertThat(atendimento).isInstanceOf(Atendimento.class);
            assertThat(atendimento.getId()).isEqualTo(id);
            assertThat(atendimento.getDia()).isEqualTo(dia);
            assertThat(atendimento.getInicioAtendimento()).isEqualTo(inicio);
            assertThat(atendimento.getTerminoAtendimento()).isEqualTo(termino);
        }
    }

    @Nested
    class AtualizarAtendimento {

        @Test
        void deveAtualizarDadosComSucesso() {
            // Arrange
            String novoDia = "Quarta-feira";
            LocalTime novoInicio = LocalTime.of(10, 30);
            LocalTime novoTermino = LocalTime.of(19, 30);

            // Act
            atendimento.atualizarDados(novoDia, novoInicio, novoTermino);

            // Assert
            assertThat(atendimento.getDia()).isEqualTo(novoDia);
            assertThat(atendimento.getInicioAtendimento()).isEqualTo(novoInicio);
            assertThat(atendimento.getTerminoAtendimento()).isEqualTo(novoTermino);
        }
    }

    @Nested
    class EqualsAndHashCode {

        @Test
        void deveConsiderarObjetosIguaisComMesmoId() {
            // Arrange
            UUID id = UUID.randomUUID();
            Atendimento a1 = Atendimento.criar(id, "Segunda", LocalTime.of(8, 0), LocalTime.of(17, 0));
            Atendimento a2 = Atendimento.criar(id, "Terça", LocalTime.of(9, 0), LocalTime.of(18, 0));

            // Act + Assert
            assertThat(a1).isEqualTo(a2);
            assertThat(a1.hashCode()).isEqualTo(a2.hashCode());
        }

        @Test
        void deveConsiderarObjetosDiferentesComIdsDiferentes() {
            // Arrange
            Atendimento a1 = Atendimento.criar(UUID.randomUUID(), "Segunda", LocalTime.of(8, 0), LocalTime.of(17, 0));
            Atendimento a2 = Atendimento.criar(UUID.randomUUID(), "Segunda", LocalTime.of(8, 0), LocalTime.of(17, 0));

            // Act + Assert
            assertThat(a1).isNotEqualTo(a2);
            assertThat(a1.hashCode()).isNotEqualTo(a2.hashCode());
        }
    }
}
