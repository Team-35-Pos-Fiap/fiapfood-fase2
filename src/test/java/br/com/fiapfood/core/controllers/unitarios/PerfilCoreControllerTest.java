package br.com.fiapfood.core.controllers.unitarios;

import br.com.fiapfood.core.controllers.impl.PerfilCoreController;
import br.com.fiapfood.core.controllers.interfaces.IPerfilCoreController;
import br.com.fiapfood.core.entities.dto.perfil.PerfilCoreDto;
import br.com.fiapfood.core.exceptions.perfil.ExclusaoPerfilNaoPermitidaException;
import br.com.fiapfood.core.exceptions.perfil.NomePerfilDuplicadoException;
import br.com.fiapfood.core.exceptions.perfil.PerfilInvalidoException;
import br.com.fiapfood.core.exceptions.perfil.PerfilNaoEncontradoException;
import br.com.fiapfood.core.usecases.perfil.interfaces.*;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;


public class PerfilCoreControllerTest {

    @Mock
    private IBuscarTodosPerfisUseCase buscarTodosUseCase;
    @Mock
    private IBuscarPerfilPorIdUseCase buscarPorIdUseCase;
    @Mock
    private ICadastrarPerfilUseCase cadastrarPerfilUseCase;
    @Mock
    private IAtualizarNomePerfilUseCase atualizarNomePerfilUseCase;
    @Mock
    private IInativarPerfilUseCase inativarPerfilUseCase;
    @Mock
    private IReativarPerfilUseCase reativarPerfilUseCase;
    private IPerfilCoreController perfilCoreController;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        perfilCoreController = new PerfilCoreController(
                buscarTodosUseCase,
                buscarPorIdUseCase,
                cadastrarPerfilUseCase,
                atualizarNomePerfilUseCase,
                inativarPerfilUseCase,
                reativarPerfilUseCase
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Nested
    class BuscarTodosPerfis {
        @DisplayName("Buscar todos perfis cadastrados chamando o use case com sucesso.")
        @Test
        void deveBuscarTodosOsPerfisCadastradosChamandoUseCase() {
            // Arrange
            List<PerfilCoreDto> perfilCoreDtoRetornadoDoUseCase = List.of(
                    new PerfilCoreDto(1, "Admin", null, null),
                    new PerfilCoreDto(2, "Cliente", null, null)
            );

            when(buscarTodosUseCase.buscar()).thenReturn(perfilCoreDtoRetornadoDoUseCase);

            // Act
            var perfisRetornadosDoController = perfilCoreController.buscarTodos();

            // Assert
            assertThat(perfisRetornadosDoController.size()).isEqualTo(perfilCoreDtoRetornadoDoUseCase.size());
            assertThat(perfisRetornadosDoController.getFirst().id()).isEqualTo(perfilCoreDtoRetornadoDoUseCase.getFirst().id());
            assertThat(perfisRetornadosDoController.getFirst().nome()).isEqualTo(perfilCoreDtoRetornadoDoUseCase.getFirst().nome());
            assertThat(perfisRetornadosDoController.getLast().id()).isEqualTo(perfilCoreDtoRetornadoDoUseCase.getLast().id());
            assertThat(perfisRetornadosDoController.getLast().nome()).isEqualTo(perfilCoreDtoRetornadoDoUseCase.getLast().nome());

            verify(buscarTodosUseCase, times(1)).buscar();
        }

        @DisplayName("Buscar todos perfis cadastrados chamando o use case com erro. Nenhum perfil for encontrado")
        @Test
        void deveLancarExcecaoSeNenhumPerfilEncontrado() {
            // Arrange
            when(buscarTodosUseCase.buscar())
                    .thenThrow(new PerfilNaoEncontradoException("Não foi encontrado nenhum perfil na base de dados."));

            // Act & Assert
            assertThatThrownBy(() -> perfilCoreController.buscarTodos())
                    .isInstanceOf(PerfilNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum perfil na base de dados.");

            verify(buscarTodosUseCase, times(1)).buscar();
        }
    }

    @Nested
    class BuscarPerfilPorId {
        @DisplayName("Deve buscar perfil por id chamando o use case com sucesso.")
        @Test
        void deveBuscarPerfilPorIdChamandoUseCase() {
            // Arrange
            int id = 1;
            PerfilCoreDto perfilCoreDtoRetornadoDoUseCase = new PerfilCoreDto(
                    1,
                    "Admin",
                    null,
                    null
            );

            when(buscarPorIdUseCase.buscar(id)).thenReturn(perfilCoreDtoRetornadoDoUseCase);

            // Act
            var  perfilRetornadosDoController = perfilCoreController.buscarPorId(id);

            // Assert
            assertThat(perfilRetornadosDoController.id()).isEqualTo(perfilCoreDtoRetornadoDoUseCase.id());
            assertThat(perfilRetornadosDoController.nome()).isEqualTo(perfilCoreDtoRetornadoDoUseCase.nome());

            verify(buscarPorIdUseCase, times(1)).buscar(id);
        }

        @DisplayName("Deve buscar perfil por id chamando o use case com erro. Perfil for encontrado")
        @Test
        void deveLancarExcecaoSeNenhumPerfilEncontradoAtravesDoId() {
            // Arrange
            int id = 1;
            when(buscarPorIdUseCase.buscar(id))
                    .thenThrow(new PerfilInvalidoException("Não foi encontrado nenhum perfil com o id informado."));

            // Act & Assert
            assertThatThrownBy(() -> perfilCoreController.buscarPorId(id))
                    .isInstanceOf(PerfilInvalidoException.class)
                    .hasMessage("Não foi encontrado nenhum perfil com o id informado.");

            verify(buscarPorIdUseCase, times(1)).buscar(id);
        }
    }

    @Nested
    class CadastrarPerfil {
        @DisplayName("Deve cadastrar perfil chamando use case com sucesso.")
        @Test
        void deveCadastrarPerfilChamandoUseCase() {
            // Arrange
            String nomePerfil = "Funcionário";
            doNothing().when(cadastrarPerfilUseCase).cadastrar(nomePerfil);
            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

            // Act
            perfilCoreController.cadastrar(nomePerfil);

            // Assert
            verify(cadastrarPerfilUseCase, times(1)).cadastrar(captor.capture());
            assertThat(captor.getValue()).isEqualTo(nomePerfil);
        }

        @DisplayName("Deve cadastrar perfil chamando use case com erro. Nome ja cadastrado")
        @Test
        void deveLancarExcecaoSeNomeJaEstiverCadastrado() {
            // Arrange
            String nomePerfil = "Funcionário";
            doThrow(new NomePerfilDuplicadoException("Já existe um perfil com o nome informado.")).when(cadastrarPerfilUseCase).cadastrar(nomePerfil);

            // Act & Assert
            assertThatThrownBy(() -> perfilCoreController.cadastrar(nomePerfil))
                    .isInstanceOf(NomePerfilDuplicadoException.class)
                    .hasMessage("Já existe um perfil com o nome informado.");

            verify(cadastrarPerfilUseCase, times(1)).cadastrar(nomePerfil);
        }
    }

    @Nested
    class AtualizarNomePerfil {
        @DisplayName("Deve atualizar o nome do perfil chamando a use case com sucesso.")
        @Test
        void deveAtualizarNomePerfilChamandoUseCase() {
            // Arrange
            int perfilId = 1;
            String novoNome = "Funcionario";

            doNothing().when(atualizarNomePerfilUseCase).atualizar(perfilId, novoNome);
            ArgumentCaptor<String> nomeCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

            // Act
            perfilCoreController.atualizarNome(perfilId, novoNome);

            // Assert
            verify(atualizarNomePerfilUseCase, times(1)).atualizar(idCaptor.capture(), nomeCaptor.capture());
            assertThat(nomeCaptor.getValue()).isEqualTo(novoNome);
            assertThat(idCaptor.getValue()).isEqualTo(perfilId);
        }

        @DisplayName("Deve atualizar o nome do perfil chamando a use case com erro. Novo nome ja cadastrado")
        @Test
        void deveLancarExcecaoSeNovoNomeJaCadastrado() {
            // Arrange
            int perfilId = 1;
            String novoNome = "Funcionario";

            doThrow(new NomePerfilDuplicadoException("Já existe um perfil com o nome informado.")).when(atualizarNomePerfilUseCase).atualizar(perfilId,novoNome);

            // Act & Assert
            assertThatThrownBy(() -> perfilCoreController.atualizarNome(perfilId, novoNome))
                    .isInstanceOf(NomePerfilDuplicadoException.class)
                    .hasMessage("Já existe um perfil com o nome informado.");

            verify(atualizarNomePerfilUseCase, times(1)).atualizar(perfilId, novoNome);
        }

        @DisplayName("Deve atualizar o nome do perfil chamando a use case com erro. Perfil nao encontrado através do id.")
        @Test
        void deveLancarExcecaoSePerfilNaoEncontradoPorId() {
            // Arrange
            int perfilId = 1;
            String novoNome = "Funcionario";

            doThrow(new PerfilInvalidoException("Não foi encontrado nenhum perfil com o id informado.")).when(atualizarNomePerfilUseCase).atualizar(perfilId,novoNome);

            // Act & Assert
            assertThatThrownBy(() -> perfilCoreController.atualizarNome(perfilId, novoNome))
                    .isInstanceOf(PerfilInvalidoException.class)
                    .hasMessage("Não foi encontrado nenhum perfil com o id informado.");

            verify(atualizarNomePerfilUseCase, times(1)).atualizar(perfilId, novoNome);
        }
    }

    @Nested
    class InativarPerfil {
        @DisplayName("Deve inativar perfil chamando use case com sucesso.")
        @Test
        void deveInativarPerfilComSucessoChamandoUseCase() {
            // Arrange
            int perfilId = 1;

            doNothing().when(inativarPerfilUseCase).inativar(perfilId);
            ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

            // Act
            perfilCoreController.inativar(perfilId);

            // Assert
            verify(inativarPerfilUseCase).inativar(idCaptor.capture());
            assertThat(idCaptor.getValue()).isEqualTo(perfilId);
        }

        @DisplayName("Deve inativar perfil chamando a use case com erro. Perfil nao encontrado através do id.")
        @Test
        void deveLancarExcecaoSePerfilNaoEncontradoPorId() {
            // Arrange
            int perfilId = 1;

            doThrow(new PerfilInvalidoException("Não foi encontrado nenhum perfil com o id informado.")).when(inativarPerfilUseCase).inativar(perfilId);

            // Act & Assert
            assertThatThrownBy(() -> perfilCoreController.inativar(perfilId))
                    .isInstanceOf(PerfilInvalidoException.class)
                    .hasMessage("Não foi encontrado nenhum perfil com o id informado.");

            verify(inativarPerfilUseCase, times(1)).inativar(perfilId);
        }

        @DisplayName("Deve inativar perfil chamando a use case com erro. Usuarios ativos associados com perfil.")
        @Test
        void deveLancarExcecaoSeExistirUsuarioAtivoAssociadoComPerfil() {
            // Arrange
            int perfilId = 1;

            doThrow(new ExclusaoPerfilNaoPermitidaException("Não é possível inativar o perfil pois há usuário ativo associado ao perfil.")).when(inativarPerfilUseCase).inativar(perfilId);

            // Act & Assert
            assertThatThrownBy(() -> perfilCoreController.inativar(perfilId))
                    .isInstanceOf(ExclusaoPerfilNaoPermitidaException.class)
                    .hasMessage("Não é possível inativar o perfil pois há usuário ativo associado ao perfil.");

            verify(inativarPerfilUseCase, times(1)).inativar(perfilId);
        }
    }

    @Nested
    class ReativarPerfil {
        @DisplayName("Deve reativar perfil chamando use case com sucesso.")
        @Test
        void deveReativarPerfilComSucessoChamandoUseCase() {
            // Arrange
            int perfilId = 1;

            doNothing().when(reativarPerfilUseCase).reativar(perfilId);
            ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

            // Act
            perfilCoreController.reativar(perfilId);

            // Assert
            verify(reativarPerfilUseCase).reativar(idCaptor.capture());
            assertThat(idCaptor.getValue()).isEqualTo(perfilId);
        }

        @DisplayName("Deve reativar perfil chamando a use case com erro. Perfil nao encontrado através do id.")
        @Test
        void deveLancarExcecaoSePerfilNaoEncontradoPorId() {
            // Arrange
            int perfilId = 1;

            doThrow(new PerfilInvalidoException("Não foi encontrado nenhum perfil com o id informado.")).when(reativarPerfilUseCase).reativar(perfilId);

            // Act & Assert
            assertThatThrownBy(() -> perfilCoreController.reativar(perfilId))
                    .isInstanceOf(PerfilInvalidoException.class)
                    .hasMessage("Não foi encontrado nenhum perfil com o id informado.");

            verify(reativarPerfilUseCase, times(1)).reativar(perfilId);
        }
    }
}
