package br.com.fiapfood.infrastructure.entities.integracao;

import br.com.fiapfood.infraestructure.entities.TipoCulinariaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import static br.com.fiapfood.utils.EntityDataGenerator.tipoCulinariaEntityValido;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class TipoCulinariaEntityIT {

    @Autowired
    private TestEntityManager entityManager;

    private TipoCulinariaEntity tipoCulinaria;

    @BeforeEach
    void setUp() {
        tipoCulinaria = tipoCulinariaEntityValido();
    }

    @Test
    void deveCriarTipoCulinariaComSucesso() {
        // Arrange
        TipoCulinariaEntity tipoCulinaria = new TipoCulinariaEntity();
        tipoCulinaria.setNome("Novo Tipo Culinaria");

        // Act
        TipoCulinariaEntity tipoSalvo = entityManager.persistAndFlush(tipoCulinaria);

        // Assert
        assertThat(tipoSalvo).isNotNull();
        assertThat(tipoSalvo.getId()).isNotNull();
        assertThat(tipoSalvo.getNome()).isEqualTo("Novo Tipo Culinaria");
    }

    @Test
    void deveGerarIdAutomaticamente() {
        // Arrange
        TipoCulinariaEntity tipoCulinaria = new TipoCulinariaEntity();
        tipoCulinaria.setNome("Novo Tipo Culinaria");

        assertThat(tipoCulinaria.getId()).isNull();

        // Act
        TipoCulinariaEntity tipoSalvo = entityManager.persistAndFlush(tipoCulinaria);

        // Assert
        assertThat(tipoSalvo).isNotNull();
        assertThat(tipoSalvo.getId()).isNotNull();
        assertThat(tipoSalvo.getId()).isInstanceOf(Integer.class);

        entityManager.clear();
        TipoCulinariaEntity tipoRecuperado = entityManager.find(TipoCulinariaEntity.class, tipoSalvo.getId());

        assertThat(tipoRecuperado).isNotNull();
        assertThat(tipoRecuperado.getId()).isNotNull();
    }

    @Test
    void deveAtualizarNomeComSucesso() {
        // Arrange
        TipoCulinariaEntity tipoCulinaria = new TipoCulinariaEntity();
        tipoCulinaria.setNome("Antigo Nome");

        entityManager.persistAndFlush(tipoCulinaria);
        Integer id = tipoCulinaria.getId();

        // Act
        TipoCulinariaEntity tipoExistente = entityManager.find(TipoCulinariaEntity.class, id);
        tipoExistente.setNome("Novo Nome");
        entityManager.persistAndFlush(tipoExistente);

        // Assert
        TipoCulinariaEntity atualizado = entityManager.find(TipoCulinariaEntity.class, id);
        assertThat(atualizado.getNome()).isEqualTo("Novo Nome");
    }
}
