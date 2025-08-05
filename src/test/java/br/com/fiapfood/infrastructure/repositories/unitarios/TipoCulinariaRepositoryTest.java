package br.com.fiapfood.infrastructure.repositories.unitarios;

import br.com.fiapfood.core.entities.dto.tipo_culinaria.TipoCulinariaCoreDto;
import br.com.fiapfood.core.presenters.TipoCulinariaPresenter;
import br.com.fiapfood.infraestructure.entities.TipoCulinariaEntity;
import br.com.fiapfood.infraestructure.repositories.impl.TipoCulinariaRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.jpa.ITipoCulinariaJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

public class TipoCulinariaRepositoryTest {

    @Mock
    private ITipoCulinariaJpaRepository tipoCulinariaJpaRepository;

    private TipoCulinariaRepository tipoCulinariaRepository;

    AutoCloseable mock;

    private MockedStatic<TipoCulinariaPresenter> tipoCulinariaPresenterMockedStatic;

    private List<TipoCulinariaEntity> listaTipoCulinariaEntity;

    private List<TipoCulinariaCoreDto> listaTipoCulinariaDto;

    private Integer tipoCulinariaId;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        tipoCulinariaPresenterMockedStatic = Mockito.mockStatic(TipoCulinariaPresenter.class);
        tipoCulinariaRepository = new TipoCulinariaRepository(tipoCulinariaJpaRepository);
        listaTipoCulinariaEntity = criarListaDeTipoCulinariaEntity();
        listaTipoCulinariaDto = criarListaDeTipoCulinariaDto();
        tipoCulinariaId = 1;
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
        tipoCulinariaPresenterMockedStatic.close();
    }

    @Test
    void devePermitirBuscarPorId() {
        // Arrange
        TipoCulinariaEntity tipoCulinariaEntity = listaTipoCulinariaEntity.getFirst();
        TipoCulinariaCoreDto tipoCulinariaCoreDto = listaTipoCulinariaDto.getFirst();

        when(tipoCulinariaJpaRepository.findById(tipoCulinariaId)).thenReturn(Optional.of(tipoCulinariaEntity));
        tipoCulinariaPresenterMockedStatic
                .when(() -> TipoCulinariaPresenter.toTipoCulinariaDto(tipoCulinariaEntity))
                .thenReturn(tipoCulinariaCoreDto);

        // Act
        TipoCulinariaCoreDto tipoCulinariaCoreDtoEncontrado = tipoCulinariaRepository.buscarPorId(tipoCulinariaId);

        // Assert
        assertThat(tipoCulinariaCoreDtoEncontrado).isNotNull();
        assertThat(tipoCulinariaCoreDtoEncontrado.id()).isEqualTo(tipoCulinariaId);
        verify(tipoCulinariaJpaRepository, times(1)).findById(tipoCulinariaId);
        tipoCulinariaPresenterMockedStatic.verify(() -> TipoCulinariaPresenter.toTipoCulinariaDto(tipoCulinariaEntity), times(1));
    }

    @Test
    void deveRetornarNuloSeTipoCulinariaNaoEncontrado() {
        // Arrange
        when(tipoCulinariaJpaRepository.findById(tipoCulinariaId)).thenReturn(Optional.empty());

        // Act
        TipoCulinariaCoreDto tipoCulinariaCoreDtoEncontrado = tipoCulinariaRepository.buscarPorId(tipoCulinariaId);

        // Assert
        assertThat(tipoCulinariaCoreDtoEncontrado).isNull();
        verify(tipoCulinariaJpaRepository, times(1)).findById(tipoCulinariaId);
    }

    @Test
    void deveBuscarTodosTiposCulinaria() {
        // Arrange
        when(tipoCulinariaJpaRepository.findAll()).thenReturn(listaTipoCulinariaEntity);

        tipoCulinariaPresenterMockedStatic
                .when(() -> TipoCulinariaPresenter.toListTipoCulinariaDto(listaTipoCulinariaEntity))
                .thenReturn(listaTipoCulinariaDto);

        // Act
        List<TipoCulinariaCoreDto> tiposCulinariaCoreDtoEncontrado = tipoCulinariaRepository.buscarTodos();

        // Assert
        assertThat(tiposCulinariaCoreDtoEncontrado).isNotNull();
        assertThat(tiposCulinariaCoreDtoEncontrado).isNotEmpty();
        assertThat(tiposCulinariaCoreDtoEncontrado).containsExactlyElementsOf(listaTipoCulinariaDto);
        verify(tipoCulinariaJpaRepository, times(1)).findAll();
        tipoCulinariaPresenterMockedStatic.verify(() -> TipoCulinariaPresenter.toListTipoCulinariaDto(listaTipoCulinariaEntity), times(1));
    }

    @Test
    void devePermitirSalvarTipoCulinaria() {
        // Arrange
        TipoCulinariaEntity tipoCulinariaEntity = listaTipoCulinariaEntity.getFirst();

        // Act
        tipoCulinariaRepository.salvar(tipoCulinariaEntity);

        // Assert
        verify(tipoCulinariaJpaRepository, times(1)).save(tipoCulinariaEntity);
    }

    @Test
    void deveRetornarTrueSeNomeJaCadastrado() {
        // Arrange
        String nome = "Italiana";
        when(tipoCulinariaJpaRepository.existsByNomeIgnoreCase(nome)).thenReturn(true);

        // Act
        boolean resultado = tipoCulinariaRepository.nomeJaCadastrado(nome);

        // Assert
        assertThat(resultado).isTrue();
        verify(tipoCulinariaJpaRepository, times(1)).existsByNomeIgnoreCase(nome);
    }

    @Test
    void deveRetornarFalseSeNomeNaoCadastrado() {
        // Arrange
        String nome = "Tipo Culinaria Inexistente";
        when(tipoCulinariaJpaRepository.existsByNomeIgnoreCase(nome)).thenReturn(false);

        // Act
        boolean resultado = tipoCulinariaRepository.nomeJaCadastrado(nome);

        // Assert
        assertThat(resultado).isFalse();
        verify(tipoCulinariaJpaRepository, times(1)).existsByNomeIgnoreCase(nome);
    }

    private List<TipoCulinariaEntity> criarListaDeTipoCulinariaEntity() {
        TipoCulinariaEntity tipo1 = new TipoCulinariaEntity(1, "Italiana");
        TipoCulinariaEntity tipo2 = new TipoCulinariaEntity(2, "Japonesa");
        return List.of(tipo1, tipo2);
    }

    private List<TipoCulinariaCoreDto> criarListaDeTipoCulinariaDto() {
        TipoCulinariaCoreDto dto1 = new TipoCulinariaCoreDto(1, "Italiana");
        TipoCulinariaCoreDto dto2 = new TipoCulinariaCoreDto(2, "Japonesa");
        return List.of(dto1, dto2);
    }
}
