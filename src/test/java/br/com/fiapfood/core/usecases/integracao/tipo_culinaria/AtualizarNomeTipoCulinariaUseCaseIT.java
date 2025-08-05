package br.com.fiapfood.core.usecases.integracao.tipo_culinaria;

import br.com.fiapfood.core.exceptions.perfil.NomePerfilDuplicadoException;
import br.com.fiapfood.core.exceptions.tipo_culinaria.NomeTipoCulinariaInvalidoException;
import br.com.fiapfood.core.exceptions.tipo_culinaria.TipoCulinariaInvalidoException;
import br.com.fiapfood.core.gateways.impl.TipoCulinariaGateway;
import br.com.fiapfood.core.gateways.interfaces.ITipoCulinariaGateway;
import br.com.fiapfood.core.usecases.tipo_culinaria.impl.AtualizarNomeTipoCulinariaUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.IAtualizarNomeTipoCulinariaUseCase;
import br.com.fiapfood.infraestructure.repositories.interfaces.ITipoCulinariaRepository;
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
public class AtualizarNomeTipoCulinariaUseCaseIT {

    private final String PERFIL_DUPLICADO = "Já existe um tipo de culinária com o nome informado.";
    private final String TIPO_CULINARIA_NAO_ENCONTRADO = "Não foi encontrado nenhum tipo de culinária com o id informado.";

    private IAtualizarNomeTipoCulinariaUseCase atualizarNomeTipoCulinariaUseCase;
    private ITipoCulinariaGateway tipoCulinariaGateway;

    @Autowired
    private ITipoCulinariaRepository tipoCulinariaRepository;

    @BeforeEach
    public void setUp() {
        tipoCulinariaGateway = new TipoCulinariaGateway(tipoCulinariaRepository);

        atualizarNomeTipoCulinariaUseCase = new AtualizarNomeTipoCulinariaUseCase(tipoCulinariaGateway);
    }

    @DisplayName("Deve atualizar nome do tipo culinária com sucesso.")
    @Test
    void deveAtualizarNomeTipoCulinariaComSucesso() {
        // Arrange
        int tipoCulinariaId = 1;
        String novoNome = "Grega";

        var tipoCulinariaAntesDeAtualizar = tipoCulinariaGateway.buscarPorId(tipoCulinariaId);

        // Act
        atualizarNomeTipoCulinariaUseCase.atualizar(tipoCulinariaId, novoNome);

        var tipoCulinariaAposAtualizar = tipoCulinariaGateway.buscarPorId(tipoCulinariaId);

        // Assert
        assertThat(tipoCulinariaAposAtualizar.getNome()).isNotEqualTo(tipoCulinariaAntesDeAtualizar.getNome());
        assertThat(tipoCulinariaAposAtualizar.getNome()).isEqualTo(novoNome);
    }

    @DisplayName("Deve atualizar nome do tipo culinária com erro. Tipo culinária não encontrado através do ID.")
    @Test
    void deveLancarExcecaoSeTipoCulinariaNaoEncontradoAtravesDoID() {
        // Arrange
        int tipoCulinariaId = 10;
        String novoNome = "Grega";

        // Act & Assert
        assertThatThrownBy(() -> atualizarNomeTipoCulinariaUseCase.atualizar(tipoCulinariaId, novoNome))
                .isInstanceOf(TipoCulinariaInvalidoException.class)
                .hasMessage(TIPO_CULINARIA_NAO_ENCONTRADO);
    }

    @DisplayName("Deve atualizar nome do tipo culinária com erro. Novo nome já cadastrado.")
    @Test
    void deveLancarExcecaoSeNovoNomeJaCadastrado() {
        // Arrange
        int tipoCulinariaId = 1;
        String novoNome = "Japonesa";

        // Act & Assert
        assertThatThrownBy(() -> atualizarNomeTipoCulinariaUseCase.atualizar(tipoCulinariaId, novoNome))
                .isInstanceOf(NomePerfilDuplicadoException.class)
                .hasMessage(PERFIL_DUPLICADO);
    }

    @DisplayName("Deve atualizar nome do tipo culinária com erro. Novo nome inválido")
    @Test
    void deveLancarExcecaoSeNovoNomeForInvalido() {
        // Arrange
        int tipoCulinariaId = 1;
        String novoNome = "";

        // Act & Assert
        assertThatThrownBy(() -> atualizarNomeTipoCulinariaUseCase.atualizar(tipoCulinariaId, novoNome))
                .isInstanceOf(NomeTipoCulinariaInvalidoException.class)
                .hasMessage("O nome do tipo de culinária é inválido.");
    }
}
