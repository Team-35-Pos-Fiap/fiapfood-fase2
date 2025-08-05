package br.com.fiapfood.infrastructure.repositories.unitarios;

import br.com.fiapfood.core.entities.dto.paginacao.PaginacaoCoreDto;
import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioInputDto;
import br.com.fiapfood.core.entities.dto.usuario.UsuarioPaginacaoInputDto;
import br.com.fiapfood.core.presenters.UsuarioPresenter;
import br.com.fiapfood.infraestructure.entities.UsuarioEntity;
import br.com.fiapfood.infraestructure.repositories.impl.UsuarioRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.jpa.IUsuarioJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsuarioRepositoryTest {

    @Mock
    private IUsuarioJpaRepository usuarioJpaRepository;

    private UsuarioRepository usuarioRepository;

    AutoCloseable mock;

    private List<UsuarioEntity> listaUsuariosEntity;

    private List<DadosUsuarioInputDto> listaUsuariosDto;

    private UUID usuarioId;

    private Integer perfilId;

    private MockedStatic<UsuarioPresenter> usuarioPresenterMockedStatic;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        usuarioPresenterMockedStatic = Mockito.mockStatic(UsuarioPresenter.class);
        usuarioRepository = new UsuarioRepository(usuarioJpaRepository);
        listaUsuariosEntity = criarListaDeUsuariosEntity();
        listaUsuariosDto = criarListaDeUsuariosDto();
        usuarioId = UUID.randomUUID();
        perfilId = 1;
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
        usuarioPresenterMockedStatic.close();
    }

    @Test
    void devePermitirBuscarUsuarioPorId() {
        // Arrange
        UsuarioEntity usuarioEntity = listaUsuariosEntity.getFirst();
        DadosUsuarioInputDto usuarioInputDto = listaUsuariosDto.getFirst();

        when(usuarioJpaRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioEntity));

        usuarioPresenterMockedStatic
                .when(() -> UsuarioPresenter.toUsuarioInputDto(usuarioEntity))
                .thenReturn(usuarioInputDto);

        // Act
        DadosUsuarioInputDto usuarioInputDtoEncontrado = usuarioRepository.buscarPorId(usuarioId);

        // Assert
        assertThat(usuarioInputDtoEncontrado).isNotNull();
        assertThat(usuarioInputDtoEncontrado).isEqualTo(usuarioInputDto);
        verify(usuarioJpaRepository, times(1)).findById(usuarioId);
        usuarioPresenterMockedStatic.verify(() -> UsuarioPresenter.toUsuarioInputDto(usuarioEntity), times(1));
    }

    @Test
    void deveRetornarNuloSeUsuarioNaoForEncontrado() {
        // Arrange
        when(usuarioJpaRepository.findById(usuarioId)).thenReturn(Optional.empty());

        // Act
        DadosUsuarioInputDto usuarioInputDtoEncontrado = usuarioRepository.buscarPorId(usuarioId);

        // Assert
        assertThat(usuarioInputDtoEncontrado).isNull();
        verify(usuarioJpaRepository, times(1)).findById(usuarioId);
    }

    @Test
    void devePermitirBuscarUsuarioPorIdPerfil() {
        // Arrange
        List<UsuarioEntity> listaUsuariosEntity = criarListaDeUsuariosEntity();
        List<DadosUsuarioInputDto> listaUsuariosDto = criarListaDeUsuariosDto();

        when(usuarioJpaRepository.findAllByIdPerfil(perfilId)).thenReturn(listaUsuariosEntity);

        usuarioPresenterMockedStatic
                .when(() -> UsuarioPresenter.toListUsuarioDto(listaUsuariosEntity))
                .thenReturn(listaUsuariosDto);

        // Act
        List<DadosUsuarioInputDto> usuariosInputDtoEncontrado = usuarioRepository.buscarPorIdPerfil(perfilId);

        // Assert
        assertThat(usuariosInputDtoEncontrado).isNotNull();
        assertThat(usuariosInputDtoEncontrado).isEqualTo(listaUsuariosDto);
        verify(usuarioJpaRepository, times(1)).findAllByIdPerfil(perfilId);
        usuarioPresenterMockedStatic.verify(() -> UsuarioPresenter.toListUsuarioDto(listaUsuariosEntity), times(1));
    }

    @Test
    void deveRetornarNuloAoBuscarUsuarioPorIdPerfilInexistente() {
        // Arrange
        when(usuarioJpaRepository.findAllByIdPerfil(perfilId)).thenReturn(Collections.emptyList());

        // Act
        List<DadosUsuarioInputDto> usuarioInputDtoEncontrado = usuarioRepository.buscarPorIdPerfil(perfilId);

        // Assert
        assertThat(usuarioInputDtoEncontrado).isNull();
        verify(usuarioJpaRepository, times(1)).findAllByIdPerfil(perfilId);
    }

    @Test
    void devePermitirBuscarUsuarioPorbuscarPorMatriculaSenha() {
        // Arrange
        String matricula = "us0001";
        String senha = "12345";
        UsuarioEntity usuarioEntity = criarListaDeUsuariosEntity().getFirst();
        DadosUsuarioInputDto usuarioInputDto = criarListaDeUsuariosDto().getFirst();

        when(usuarioJpaRepository.findByMatriculaSenha(matricula, senha)).thenReturn(Optional.of(usuarioEntity));

        usuarioPresenterMockedStatic
                .when(() -> UsuarioPresenter.toUsuarioInputDto(usuarioEntity))
                .thenReturn(usuarioInputDto);

        // Act
        DadosUsuarioInputDto usuariosInputDtoEncontrado = usuarioRepository.buscarPorMatriculaSenha(matricula, senha);

        // Assert
        assertThat(usuariosInputDtoEncontrado).isNotNull();
        assertThat(usuariosInputDtoEncontrado).isEqualTo(usuarioInputDto);
        verify(usuarioJpaRepository, times(1)).findByMatriculaSenha(matricula, senha);
        usuarioPresenterMockedStatic.verify(() -> UsuarioPresenter.toUsuarioInputDto(usuarioEntity), times(1));
    }

    @Test
    void deveRetornarNuloAoBuscarUsuarioPorMatriculaSenhaInexistente() {
        // Arrange
        String matricula = "us0001";
        String senha = "12345";

        when(usuarioJpaRepository.findByMatriculaSenha(matricula, senha)).thenReturn(Optional.empty());

        // Act
        DadosUsuarioInputDto usuariosInputDtoEncontrado = usuarioRepository.buscarPorMatriculaSenha(matricula, senha);

        // Assert
        assertThat(usuariosInputDtoEncontrado).isNull();
        verify(usuarioJpaRepository, times(1)).findByMatriculaSenha(matricula, senha);
    }


    @Test
    void devePermitirBuscarUsuariosComPaginacao() {
        // Arrange
        List<UsuarioEntity> usuarios = criarListaDeUsuariosEntity();
        Page<UsuarioEntity> paginaUsuarios = new PageImpl<>(usuarios);
        List<DadosUsuarioInputDto> listaUsuariosDto = criarListaDeUsuariosDto();
        PaginacaoCoreDto paginacao = new PaginacaoCoreDto(1, 1, paginaUsuarios.getSize());
        UsuarioPaginacaoInputDto usuarioPaginacaoInputDto = new UsuarioPaginacaoInputDto(listaUsuariosDto, paginacao);

        when(usuarioJpaRepository.findAll(any(PageRequest.class))).thenReturn(paginaUsuarios);

        usuarioPresenterMockedStatic
                .when(() -> UsuarioPresenter.toUsuarioPaginacaoInputDto(paginaUsuarios))
                .thenReturn(usuarioPaginacaoInputDto);

        // Act
        UsuarioPaginacaoInputDto usuariosPaginacaoInputDtoEncontrado = usuarioRepository.buscarTodos(1);

        // Assert
        assertThat(usuariosPaginacaoInputDtoEncontrado).isNotNull();
        assertThat(usuariosPaginacaoInputDtoEncontrado.usuarios()).isNotEmpty();
        assertThat(usuariosPaginacaoInputDtoEncontrado.usuarios().size()).isEqualTo(2);
        assertThat(usuariosPaginacaoInputDtoEncontrado.paginacao().totalItens()).isEqualTo(2);
        assertThat(usuariosPaginacaoInputDtoEncontrado.paginacao().paginaAtual()).isEqualTo(1);
        assertThat(usuariosPaginacaoInputDtoEncontrado.paginacao().totalPaginas()).isEqualTo(1);
        verify(usuarioJpaRepository, times(1)).findAll(any(PageRequest.class));
        usuarioPresenterMockedStatic.verify(() -> UsuarioPresenter.toUsuarioPaginacaoInputDto(paginaUsuarios), times(1));
    }

    @Test
    void deveRetornarNuloAoBuscarUsuariosComPaginacao() {
        // Arrange
        Page<UsuarioEntity> paginaUsuarios = new PageImpl<>(Collections.emptyList());
        List<DadosUsuarioInputDto> listaUsuariosDto = Collections.emptyList();
        PaginacaoCoreDto paginacao = new PaginacaoCoreDto(1, 1, paginaUsuarios.getSize());
        UsuarioPaginacaoInputDto usuarioPaginacaoInputDto = new UsuarioPaginacaoInputDto(listaUsuariosDto, paginacao);

        when(usuarioJpaRepository.findAll(any(PageRequest.class))).thenReturn(paginaUsuarios);

        usuarioPresenterMockedStatic
                .when(() -> UsuarioPresenter.toUsuarioPaginacaoInputDto(paginaUsuarios))
                .thenReturn(usuarioPaginacaoInputDto);

        // Act
        UsuarioPaginacaoInputDto usuariosPaginacaoInputDtoEncontrado = usuarioRepository.buscarTodos(1);

        // Assert
        assertThat(usuariosPaginacaoInputDtoEncontrado).isNull();
        verify(usuarioJpaRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void devePermitirSalvarUsuario() {
        // Arrange
        UsuarioEntity usuarioEntity = criarListaDeUsuariosEntity().getFirst();

        // Act
        usuarioRepository.salvar(usuarioEntity);

        // Assert
        verify(usuarioJpaRepository, times(1)).save(usuarioEntity);
    }

    @Test
    void deveRetornarTrueSeEmailJaCadastrado() {
        // Arrange
        String email = "test@example.com";
        when(usuarioJpaRepository.existsByEmail(email)).thenReturn(true);

        // Act
        boolean resultado = usuarioRepository.emailJaCadastrado(email);

        // Assert
        assertThat(resultado).isTrue();
    }

    @Test
    void deveRetornarFalseSeEmailNaoCadastrado() {
        // Arrange
        String email = "test@example.com";
        when(usuarioJpaRepository.existsByEmail(email)).thenReturn(false);

        // Act
        boolean resultado = usuarioRepository.emailJaCadastrado(email);

        // Assert
        assertThat(resultado).isFalse();
        verify(usuarioJpaRepository, times(1)).existsByEmail(email);
    }

    @Test
     void deveRetornarTrueSeMatriculaJaCadastrada() {
         // Arrange
         UsuarioEntity usuarioEntity = criarListaDeUsuariosEntity().getFirst();
         String matricula = "us0001";
         when(usuarioJpaRepository.findByMatricula(matricula)).thenReturn(Optional.of(usuarioEntity));

         // Act
         boolean resultado = usuarioRepository.matriculaJaCadastrada(matricula);

        // Assert
        assertThat(resultado).isTrue();
        verify(usuarioJpaRepository, times(1)).findByMatricula(matricula);
     }

    @Test
    void deveRetornarFalseSeMatriculaNaoCadastrada() {
        // Arrange
        String matricula = "us0001";
        when(usuarioJpaRepository.findByMatricula(matricula)).thenReturn(Optional.empty());

        // Act
        boolean resultado = usuarioRepository.matriculaJaCadastrada(matricula);

        // Assert
        assertThat(resultado).isFalse();
        verify(usuarioJpaRepository, times(1)).findByMatricula(matricula);
    }

    private List<UsuarioEntity> criarListaDeUsuariosEntity() {
        UsuarioEntity usuario1 = new UsuarioEntity();
        usuario1.setId(UUID.randomUUID());
        usuario1.setNome("Nome Teste 1");

        UsuarioEntity usuario2 = new UsuarioEntity();
        usuario2.setId(UUID.randomUUID());
        usuario2.setNome("Nome Teste 2");

        return List.of(usuario1, usuario2);
    }

    private List<DadosUsuarioInputDto> criarListaDeUsuariosDto() {
        List<UsuarioEntity> listaUsuariosEntity = criarListaDeUsuariosEntity();
        UsuarioEntity usuario1 = listaUsuariosEntity.getFirst();
        UsuarioEntity usuario2 = listaUsuariosEntity.getLast();

        DadosUsuarioInputDto usuarioInputDto1 = new DadosUsuarioInputDto(
                usuario1.getId(),
                usuario1.getNome(),
                null,
                null,
                null,
                null, null,
                null,
                null
        );
        DadosUsuarioInputDto usuarioInputDto2 = new DadosUsuarioInputDto(
                usuario2.getId(),
                usuario2.getNome(),
                null,
                null,
                null,
                null, null,
                null,
                null
        );

        return List.of(usuarioInputDto1, usuarioInputDto2);
    }
}
