package br.com.fiapfood.infrastructure.entities.integracao;

import br.com.fiapfood.infraestructure.entities.EnderecoEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class EnderecoEntityIT {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void deveCadastrarEnderecoComSucesso() {
        // Arrange
        EnderecoEntity endereco = new EnderecoEntity();
        endereco.setEndereco("Rua das Palmeiras");
        endereco.setCidade("Rio de Janeiro");
        endereco.setBairro("Copacabana");
        endereco.setEstado("RJ");
        endereco.setNumero(100);
        endereco.setCep("22070-900");
        endereco.setComplemento("Apto 201");

        // Act
        EnderecoEntity enderecoSalvo = entityManager.persistAndFlush(endereco);

        // Assert
        assertThat(enderecoSalvo).isNotNull();
        assertThat(enderecoSalvo.getId()).isNotNull();
        assertThat(enderecoSalvo.getEndereco()).isEqualTo("Rua das Palmeiras");
        assertThat(enderecoSalvo.getCidade()).isEqualTo("Rio de Janeiro");
        assertThat(enderecoSalvo.getBairro()).isEqualTo("Copacabana");
        assertThat(enderecoSalvo.getEstado()).isEqualTo("RJ");
        assertThat(enderecoSalvo.getNumero()).isEqualTo(100);
        assertThat(enderecoSalvo.getCep()).isEqualTo("22070-900");
        assertThat(enderecoSalvo.getComplemento()).isEqualTo("Apto 201");
    }

    @Test
    void deveGerarIdAutomaticamente() {
        // Arrange
        EnderecoEntity endereco = new EnderecoEntity();
        endereco.setEndereco("Rua A");
        endereco.setCidade("Cidade A");
        endereco.setBairro("Bairro A");
        endereco.setEstado("RJ");
        endereco.setCep("20000-000");

        assertThat(endereco.getId()).isNull();

        // Act
        EnderecoEntity enderecoSalvo = entityManager.persistAndFlush(endereco);

        // Assert
        assertThat(enderecoSalvo.getId()).isNotNull();
        assertThat(enderecoSalvo.getId()).isInstanceOf(UUID.class);

        entityManager.clear();
        EnderecoEntity enderecoRecuperado = entityManager.find(EnderecoEntity.class, enderecoSalvo.getId());

        assertThat(enderecoRecuperado).isNotNull();
        assertThat(enderecoRecuperado.getId()).isNotNull();
    }
}
