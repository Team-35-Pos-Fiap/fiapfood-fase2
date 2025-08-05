package br.com.fiapfood.core.usecases.integracao.perfil;

import br.com.fiapfood.core.exceptions.perfil.PerfilNaoEncontradoException;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.usecases.perfil.impl.BuscarTodosPerfisUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.IBuscarTodosPerfisUseCase;
import br.com.fiapfood.infraestructure.repositories.interfaces.IPerfilRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BuscarTodosPerfisUseCaseIT {
    private final String PERFIL_NAO_ENCONTRADO = "Não foi encontrado nenhum perfil na base de dados.";

    private IBuscarTodosPerfisUseCase buscarTodosPerfisUseCase;
    private IPerfilGateway perfilGateway;

    @Autowired
    private IPerfilRepository perfilRepository;

    @BeforeEach
    public void setUp() {
        perfilGateway = new PerfilGateway(perfilRepository);

        buscarTodosPerfisUseCase = new BuscarTodosPerfisUseCase(perfilGateway);
    }

    @DisplayName("Deve buscar todos perfis com sucesso.")
    @Test
    void deveBuscarTodosPerfisComSucesso() {
        // Arrange
        List<String> perfisCadstrados = List.of("Dono", "Cliente", "Administrador", "Entregador");

        // Act
        var perfis = buscarTodosPerfisUseCase.buscar();

        // Assert
        assertThat(perfis.size()).isEqualTo(4);
        for(int i =0; i < perfisCadstrados.size(); i++) {
            assertThat(perfis.get(i).id()).isEqualTo(i + 1);
            assertThat(perfis.get(i).nome()).isEqualTo(perfisCadstrados.get(i));
        }
    }

    @DisplayName("Deve buscar todos perfis com erro. Nenhum perfil encontrado no banco de dados.")
    @Test
    @Sql(scripts = "/db_clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // Limpando o BD para retornar o erro
    void deveLancarExcecaoSeNenhumPerfilEncontrado() {
        // Arrange

        //Act & Assert
        assertThatThrownBy(() -> buscarTodosPerfisUseCase.buscar())
                .isInstanceOf(PerfilNaoEncontradoException.class)
                .hasMessage(PERFIL_NAO_ENCONTRADO);
    }
}
