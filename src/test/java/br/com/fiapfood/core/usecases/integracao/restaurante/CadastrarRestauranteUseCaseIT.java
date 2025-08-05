package br.com.fiapfood.core.usecases.integracao.restaurante;

import br.com.fiapfood.core.entities.Restaurante;
import br.com.fiapfood.core.entities.dto.endereco.DadosEnderecoCoreDto;
import br.com.fiapfood.core.entities.dto.restaurante.CadastrarRestauranteCoreDto;
import br.com.fiapfood.core.entities.dto.restaurante.DadosRestauranteDto;
import br.com.fiapfood.core.entities.dto.restaurante.RestaurantePaginacaoInputDto;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.impl.RestauranteGateway;
import br.com.fiapfood.core.gateways.impl.UsuarioGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IRestauranteGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.restaurante.impl.CadastrarRestauranteUseCase;
import br.com.fiapfood.core.usecases.restaurante.interfaces.ICadastrarRestauranteUseCase;
import br.com.fiapfood.infraestructure.repositories.interfaces.IPerfilRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.IRestauranteRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.IUsuarioRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CadastrarRestauranteUseCaseIT {

    private final String PERFIL_USUARIO_VALIDO = "Dono";
    private final String USUARIO_DONO = "Não é possível cadastrar o restaurante, pois o responsável não possui o perfil de dono.";
    private final String USUARIO_INATIVO = "Não é possível cadastrar o restaurante, pois o responsável se encontra inativo.";

    private ICadastrarRestauranteUseCase cadastrarRestauranteUseCase;
    private IRestauranteGateway restauranteGateway;
    private IUsuarioGateway usuarioGateway;
    private IPerfilGateway perfilGateway;

    @Autowired
    private IRestauranteRepository restauranteRepository;
    @Autowired
    private IUsuarioRepository usuarioRepository;
    @Autowired
    private IPerfilRepository perfilRepository;

    @BeforeEach
    void setUp() {
        restauranteGateway = new RestauranteGateway(restauranteRepository);
        usuarioGateway = new UsuarioGateway(usuarioRepository);
        perfilGateway = new PerfilGateway(perfilRepository);

        cadastrarRestauranteUseCase = new CadastrarRestauranteUseCase(restauranteGateway, usuarioGateway, perfilGateway);
    }

//    @Test
//    void deveCadastrarRestauranteComSucesso() {
//        // Arrange
//        UUID idDono = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c"); // ativo, perfil = Dono
//        CadastrarRestauranteCoreDto dto = new CadastrarRestauranteCoreDto(
//                "Novo Restaurante",
//                new DadosEnderecoCoreDto("Rua A", "Cidade A", "Bairro A", "Estado A", "123", 50, "Complemento A"),
//                idDono,
//                1,
//                List.of()
//        );
//
//        // Act
//        cadastrarRestauranteUseCase.cadastrar(dto);
//
//        // Assert
//        List<DadosRestauranteDto> restaurantes = restauranteGateway.buscarTodos(2).restaurantes();
//        boolean encontrado = restaurantes.stream().anyMatch(r ->
//                r.nome().equals("Novo Restaurante") &&
//                        r.idDono().equals(idDono) &&
//                        r.idTipoCulinaria().equals(1)
//        );
//
//        assertThat(encontrado).isTrue();
//    }

}
