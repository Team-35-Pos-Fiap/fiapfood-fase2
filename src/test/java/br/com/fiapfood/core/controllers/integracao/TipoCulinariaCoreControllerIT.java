package br.com.fiapfood.core.controllers.integracao;

import br.com.fiapfood.core.controllers.impl.TipoCulinariaCoreController;
import br.com.fiapfood.core.controllers.interfaces.ITipoCulinariaCoreController;
import br.com.fiapfood.core.exceptions.perfil.NomePerfilDuplicadoException;
import br.com.fiapfood.core.exceptions.tipo_culinaria.TipoCulinariaInvalidoException;
import br.com.fiapfood.core.exceptions.tipo_culinaria.TipoCulinariaNaoEncontradoException;
import br.com.fiapfood.core.gateways.impl.TipoCulinariaGateway;
import br.com.fiapfood.core.gateways.interfaces.ITipoCulinariaGateway;
import br.com.fiapfood.core.usecases.tipo_culinaria.impl.AtualizarNomeTipoCulinariaUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.impl.BuscarTipoCulinariaPorIdUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.impl.BuscarTodosTiposCulinariaUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.impl.CadastrarTipoCulinariaUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.IAtualizarNomeTipoCulinariaUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.IBuscarTipoCulinariaPorIdUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.IBuscarTodosTiposCulinariaUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.ICadastrarTipoCulinariaUseCase;
import br.com.fiapfood.infraestructure.repositories.interfaces.ITipoCulinariaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class TipoCulinariaCoreControllerIT {

    private ITipoCulinariaCoreController tipoCulinariaCoreController;
    @Autowired
    ITipoCulinariaRepository tipoCulinariaRepository;

    @BeforeEach
    void setup() {
        ITipoCulinariaGateway iTipoCulinariaGateway = new TipoCulinariaGateway(tipoCulinariaRepository);
        IBuscarTodosTiposCulinariaUseCase buscarTodosUseCase = new BuscarTodosTiposCulinariaUseCase(iTipoCulinariaGateway);
        IBuscarTipoCulinariaPorIdUseCase buscarPorIdUseCase = new BuscarTipoCulinariaPorIdUseCase(iTipoCulinariaGateway);
        ICadastrarTipoCulinariaUseCase cadastrarTipoCulinariaUseCase = new CadastrarTipoCulinariaUseCase(iTipoCulinariaGateway);
        IAtualizarNomeTipoCulinariaUseCase atualizarNomeTipoCulinariaUseCase = new AtualizarNomeTipoCulinariaUseCase(iTipoCulinariaGateway);

        tipoCulinariaCoreController = new TipoCulinariaCoreController(buscarTodosUseCase, buscarPorIdUseCase, cadastrarTipoCulinariaUseCase, atualizarNomeTipoCulinariaUseCase);
    }

    @Nested
    class BuscarTodosRequest {
        @DisplayName("Buscar todos tipo culinária chamando use case com sucesso.")
        @Test
        void buscarTodosRequestComSucesso() {
            // Arrange
            List<String> tiposCulinariaCadastrados = List.of("Brasileira", "Italiana", "Japonesa", "Churrasco", "Vegetariana");

            // Act
            var tiposCulinariaRetornadosDoController = tipoCulinariaCoreController.buscarTodos();

            // Assert
            assertThat(tiposCulinariaRetornadosDoController.size()).isEqualTo(tiposCulinariaCadastrados.size());
            for (int i = 0; i < tiposCulinariaCadastrados.size(); i++) {
                assertThat(tiposCulinariaRetornadosDoController.get(i).nome()).isEqualTo(tiposCulinariaCadastrados.get(i));
            }
        }

        @DisplayName("Buscar todos tipo culinária chamando use case com erro. Nenhum tipo cadastrado.")
        @Test
        @Sql(scripts = "/db_clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // Limpando o BD para retornar o erro
        void deveLancarExcecaoSeNenhumTipoCadstrado() {
            // Arrange

            // Act & Assert
            assertThatThrownBy(() -> tipoCulinariaCoreController.buscarTodos())
                    .isInstanceOf(TipoCulinariaNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum tipo de culinária na base de dados.");
        }

    }

    @Nested
    class BuscarPorIdRequest {
        @DisplayName("Buscar tipo culinária por id chamando o use case com sucesso.")
        @Test
        void buscarPorIdRequest() {
            // Arrange
            int tipoCulinariaId = 1;
            String nomeEsperado = "Brasileira";

            // Act
            var tipoCulinariaRetornadoPeloController = tipoCulinariaCoreController.buscarPorId(tipoCulinariaId);

            // Assert
            assertThat(tipoCulinariaRetornadoPeloController.id()).isEqualTo(tipoCulinariaId);
            assertThat(tipoCulinariaRetornadoPeloController.nome()).isEqualTo(nomeEsperado);
        }

        @DisplayName("Buscar tipo culinária por id chamando use case com erro. Tipo culinária não encontrado por id.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarTipoCulinariaPorId() {
            // Arrange
            int tipoCulinariaId = 100;

            // Act & Assert
            assertThatThrownBy(() -> tipoCulinariaCoreController.buscarPorId(tipoCulinariaId))
                    .isInstanceOf(TipoCulinariaInvalidoException.class)
                    .hasMessage("Não foi encontrado nenhum tipo de culinária com o id informado.");
        }
    }

    @Nested
    class CadastrarRequest {
        @DisplayName("Cadastrar tipo culinária chamando use case com sucesso.")
        @Test
        void deveCadastrarNovoTipoCulinariaComSucesso() {
            //Arrange
            String nomeTipoCulinaria = "Grega";
            var sizeTiposCulinariaAntesDoCadastro = tipoCulinariaCoreController.buscarTodos().size();

            // Act
            tipoCulinariaCoreController.cadastrar(nomeTipoCulinaria);
            var todosTiposCulinariaAtualizados = tipoCulinariaCoreController.buscarTodos();
            boolean tipoCulinariaCriado = todosTiposCulinariaAtualizados.stream().anyMatch(tipo -> tipo.nome().equals(nomeTipoCulinaria));

            // Assert
            assertThat(tipoCulinariaCriado).isTrue();
            assertThat(todosTiposCulinariaAtualizados.size()).isEqualTo(sizeTiposCulinariaAntesDoCadastro + 1);
        }

        @DisplayName("Cadastrar tipo culinária chamando use case com erro. Nome já cadastrado.")
        @Test
        void deveLancarExcecaoSeNomeJaEstiverCadastrado() {
            //Arrange
            String nomeTipoCulinaria = "Churrasco";

            // Act & Assert
            assertThatThrownBy(() -> tipoCulinariaCoreController.cadastrar(nomeTipoCulinaria))
                    .isInstanceOf(NomePerfilDuplicadoException.class)
                    .hasMessage("Já existe um tipo de culinária com o nome informado.");
        }
    }

    @Nested
    class AtualizarRequest {
        @DisplayName("Atualizar nome tipo culinária chamando use case com sucesso.")
        @Test
        void deveAtualizarNomeTipoCulinariaComSucesso() {
            //Arrange
            int tipoCulinariaId = 1;
            String nomeTipoCulinaria = "Grega";

            // Act
            tipoCulinariaCoreController.atualizar(tipoCulinariaId, nomeTipoCulinaria);
            var tipoCulinariaAtualizado = tipoCulinariaCoreController.buscarPorId(tipoCulinariaId);

            // Assert
            assertThat(tipoCulinariaAtualizado.nome()).isEqualTo(nomeTipoCulinaria);
            assertThat(tipoCulinariaAtualizado.id()).isEqualTo(tipoCulinariaId);
        }

        @DisplayName("Atualizar nome tipo culinária chamando use case com erro. Novo nome ja cadastrado.")
        @Test
        void deveLancarExcecaoSeNovoNomeJaCadastrado() {
            //Arrange
            int tipoCulinariaId = 1;
            String nomeTipoCulinaria = "Churrasco";

            // Act & Assert
            assertThatThrownBy(() -> tipoCulinariaCoreController.atualizar(tipoCulinariaId, nomeTipoCulinaria))
                    .isInstanceOf(NomePerfilDuplicadoException.class)
                    .hasMessage("Já existe um tipo de culinária com o nome informado.");
        }

        @DisplayName("Atualizar nome tipo culinária chamando o use case com erro. Tipo culinária nao encontrado por id.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarTipoCulinariaPorId() {
            //Arrange
            int tipoCulinariaId = 100;
            String nomeTipoCulinaria = "Grega";

            // Act & Assert
            assertThatThrownBy(() -> tipoCulinariaCoreController.atualizar(tipoCulinariaId, nomeTipoCulinaria))
                    .isInstanceOf(TipoCulinariaInvalidoException.class)
                    .hasMessage("Não foi encontrado nenhum tipo de culinária com o id informado.");
        }
    }
}
