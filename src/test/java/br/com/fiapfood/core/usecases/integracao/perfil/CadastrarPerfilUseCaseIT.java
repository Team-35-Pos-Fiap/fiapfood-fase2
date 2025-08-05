package br.com.fiapfood.core.usecases.integracao.perfil;

import br.com.fiapfood.core.exceptions.perfil.NomePerfilDuplicadoException;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.usecases.perfil.impl.CadastrarPerfilUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.ICadastrarPerfilUseCase;
import br.com.fiapfood.infraestructure.repositories.interfaces.IPerfilRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CadastrarPerfilUseCaseIT {
    private final String PERFIL_DUPLICADO = "Já existe um perfil com o nome informado.";

    private ICadastrarPerfilUseCase cadastrarPerfilUseCase;
    private IPerfilGateway perfilGateway;

    @Autowired
    private IPerfilRepository perfilRepository;

    @BeforeEach
    public void setUp() {
        perfilGateway = new PerfilGateway(perfilRepository);

        cadastrarPerfilUseCase = new CadastrarPerfilUseCase(perfilGateway);
    }

    @DisplayName("Deve cadastrar perfil com sucesso.")
    @Test
    void deveCadastrarPerfilComSucesso() {
        // Arrange
        String novoNome = "Funcionario";

        // Act
        cadastrarPerfilUseCase.cadastrar(novoNome);

        var todosPerfis = perfilGateway.buscarTodos();
        var perfilCadastrado = todosPerfis.stream()
                .filter(perfil -> perfil.getNome().equals(novoNome))
                .findFirst();

        // Assert
        assertThat(perfilCadastrado).isPresent();
        assertThat(perfilCadastrado.get().getId()).isNotNull();
    }

    @DisplayName("Deve cadastrar perfil com erro. Novo nome já cadastrado.")
    @Test
    void deveLancarExcecaoSeNovoNomeJaCadastrado() {
        // Arrange
        String novoNome = "Entregador";

        // Act & Assert
        assertThatThrownBy(() -> cadastrarPerfilUseCase.cadastrar(novoNome))
                .isInstanceOf(NomePerfilDuplicadoException.class)
                .hasMessage(PERFIL_DUPLICADO);
    }
}
