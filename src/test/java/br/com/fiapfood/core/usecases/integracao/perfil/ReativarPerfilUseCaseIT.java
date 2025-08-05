package br.com.fiapfood.core.usecases.integracao.perfil;

import br.com.fiapfood.core.exceptions.perfil.PerfilInvalidoException;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.usecases.perfil.impl.ReativarPerfilUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.IReativarPerfilUseCase;
import br.com.fiapfood.infraestructure.repositories.interfaces.IPerfilRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ReativarPerfilUseCaseIT {
    private final String PERFIS_NAO_ENCONTRADOS = "Não foi encontrado nenhum perfil com o id informado.";

    private IReativarPerfilUseCase reativarPerfilUseCase;
    private IPerfilGateway perfilGateway;

    @Autowired
    private IPerfilRepository perfilRepository;

    @BeforeEach
    public void setUp() {
        perfilGateway = new PerfilGateway(perfilRepository);

        reativarPerfilUseCase = new ReativarPerfilUseCase(perfilGateway);
    }

    @DisplayName("Deve reativar perfil com sucesso.")
    @Test
    void deveReativarPerfilComSucesso() {
        // Arrange
        int perfilIdInativo = 3;

        // Act
        reativarPerfilUseCase.reativar(perfilIdInativo);
        var perfilAposReativacao = perfilGateway.buscarPorId(perfilIdInativo);

        // Assert
        assertThat(perfilAposReativacao.getId()).isEqualTo(perfilIdInativo);
        assertThat(perfilAposReativacao.getDataInativacao()).isNull();
    }

    @DisplayName("Deve reativar perfil com erro. Perfil não encontrado através do ID.")
    @Test
    void deveLancarExcecaoSeNaoEncontrarPerfilPorId() {
        // Arrange
        int perfilId = 10;

        // Act & Assert
        assertThatThrownBy(() -> reativarPerfilUseCase.reativar(perfilId))
                .isInstanceOf(PerfilInvalidoException.class)
                .hasMessage(PERFIS_NAO_ENCONTRADOS);
    }
}
