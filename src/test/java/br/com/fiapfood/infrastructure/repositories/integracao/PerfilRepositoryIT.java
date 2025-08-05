package br.com.fiapfood.infrastructure.repositories.integracao;

import br.com.fiapfood.core.entities.dto.perfil.PerfilCoreDto;
import br.com.fiapfood.infraestructure.entities.PerfilEntity;
import br.com.fiapfood.infraestructure.repositories.impl.PerfilRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.jpa.IPerfilJpaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PerfilRepositoryIT {

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private IPerfilJpaRepository perfilJpaRepository;

    @Test
    void deveBuscarPorId() {
        // Arrange
        PerfilEntity perfilCliente = criaPerfilCliente();
        PerfilEntity perfilSalvo = perfilJpaRepository.save(perfilCliente);
        Integer idBusca = perfilSalvo.getId();

        // Act
        PerfilCoreDto perfilEncontrado = perfilRepository.buscarPorId(idBusca);

        // Assert
        assertThat(perfilEncontrado).isNotNull();
        assertThat(perfilEncontrado.id()).isEqualTo(idBusca);
        assertThat(perfilEncontrado.nome()).isEqualTo(perfilCliente.getNome());
    }

    @Test
    void deveRetornarNuloAoBuscarPorIdInexistente() {
        // Arrange
        Integer idInexistente = 9999;

        // Act
        PerfilCoreDto perfilEncontrado = perfilRepository.buscarPorId(idInexistente);

        // Assert
        assertThat(perfilEncontrado).isNull();
    }

    @Test
    void deveBuscarTodosPerfisCadastrados() {
        // Arrange
        PerfilEntity perfilCliente = criaPerfilCliente();
        PerfilEntity perfilDono = criaPerfilDono();

        perfilJpaRepository.save(perfilCliente);
        perfilJpaRepository.save(perfilDono);

        // Act
        List<PerfilCoreDto> listaPerfis = perfilRepository.buscarTodos();

        // Assert
        assertThat(listaPerfis).isNotNull();
        assertThat(listaPerfis).hasSize(2);
        assertThat(listaPerfis)
                .extracting(PerfilCoreDto::nome)
                .contains("Dono", "Cliente");
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaPerfis() {
        // Act
        List<PerfilCoreDto> resultado = perfilRepository.buscarTodos();

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
    }

    @Test
    void deveSalvarNovoPerfil() {
        // Arrange
        PerfilEntity novoPerfil = new PerfilEntity(null, "Novo perfil", LocalDate.now(), null);

        // Act
        PerfilEntity perfilSalvo = perfilJpaRepository.save(novoPerfil);

        // Assert
        assertThat(perfilSalvo).isNotNull();
        assertThat(perfilSalvo.getId()).isNotNull();
        assertThat(perfilSalvo.getNome()).isEqualTo("Novo perfil");

        Optional<PerfilEntity> perfilEncontrado = perfilJpaRepository.findById(perfilSalvo.getId());
        assertThat(perfilEncontrado).isPresent();
        assertThat(perfilEncontrado.get().getNome()).isEqualTo("Novo perfil");
    }

    @Test
    void deveAtualizarPerfilComSucesso() {
        // Arrange
        PerfilEntity perfilDono = criaPerfilDono();
        PerfilEntity perfilSalvo = perfilJpaRepository.save(perfilDono);

        perfilSalvo.setNome("Nome atualizado");

        // Act
        PerfilEntity perfilAtualizado = perfilJpaRepository.save(perfilSalvo);

        // Assert
        assertThat(perfilAtualizado).isNotNull();
        assertThat(perfilAtualizado.getId()).isEqualTo(perfilSalvo.getId());
        assertThat(perfilAtualizado.getNome()).isEqualTo("Nome atualizado");
    }

    @Test
    void deveChecarNomePerfilJaCadastrado() {
        // Arrange
        PerfilEntity perfilSalvo = perfilJpaRepository.save(criaPerfilCliente());
        String nomeExistente = perfilSalvo.getNome();

        // Act
        boolean resultadoExistente = perfilRepository.nomeJaCadastrado(nomeExistente);

        // Assert
        assertThat(resultadoExistente).isTrue();
    }

    @Test
    void deveChecarNomePerfilNaoCadastrado() {
        // Arrange
        String nomeInexistente = "Perfil Inexistente";

        // Act
        boolean resultadoInexistente = perfilRepository.nomeJaCadastrado(nomeInexistente);

        // Assert
        assertThat(resultadoInexistente).isFalse();
    }

    private PerfilEntity criaPerfilCliente() {
        return new PerfilEntity(
                null,
                "Cliente",
                LocalDate.now(),
                null
        );
    }

    private PerfilEntity criaPerfilDono() {
        return new PerfilEntity(
                null,
                "Dono",
                LocalDate.now(),
                null
        );
    }
}
