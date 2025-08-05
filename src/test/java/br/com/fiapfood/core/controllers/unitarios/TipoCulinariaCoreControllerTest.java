package br.com.fiapfood.core.controllers.unitarios;

import br.com.fiapfood.core.controllers.impl.TipoCulinariaCoreController;
import br.com.fiapfood.core.controllers.interfaces.ITipoCulinariaCoreController;
import br.com.fiapfood.core.entities.dto.tipo_culinaria.TipoCulinariaCoreDto;
import br.com.fiapfood.core.exceptions.perfil.NomePerfilDuplicadoException;
import br.com.fiapfood.core.exceptions.tipo_culinaria.TipoCulinariaInvalidoException;
import br.com.fiapfood.core.exceptions.tipo_culinaria.TipoCulinariaNaoEncontradoException;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.IAtualizarNomeTipoCulinariaUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.IBuscarTipoCulinariaPorIdUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.IBuscarTodosTiposCulinariaUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.ICadastrarTipoCulinariaUseCase;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static br.com.fiapfood.utils.DtoDataGenerator.tipoCulinariaCoreDtoValido;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class TipoCulinariaCoreControllerTest {

    @Mock
    private IBuscarTodosTiposCulinariaUseCase buscarTodosUseCase;
    @Mock
    private IBuscarTipoCulinariaPorIdUseCase buscarPorIdUseCase;
    @Mock
    private ICadastrarTipoCulinariaUseCase cadastrarTipoCulinariaUseCase;
    @Mock
    private IAtualizarNomeTipoCulinariaUseCase atualizarNomeTipoCulinariaUseCase;
    private ITipoCulinariaCoreController tipoCulinariaCoreController;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        tipoCulinariaCoreController = new TipoCulinariaCoreController(
                buscarTodosUseCase,
                buscarPorIdUseCase,
                cadastrarTipoCulinariaUseCase,
                atualizarNomeTipoCulinariaUseCase
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Nested
    class BuscarTodosRequest {
        @DisplayName("Buscar todos tipo culinária chamando use case com sucesso.")
        @Test
        void buscarTodosRequestComSucesso() {
            // Arrange
            List<TipoCulinariaCoreDto> todosTipoCulinariaRetornadosDoUseCase = List.of(
                    tipoCulinariaCoreDtoValido(),
                    tipoCulinariaCoreDtoValido()
            );

            when(buscarTodosUseCase.buscar()).thenReturn(todosTipoCulinariaRetornadosDoUseCase);

            // Act
            var tiposCulinariaRetornadosDoController = tipoCulinariaCoreController.buscarTodos();

            // Assert
            assertThat(tiposCulinariaRetornadosDoController.size()).isEqualTo(todosTipoCulinariaRetornadosDoUseCase.size());

            assertThat(tiposCulinariaRetornadosDoController.getFirst().id()).isEqualTo(todosTipoCulinariaRetornadosDoUseCase.getFirst().id());
            assertThat(tiposCulinariaRetornadosDoController.getFirst().nome()).isEqualTo(todosTipoCulinariaRetornadosDoUseCase.getFirst().nome());
            assertThat(tiposCulinariaRetornadosDoController.getLast().id()).isEqualTo(todosTipoCulinariaRetornadosDoUseCase.getLast().id());
            assertThat(tiposCulinariaRetornadosDoController.getLast().nome()).isEqualTo(todosTipoCulinariaRetornadosDoUseCase.getLast().nome());
            verify(buscarTodosUseCase, times(1)).buscar();
        }

        @DisplayName("Buscar todos tipo culinária chamando use case com erro. Nenhum tipo cadastrado.")
        @Test
        void deveLancarExcecaoSeNenhumTipoCadstrado() {
            // Arrange
            when(buscarTodosUseCase.buscar()).thenThrow(new TipoCulinariaNaoEncontradoException("Não foi encontrado nenhum tipo de culinária na base de dados."));

            // Act & Assert
            assertThatThrownBy(() -> tipoCulinariaCoreController.buscarTodos())
                    .isInstanceOf(TipoCulinariaNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum tipo de culinária na base de dados.");
            verify(buscarTodosUseCase, times(1)).buscar();
        }

    }

    @Nested
    class BuscarPorIdRequest {
        @DisplayName("Buscar tipo culinária por id chamando o use case com sucesso.")
        @Test
        void buscarPorIdRequest() {
            // Arrange
            int tipoCulinariaId = 1;
            TipoCulinariaCoreDto tipoCulinariaRetornadoDoUseCase = tipoCulinariaCoreDtoValido();

            when(buscarPorIdUseCase.buscar(anyInt())).thenReturn(tipoCulinariaRetornadoDoUseCase);

            // Act
            var tipoCulinariaRetornadoPeloController = tipoCulinariaCoreController.buscarPorId(tipoCulinariaId);

            // Assert
            assertThat(tipoCulinariaRetornadoPeloController.id()).isEqualTo(tipoCulinariaId);
            assertThat(tipoCulinariaRetornadoPeloController.nome()).isEqualTo(tipoCulinariaRetornadoDoUseCase.nome());
            verify(buscarPorIdUseCase, times(1)).buscar(anyInt());
        }

        @DisplayName("Buscar tipo culinária por id chamando use case com erro. Tipo culinária não encontrado por id.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarTipoCulinariaPorId() {
            // Arrange
            int tipoCulinariaId = 1;

            when(buscarPorIdUseCase.buscar(anyInt())).thenThrow(new TipoCulinariaInvalidoException("Não foi encontrado nenhum tipo de culinária com o id informado."));

            // Act & Assert
            assertThatThrownBy(() -> tipoCulinariaCoreController.buscarPorId(tipoCulinariaId))
                    .isInstanceOf(TipoCulinariaInvalidoException.class)
                    .hasMessage("Não foi encontrado nenhum tipo de culinária com o id informado.");
            verify(buscarPorIdUseCase, times(1)).buscar(anyInt());
        }
    }

    @Nested
    class CadastrarRequest {
        @DisplayName("Cadastrar tipo culinária chamando use case com sucesso.")
        @Test
        void deveCadastrarNovoTipoCulinariaComSucesso() {
            //Arrange
            String nomeTipoCulinaria = "Grega";

            doNothing().when(cadastrarTipoCulinariaUseCase).cadastrar(anyString());
            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

            // Act
            tipoCulinariaCoreController.cadastrar(nomeTipoCulinaria);

            // Assert
            verify(cadastrarTipoCulinariaUseCase, times(1)).cadastrar(captor.capture());
            assertThat(captor.getValue()).isEqualTo(nomeTipoCulinaria);
        }

        @DisplayName("Cadastrar tipo culinária chamando use case com erro. Nome já cadastrado.")
        @Test
        void deveLancarExcecaoSeNomeJaEstiverCadastrado() {
            //Arrange
            String nomeTipoCulinaria = "Grega";

            doThrow(new NomePerfilDuplicadoException("Já existe um tipo de culinária com o nome informado.")).when(cadastrarTipoCulinariaUseCase).cadastrar(anyString());

            // Act & Assert
            assertThatThrownBy(() -> tipoCulinariaCoreController.cadastrar(nomeTipoCulinaria))
                    .isInstanceOf(NomePerfilDuplicadoException.class)
                    .hasMessage("Já existe um tipo de culinária com o nome informado.");
            verify(cadastrarTipoCulinariaUseCase, times(1)).cadastrar(anyString());
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

            doNothing().when(atualizarNomeTipoCulinariaUseCase).atualizar(anyInt(), anyString());
            ArgumentCaptor<String> novoNomeCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

            // Act & Assert
            tipoCulinariaCoreController.atualizar(tipoCulinariaId, nomeTipoCulinaria);

            // Assert
            verify(atualizarNomeTipoCulinariaUseCase, times(1)).atualizar(idCaptor.capture(), novoNomeCaptor.capture());
            assertThat(novoNomeCaptor.getValue()).isEqualTo(nomeTipoCulinaria);
            assertThat(idCaptor.getValue()).isEqualTo(tipoCulinariaId);
        }

        @DisplayName("Atualizar nome tipo culinária chamando use case com erro. Novo nome ja cadastrado.")
        @Test
        void deveLancarExcecaoSeNovoNomeJaCadastrado() {
            //Arrange
            int tipoCulinariaId = 1;
            String nomeTipoCulinaria = "Grega";

            doThrow(new NomePerfilDuplicadoException("Já existe um tipo de culinária com o nome informado.")).when(atualizarNomeTipoCulinariaUseCase).atualizar(anyInt(), anyString());

            // Act & Assert
            // Act & Assert
            assertThatThrownBy(() -> tipoCulinariaCoreController.atualizar(tipoCulinariaId, nomeTipoCulinaria))
                    .isInstanceOf(NomePerfilDuplicadoException.class)
                    .hasMessage("Já existe um tipo de culinária com o nome informado.");
            verify(atualizarNomeTipoCulinariaUseCase, times(1)).atualizar(anyInt(), anyString());
        }

        @DisplayName("Atualizar nome tipo culinária chamando o use case com erro. Tipo culinária nao encontrado por id.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarTipoCulinariaPorId() {
            //Arrange
            int tipoCulinariaId = 1;
            String nomeTipoCulinaria = "Grega";

            doThrow(new TipoCulinariaInvalidoException("Não foi encontrado nenhum tipo de culinária com o id informado.")).when(atualizarNomeTipoCulinariaUseCase).atualizar(anyInt(), anyString());

            // Act & Assert
            // Act & Assert
            assertThatThrownBy(() -> tipoCulinariaCoreController.atualizar(tipoCulinariaId, nomeTipoCulinaria))
                    .isInstanceOf(TipoCulinariaInvalidoException.class)
                    .hasMessage("Não foi encontrado nenhum tipo de culinária com o id informado.");
            verify(atualizarNomeTipoCulinariaUseCase, times(1)).atualizar(anyInt(), anyString());
        }
    }
}
