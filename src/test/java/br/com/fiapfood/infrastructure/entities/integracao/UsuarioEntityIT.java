package br.com.fiapfood.infrastructure.entities.integracao;

import br.com.fiapfood.infraestructure.entities.EnderecoEntity;
import br.com.fiapfood.infraestructure.entities.LoginEntity;
import br.com.fiapfood.infraestructure.entities.PerfilEntity;
import br.com.fiapfood.infraestructure.entities.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import static org.assertj.core.api.Assertions.assertThat;
import static br.com.fiapfood.utils.EntityDataGenerator.enderecoEntityValido;
import static br.com.fiapfood.utils.EntityDataGenerator.perfilEntityValido;
import static br.com.fiapfood.utils.EntityDataGenerator.loginEntityValido;

import java.time.LocalDateTime;
import java.util.UUID;

@DataJpaTest
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UsuarioEntityIT {

    @Autowired
    private TestEntityManager entityManager;

    private UsuarioEntity usuario;

    @BeforeEach
    void setUp() {
        // Arrange
        usuario = new UsuarioEntity();
        usuario.setNome("Usuario Teste");
        usuario.setEmail("usuario.test@email.com");
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setIsAtivo(true);
        usuario.setDadosEndereco(enderecoEntityValido());
        usuario.setPerfil(perfilEntityValido());
        usuario.setDadosLogin(loginEntityValido());
    }

    @Test
    void deveCriarNovoUsuario() {
        // Arrange
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNome("João Silva");
        usuario.setEmail("joao.silva@gmail.com");
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setIsAtivo(true);
        usuario.setDadosEndereco(enderecoEntityValido());
        usuario.setPerfil(perfilEntityValido());
        usuario.setDadosLogin(loginEntityValido());

        // Act
        UsuarioEntity usuarioSalvo = entityManager.persistAndFlush(usuario);

        // Assert
        assertThat(usuarioSalvo).isNotNull();
        assertThat(usuarioSalvo.getId()).isNotNull();
        assertThat(usuarioSalvo.getNome()).isEqualTo("João Silva");
        assertThat(usuarioSalvo.getEmail()).isEqualTo("joao.silva@gmail.com");
        assertThat(usuarioSalvo.getIsAtivo()).isTrue();
    }

    @Test
    void deveGerarIdAutomaticamente() {
        // Assert
        assertThat(usuario.getId()).isNull();

        // Act
        UsuarioEntity usuarioSalvo = entityManager.persistAndFlush(usuario);

        // Assert
        assertThat(usuarioSalvo.getId()).isNotNull().isInstanceOf(UUID.class);
    }

    @Test
    void deveCriarRelacaoComOutrasTabelas() {
        // Act
        UsuarioEntity usuarioSalvo = entityManager.persistAndFlush(usuario);
        entityManager.clear();

        UsuarioEntity usuarioRecuperado = entityManager.find(UsuarioEntity.class, usuarioSalvo.getId());

        // Assert
        assertThat(usuarioRecuperado.getPerfil())
                .isNotNull()
                .extracting(PerfilEntity::getNome)
                .isEqualTo("Dono");

        assertThat(usuarioRecuperado.getDadosEndereco())
                .isNotNull()
                .extracting(EnderecoEntity::getCidade)
                .isEqualTo("São Gonçalo");

        assertThat(usuarioRecuperado.getDadosLogin())
                .isNotNull()
                .extracting(LoginEntity::getMatricula)
                .isEqualTo("us0010");
    }

    @Test
    void deveRemoverEnderecoQuandoUsuarioRemovido() {
        // Arrange
        UsuarioEntity usuarioSalvo = entityManager.persistAndFlush(usuario);
        UUID enderecoId = usuarioSalvo.getDadosEndereco().getId();

        // Act
        entityManager.remove(usuarioSalvo);
        entityManager.flush();
        entityManager.clear();

        // Assert
        EnderecoEntity endereco = entityManager.find(EnderecoEntity.class, enderecoId);
        assertThat(endereco).isNull();
    }

    @Test
    void deveRemoverLoginQuandoUsuarioRemovido() {
        // Arrange
        UsuarioEntity usuarioSalvo = entityManager.persistAndFlush(usuario);
        UUID loginId = usuarioSalvo.getDadosLogin().getId();

        // Act
        entityManager.remove(usuarioSalvo);
        entityManager.flush();
        entityManager.clear();

        // Assert
        LoginEntity login = entityManager.find(LoginEntity.class, loginId);
        assertThat(login).isNull();
    }
}
