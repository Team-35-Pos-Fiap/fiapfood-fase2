package br.com.fiapfood.infrastructure.entities.integracao;

import br.com.fiapfood.infraestructure.entities.AtendimentoEntity;
import br.com.fiapfood.infraestructure.entities.ItemEntity;
import br.com.fiapfood.infraestructure.entities.RestauranteEntity;
import br.com.fiapfood.infraestructure.enums.Dia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@DataJpaTest
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AtendimentoEntityIT {

    @Autowired
    private TestEntityManager entityManager;

    private AtendimentoEntity atendimento;

    private RestauranteEntity restaurante;

    @BeforeEach
    void setUp() {
        criaAtendimentoVinculadoAoRestaurante();
    }

    @Test
    void deveSalvarAtendimentoVinculadoARestaurante() {
        // Act
        RestauranteEntity restauranteSalvo = entityManager.persistAndFlush(restaurante);

        // Assert
        assertThat(restauranteSalvo.getId()).isNotNull();
        assertThat(restauranteSalvo.getAtendimentos()).hasSize(1);

        AtendimentoEntity atendimentoSalvo = restauranteSalvo.getAtendimentos().getFirst();
        assertThat(atendimentoSalvo.getId()).isNotNull();
        assertThat(atendimentoSalvo.getDia()).isEqualTo(Dia.SEGUNDA_FEIRA);
    }

    @Test
    void deveGerarIDAutomaticamenteVinculadoARestaurante() {
        // Arrange
        assertThat(atendimento.getId()).isNull();

        // Act
        RestauranteEntity restauranteSalvo = entityManager.persistAndFlush(restaurante);

        // Assert
        AtendimentoEntity atendimentoSalvo = restauranteSalvo.getAtendimentos().getFirst();
        assertThat(atendimentoSalvo.getId()).isNotNull();
        assertThat(atendimentoSalvo.getId()).isInstanceOf(UUID.class);
    }

    @Test
    void deveAtualizarAtendimentoDia() {
        // Arrange
        RestauranteEntity restauranteSalvo = entityManager.persistAndFlush(restaurante);
        AtendimentoEntity atendimentoSalvo = restauranteSalvo.getAtendimentos().getFirst();
        atendimentoSalvo.setDia(Dia.QUARTA_FEIRA);

        // Act
        RestauranteEntity restauranteAtualizado = entityManager.persistAndFlush(restauranteSalvo);

        // Assert
        AtendimentoEntity atendimentoAtualizado = restauranteAtualizado.getAtendimentos().getFirst();
        assertThat(atendimentoAtualizado.getDia()).isEqualTo(Dia.QUARTA_FEIRA);
    }

    private void criaAtendimentoVinculadoAoRestaurante() {
        atendimento = new AtendimentoEntity();
        atendimento.setDia(Dia.SEGUNDA_FEIRA);
        atendimento.setInicioAtendimento(LocalTime.of(10, 0));
        atendimento.setTerminoAtendimento(LocalTime.of(18, 0));

        restaurante = new RestauranteEntity();
        restaurante.setNome("Restaurante Exemplo");
        restaurante.getAtendimentos().add(atendimento);
    }
}
