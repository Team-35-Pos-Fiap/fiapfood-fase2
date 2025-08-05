package br.com.fiapfood.infrastructure.repositories.integracao;

import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioInputDto;
import br.com.fiapfood.core.entities.dto.usuario.UsuarioPaginacaoInputDto;
import br.com.fiapfood.infraestructure.entities.UsuarioEntity;
import br.com.fiapfood.infraestructure.repositories.impl.UsuarioRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.jpa.IUsuarioJpaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static br.com.fiapfood.utils.EntityDataGenerator.usuarioEntityValido;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UsuarioRepositoryIT {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private IUsuarioJpaRepository usuarioJpaRepository;

    private UsuarioEntity usuarioEntity;

    @BeforeEach
    void setUp() {
        usuarioEntity = usuarioEntityValido();
    }

    @Test
    void deveSalvarNovoUsuarioComSucesso() {
        // Act
        usuarioRepository.salvar(usuarioEntity);

        // Assert
        DadosUsuarioInputDto usuarioSalvo = usuarioRepository.buscarPorId(usuarioEntity.getId());
        assertThat(usuarioSalvo).isNotNull();
        assertThat(usuarioSalvo.id()).isEqualTo(usuarioEntity.getId());
        assertThat(usuarioSalvo.nome()).isEqualTo(usuarioEntity.getNome());
        assertThat(usuarioSalvo.email()).isEqualTo(usuarioEntity.getEmail());
        assertThat(usuarioSalvo.isAtivo()).isEqualTo(usuarioEntity.getIsAtivo());
    }

    @Test
    void deveBuscarUsuarioPorId() {
        // Arrange
        UUID usuarioIdExistente = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");

        // Act
        DadosUsuarioInputDto usuarioEncontrado = usuarioRepository.buscarPorId(usuarioIdExistente);

        // Assert
        assertThat(usuarioEncontrado).isNotNull();
        assertThat(usuarioEncontrado.id()).isEqualTo(usuarioIdExistente);
        assertThat(usuarioEncontrado.nome()).isNotBlank();
        assertThat(usuarioEncontrado.email()).isNotBlank();
    }

    @Test
    void deveRetornarNuloAoBuscarUsuarioPorIdInexistente() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();

        // Act
        DadosUsuarioInputDto usuarioEncontrado = usuarioRepository.buscarPorId(idInexistente);

        // Assert
        assertThat(usuarioEncontrado).isNull();
    }

    @Test
    void deveBuscarUsuarioAtivoPorId() {
        // Arrange
        UUID usuarioIdExistente = UUID.fromString("b48bc2dc-fd87-462d-a8a6-6e74674d0338");

        // Act
        DadosUsuarioInputDto usuarioAtivo = usuarioRepository.buscarPorId(usuarioIdExistente);

        // Assert
        assertThat(usuarioAtivo).isNotNull();
        assertThat(usuarioAtivo.id()).isEqualTo(usuarioIdExistente);
        assertThat(usuarioAtivo.isAtivo()).isTrue();
    }

    @Test
    void deveBuscarUsuarioInativoPorId() {
        // Act
        UUID usuarioIdInativo = UUID.fromString("60127300-b56a-4394-a208-d9ef8eb864c7");
        DadosUsuarioInputDto usuarioInativo = usuarioRepository.buscarPorId(usuarioIdInativo);

        // Assert
        assertThat(usuarioInativo).isNotNull();
        assertThat(usuarioInativo.id()).isEqualTo(usuarioIdInativo);
        assertThat(usuarioInativo.isAtivo()).isFalse();
    }

    @Test
    void deveVerificarEmailJaCadastrado() {
        // Arrange
        String emailCadastrado = "thiago@fiapfood.com";

        // Act
        boolean emailExiste = usuarioRepository.emailJaCadastrado(emailCadastrado);

        // Assert
        assertThat(emailExiste).isTrue();
    }

    @Test
    void deveVerificarEmailNaoCadastrado() {
        // Arrange
        String emailNaoCadastrado = "email.inexistente@example.com";

        // Act
        boolean emailNaoExiste = usuarioRepository.emailJaCadastrado(emailNaoCadastrado);

        // Assert
        assertThat(emailNaoExiste).isFalse();
    }

    @Test
    void deveBuscarUsuariosPaginadosComSucesso() {
        // Act
        UsuarioPaginacaoInputDto paginaUsuarios = usuarioRepository.buscarTodos(1);

        // Assert
        assertThat(paginaUsuarios).isNotNull();
        assertThat(paginaUsuarios.usuarios()).isNotEmpty();
        assertThat(paginaUsuarios.usuarios().size()).isLessThanOrEqualTo(5);
        assertThat(paginaUsuarios.paginacao().totalItens()).isEqualTo(5);
        assertThat(paginaUsuarios.paginacao().paginaAtual()).isEqualTo(1);
        assertThat(paginaUsuarios.paginacao().totalPaginas()).isEqualTo(1);
    }

    @Test
    @Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deveRetornarNuloAoBuscarUsuariosPaginadosSemNenhumUsuario() {
        // Act
        UsuarioPaginacaoInputDto paginaUsuarios = usuarioRepository.buscarTodos(1);

        // Assert
        assertThat(paginaUsuarios).isNull();
    }

    @Test
    void deveRetornarNuloSeNenhumUsuarioEncontradoNaPagina() {
        // Act
        UsuarioPaginacaoInputDto paginaUsuarios = usuarioRepository.buscarTodos(9999);

        // Assert
        assertThat(paginaUsuarios).isNull();
    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        // Arrange
        UUID usuarioIdExistente = UUID.fromString("b48bc2dc-fd87-462d-a8a6-6e74674d0338");
        Optional<UsuarioEntity> usuarioEncontrado = usuarioJpaRepository.findById(usuarioIdExistente);
        UsuarioEntity usuarioExistente =  usuarioEncontrado.get();
        String nomeOriginal = usuarioExistente.getNome();
        String novoNome = "Nome Atualizado";

        // Act
        usuarioExistente.setNome(novoNome);
        usuarioRepository.salvar(usuarioExistente);

        // Assert
        DadosUsuarioInputDto usuarioAtualizado = usuarioRepository.buscarPorId(usuarioIdExistente);
        assertThat(usuarioAtualizado).isNotNull();
        assertThat(usuarioAtualizado.id()).isEqualTo(usuarioIdExistente);
        assertThat(usuarioAtualizado.nome()).isEqualTo(novoNome);
        assertThat(usuarioAtualizado.nome()).isNotEqualTo(nomeOriginal);
    }

    @Test
    void deveBuscarUsuarioPorIdPerfil(){
        // Arrange
        Integer perfilIdExistente = 1;

        // Act
        List<DadosUsuarioInputDto> usuariosEncontrados = usuarioRepository.buscarPorIdPerfil(perfilIdExistente);

        // Assert
        assertThat(usuariosEncontrados).isNotNull();
        assertThat(usuariosEncontrados).isNotEmpty();
        assertThat(usuariosEncontrados.size()).isEqualTo(3);
        assertThat(usuariosEncontrados.getFirst().idPerfil()).isEqualTo(perfilIdExistente);
        assertThat(usuariosEncontrados.getLast().idPerfil()).isEqualTo(perfilIdExistente);
    }

    @Test
    void deveRetornarNuloAoBuscarUsuarioPorIdPerfilInexistente() {
        // Arrange
        Integer perfilIdInexistente = 3;

        // Act
        List<DadosUsuarioInputDto> usuariosEncontrados = usuarioRepository.buscarPorIdPerfil(perfilIdInexistente);

        // Assert
        assertThat(usuariosEncontrados).isNull();
    }

    @Test
    void deveBuscarUsuarioPorMatriculaSenha(){
        // Arrange
        String matriculaExistente = "us0001";
        String senhaExistente = "123";

        // Act
        DadosUsuarioInputDto usuariosEncontrado = usuarioRepository
                .buscarPorMatriculaSenha(matriculaExistente, senhaExistente);

        // Assert
        assertThat(usuariosEncontrado).isNotNull();
        assertThat(usuariosEncontrado.login().matricula()).isEqualTo(matriculaExistente);
        assertThat(usuariosEncontrado.login().senha()).isEqualTo(senhaExistente);
    }

    @Test
    void deveRetornarNuloAoBuscarUsuarioPorMatriculaSenhaInexistente(){
        // Arrange
        String matriculaInexistente = "us9999";
        String senhaInexistente = "99999";

        // Act
        DadosUsuarioInputDto usuariosEncontrado = usuarioRepository
                .buscarPorMatriculaSenha(matriculaInexistente, senhaInexistente);

        // Assert
        assertThat(usuariosEncontrado).isNull();
    }

    @Test
    void deveVerificarMatriculaJaCadastrada() {
        // Arrange
        String matriculaExistente = "us0001";

        // Act
        boolean matriculaExiste = usuarioRepository.matriculaJaCadastrada(matriculaExistente);

        // Assert
        assertThat(matriculaExiste).isTrue();
    }

    @Test
    void deveVerificarMatriculaNaoCadastrada() {
        // Arrange
        String matriculaNaoCadastrada = "us9999";

        // Act
        boolean matriculaExiste = usuarioRepository.matriculaJaCadastrada(matriculaNaoCadastrada);

        // Assert
        assertThat(matriculaExiste).isFalse();
    }
}
