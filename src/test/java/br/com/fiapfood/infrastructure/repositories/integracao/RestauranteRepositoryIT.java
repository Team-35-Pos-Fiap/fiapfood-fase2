package br.com.fiapfood.infrastructure.repositories.integracao;

import br.com.fiapfood.core.entities.dto.restaurante.DadosRestauranteDto;
import br.com.fiapfood.core.entities.dto.restaurante.RestaurantePaginacaoInputDto;
import br.com.fiapfood.infraestructure.entities.RestauranteEntity;
import br.com.fiapfood.infraestructure.repositories.impl.RestauranteRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static br.com.fiapfood.utils.EntityDataGenerator.restauranteEntityValido;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class RestauranteRepositoryIT {

    @Autowired
    private RestauranteRepository restauranteRepository;

    private RestauranteEntity restauranteEntity;

    @BeforeEach
    void setUp() {
        restauranteEntity = restauranteEntityValido();
    }

    @Test
    void deveSalvarRestauranteComSucesso() {
        // Act
        restauranteRepository.salvarRestaurante(restauranteEntity);

        // Assert
        DadosRestauranteDto dadosRestauranteDtoSalvo = restauranteRepository.buscarPorId(restauranteEntity.getId());
        assertThat(dadosRestauranteDtoSalvo).isNotNull();
        assertThat(dadosRestauranteDtoSalvo.id()).isNotNull();
        assertThat(dadosRestauranteDtoSalvo.nome()).isEqualTo(restauranteEntity.getNome());
        assertThat(dadosRestauranteDtoSalvo.isAtivo()).isTrue();
    }

    @Test
    void deveBuscarRestaurantePorId() {
        // Arrange
        UUID idRestauranteExistente = UUID.fromString("40d5955e-c0bd-41da-b434-e46fa69bda14");

        // Act
        DadosRestauranteDto dadosRestauranteDtoEncontrado = restauranteRepository.buscarPorId(idRestauranteExistente);

        // Assert
        assertThat(dadosRestauranteDtoEncontrado).isNotNull();
        assertThat(dadosRestauranteDtoEncontrado.id()).isEqualTo(idRestauranteExistente);
        assertThat(dadosRestauranteDtoEncontrado.id()).isInstanceOf(UUID.class);
    }

    @Test
    void deveRetornarNuloAoBuscarRestaurantePorIdInexistente() {
        // Arrange
        UUID idRestauranteInexistente = UUID.randomUUID();

        // Act
        DadosRestauranteDto dadosRestauranteDtoEncontrado = restauranteRepository.buscarPorId(idRestauranteInexistente);

        // Assert
        assertThat(dadosRestauranteDtoEncontrado).isNull();
    }

    @Test
    void deveBuscarRestaurentesPaginadosComSucesso() {
        // Act
        RestaurantePaginacaoInputDto paginaRestaurantesInputDto = restauranteRepository.buscarRestaurantesComPaginacao(1);

        // Assert
        assertThat(paginaRestaurantesInputDto).isNotNull();
        assertThat(paginaRestaurantesInputDto.restaurantes()).isNotEmpty();
        assertThat(paginaRestaurantesInputDto.restaurantes().size()).isEqualTo(5);
        assertThat(paginaRestaurantesInputDto.paginacao().totalItens()).isEqualTo(5);
        assertThat(paginaRestaurantesInputDto.paginacao().paginaAtual()).isEqualTo(1);
        assertThat(paginaRestaurantesInputDto.paginacao().totalPaginas()).isEqualTo(1);
    }

    @Test
    @Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deveRetornarNuloRestaurentesPaginados(){
        // Act
        RestaurantePaginacaoInputDto paginaRestaurantesInputDto = restauranteRepository.buscarRestaurantesComPaginacao(1);

        // Assert
        assertThat(paginaRestaurantesInputDto).isNull();
    }
}
