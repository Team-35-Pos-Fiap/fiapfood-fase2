package br.com.fiapfood.core.usecases.integracao.restaurante;

import br.com.fiapfood.core.entities.dto.endereco.DadosEnderecoCoreDto;
import br.com.fiapfood.core.exceptions.restaurante.AtualizacaoEnderecoRestauranteNaoPermitidaException;
import br.com.fiapfood.core.exceptions.restaurante.RestauranteNaoEncontradoException;
import br.com.fiapfood.core.gateways.impl.RestauranteGateway;
import br.com.fiapfood.core.gateways.interfaces.IRestauranteGateway;
import br.com.fiapfood.core.usecases.restaurante.impl.AtualizarEnderecoRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.IAtualizarEnderecoRestauranteUseCase;
import br.com.fiapfood.infraestructure.repositories.interfaces.IRestauranteRepository;
import br.com.fiapfood.utils.DtoDataGenerator;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class AtualizarEnderecoRestauranteUseCaseIT {

    private final String RESTAURANTE_INATIVO = "Não é possível alterar o nome do restaurante, pois ele se encontra inativo.";
    private final String RESTAURANTE_NAO_ENCONTRADO = "Não foi encontrado nenhum restaurante com o id informado.";

    private IAtualizarEnderecoRestauranteUseCase atualizarEnderecoRestauranteUseCase;

    private IRestauranteGateway restauranteGateway;

    @Autowired
    private IRestauranteRepository restauranteRepository;

    @BeforeEach
    void setUp() {
        restauranteGateway = new RestauranteGateway(restauranteRepository);
        atualizarEnderecoRestauranteUseCase = new AtualizarEnderecoRestauranteUseCase(restauranteGateway);
    }

    @Test
    void deveAtualizarEnderecoDoRestauranteComSucesso() {
        // Arrange
        UUID idRestaurante = UUID.fromString("40d5955e-c0bd-41da-b434-e46fa69bda14");
        DadosEnderecoCoreDto novoEndereco = new DadosEnderecoCoreDto(
                "Rua Nova", "12345-11", "Centro", "Jardim Oliva", "SP", 50, "Apto 101"
        );

        // Act
        atualizarEnderecoRestauranteUseCase.atualizar(idRestaurante, novoEndereco);
        var restauranteAtualizado = restauranteGateway.buscarPorId(idRestaurante);

        // Assert
        assertThat(restauranteAtualizado).isNotNull();
        assertThat(restauranteAtualizado.getDadosEndereco().getCidade()).isEqualTo("Rua Nova");
        assertThat(restauranteAtualizado.getDadosEndereco().getCep()).isEqualTo("12345-11");
        assertThat(restauranteAtualizado.getDadosEndereco().getBairro()).isEqualTo("Centro");
        assertThat(restauranteAtualizado.getDadosEndereco().getEndereco()).isEqualTo("Jardim Oliva");
        assertThat(restauranteAtualizado.getDadosEndereco().getEstado()).isEqualTo("SP");
        assertThat(restauranteAtualizado.getDadosEndereco().getNumero()).isEqualTo(50);
        assertThat(restauranteAtualizado.getDadosEndereco().getComplemento()).isEqualTo("Apto 101");
    }

    @Test
    void deveLancarExcecaoQuandoRestauranteEstiverInativo() {
        // Arrange
        UUID idRestauranteInativo = UUID.fromString("fc8a9535-d6be-465f-8bf1-d9885e91c91d");
        DadosEnderecoCoreDto novoEndereco = new DadosEnderecoCoreDto(
                "Rua Nova", "12345-11", "Centro", "Jardim Oliva", "SP", 50, "Apto 101"
        );

        // Act + Assert
        assertThatThrownBy(() -> atualizarEnderecoRestauranteUseCase.atualizar(idRestauranteInativo, novoEndereco))
                .isInstanceOf(AtualizacaoEnderecoRestauranteNaoPermitidaException.class)
                .hasMessage(RESTAURANTE_INATIVO);
    }

    @Test
    void deveLancarExcecaoQuandoRestauranteNaoEncontrado() {
        // Arrange
        UUID idRestauranteInativo = UUID.fromString("4cbd1ae7-163f-4f85-9c66-665f9f665840");
        DadosEnderecoCoreDto novoEndereco = new DadosEnderecoCoreDto(
                "Rua Nova", "12345-11", "Centro", "Jardim Oliva", "SP", 50, "Apto 101"
        );

        // Act + Assert
        assertThatThrownBy(() -> atualizarEnderecoRestauranteUseCase.atualizar(idRestauranteInativo, novoEndereco))
                .isInstanceOf(RestauranteNaoEncontradoException.class)
                .hasMessage(RESTAURANTE_NAO_ENCONTRADO);
    }
}
