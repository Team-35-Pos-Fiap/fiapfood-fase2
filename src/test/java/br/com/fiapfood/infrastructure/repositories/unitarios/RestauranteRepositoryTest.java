package br.com.fiapfood.infrastructure.repositories.unitarios;

import br.com.fiapfood.core.entities.dto.paginacao.PaginacaoCoreDto;
import br.com.fiapfood.core.entities.dto.restaurante.DadosRestauranteDto;
import br.com.fiapfood.core.entities.dto.restaurante.RestaurantePaginacaoInputDto;
import br.com.fiapfood.core.presenters.RestaurantePresenter;
import br.com.fiapfood.infraestructure.entities.RestauranteEntity;
import br.com.fiapfood.infraestructure.repositories.impl.RestauranteRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.jpa.IRestauranteJpaRepository;
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
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RestauranteRepositoryTest {

    @Mock
    private IRestauranteJpaRepository restauranteJpaRepository;

    private RestauranteRepository restauranteRepository;

    AutoCloseable mock;

    private MockedStatic<RestaurantePresenter> mockedRestaurantePresenterMockedStatic;

    private List<RestauranteEntity> listaRestauranteEntity;

    private List<DadosRestauranteDto> listaRestauranteDto;

    private UUID restauranteId;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        mockedRestaurantePresenterMockedStatic = Mockito.mockStatic(RestaurantePresenter.class);
        restauranteRepository = new RestauranteRepository(restauranteJpaRepository);
        listaRestauranteEntity = criarListaDeRestaurantesEntity();
        listaRestauranteDto = criarListaDeRestaurantesDto();
        restauranteId = UUID.randomUUID();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
        mockedRestaurantePresenterMockedStatic.close();
    }

    @Test
    void devePermitirBuscarPorId() {
        // Arrange
        RestauranteEntity restauranteEntity = listaRestauranteEntity.getFirst();
        DadosRestauranteDto restauranteDto = listaRestauranteDto.getFirst();

        when(restauranteJpaRepository.findById(restauranteId)).thenReturn(Optional.of(restauranteEntity));

        mockedRestaurantePresenterMockedStatic
                .when(() -> RestaurantePresenter.toRestauranteDto(restauranteEntity))
                .thenReturn(restauranteDto);

        // Act
        DadosRestauranteDto restauranteDtoEncontrado = restauranteRepository.buscarPorId(restauranteId);

        // Assert
        assertThat(restauranteDtoEncontrado).isNotNull();
        assertThat(restauranteDtoEncontrado.id()).isEqualTo(restauranteDto.id());
        verify(restauranteJpaRepository, times(1)).findById(restauranteId);
        mockedRestaurantePresenterMockedStatic.verify(() -> RestaurantePresenter.toRestauranteDto(restauranteEntity), times(1));
    }

    @Test
    void deveRetornarNuloSeRestauranteNaoEncontrado() {
        // Arrange
        when(restauranteJpaRepository.findById(restauranteId)).thenReturn(Optional.empty());

        // Act
        DadosRestauranteDto restauranteDtoEncontrado = restauranteRepository.buscarPorId(restauranteId);

        // Assert
        assertThat(restauranteDtoEncontrado).isNull();
        verify(restauranteJpaRepository, times(1)).findById(restauranteId);
    }

    @Test
    void devePermitirSalvarRestaurante() {
        // Arrange
        RestauranteEntity restauranteEntity = listaRestauranteEntity.getFirst();

        // Act
        restauranteRepository.salvarRestaurante(restauranteEntity);

        // Assert
        verify(restauranteJpaRepository, times(1)).save(restauranteEntity);
    }

    @Test
    void deveBuscarRestaurantesComPaginacao() {
        // Arrange
        int pagina = 1;
        Page<RestauranteEntity> paginaRestaurantes = new PageImpl<>(listaRestauranteEntity);
        RestaurantePaginacaoInputDto dtoPaginadoEsperado =
                new RestaurantePaginacaoInputDto(listaRestauranteDto, new PaginacaoCoreDto(1, 1, 2));

        when(restauranteJpaRepository.findAll(PageRequest.of(pagina - 1, 5))).thenReturn(paginaRestaurantes);

        mockedRestaurantePresenterMockedStatic
                .when(() -> RestaurantePresenter.toRestaurantePaginacaoInputDto(paginaRestaurantes))
                .thenReturn(dtoPaginadoEsperado);

        // Act
        RestaurantePaginacaoInputDto restaurantePaginacaoInputDto = restauranteRepository.buscarRestaurantesComPaginacao(pagina);

        // Assert
        assertThat(restaurantePaginacaoInputDto).isNotNull();
        assertThat(restaurantePaginacaoInputDto.restaurantes()).isEqualTo(listaRestauranteDto);
        verify(restauranteJpaRepository, times(1)).findAll(PageRequest.of(pagina - 1, 5));
        mockedRestaurantePresenterMockedStatic
                .verify(() -> RestaurantePresenter.toRestaurantePaginacaoInputDto(paginaRestaurantes), times(1));
    }

    @Test
    void deveRetornarNuloSeNaoHouverRestaurantesNaPaginacao() {
        // Arrange
        int pagina = 1;
        Page<RestauranteEntity> paginaVazia = new PageImpl<>(Collections.emptyList());

        when(restauranteJpaRepository.findAll(PageRequest.of(pagina - 1, 5))).thenReturn(paginaVazia);

        // Act
        RestaurantePaginacaoInputDto restaurantePaginacaoInputDto = restauranteRepository.buscarRestaurantesComPaginacao(pagina);

        // Assert
        assertThat(restaurantePaginacaoInputDto).isNull();
        verify(restauranteJpaRepository).findAll(PageRequest.of(pagina - 1, 5));
    }


    private List<RestauranteEntity> criarListaDeRestaurantesEntity() {
        RestauranteEntity restaurante1 = new RestauranteEntity();
        restaurante1.setId(UUID.randomUUID());
        restaurante1.setNome("Restaurante 1");

        RestauranteEntity restaurante2 = new RestauranteEntity();
        restaurante2.setId(UUID.randomUUID());
        restaurante2.setNome("Restaurante 2");

        return List.of(restaurante1, restaurante2);
    }

    private List<DadosRestauranteDto> criarListaDeRestaurantesDto() {
        RestauranteEntity restaurante1 = listaRestauranteEntity.getFirst();
        RestauranteEntity restaurante2 = listaRestauranteEntity.getLast();

        DadosRestauranteDto restauranteDto1 = new DadosRestauranteDto(
                restaurante1.getId(),
                restaurante1.getNome(),
                null,
                null,
                null,
                null,
                null,
                null
        );
        DadosRestauranteDto restauranteDto2 = new DadosRestauranteDto(
                restaurante2.getId(),
                restaurante2.getNome(),
                null,
                null,
                null,
                null,
                null,
                null
        );

        return List.of(restauranteDto1, restauranteDto2);
    }
}
