package br.com.fiapfood.infrastructure.entities.integracao;

import br.com.fiapfood.infraestructure.entities.PerfilEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PerfilEntityIT {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void deveCadastrarPerfilComSucesso(){
        // Arrange
        PerfilEntity perfil = new PerfilEntity();
        perfil.setNome("Dono");
        perfil.setDataCriacao(LocalDate.now());
        perfil.setDataInativacao(null);

        // Act
        PerfilEntity perfilSalvo = entityManager.persistAndFlush(perfil);

        // Assert
        assertThat(perfilSalvo).isNotNull();
        assertThat(perfilSalvo.getId()).isNotNull();
        assertThat(perfilSalvo.getId()).isInstanceOf(Integer.class);
        assertThat(perfilSalvo.getNome()).isEqualTo("Dono");
        assertThat(perfilSalvo.getNome()).isInstanceOf(String.class);
    }

    @Test
    void deveGerarIdAutomaticamente(){
        // Arrange + Assert
        PerfilEntity perfil = new PerfilEntity();
        perfil.setNome("Dono");
        perfil.setDataCriacao(LocalDate.now());
        perfil.setDataInativacao(null);

        assertThat(perfil.getId()).isNull();
        // Act
        PerfilEntity perfilSalvo = entityManager.persistAndFlush(perfil);

        entityManager.clear();
        PerfilEntity perfilRecuperado = entityManager.find(PerfilEntity.class, perfilSalvo.getId());

        // Assert
        assertThat(perfilRecuperado).isNotNull();
        assertThat(perfilRecuperado.getId()).isNotNull();
    }
}
