package br.com.fiapfood.core.usecases.integracao.tipo_culinaria;

import br.com.fiapfood.core.exceptions.perfil.NomePerfilDuplicadoException;
import br.com.fiapfood.core.exceptions.tipo_culinaria.NomeTipoCulinariaInvalidoException;
import br.com.fiapfood.core.gateways.impl.TipoCulinariaGateway;
import br.com.fiapfood.core.gateways.interfaces.ITipoCulinariaGateway;
import br.com.fiapfood.core.usecases.tipo_culinaria.impl.CadastrarTipoCulinariaUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.ICadastrarTipoCulinariaUseCase;
import br.com.fiapfood.infraestructure.repositories.interfaces.ITipoCulinariaRepository;
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
public class CadastrarTipoCulinariaUseCaseIT {
    private final String PERFIL_DUPLICADO = "Já existe um tipo de culinária com o nome informado.";

    private ICadastrarTipoCulinariaUseCase cadastrarTipoCulinariaUseCase;
    private ITipoCulinariaGateway tipoCulinariaGateway;

    @Autowired
    private ITipoCulinariaRepository tipoCulinariaRepository;

    @BeforeEach
    public void setUp() {
        tipoCulinariaGateway = new TipoCulinariaGateway(tipoCulinariaRepository);

        cadastrarTipoCulinariaUseCase = new CadastrarTipoCulinariaUseCase(tipoCulinariaGateway);
    }

    @DisplayName("Deve cadastrar tipo culinária com sucesso.")
    @Test
    void deveCadastrarTipoCulinariaComSucesso() {
        // Arrange
        String novoNome = "Grega";

        // Act
        cadastrarTipoCulinariaUseCase.cadastrar(novoNome);
        var todosTiposCulinaria = tipoCulinariaGateway.buscarTodos();
        boolean novoTipoRegisterado = todosTiposCulinaria.stream().anyMatch(tipo -> tipo.getNome().equals(novoNome));

        // Assert
        assertThat(novoTipoRegisterado).isTrue();
    }

    @DisplayName("Deve cadastrar tipo culinária com erro. Novo nome já cadastrado.")
    @Test
    void deveLancarExcecaoSeNomeJaCadastrado() {
        // Arrange
        String novoNome = "Brasileira";

        // Act & Assert
        assertThatThrownBy(() -> cadastrarTipoCulinariaUseCase.cadastrar(novoNome))
                .isInstanceOf(NomePerfilDuplicadoException.class)
                .hasMessage(PERFIL_DUPLICADO);
    }

    @DisplayName("Deve cadastrar tipo culinária com erro. Novo nome inválido")
    @Test
    void deveLancarExcecaoSeNomeForInvalido() {
        // Arrange
        String novoNome = "";

        // Act & Assert
        assertThatThrownBy(() -> cadastrarTipoCulinariaUseCase.cadastrar( novoNome))
                .isInstanceOf(NomeTipoCulinariaInvalidoException.class)
                .hasMessage("O nome do tipo de culinária é inválido.");
    }
}
