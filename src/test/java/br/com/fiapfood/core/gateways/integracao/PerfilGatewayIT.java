package br.com.fiapfood.core.gateways.integracao;

import br.com.fiapfood.core.entities.dto.perfil.PerfilCoreDto;
import br.com.fiapfood.core.exceptions.perfil.PerfilInvalidoException;
import br.com.fiapfood.core.exceptions.perfil.PerfilNaoEncontradoException;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.infraestructure.repositories.interfaces.IPerfilRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PerfilGatewayIT {

    private final String PERFIL_NAO_ENCONTRADO = "Não foi encontrado nenhum perfil na base de dados.";
    private final String PERFIS_NAO_ENCONTRADOS = "Não foi encontrado nenhum perfil com o id informado.";

    @Autowired
    private IPerfilRepository perfilRepository;
    private IPerfilGateway perfilGateway;

    @BeforeEach
    public void setUp() {
        perfilGateway = new PerfilGateway(perfilRepository);
    }

    @Nested
    class BuscarTodos{
        @DisplayName("Deve buscar todos os perfis chamando o repository com sucesso.")
        @Test
        void deveBuscarTodosOsPerfisComSucesso(){
            // Arrange

            // Act
            var perfis = perfilGateway.buscarTodos();

            // Assert
            assertThat(perfis).isNotNull();
            assertThat(perfis.size()).isEqualTo(4);
        }

        @DisplayName("Deve buscar todos os perfis chamando o repository com erro. Nenhum perfil encontrado.")
        @Test
        @Sql(scripts = "/db_clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // Limpando o BD para retornar o erro
        void deveLancarExcecaoSeNenhumPerfilRetornadoPeloGateway(){
            // Arrange

            // Act & Assert
            assertThatThrownBy(() -> perfilGateway.buscarTodos())
                    .isInstanceOf(PerfilNaoEncontradoException.class)
                    .hasMessage(PERFIL_NAO_ENCONTRADO);
        }
    }

    @Nested
    class BuscarPorId{
        @DisplayName("Deve buscar perfil por ID chamando o repository com sucesso.")
        @Test
        void deveBuscarPerfilPorIdComSucesso() {
            // Arrange
            int perfilId = 1;

            // Act
            var perfil = perfilGateway.buscarPorId(perfilId);

            // Assert
            assertThat(perfil).isNotNull();
            assertThat(perfil.getId()).isEqualTo(perfilId);
            assertThat(perfil.getNome()).isEqualTo("Dono");
        }

        @DisplayName("Deve buscar perfil por ID chamando o repository com erro. Usuário nao encontrado.")
        @Test
        void deveLancarExcecaoSeNenhumPerfilRetornadoPeloGateway() {
            // Arrange
            int perfilId = 100;

            // Act & Assert
            assertThatThrownBy(() -> perfilGateway.buscarPorId(perfilId))
                    .isInstanceOf(PerfilInvalidoException.class)
                    .hasMessage(PERFIS_NAO_ENCONTRADOS);
        }
    }

    @Nested
    class Salvar{
        @DisplayName("Deve salvar perfil com sucesso.")
        @Test
        void deveSalvarPerfilComSucesso(){
            // Arrange
            PerfilCoreDto perfilDto = new PerfilCoreDto(
                    null,
                    "Admin",
                    LocalDate.now(),
                    null
            );
            int perfisSizeAntesDeSalvar = perfilGateway.buscarTodos().size();

            // Act
            perfilGateway.salvar(perfilDto);
            var perfisAposSalvar = perfilGateway.buscarTodos();
            boolean novoPerfilCadastrado = perfisAposSalvar.stream()
                    .anyMatch(perfil -> perfil.getNome().equals(perfilDto.nome()));

            // Assert
            assertThat(novoPerfilCadastrado).isTrue();
            assertThat(perfisAposSalvar.size()).isEqualTo(perfisSizeAntesDeSalvar + 1);
        }
    }

    @Nested
    class NomeJaCadastrado{
        @DisplayName("Deve retornar false se nome de perfil não cadastrado.")
        @Test
        void deveRetornarFalseSeNomeDePerfilNaoCadastrado(){
            // Arrange
            String nomePerfil = "Funcionario";

            // Act
            boolean result = perfilGateway.nomeJaCadastrado(nomePerfil);

            // Assert
            assertThat(result).isFalse();
        }

        @DisplayName("Deve retornar true se nome de perfil já cadastrado.")
        @Test
        void deveRetornarTrueSeNomeDePerfilNaoCadastrado(){
            // Arrange
            String nomePerfil = "Administrador";

            // Act
            boolean result = perfilGateway.nomeJaCadastrado(nomePerfil);

            // Assert
            assertThat(result).isTrue();
        }
    }
}
