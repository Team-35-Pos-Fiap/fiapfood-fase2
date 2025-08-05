package br.com.fiapfood.infrastructure.repositories.unitarios;

import br.com.fiapfood.core.entities.dto.perfil.PerfilCoreDto;
import br.com.fiapfood.core.presenters.PerfilPresenter;
import br.com.fiapfood.infraestructure.entities.PerfilEntity;
import br.com.fiapfood.infraestructure.repositories.impl.PerfilRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.jpa.IPerfilJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PerfilRepositoryTest {

    @Mock
    private IPerfilJpaRepository perfilJpaRepository;

    private PerfilRepository perfilRepository;

    AutoCloseable mock;

    private Integer perfilId;

    private List<PerfilEntity> listaPerfilEntity;

    private List<PerfilCoreDto> listaPerfilCoreDto;

    private MockedStatic<PerfilPresenter> perfilPresenterMockedStatic;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        perfilPresenterMockedStatic = Mockito.mockStatic(PerfilPresenter.class);
        perfilId = 1;
        perfilRepository = new PerfilRepository(perfilJpaRepository);
        listaPerfilCoreDto = criarListaDePerfisDto();
        listaPerfilEntity = criarListaDePerfisEntity();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
        perfilPresenterMockedStatic.close();
    }

    @Test
    void devePermitirBuscarPorId() {
        // Arrange
        PerfilEntity perfilEntity = listaPerfilEntity.getFirst();

        when(perfilJpaRepository.findById(any(Integer.class))).thenReturn(Optional.of(perfilEntity));

        perfilPresenterMockedStatic
                .when(() -> PerfilPresenter.toPerfilDto(perfilEntity))
                .thenReturn(listaPerfilCoreDto.getFirst());

        // Act
        PerfilCoreDto perfilCoreDtoEncontrado = perfilRepository.buscarPorId(perfilId);

        // Assert
        assertThat(perfilCoreDtoEncontrado).isNotNull();
        assertThat(perfilCoreDtoEncontrado.id()).isEqualTo(perfilId);
        assertThat(perfilCoreDtoEncontrado.nome()).isEqualTo(perfilEntity.getNome());

        verify(perfilJpaRepository, times(1)).findById(perfilId);
        perfilPresenterMockedStatic.verify(() -> PerfilPresenter.toPerfilDto(perfilEntity), times(1));
    }

    @Test
    void deveRetornarNuloSeNaoEcontrarPerfilAoBuscarPorId() {
        // Arrange
        PerfilEntity perfilEntity = listaPerfilEntity.getFirst();
        when(perfilJpaRepository.findById(perfilId)).thenReturn(Optional.empty());

        perfilPresenterMockedStatic
                .when(() -> PerfilPresenter.toPerfilDto(perfilEntity))
                .thenReturn(listaPerfilCoreDto.getFirst());

        // Act
        PerfilCoreDto perfilCoreDtoEncontrado = perfilRepository.buscarPorId(perfilId);

        // Assert
        assertThat(perfilCoreDtoEncontrado).isNull();
        verify(perfilJpaRepository, times(1)).findById(perfilId);
        perfilPresenterMockedStatic.verify(() -> PerfilPresenter.toPerfilDto(perfilEntity), times(0));
    }

    @Test
    void deveBuscarTodosPerfisCadastrados() {
        // Arrange
        when(perfilJpaRepository.findAll()).thenReturn(listaPerfilEntity);

        perfilPresenterMockedStatic
                .when(() -> PerfilPresenter.toListPerfilDto(listaPerfilEntity))
                .thenReturn(listaPerfilCoreDto);

        // Act
        List<PerfilCoreDto> perfisEncontrados = perfilRepository.buscarTodos();

        // Assert
        assertThat(perfisEncontrados).isNotNull();
        assertThat(perfisEncontrados).isNotEmpty();
        assertThat(perfisEncontrados).hasSize(2);
        assertThat(perfisEncontrados).containsExactlyElementsOf(listaPerfilCoreDto);
        assertThat(perfisEncontrados.get(0).id()).isEqualTo(1);
        assertThat(perfisEncontrados.get(1).id()).isEqualTo(2);
        verify(perfilJpaRepository, times(1)).findAll();
        perfilPresenterMockedStatic.verify(() -> PerfilPresenter.toListPerfilDto(listaPerfilEntity), times(1));
    }

    @Test
    void deveRetornarNuloCasoNenhumPerfilCadastrado() {
        // Arrange
        List<PerfilEntity> listaEntityVazia = Collections.emptyList();
        List<PerfilCoreDto> listaDtoVazia = Collections.emptyList();

        when(perfilJpaRepository.findAll()).thenReturn(listaEntityVazia);

        perfilPresenterMockedStatic
                .when(() -> PerfilPresenter.toListPerfilDto(listaEntityVazia))
                .thenReturn(listaDtoVazia);

        // Act
        List<PerfilCoreDto> perfisCoreDtoEncontrados = perfilRepository.buscarTodos();

        // Assert
        assertThat(perfisCoreDtoEncontrados).isEmpty();
        verify(perfilJpaRepository, times(1)).findAll();
        perfilPresenterMockedStatic.verify(() -> PerfilPresenter.toListPerfilDto(listaEntityVazia), times(1));
    }

    @Test
    void deveChecarSeNomePerfilJaCadastrado() {
        // Arrange
        PerfilEntity perfilEntity = listaPerfilEntity.getFirst();
        String nome = perfilEntity.getNome();

        when(perfilJpaRepository.existsByNomeIgnoreCase(nome)).thenReturn(true);

        // Act
        boolean resultado = perfilRepository.nomeJaCadastrado(nome);

        // Assert
        assertThat(resultado).isTrue();
        verify(perfilJpaRepository, times(1)).existsByNomeIgnoreCase(nome);
    }

    @Test
    void deveChecarSeNomePerfilNaoCadastrado() {
        // Arrange
        String nome = "Perfil não cadastrado";

        when(perfilJpaRepository.existsByNomeIgnoreCase(nome)).thenReturn(false);

        // Act
        boolean resultado = perfilRepository.nomeJaCadastrado(nome);

        // Assert
        assertThat(resultado).isFalse();
        verify(perfilJpaRepository, times(1)).existsByNomeIgnoreCase(nome);
    }


    private List<PerfilCoreDto> criarListaDePerfisDto() {
        PerfilCoreDto perfil1 = new PerfilCoreDto(1, "Perfil Teste 1", null, null);
        PerfilCoreDto perfil2 = new PerfilCoreDto(2, "Perfil Teste 2", null, null);

        return List.of(perfil1, perfil2);
    }

    private List<PerfilEntity> criarListaDePerfisEntity() {
        PerfilEntity perfil1 = new PerfilEntity(1, "Perfil Teste 1", null, null);
        PerfilEntity perfil2 = new PerfilEntity(2, "Perfil Teste 2", null, null);

        return List.of(perfil1, perfil2);
    }
}
