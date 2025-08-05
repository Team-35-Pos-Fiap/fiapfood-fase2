package br.com.fiapfood.infrastructure.entities.integracao;

import br.com.fiapfood.infraestructure.entities.RestauranteEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import static org.assertj.core.api.Assertions.assertThat;
import static br.com.fiapfood.utils.EntityDataGenerator.restauranteEntityValido;

@DataJpaTest
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RestauranteEntityIT {

    @Autowired
    private TestEntityManager entityManager;

    private RestauranteEntity restaurante;

    @BeforeEach
    void setUp() {
        restaurante = restauranteEntityValido();
    }

    @Test
    void deveCadastrarRestauranteComSucesso() {
        // Arrange
        RestauranteEntity restaurante = new RestauranteEntity();
        restaurante.setNome("Restaurante Exemplo");

        // Act
        RestauranteEntity restauranteSalvo = entityManager.persistAndFlush(restaurante);

        // Assert
        assertThat(restauranteSalvo).isNotNull();
        assertThat(restauranteSalvo.getId()).isNotNull();
        assertThat(restauranteSalvo.getNome()).isEqualTo("Restaurante Exemplo");
    }

    @Test
    void deveAtualizarNomeRestaurante() {
        // Arrange
        RestauranteEntity restaurante = new RestauranteEntity();
        restaurante.setNome("Restaurante Exemplo");

        RestauranteEntity restauranteEncontrado = entityManager.persistAndFlush(restaurante);
        restauranteEncontrado.setNome("Restaurante Atualizado");

        // Act
        RestauranteEntity restauranteAtualizado = entityManager.persistAndFlush(restauranteEncontrado);

        // Assert
        assertThat(restauranteAtualizado.getNome()).isEqualTo("Restaurante Atualizado");
    }

    @Test
    void deveAlterarStatusAtivoRestaurante() {
        // Arrange
        RestauranteEntity restaurante = new RestauranteEntity();
        restaurante.setNome("Restaurante Exemplo");
        restaurante.setIsAtivo(false);
        RestauranteEntity restauranteSalvo = entityManager.persistAndFlush(restaurante);

        // Act
        restauranteSalvo.setIsAtivo(true);
        RestauranteEntity atualizado = entityManager.persistAndFlush(restauranteSalvo);

        // Assert
        assertThat(atualizado.getIsAtivo()).isTrue();
    }

}
