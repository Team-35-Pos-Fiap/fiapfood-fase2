package br.com.fiapfood.infrastructure.repositories.integracao;

import br.com.fiapfood.core.entities.dto.tipo_culinaria.TipoCulinariaCoreDto;
import br.com.fiapfood.infraestructure.entities.TipoCulinariaEntity;
import br.com.fiapfood.infraestructure.repositories.impl.TipoCulinariaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static br.com.fiapfood.utils.EntityDataGenerator.tipoCulinariaEntityValido;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class TipoCulinariaRepositoryIT {

    @Autowired
    private TipoCulinariaRepository tipoCulinariaRepository;

    private TipoCulinariaEntity tipoCulinariaEntity;

    @BeforeEach
    void setUp() {
        tipoCulinariaEntity = tipoCulinariaEntityValido();
    }

    @Test
    void deveSalvarTipoCulinariaComSucesso() {
        // Act
        tipoCulinariaRepository.salvar(tipoCulinariaEntity);

        // Assert
        TipoCulinariaCoreDto tipoCulinariaCoreDtoEncontrado  = tipoCulinariaRepository.buscarPorId(tipoCulinariaEntity.getId());
        assertThat(tipoCulinariaCoreDtoEncontrado).isNotNull();
        assertThat(tipoCulinariaCoreDtoEncontrado.id()).isNotNull();
        assertThat(tipoCulinariaCoreDtoEncontrado.id()).isEqualTo(tipoCulinariaEntity.getId());
        assertThat(tipoCulinariaCoreDtoEncontrado.nome()).isEqualTo(tipoCulinariaEntity.getNome());
    }

    @Test
    void deveBuscarTipoCulinariaPorId() {
        // Arrange
        Integer tipoCulinariaIdExistente = 1;

        // Act
        TipoCulinariaCoreDto tipoCulinariaCoreDtoEncontrado  = tipoCulinariaRepository.buscarPorId(tipoCulinariaIdExistente);

        // Assert
        assertThat(tipoCulinariaCoreDtoEncontrado).isNotNull();
        assertThat(tipoCulinariaCoreDtoEncontrado.id()).isEqualTo(tipoCulinariaIdExistente);
    }

    @Test
    void deveRetornarNuloAoBuscarTipoCulinariaPorIdInexistente() {
        // Arrange
        Integer tipoCulinariaIdInexistente = 9999;

        // Act
        TipoCulinariaCoreDto tipoCulinariaCoreDtoEncontrado  = tipoCulinariaRepository.buscarPorId(tipoCulinariaIdInexistente);

        // Assert
        assertThat(tipoCulinariaCoreDtoEncontrado).isNull();
    }

    @Test
    void deveBuscarTodosTipoCulinariaComSucesso() {
        // Act
        List<TipoCulinariaCoreDto> tipoCulinariaCoreDtos = tipoCulinariaRepository.buscarTodos();

        // Assert
        assertThat(tipoCulinariaCoreDtos).isNotNull();
        assertThat(tipoCulinariaCoreDtos).isNotEmpty();
        assertThat(tipoCulinariaCoreDtos.size()).isEqualTo(5);
    }

    @Test
    @Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deveRetornarVazioAoBuscarTodosTipoCulinaria() {
        // Act
        List<TipoCulinariaCoreDto> tipoCulinariaCoreDtos = tipoCulinariaRepository.buscarTodos();

        // Assert
        assertThat(tipoCulinariaCoreDtos).isEmpty();
        assertThat(tipoCulinariaCoreDtos.size()).isEqualTo(0);
    }

    @Test
    void deveVerificarNomeJaCadastrado() {
        // Arrange
        String nomeJaCadastrado = "Brasileira";

        // Act
        boolean existeNome = tipoCulinariaRepository.nomeJaCadastrado(nomeJaCadastrado);

        // Assert
        assertThat(existeNome).isTrue();
    }

    @Test
    void deveVerificarNomeNaoCadastrado() {
        // Arrange
        String nomeJaCadastrado = "Mexicana";

        // Act
        boolean existeNome = tipoCulinariaRepository.nomeJaCadastrado(nomeJaCadastrado);

        // Assert
        assertThat(existeNome).isFalse();
    }
}
