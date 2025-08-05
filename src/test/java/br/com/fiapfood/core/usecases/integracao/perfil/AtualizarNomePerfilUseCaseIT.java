package br.com.fiapfood.core.usecases.integracao.perfil;

import br.com.fiapfood.core.entities.Perfil;
import br.com.fiapfood.core.exceptions.perfil.NomePerfilDuplicadoException;
import br.com.fiapfood.core.exceptions.perfil.PerfilInvalidoException;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.usecases.perfil.impl.AtualizarNomePerfilUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.IAtualizarNomePerfilUseCase;
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
public class AtualizarNomePerfilUseCaseIT {

    private final String PERFIL_DUPLICADO = "Já existe um perfil com o nome informado.";
    private final String PERFIS_NAO_ENCONTRADOS = "Não foi encontrado nenhum perfil com o id informado.";

    private IAtualizarNomePerfilUseCase atualizarNomePerfilUseCase;
    private IPerfilGateway perfilGateway;

    @Autowired
    private IPerfilRepository perfilRepository;

    @BeforeEach
    public void setUp() {
        perfilGateway = new PerfilGateway(perfilRepository);

        atualizarNomePerfilUseCase = new AtualizarNomePerfilUseCase(perfilGateway);
    }

    @DisplayName("Deve atualizar nome do perfil com sucesso.")
    @Test
    void deveAtualizarNomePerfilComSucesso() {
        // Arrange
        int perfilId = 1;
        String novoNome = "Funcionario";
        Perfil perfilAntesDeAtualizar = perfilGateway.buscarPorId(perfilId);
        String perfilNomeAntesDeAtualizar = perfilAntesDeAtualizar.getNome();

        // Act
        atualizarNomePerfilUseCase.atualizar(perfilId,novoNome);

        Perfil perfilAposAtualizar = perfilGateway.buscarPorId(perfilId);
        String perfilNomeAposAtualizar = perfilAposAtualizar.getNome();

        // Assert
        assertThat(perfilNomeAntesDeAtualizar).isNotEqualTo(perfilNomeAposAtualizar);
        assertThat(perfilNomeAposAtualizar).isEqualTo(novoNome);
    }

    @DisplayName("Deve atualizar nome do perfil com erro. Novo nome já cadastrado.")
    @Test
    void deveLancarExcecaoSeNovoNomeJaCadastrado() {
        // Arrange
        int perfilId = 1;
        String novoNome = "Cliente";

        // Act & Assert
        assertThatThrownBy(() -> atualizarNomePerfilUseCase.atualizar(perfilId, novoNome))
                .isInstanceOf(NomePerfilDuplicadoException.class)
                .hasMessage(PERFIL_DUPLICADO);
    }

    @DisplayName("Deve atualizar nome do perfil com erro. Perfil náo encontrado através do ID.")
    @Test
    void deveLancarExcecaoSeNaoEncontrarPerfilAtravesDoId() {
        // Arrange
        int perfilId = 10;
        String novoNome = "Funcionario";

        //Act & Assert
        assertThatThrownBy(() -> atualizarNomePerfilUseCase.atualizar(perfilId, novoNome))
                .isInstanceOf(PerfilInvalidoException.class)
                .hasMessage(PERFIS_NAO_ENCONTRADOS);
    }
}
