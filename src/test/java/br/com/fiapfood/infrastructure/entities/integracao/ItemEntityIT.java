package br.com.fiapfood.infrastructure.entities.integracao;

import br.com.fiapfood.infraestructure.entities.ItemEntity;
import br.com.fiapfood.infraestructure.entities.RestauranteEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ItemEntityIT {

    @Autowired
    private TestEntityManager entityManager;

    private ItemEntity item;

    private RestauranteEntity restaurante;

    @BeforeEach
    void setUp() {
        criaItemVinculadoAoRestaurante();
    }

    @Test
    void deveSalvarItemComSucessoVinculadoARestaurante() {
        // Act
        RestauranteEntity restauranteSalvo = entityManager.persistAndFlush(restaurante);

        // Assert
        assertThat(restauranteSalvo.getId()).isNotNull();
        assertThat(restauranteSalvo.getItens()).hasSize(1);

        ItemEntity itemSalvo = restauranteSalvo.getItens().getFirst();
        assertThat(itemSalvo.getId()).isNotNull();
        assertThat(itemSalvo.getNome()).isEqualTo("Hambúrguer");
        assertThat(itemSalvo.getDescricao()).isEqualTo("Pão brioche e carne");
        assertThat(itemSalvo.getIsDisponivelConsumoPresencial()).isTrue();
        assertThat(itemSalvo.getIsDisponivel()).isTrue();
        assertThat(itemSalvo.getPreco()).isEqualByComparingTo(new BigDecimal("29.90"));
    }

    @Test
    void deveGerarIDAutomaticamenteVinculadoARestaurante() {
        // Arrange
        assertThat(item.getId()).isNull();

        // Act
        RestauranteEntity restauranteSalvo = entityManager.persistAndFlush(restaurante);

        // Assert
        ItemEntity itemSalvo = restauranteSalvo.getItens().getFirst();
        assertThat(itemSalvo.getId()).isNotNull();
        assertThat(itemSalvo.getId()).isInstanceOf(UUID.class);
    }

    @Test
    void deveAtualizarPrecoItem() {
        // Arrange
        RestauranteEntity restauranteSalvo = entityManager.persistAndFlush(restaurante);
        ItemEntity itemSalvo = restauranteSalvo.getItens().getFirst();
        itemSalvo.setPreco(new BigDecimal("32.50"));

        // Act
        RestauranteEntity restauranteAtualizado = entityManager.persistAndFlush(restauranteSalvo);

        // Assert
        ItemEntity itemAtualizado = restauranteAtualizado.getItens().getFirst();
        assertThat(itemAtualizado.getPreco()).isEqualByComparingTo(new BigDecimal("32.50"));
    }

    private void criaItemVinculadoAoRestaurante() {
        item = new ItemEntity();
        item.setNome("Hambúrguer");
        item.setDescricao("Pão brioche e carne");
        item.setPreco(new BigDecimal("29.90"));
        item.setIsDisponivelConsumoPresencial(true);
        item.setIsDisponivel(true);

        restaurante = new RestauranteEntity();
        restaurante.setNome("Restaurante Exemplo");
        restaurante.getItens().add(item);
    }
}
