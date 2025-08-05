package br.com.fiapfood.core.usecases.integracao.perfil;

import br.com.fiapfood.core.exceptions.perfil.ExclusaoPerfilNaoPermitidaException;
import br.com.fiapfood.core.exceptions.perfil.PerfilInvalidoException;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.impl.UsuarioGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.perfil.impl.InativarPerfilUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.IInativarPerfilUseCase;
import br.com.fiapfood.infraestructure.repositories.interfaces.IPerfilRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.IUsuarioRepository;
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
public class InativarPerfilUseCaseIT {
    private final String USUARIOS_ATIVOS = "Não é possível inativar o perfil pois há usuário ativo associado ao perfil.";
    private final String PERFIS_NAO_ENCONTRADOS = "Não foi encontrado nenhum perfil com o id informado.";

    private IInativarPerfilUseCase inativarPerfilUseCase;
    private IPerfilGateway perfilGateway;
    private IUsuarioGateway usuarioGateway;

    @Autowired
    private IPerfilRepository perfilRepository;
    @Autowired
    private IUsuarioRepository usuarioRepository;

    @BeforeEach
    public void setUp() {
        perfilGateway = new PerfilGateway(perfilRepository);
        usuarioGateway = new UsuarioGateway(usuarioRepository);

        inativarPerfilUseCase = new InativarPerfilUseCase(perfilGateway, usuarioGateway);
    }

    @DisplayName("Deve inativar perfil com sucesso.")
    @Test
    void deveInativarPerfilComSucesso() {
        // Arrange
        int perfilId = 4;

        // Act
        inativarPerfilUseCase.inativar(perfilId);
        var perfilAposInativacao = perfilRepository.buscarPorId(perfilId);

        // Assert
        assertThat(perfilAposInativacao.id()).isEqualTo(perfilId);
        assertThat(perfilAposInativacao.dataInativacao()).isNotNull();
    }

    @DisplayName("Deve inativar perfil com erro. Perfil não encontrado através do ID.")
    @Test
    void deveLancarExcecaoSeNaoEncontrarPerfilPorId() {
        // Arrange
        int perfilId = 10;

        // Act & Assert
        assertThatThrownBy(() -> inativarPerfilUseCase.inativar(perfilId))
                .isInstanceOf(PerfilInvalidoException.class)
                .hasMessage(PERFIS_NAO_ENCONTRADOS);
    }

    @DisplayName("Deve inativar perfil com erro. Usuários ativos cadastrados com perfil.")
    @Test
    void deveLancarExcecaoSeUsuariosAtivosEstiveremRegistradosComPerfil() {
        // Arrange
        int perfilId = 1;

        // Act & Assert
        assertThatThrownBy(() -> inativarPerfilUseCase.inativar(perfilId))
                .isInstanceOf(ExclusaoPerfilNaoPermitidaException.class)
                .hasMessage(USUARIOS_ATIVOS);
    }
}
