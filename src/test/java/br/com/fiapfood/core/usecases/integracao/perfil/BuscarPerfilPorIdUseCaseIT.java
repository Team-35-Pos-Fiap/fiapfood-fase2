package br.com.fiapfood.core.usecases.integracao.perfil;

import br.com.fiapfood.core.exceptions.perfil.PerfilInvalidoException;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.usecases.perfil.impl.BuscarPerfilPorIdUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.IBuscarPerfilPorIdUseCase;
import br.com.fiapfood.infraestructure.repositories.interfaces.IPerfilRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BuscarPerfilPorIdUseCaseIT {

    private final String PERFIS_NAO_ENCONTRADOS = "Não foi encontrado nenhum perfil com o id informado.";

    private IBuscarPerfilPorIdUseCase buscarPerfilPorIdUseCase;
    private IPerfilGateway perfilGateway;

    @Autowired
    private IPerfilRepository perfilRepository;

    @BeforeEach
    public void setUp() {
        perfilGateway = new PerfilGateway(perfilRepository);

        buscarPerfilPorIdUseCase = new BuscarPerfilPorIdUseCase(perfilGateway);
    }

    @DisplayName("Deve buscar perfil por id com sucesso.")
    @Test
    void deveBuscarPerfilPorIdComSucesso() {
        // Arrange
        int perfilId = 1;
        String perfilNome = "Dono";

        // Act
        var perfil = buscarPerfilPorIdUseCase.buscar(perfilId);

        // Assert
        assertThat(perfil.id()).isEqualTo(perfilId);
        assertThat(perfil.nome()).isEqualTo(perfilNome);
    }

    @DisplayName("Deve buscar perfil por id com erro. Nenhum perfil encontrado através do ID.")
    @Test
    void deveLancarExcecaoSeNenhumPerfilEncontradoPorId() {
        // Arrange
        int perfilId = 10;

        //Act & Assert
        assertThatThrownBy(() -> buscarPerfilPorIdUseCase.buscar(perfilId))
                .isInstanceOf(PerfilInvalidoException.class)
                .hasMessage(PERFIS_NAO_ENCONTRADOS);
    }
}
