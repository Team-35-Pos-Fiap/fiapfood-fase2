package br.com.fiapfood.infrastructure.entities.integracao;

import br.com.fiapfood.infraestructure.entities.ImagemEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static br.com.fiapfood.utils.EntityDataGenerator.imagemEntityValida;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ImagemEntityIT {

    @Autowired
    private TestEntityManager entityManager;

    private ImagemEntity imagem;

    @BeforeEach
    void setUp() {
        imagem = imagemEntityValida();
    }

    @Test
    void deveSalvarImagemComSucesso() {
        // Act
        ImagemEntity imagemSalva = entityManager.persistAndFlush(imagem);

        // Assert
        assertThat(imagemSalva).isNotNull();
        assertThat(imagemSalva.getId()).isNotNull();
        assertThat(imagemSalva.getNome()).isEqualTo("exemplo.jpg");
        assertThat(imagemSalva.getTipo()).isEqualTo("image/jpeg");
        assertThat(imagemSalva.getConteudo()).isNotNull();
        assertThat(imagemSalva.getConteudo().length).isGreaterThan(0);
    }

    @Test
    void deveGerarIDAutomaticamente() {
        // Arrange
        assertThat(imagem.getId()).isNull();

        // Act
        ImagemEntity imagemSalva = entityManager.persistAndFlush(imagem);

        // Assert
        assertThat(imagemSalva.getId()).isNotNull();
        assertThat(imagemSalva.getId()).isInstanceOf(UUID.class);
    }

    @Test
    void deveAtualizarNomeDaImagem() {
        // Arrange
        ImagemEntity imagemSalva = entityManager.persistAndFlush(imagem);
        imagemSalva.setNome("novo_nome.png");

        // Act
        ImagemEntity atualizada = entityManager.persistAndFlush(imagemSalva);

        // Assert
        assertThat(atualizada.getNome()).isEqualTo("novo_nome.png");
    }

    @Test
    void deveExcluirImagemComSucesso() {
        // Arrange
        ImagemEntity imagemSalva = entityManager.persistAndFlush(imagem);
        UUID id = imagemSalva.getId();

        // Act
        entityManager.remove(imagemSalva);
        entityManager.flush();

        // Assert
        ImagemEntity imagemExcluida = entityManager.find(ImagemEntity.class, id);
        assertThat(imagemExcluida).isNull();
    }
}
