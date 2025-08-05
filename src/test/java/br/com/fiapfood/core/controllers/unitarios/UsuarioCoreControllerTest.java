package br.com.fiapfood.core.controllers.unitarios;

import br.com.fiapfood.core.controllers.impl.UsuarioCoreController;
import br.com.fiapfood.core.controllers.interfaces.IUsuarioCoreController;
import br.com.fiapfood.core.entities.dto.endereco.DadosEnderecoCoreDto;
import br.com.fiapfood.core.entities.dto.paginacao.PaginacaoCoreDto;
import br.com.fiapfood.core.entities.dto.usuario.CadastrarUsuarioCoreDto;
import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioCoreDto;
import br.com.fiapfood.core.entities.dto.usuario.UsuarioPaginacaoCoreDto;
import br.com.fiapfood.core.exceptions.perfil.PerfilInvalidoException;
import br.com.fiapfood.core.exceptions.usuario.*;
import br.com.fiapfood.core.usecases.login.interfaces.IAtualizarMatriculaUseCase;
import br.com.fiapfood.core.usecases.login.interfaces.IAtualizarSenhaUseCase;
import br.com.fiapfood.core.usecases.login.interfaces.IValidarAcessoUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.*;
import br.com.fiapfood.infraestructure.controllers.request.endereco.DadosEnderecoDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.CadastrarUsuarioDto;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.UUID;

import static br.com.fiapfood.utils.DtoDataGenerator.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UsuarioCoreControllerTest {

    @Mock
    private IBuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;
    @Mock
    private IBuscarTodosUsuariosUseCase buscarTodosUsuariosUseCase;
    @Mock
    private ICadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    @Mock
    private IAtualizarEmailUsuarioUseCase atualizarEmailUsuarioUseCase;
    @Mock
    private IAtualizarNomeUsuarioUseCase atualizarNomeUsuarioUseCase;
    @Mock
    private IInativarUsuarioUseCase inativarUsuarioUseCase;
    @Mock
    private IReativarUsuarioUseCase reativarUsuarioUseCase;
    @Mock
    private IAtualizarPerfilUsuarioUseCase atualizarPerfilUsuarioUseCase;
    @Mock
    private IAtualizarEnderecoUsuarioUseCase atualizarEnderecoUsuarioUseCase;
    @Mock
    private IValidarAcessoUseCase validarAcessoUseCase;
    @Mock
    private IAtualizarSenhaUseCase atualizarSenhaUseCase;
    @Mock
    private IAtualizarMatriculaUseCase atualizarMatriculaUseCase;
    private IUsuarioCoreController usuarioCoreController;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        usuarioCoreController = new UsuarioCoreController(
                buscarUsuarioPorIdUseCase,
                buscarTodosUsuariosUseCase,
                cadastrarUsuarioUseCase,
                atualizarEmailUsuarioUseCase,
                atualizarNomeUsuarioUseCase,
                inativarUsuarioUseCase,
                reativarUsuarioUseCase,
                atualizarPerfilUsuarioUseCase,
                atualizarEnderecoUsuarioUseCase,
                validarAcessoUseCase,
                atualizarSenhaUseCase,
                atualizarMatriculaUseCase
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Nested
    class CadastrarUsuarioRequest {
        @DisplayName("Cadastrar novo usuário chamando o use case com sucesso")
        @Test
        void devePermitirCadastrarUsuarioComSucesso(){
            // Arrange
            CadastrarUsuarioDto dadosUsuarioController = cadastrarUsuarioDtoValido();
            CadastrarUsuarioCoreDto dadosUsuarioUseCase = cadastrarUsuarioCoreDtoValido();

            doNothing().when(cadastrarUsuarioUseCase).cadastrar(any(CadastrarUsuarioCoreDto.class));
            ArgumentCaptor<CadastrarUsuarioCoreDto> captor = ArgumentCaptor.forClass(CadastrarUsuarioCoreDto.class);

            // Act
            usuarioCoreController.cadastrar(dadosUsuarioController);

            // Assert
            verify(cadastrarUsuarioUseCase, times(1)).cadastrar(captor.capture());
            assertThat(captor.getValue()).isEqualTo(dadosUsuarioUseCase);
        }

        @DisplayName("Cadastrar novo usuário chamando o use case com erro. Email já cadastrado")
        @Test
        void deveLancarExcecaoSeEmailJaCadastrado(){
            // Arrange
            CadastrarUsuarioDto dadosUsuarioController = cadastrarUsuarioDtoValido();

            doThrow(new EmailDuplicadoException("Já existe um usuário com o email informado.")).when(cadastrarUsuarioUseCase).cadastrar(any(CadastrarUsuarioCoreDto.class));

            //Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.cadastrar(dadosUsuarioController))
                    .isInstanceOf(EmailDuplicadoException.class)
                    .hasMessage("Já existe um usuário com o email informado.");
            verify(cadastrarUsuarioUseCase, times(1)).cadastrar(any(CadastrarUsuarioCoreDto.class));
        }

        @DisplayName("Cadastrar novo usuário chamando o use case com erro. Matricula já cadastrada")
        @Test
        void deveLancarExcecaoSeMatriculaJaCadastrado(){
            // Arrange
            CadastrarUsuarioDto dadosUsuarioController = cadastrarUsuarioDtoValido();

            doThrow(new MatriculaDuplicadaException("Já existe um usuário com a matrícula informada.")).when(cadastrarUsuarioUseCase).cadastrar(any(CadastrarUsuarioCoreDto.class));

            //Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.cadastrar(dadosUsuarioController))
                    .isInstanceOf(MatriculaDuplicadaException.class)
                    .hasMessage("Já existe um usuário com a matrícula informada.");
            verify(cadastrarUsuarioUseCase, times(1)).cadastrar(any(CadastrarUsuarioCoreDto.class));
        }

        @DisplayName("Cadastrar novo usuário chamando o use case com erro. Perfil informado não cadastrado.")
        @Test
        void deveLancarExcecaoSePerfilNaoCadstrado(){
            // Arrange
            CadastrarUsuarioDto dadosUsuarioController = cadastrarUsuarioDtoValido();

            doThrow(new PerfilInvalidoException("Não foi encontrado nenhum perfil com o id informado.")).when(cadastrarUsuarioUseCase).cadastrar(any(CadastrarUsuarioCoreDto.class));

            //Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.cadastrar(dadosUsuarioController))
                    .isInstanceOf(PerfilInvalidoException.class)
                    .hasMessage("Não foi encontrado nenhum perfil com o id informado.");
            verify(cadastrarUsuarioUseCase, times(1)).cadastrar(any(CadastrarUsuarioCoreDto.class));
        }
    }

    @Nested
    class InativarUsuarioRequest {
        @DisplayName("Inativar usuário chamando use case com sucesso.")
        @Test
        void devePermitirInativarUsuarioComSucesso(){
            // Arrange
            UUID id = UUID.randomUUID();

            doNothing().when(inativarUsuarioUseCase).inativar(any(UUID.class));
            ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

            // Act
            usuarioCoreController.inativar(id);

            // Assert
            verify(inativarUsuarioUseCase, times(1)).inativar(captor.capture());
            assertThat(captor.getValue()).isEqualTo(id);
        }

        @DisplayName("Inativar usuário chamando o use case com erro. Usuário não encontrado.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioPorId(){
            // Arrange
            UUID id = UUID.randomUUID();

            doThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o id informado.")).when(inativarUsuarioUseCase).inativar(any(UUID.class));

            //Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.inativar(id))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum usuário com o id informado.");
            verify(inativarUsuarioUseCase, times(1)).inativar(any(UUID.class));
        }

        @DisplayName("Inativar usuário chamando o use case com erro. Usuário já está inativo.")
        @Test
        void deveLancarExcecaoSeUsuarioJaEstiverInativo(){
            // Arrange
            UUID id = UUID.randomUUID();

            doThrow(new AtualizacaoStatusUsuarioNaoPermitidaException("Não é possível inativar o usuário pois ele já se encontra inativo.")).when(inativarUsuarioUseCase).inativar(any(UUID.class));

            //Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.inativar(id))
                    .isInstanceOf(AtualizacaoStatusUsuarioNaoPermitidaException.class)
                    .hasMessage("Não é possível inativar o usuário pois ele já se encontra inativo.");
            verify(inativarUsuarioUseCase, times(1)).inativar(any(UUID.class));
        }
    }

    @Nested
    class AtivarUsuarioRequest {
        @DisplayName("Ativar usuário chamando use case com sucesso.")
        @Test
        void devePermitirReativarUsuarioComSucesso(){
            // Arrange
            UUID id = UUID.randomUUID();

            doNothing().when(reativarUsuarioUseCase).reativar(any(UUID.class));
            ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

            // Act
            usuarioCoreController.reativar(id);

            // Assert
            verify(reativarUsuarioUseCase, times(1)).reativar(captor.capture());
            assertThat(captor.getValue()).isEqualTo(id);
        }

        @DisplayName("Reativar usuário chamando o use case com erro. Usuário não encontrado.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioPorId(){
            // Arrange
            UUID id = UUID.randomUUID();

            doThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o id informado.")).when(reativarUsuarioUseCase).reativar(any(UUID.class));

            //Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.reativar(id))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum usuário com o id informado.");
            verify(reativarUsuarioUseCase, times(1)).reativar(any(UUID.class));
        }

        @DisplayName("Reativar usuário chamando o use case com erro. Usuário já está inativo.")
        @Test
        void deveLancarExcecaoSeUsuarioJaEstiverInativo(){
            // Arrange
            UUID id = UUID.randomUUID();

            doThrow(new AtualizacaoStatusUsuarioNaoPermitidaException("Não é possível inativar o usuário pois ele já se encontra inativo.")).when(reativarUsuarioUseCase).reativar(any(UUID.class));

            //Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.reativar(id))
                    .isInstanceOf(AtualizacaoStatusUsuarioNaoPermitidaException.class)
                    .hasMessage("Não é possível inativar o usuário pois ele já se encontra inativo.");
            verify(reativarUsuarioUseCase, times(1)).reativar(any(UUID.class));
        }
    }

    @Nested
    class BuscarUsuarioPorIdRequest {

        @DisplayName("Busca usuário por id chamando o use case com sucesso.")
        @Test
        void devePermitirBuscarUsuarioPorId(){
            // Arrange
            DadosUsuarioCoreDto usuarioEsperado = dadosUsuarioCoreDtoValido();
            UUID id = usuarioEsperado.id();

            when(buscarUsuarioPorIdUseCase.buscar(any(UUID.class)))
                    .thenReturn(usuarioEsperado);
            ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

            // Act
            var usuarioRetornadoDoController = usuarioCoreController.buscarUsuarioPorId(id);

            // Assert
            verify(buscarUsuarioPorIdUseCase, times(1)).buscar(captor.capture());
            assertThat(captor.getValue()).isEqualTo(id);
            assertThat(usuarioEsperado.id()).isEqualTo(usuarioRetornadoDoController.id());
            assertThat(usuarioEsperado.nome()).isEqualTo(usuarioRetornadoDoController.nome());
            assertThat(usuarioEsperado.email()).isEqualTo(usuarioRetornadoDoController.email());
        }

        @DisplayName("Busca usuário por id chamando o use case com erro. Usuário não encontrado.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioAtivoPorId(){
            // Arrange
            UUID id = UUID.randomUUID();

            when(buscarUsuarioPorIdUseCase.buscar(any(UUID.class)))
                    .thenThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o id informado."));

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.buscarUsuarioPorId(id))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum usuário com o id informado.");
            verify(buscarUsuarioPorIdUseCase, times(1)).buscar(any(UUID.class));
        }
    }

    @Nested
    class BuscarTodosUsuariosRequest {
        @DisplayName("Buscar todos usuarios chamando o use case com sucesso")
        @Test
        void deveRetornarListaDeUsuariosPaginada(){
            // Arrange
            when(buscarTodosUsuariosUseCase.buscar(anyInt()))
                    .thenReturn(new UsuarioPaginacaoCoreDto(new ArrayList<>(), new PaginacaoCoreDto(1, 1, 1)));

            // Act
            var usuariosRetornadosPeloController = usuarioCoreController.buscarTodos(1);

            // Assert
            assertThat(usuariosRetornadosPeloController).isNotNull();
            verify(buscarTodosUsuariosUseCase, times(1)).buscar(anyInt());
        }

        @DisplayName("Buscar todos usuarios com erro. Pagina nao contem nenhum usuario")
        @Test
        void deveRetornarExcecaoSePaginaForMaiorDoQueOLimite(){
            // Arrange
            int pagina = 10;

            when(buscarTodosUsuariosUseCase.buscar(anyInt())).thenThrow(new UsuarioNaoEncontradoException("Não foram encontrados usuários na base de dados para a página informada."));

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.buscarTodos(pagina))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foram encontrados usuários na base de dados para a página informada.");
            verify(buscarTodosUsuariosUseCase, times(1)).buscar(anyInt());
        }
    }

    @Nested
    class AtualizarEmailRequest {

        @DisplayName("Atualizar email chamando o use case com sucesso")
        @Test
        void devePermitirAtualizarEmail(){
            // Arrange
            UUID id = UUID.randomUUID();
            String novoEmail = "john.doe@email.com";

            doNothing().when(atualizarEmailUsuarioUseCase).atualizar(any(UUID.class), anyString());
            ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<UUID> usuarioCaptor = ArgumentCaptor.forClass(UUID.class);

            // Act
            usuarioCoreController.atualizarEmail(id, novoEmail);

            // Assert
            verify(atualizarEmailUsuarioUseCase, times(1)).atualizar(usuarioCaptor.capture(), emailCaptor.capture());
            assertThat(emailCaptor.getValue()).isEqualTo(novoEmail);
            assertThat(usuarioCaptor.getValue()).isEqualTo(id);
        }

        @DisplayName("Atualizar email chamando o use case com erro. Usuário nao encontrado.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioPorId(){
            // Arrange
            UUID id = UUID.randomUUID();
            String novoEmail = "john.doe@email.com";

            doThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o id informado.")).when(atualizarEmailUsuarioUseCase).atualizar(any(UUID.class), anyString());

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarEmail(id, novoEmail))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum usuário com o id informado.");
            verify(atualizarEmailUsuarioUseCase, times(1)).atualizar(any(UUID.class), anyString());
        }

        @DisplayName("Atualizar email chamando o use case com erro. Usuário encontrado inativo")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioAtivoPorId(){
            // Arrange
            UUID id = UUID.randomUUID();
            String novoEmail = "john.doe@email.com";

            doThrow(new AtualizacaoEmailUsuarioNaoPermitidoException("Não é possível alterar o email de um usuário inativo.")).when(atualizarEmailUsuarioUseCase).atualizar(any(UUID.class), anyString());

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarEmail(id, novoEmail))
                    .isInstanceOf(AtualizacaoEmailUsuarioNaoPermitidoException.class)
                    .hasMessage("Não é possível alterar o email de um usuário inativo.");
            verify(atualizarEmailUsuarioUseCase, times(1)).atualizar(any(UUID.class), anyString());
        }

        @DisplayName("Atualizar email com erro. Novo email ja cadastrado.")
        @Test
        void deveLancarExecaoSeEmailJaCadastrado(){
            // Arrange
            UUID id = UUID.randomUUID();
            String novoEmail = "john.doe@email.com";

            doThrow(new AtualizacaoEmailUsuarioNaoPermitidoException("Já existe um usuário com o email informado.")).when(atualizarEmailUsuarioUseCase).atualizar(any(UUID.class), anyString());

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarEmail(id, novoEmail))
                    .isInstanceOf(AtualizacaoEmailUsuarioNaoPermitidoException.class)
                    .hasMessage("Já existe um usuário com o email informado.");
            verify(atualizarEmailUsuarioUseCase, times(1)).atualizar(any(UUID.class), anyString());
        }
    }

    @Nested
    class AtualizarPerfilRequest {
        @DisplayName("Atualizar perfil chamando o use case com sucesso.")
        @Test
        void devePermitirAtualizarPerfil(){
            // Arrange
            UUID id = UUID.randomUUID();
            int novoPerfilId = 2;

            doNothing().when(atualizarPerfilUsuarioUseCase).atualizar(any(UUID.class), anyInt());
            ArgumentCaptor<Integer> perfilIdCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<UUID> usuarioIdCaptor = ArgumentCaptor.forClass(UUID.class);

            // Act
            usuarioCoreController.atualizarPerfil(id, novoPerfilId);

            // Assert
            verify(atualizarPerfilUsuarioUseCase, times(1)).atualizar(usuarioIdCaptor.capture(), perfilIdCaptor.capture());
            assertThat(perfilIdCaptor.getValue()).isEqualTo(novoPerfilId);
            assertThat(usuarioIdCaptor.getValue()).isEqualTo(id);
        }

        @DisplayName("Atualizar perfil chamando o use case com erro. Usuário nao encontrado por id.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioPorId(){
            // Arrange
            UUID id = UUID.randomUUID();
            int novoPerfilId = 2;

            doThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o id informado.")).when(atualizarPerfilUsuarioUseCase).atualizar(any(UUID.class), anyInt());

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarPerfil(id, novoPerfilId))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum usuário com o id informado.");
            verify(atualizarPerfilUsuarioUseCase, times(1)).atualizar(any(UUID.class), anyInt());
        }

        @DisplayName("Atualizar perfil chamando o use case com erro. Usuário encontrado esta inativo")
        @Test
        void deveLancarExcecaoSeUsuarioEstiverInativo(){
            // Arrange
            UUID id = UUID.randomUUID();
            int novoPerfilId = 2;

            doThrow(new UsuarioInativoException("O perfil selecionado é o mesmo que o usuário já possui.")).when(atualizarPerfilUsuarioUseCase).atualizar(any(UUID.class), anyInt());

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarPerfil(id, novoPerfilId))
                    .isInstanceOf(UsuarioInativoException.class)
                    .hasMessage("O perfil selecionado é o mesmo que o usuário já possui.");
            verify(atualizarPerfilUsuarioUseCase, times(1)).atualizar(any(UUID.class), anyInt());
        }

        @DisplayName("Atualizar perfil com erro. Novo perfil é o mesmo do ja cadastrado.")
        @Test
        void deveLancarExcecaoSeNovoPerfilForOMesmoDoRegistrado(){
            // Arrange
            UUID id = UUID.randomUUID();
            int novoPerfilId = 2;

            doThrow(new AtualizacaoPerfilNaoPermitidaException("O perfil selecionado é o mesmo que o usuário já possui.")).when(atualizarPerfilUsuarioUseCase).atualizar(any(UUID.class), anyInt());

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarPerfil(id, novoPerfilId))
                    .isInstanceOf(AtualizacaoPerfilNaoPermitidaException.class)
                    .hasMessage("O perfil selecionado é o mesmo que o usuário já possui.");
            verify(atualizarPerfilUsuarioUseCase, times(1)).atualizar(any(UUID.class), anyInt());
        }
    }

    @Nested
    class AtualizarEnderecoRequest {
        @DisplayName("Atualizar endereco chamando o use case com sucesso.")
        @Test
        void devePermitirAtualizarEndereco(){
            // Arrange
            UUID usuarioId = UUID.randomUUID();
            DadosEnderecoCoreDto dadosEnderecoParaOUseCase = dadosEnderecoCoreDtoValido();
            DadosEnderecoDto dadosEnderecoDoController = dadosEnderecoDtoValido();

            doNothing().when(atualizarEnderecoUsuarioUseCase)
                    .atualizar(any(UUID.class), any(DadosEnderecoCoreDto.class));

            ArgumentCaptor<UUID> usuarioIdCaptor = ArgumentCaptor.forClass(UUID.class);
            ArgumentCaptor<DadosEnderecoCoreDto> dadosEnderecoCaptor = ArgumentCaptor.forClass(DadosEnderecoCoreDto.class);

            // Act
            usuarioCoreController.atualizarDadosEndereco(usuarioId, dadosEnderecoDoController);

            // Assert
            verify(atualizarEnderecoUsuarioUseCase, times(1)).atualizar(usuarioIdCaptor.capture(), dadosEnderecoCaptor.capture());
            assertThat(usuarioIdCaptor.getValue()).isEqualTo(usuarioId);
            assertThat(dadosEnderecoCaptor.getValue()).isEqualTo(dadosEnderecoParaOUseCase);
        }

        @DisplayName("Atualizar endereço chamando o use case com erro. Usuario nao encontrado por id.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioPorId(){
            // Arrange
            UUID usuarioId = UUID.randomUUID();
            DadosEnderecoDto dadosEnderecoDoController = dadosEnderecoDtoValido();

            doThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o id informado.")).when(atualizarEnderecoUsuarioUseCase).atualizar(any(UUID.class), any(DadosEnderecoCoreDto.class));

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarDadosEndereco(usuarioId, dadosEnderecoDoController))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum usuário com o id informado.");
            verify(atualizarEnderecoUsuarioUseCase, times(1)).atualizar(any(UUID.class), any(DadosEnderecoCoreDto.class));
        }

        @DisplayName("Atualizar endereço chamando o use case com erro. Usuario encontrado esta inativo.")
        @Test
        void deveLancarExcecaoSeUsuarioEstiverInativo(){
            // Arrange
            UUID usuarioId = UUID.randomUUID();
            DadosEnderecoDto dadosEnderecoDoController = dadosEnderecoDtoValido();

            doThrow(new UsuarioInativoException("Não é possível alterar o nome de um usuário inativo.")).when(atualizarEnderecoUsuarioUseCase).atualizar(any(UUID.class), any(DadosEnderecoCoreDto.class));

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarDadosEndereco(usuarioId, dadosEnderecoDoController))
                    .isInstanceOf(UsuarioInativoException.class)
                    .hasMessage("Não é possível alterar o nome de um usuário inativo.");
            verify(atualizarEnderecoUsuarioUseCase, times(1)).atualizar(any(UUID.class), any(DadosEnderecoCoreDto.class));
        }
    }

    @Nested
    class AtualizarNomeRequest {
        @DisplayName("Atualizar nome chamando o use case com sucesso.")
        @Test
        void devePermitirAtualizarNome(){
            // Arrange
            UUID idUsuario = UUID.randomUUID();
            String novoNome = "John Doe";

            doNothing().when(atualizarNomeUsuarioUseCase).atualizar(any(UUID.class), anyString());

            ArgumentCaptor<UUID> idUsuarioCaptor = ArgumentCaptor.forClass(UUID.class);
            ArgumentCaptor<String> novoNomeCaptor = ArgumentCaptor.forClass(String.class);

            // Act
            usuarioCoreController.atualizarNome(idUsuario,novoNome);

            // Assert
            verify(atualizarNomeUsuarioUseCase, times(1)).atualizar(idUsuarioCaptor.capture(), novoNomeCaptor.capture());
            assertThat(idUsuarioCaptor.getValue()).isEqualTo(idUsuario);
            assertThat(novoNomeCaptor.getValue()).isEqualTo(novoNome);
        }

        @DisplayName("Atualizar nome chamando o use case com erro. Usuario nao encontrado por id.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioPorId(){
            // Arrange
            UUID idUsuario = UUID.randomUUID();
            String novoNome = "John Doe";

            doThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o id informado.")).when(atualizarNomeUsuarioUseCase).atualizar(any(UUID.class), anyString());

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarNome(idUsuario, novoNome))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum usuário com o id informado.");
            verify(atualizarNomeUsuarioUseCase, times(1)).atualizar(any(UUID.class), anyString());
        }

        @DisplayName("Atualizar nome chamando o use case com erro. Usuario encontrado esta inativo")
        @Test
        void deveLancarExcecaoSeUsuarioEncontradoEstiverInativo(){
            // Arrange
            UUID idUsuario = UUID.randomUUID();
            String novoNome = "John Doe";

            doThrow(new UsuarioInativoException("Não é possível alterar o nome de um usuário inativo.")).when(atualizarNomeUsuarioUseCase).atualizar(any(UUID.class), anyString());

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarNome(idUsuario, novoNome))
                    .isInstanceOf(UsuarioInativoException.class)
                    .hasMessage("Não é possível alterar o nome de um usuário inativo.");
            verify(atualizarNomeUsuarioUseCase, times(1)).atualizar(any(UUID.class), anyString());
        }

        @DisplayName("Atualizar nome chamando o use case com erro. Novo nome igual ao atual.")
        @Test
        void deveLancarExcecaoSeNovoNomeForIgualAoAtual(){
            // Arrange
            UUID idUsuario = UUID.randomUUID();
            String novoNome = "John Doe";

            doThrow(new AtualizacaoNomeUsuarioNaoPermitidoException("Não é possível alterar o nome do usuário pois ele é igual ao nome do usuário.")).when(atualizarNomeUsuarioUseCase).atualizar(any(UUID.class), anyString());

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarNome(idUsuario, novoNome))
                    .isInstanceOf(AtualizacaoNomeUsuarioNaoPermitidoException.class)
                    .hasMessage("Não é possível alterar o nome do usuário pois ele é igual ao nome do usuário.");
            verify(atualizarNomeUsuarioUseCase, times(1)).atualizar(any(UUID.class), anyString());
        }
    }

    @Nested
    class AtualizarSenhaRequest {
        @DisplayName("Trocar senha chamando o use case com sucesso")
        @Test
        void devePermitirTrocarSenha(){
            // Arrange
            UUID usuarioId = UUID.randomUUID();
            String novaSenha = "124";

            doNothing().when(atualizarSenhaUseCase).atualizar(any(UUID.class), anyString());

            ArgumentCaptor<UUID> usuarioCaptor = ArgumentCaptor.forClass(UUID.class);
            ArgumentCaptor<String> senhaCaptor = ArgumentCaptor.forClass(String.class);

            // Act
            usuarioCoreController.atualizarSenha(usuarioId, novaSenha);

            // Assert
            verify(atualizarSenhaUseCase, times(1)).atualizar(usuarioCaptor.capture(), senhaCaptor.capture());
            assertThat(senhaCaptor.getValue()).isEqualTo(novaSenha);
            assertThat(usuarioCaptor.getValue()).isEqualTo(usuarioId);
        }

        @DisplayName("Atualizar senha chamndo o use case com erro. Usuario nao encontrado atraves do id")
        @Test
        void deveLancarExcecaoSeUsuarioNaoEncontradoAtravesDoId(){
            // Arrange
            UUID usuarioId = UUID.randomUUID();
            String novaSenha = "124";

            doThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o id informado.")).when(atualizarSenhaUseCase).atualizar(any(UUID.class), anyString());

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarSenha(usuarioId, novaSenha))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum usuário com o id informado.");
            verify(atualizarSenhaUseCase, times(1)).atualizar(any(UUID.class), anyString());
        }

        @DisplayName("Atualizar senha chamando o use case com erro. Usuário encontrado esta inativo")
        @Test
        void deveLancarExcecaoSeUsuarioEstiverInativo(){
            // Arrange
            UUID usuarioId = UUID.randomUUID();
            String novaSenha = "124";

            doThrow(new UsuarioInativoException("Não é possível alterar a senha de um usuário inativo.")).when(atualizarSenhaUseCase).atualizar(any(UUID.class), anyString());

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarSenha(usuarioId, novaSenha))
                    .isInstanceOf(UsuarioInativoException.class)
                    .hasMessage("Não é possível alterar a senha de um usuário inativo.");
            verify(atualizarSenhaUseCase, times(1)).atualizar(any(UUID.class), anyString());
        }
    }

    @Nested
    class AtualizarMatriculaRequest {
        @DisplayName("Trocar matricula chamando o use case com sucesso")
        @Test
        void devePermitirTrocarMatricula(){
            // Arrange
            UUID usuarioId = UUID.randomUUID();
            String novaMatricula = "us0010";

            doNothing().when(atualizarMatriculaUseCase).atualizar(any(UUID.class), anyString());

            ArgumentCaptor<UUID> usuarioCaptor = ArgumentCaptor.forClass(UUID.class);
            ArgumentCaptor<String> matriculaCaptor = ArgumentCaptor.forClass(String.class);

            // Act
            usuarioCoreController.atualizarMatricula(usuarioId, novaMatricula);

            // Assert
            verify(atualizarMatriculaUseCase, times(1)).atualizar(usuarioCaptor.capture(), matriculaCaptor.capture());
            assertThat(matriculaCaptor.getValue()).isEqualTo(novaMatricula);
            assertThat(usuarioCaptor.getValue()).isEqualTo(usuarioId);
        }

        @DisplayName("Atualizar matricula chamando o use case com erro. Usuário nao encontrado através do id.")
        @Test
        void deveLancarExcecaoSeUsuarioNaoEncontradoAtravesDoId(){
            // Arrange
            UUID usuarioId = UUID.randomUUID();
            String novaMatricula = "us0010";

            doThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o id informado.")).when(atualizarMatriculaUseCase).atualizar(any(UUID.class), anyString());

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarMatricula(usuarioId, novaMatricula))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum usuário com o id informado.");
            verify(atualizarMatriculaUseCase, times(1)).atualizar(any(UUID.class), anyString());
        }

        @DisplayName("Atualizar matricula chamando o use case com erro. Usuário encontrado está inativo")
        @Test
        void deveLancarExcecaoSeUsuarioEstiverInativo(){
            // Arrange
            UUID usuarioId = UUID.randomUUID();
            String novaMatricula = "us0010";

            doThrow(new UsuarioInativoException("Não é possível alterar a senha de um usuário inativo.")).when(atualizarMatriculaUseCase).atualizar(any(UUID.class), anyString());

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarMatricula(usuarioId, novaMatricula))
                    .isInstanceOf(UsuarioInativoException.class)
                    .hasMessage("Não é possível alterar a senha de um usuário inativo.");
            verify(atualizarMatriculaUseCase, times(1)).atualizar(any(UUID.class), anyString());
        }

        @DisplayName("Atualizar matricula chamando o use case com erro. Nova matricula ja cadastrada.")
        @Test
        void deveLancarExcecaoSeNovaMatriculaJaCadastrada(){
            // Arrange
            UUID usuarioId = UUID.randomUUID();
            String novaMatricula = "us0010";

            doThrow(new MatriculaDuplicadaException("Já existe um usuário com a matrícula informada.")).when(atualizarMatriculaUseCase).atualizar(any(UUID.class), anyString());

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarMatricula(usuarioId, novaMatricula))
                    .isInstanceOf(MatriculaDuplicadaException.class)
                    .hasMessage("Já existe um usuário com a matrícula informada.");
            verify(atualizarMatriculaUseCase, times(1)).atualizar(any(UUID.class), anyString());
        }
    }

    @Nested
    class ValidarLoginRequest {
        @DisplayName("Validação de login chamando o use case com sucesso")
        @Test
        void devePermitirValidarLogin(){
            // Arrange
            String matricula = "us0001";
            String senha = "123";

            when(validarAcessoUseCase.validar(anyString(), anyString()))
                    .thenReturn("Acesso liberado.");

            ArgumentCaptor<String> matriculaCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<String> senhaCaptor = ArgumentCaptor.forClass(String.class);

            // Act
            String mensagemRetornada = usuarioCoreController.validarAcesso(matricula, senha);

            // Assert
            verify(validarAcessoUseCase, times(1)).validar(matriculaCaptor.capture(), senhaCaptor.capture());
            assertThat(matriculaCaptor.getValue()).isEqualTo(matricula);
            assertThat(senhaCaptor.getValue()).isEqualTo(senha);
            assertThat(mensagemRetornada).isNotNull();
            assertThat(mensagemRetornada).isEqualTo("Acesso liberado.");
        }

        @DisplayName("Validação de login chamando o use case com erro. Usuario não encontrado através de matrícula e senha.")
        @Test
        void deveLancarExcecaoSeUsuarioNaoEncontradoAtravesDaMatriculaESenha(){
            // Arrange
            String matricula = "us0001";
            String senha = "123";

            when(validarAcessoUseCase.validar(anyString(), anyString()))
                    .thenThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com a matrícula e senha informados."));

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.validarAcesso(matricula, senha))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum usuário com a matrícula e senha informados.");
            verify(validarAcessoUseCase, times(1)).validar(anyString(), anyString());
        }

        @DisplayName("Validação de login chamando o use case com erro. Usuario encontrado está inativo")
        @Test
        void deveLancarExcecaoSeUsuarioEncontradoEstiverInativo(){
            // Arrange
            String matricula = "us0001";
            String senha = "123";

            when(validarAcessoUseCase.validar(anyString(), anyString()))
                    .thenThrow(new UsuarioSemAcessoException("Não é possível realizar o login para usuários inativos."));

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.validarAcesso(matricula, senha))
                    .isInstanceOf(UsuarioSemAcessoException.class)
                    .hasMessage("Não é possível realizar o login para usuários inativos.");
            verify(validarAcessoUseCase, times(1)).validar(anyString(), anyString());
        }
    }
}
