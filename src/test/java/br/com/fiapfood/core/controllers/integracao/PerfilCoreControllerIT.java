package br.com.fiapfood.core.controllers.integracao;

import br.com.fiapfood.core.controllers.impl.PerfilCoreController;
import br.com.fiapfood.core.controllers.interfaces.IPerfilCoreController;
import br.com.fiapfood.core.exceptions.perfil.ExclusaoPerfilNaoPermitidaException;
import br.com.fiapfood.core.exceptions.perfil.NomePerfilDuplicadoException;
import br.com.fiapfood.core.exceptions.perfil.PerfilInvalidoException;
import br.com.fiapfood.core.exceptions.perfil.PerfilNaoEncontradoException;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.impl.UsuarioGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.perfil.impl.*;
import br.com.fiapfood.core.usecases.perfil.interfaces.*;
import br.com.fiapfood.infraestructure.repositories.interfaces.IPerfilRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.IUsuarioRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PerfilCoreControllerIT {

    private IPerfilCoreController perfilCoreController;
    @Autowired
    IPerfilRepository perfilRepository;
    @Autowired
    IUsuarioRepository usuarioRepository;

    @BeforeEach
    public void setup() {
        IPerfilGateway perfilGateway = new PerfilGateway(perfilRepository);
        IUsuarioGateway usuarioGateway = new UsuarioGateway(usuarioRepository);
        ICadastrarPerfilUseCase cadastrarPerfilUseCase = new CadastrarPerfilUseCase(perfilGateway);
        IAtualizarNomePerfilUseCase atualizarNomePerfilUseCase = new AtualizarNomePerfilUseCase(perfilGateway);
        IInativarPerfilUseCase inativarPerfilUseCase = new InativarPerfilUseCase(perfilGateway, usuarioGateway);
        IReativarPerfilUseCase reativarPerfilUseCase = new ReativarPerfilUseCase(perfilGateway);
        IBuscarTodosPerfisUseCase buscarTodosPerfisUseCase = new BuscarTodosPerfisUseCase(perfilGateway);
        IBuscarPerfilPorIdUseCase buscarPerfilPorIdUseCase = new BuscarPerfilPorIdUseCase(perfilGateway);

        perfilCoreController = new PerfilCoreController(
                buscarTodosPerfisUseCase,
                buscarPerfilPorIdUseCase,
                cadastrarPerfilUseCase,
                atualizarNomePerfilUseCase,
                inativarPerfilUseCase,
                reativarPerfilUseCase
        );
    }

    @Nested
    class BuscarTodosPerfis {
        @DisplayName("Buscar todos perfis cadastrados chamando o use case com sucesso.")
        @Test
        void deveBuscarTodosOsPerfisCadastradosChamandoUseCase() {
            // Arrange
            List<String> expectedNomes = List.of("Dono", "Cliente", "Administrador", "Entregador");

            // Act
            var perfisRetornadosDoController = perfilCoreController.buscarTodos();

            // Assert
            for (int i = 0; i < expectedNomes.size(); i++) {
                assertThat(perfisRetornadosDoController.get(i).nome()).isEqualTo(expectedNomes.get(i));
            }
        }

        @DisplayName("Buscar todos perfis cadastrados chamando o use case com erro. Nenhum perfil for encontrado")
        @Test
        @Sql(scripts = "/db_clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // Limpando o BD para retornar o erro
        void deveLancarExcecaoSeNenhumPerfilEncontrado() {
            // Arrange

            // Act & Assert
            assertThatThrownBy(() -> perfilCoreController.buscarTodos())
                    .isInstanceOf(PerfilNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum perfil na base de dados.");
        }
    }

    @Nested
    class BuscarPerfilPorId {
        @DisplayName("Deve buscar perfil por id chamando o use case com sucesso.")
        @Test
        void deveBuscarPerfilPorIdChamandoUseCase() {
            // Arrange
            int id = 1;

            // Act
            var  perfilRetornadosDoController = perfilCoreController.buscarPorId(id);

            // Assert
            assertThat(perfilRetornadosDoController.id()).isEqualTo(1);
            assertThat(perfilRetornadosDoController.nome()).isEqualTo("Dono");
        }

        @DisplayName("Deve buscar perfil por id chamando o use case com erro. Perfil for encontrado")
        @Test
        void deveLancarExcecaoSeNenhumPerfilEncontradoAtravesDoId() {
            // Arrange
            int id = 100;

            // Act & Assert
            assertThatThrownBy(() -> perfilCoreController.buscarPorId(id))
                    .isInstanceOf(PerfilInvalidoException.class)
                    .hasMessage("Não foi encontrado nenhum perfil com o id informado.");
        }
    }

    @Nested
    class CadastrarPerfil {
        @DisplayName("Deve cadastrar perfil chamando use case com sucesso.")
        @Test
        void deveCadastrarPerfilChamandoUseCase() {
            // Arrange
            String nomePerfil = "Funcionário";
            int sizeAntesDeAdicionarNovoPerfil = perfilCoreController.buscarTodos().size();

            // Act
            perfilCoreController.cadastrar(nomePerfil);
            var todosPerfisAtualizado = perfilCoreController.buscarTodos(); // Usado para checar se foi mesmo cadastrado
            boolean perfilCriado = todosPerfisAtualizado.stream()
                    .anyMatch(perfil -> perfil.nome().equals(nomePerfil));

            // Assert
            assertThat(todosPerfisAtualizado.size()).isEqualTo(sizeAntesDeAdicionarNovoPerfil + 1);
            assertThat(perfilCriado).isTrue();
        }

        @DisplayName("Deve cadastrar perfil chamando use case com erro. Nome ja cadastrado")
        @Test
        void deveLancarExcecaoSeNomeJaEstiverCadastrado() {
            // Arrange
            String nomePerfil = "Entregador";

            // Act & Assert
            assertThatThrownBy(() -> perfilCoreController.cadastrar(nomePerfil))
                    .isInstanceOf(NomePerfilDuplicadoException.class)
                    .hasMessage("Já existe um perfil com o nome informado.");
        }
    }

    @Nested
    class AtualizarNomePerfil {
        @DisplayName("Deve atualizar o nome do perfil chamando a use case com sucesso.")
        @Test
        void deveAtualizarNomePerfilChamandoUseCase() {
            // Arrange
            int perfilId = 4;
            String novoNome = "Funcionario";

            // Act
            perfilCoreController.atualizarNome(perfilId, novoNome);
            var perfilAposAtualizacao = perfilCoreController.buscarPorId(perfilId);

            // Assert
            assertThat(perfilAposAtualizacao.nome()).isEqualTo(novoNome);
        }

        @DisplayName("Deve atualizar o nome do perfil chamando a use case com erro. Novo nome ja cadastrado")
        @Test
        void deveLancarExcecaoSeNovoNomeJaCadastrado() {
            // Arrange
            int perfilId = 4;
            String novoNome = "Administrador";

            // Act & Assert
            assertThatThrownBy(() -> perfilCoreController.atualizarNome(perfilId, novoNome))
                    .isInstanceOf(NomePerfilDuplicadoException.class)
                    .hasMessage("Já existe um perfil com o nome informado.");
        }

        @DisplayName("Deve atualizar o nome do perfil chamando a use case com erro. Perfil nao encontrado através do id.")
        @Test
        void deveLancarExcecaoSePerfilNaoEncontradoPorId() {
            // Arrange
            int perfilId = 100;
            String novoNome = "Funcionario";

            // Act & Assert
            assertThatThrownBy(() -> perfilCoreController.atualizarNome(perfilId, novoNome))
                    .isInstanceOf(PerfilInvalidoException.class)
                    .hasMessage("Não foi encontrado nenhum perfil com o id informado.");
        }
    }

    @Nested
    class InativarPerfil {
        @DisplayName("Deve inativar perfil chamando use case com sucesso.")
        @Test
        void deveInativarPerfilComSucessoChamandoUseCase() {
            // Arrange
            int perfilId = 4;

            // Act
            perfilCoreController.inativar(perfilId);
            var perfilAposInativacao = perfilCoreController.buscarPorId(perfilId);

            // Assert
            assertThat(perfilAposInativacao.dataInativacao()).isNotNull();
        }

        @DisplayName("Deve inativar perfil chamando a use case com erro. Perfil nao encontrado através do id.")
        @Test
        void deveLancarExcecaoSePerfilNaoEncontradoPorId() {
            // Arrange
            int perfilId = 100;

            // Act & Assert
            assertThatThrownBy(() -> perfilCoreController.inativar(perfilId))
                    .isInstanceOf(PerfilInvalidoException.class)
                    .hasMessage("Não foi encontrado nenhum perfil com o id informado.");
        }

        @DisplayName("Deve inativar perfil chamando a use case com erro. Usuarios ativos associados com perfil.")
        @Test
        void deveLancarExcecaoSeExistirUsuarioAtivoAssociadoComPerfil() {
            // Arrange
            int perfilId = 1;

            // Act & Assert
            Assertions.assertThatThrownBy(() -> perfilCoreController.inativar(perfilId))
                    .isInstanceOf(ExclusaoPerfilNaoPermitidaException.class)
                    .hasMessage("Não é possível inativar o perfil pois há usuário ativo associado ao perfil.");
        }
    }

    @Nested
    class ReativarPerfil {
        @DisplayName("Deve reativar perfil chamando use case com sucesso.")
        @Test
        void deveReativarPerfilComSucessoChamandoUseCase() {
            // Arrange
            int perfilId = 3;

            // Act
            perfilCoreController.reativar(perfilId);
            var perfilAposReativacao = perfilCoreController.buscarPorId(perfilId);

            // Assert
            assertThat(perfilAposReativacao.dataInativacao()).isNull();
        }

        @DisplayName("Deve reativar perfil chamando a use case com erro. Perfil nao encontrado através do id.")
        @Test
        void deveLancarExcecaoSePerfilNaoEncontradoPorId() {
            // Arrange
            int perfilId = 100;

            // Act & Assert
            assertThatThrownBy(() -> perfilCoreController.reativar(perfilId))
                    .isInstanceOf(PerfilInvalidoException.class)
                    .hasMessage("Não foi encontrado nenhum perfil com o id informado.");
        }
    }
}
