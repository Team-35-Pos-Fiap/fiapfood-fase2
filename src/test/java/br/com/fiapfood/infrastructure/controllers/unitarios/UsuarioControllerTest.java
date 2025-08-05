package br.com.fiapfood.infrastructure.controllers.unitarios;

import br.com.fiapfood.core.controllers.interfaces.IUsuarioCoreController;
import br.com.fiapfood.core.exceptions.perfil.PerfilNaoEncontradoException;
import br.com.fiapfood.core.exceptions.usuario.*;
import br.com.fiapfood.infraestructure.controllers.UsuarioController;
import br.com.fiapfood.infraestructure.controllers.exceptions.ErrorHandler;
import br.com.fiapfood.infraestructure.controllers.request.endereco.DadosEnderecoDto;
import br.com.fiapfood.infraestructure.controllers.request.login.LoginDto;
import br.com.fiapfood.infraestructure.controllers.request.login.MatriculaDto;
import br.com.fiapfood.infraestructure.controllers.request.login.SenhaDto;
import br.com.fiapfood.infraestructure.controllers.request.paginacao.PaginacaoDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.*;
import br.com.fiapfood.infraestructure.utils.MensagensUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.UUID;

import static br.com.fiapfood.utils.DtoDataGenerator.*;
import static br.com.fiapfood.utils.JsonToString.asJsonString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasKey;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UsuarioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IUsuarioCoreController usuarioCoreController;

    AutoCloseable mock;

    @BeforeEach
    void setUp() throws Exception {
        mock = MockitoAnnotations.openMocks(this);
        UsuarioController usuarioController = new UsuarioController(usuarioCoreController);
        this.mockMvc = MockMvcBuilders.standaloneSetup(usuarioController)
                .setControllerAdvice(new ErrorHandler())
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Nested
    class CadastrarUsuarioRequest {
        @DisplayName("Cadastrar novo usuário com sucesso")
        @Test
        void devePermitirCadastrarUsuarioComSucesso() throws Exception {
            // Arrange
            CadastrarUsuarioDto dadosUsuario = cadastrarUsuarioDtoValido();

            doNothing().when(usuarioCoreController).cadastrar(any(CadastrarUsuarioDto.class));

            //Act & Assert
            mockMvc.perform(post("/usuarios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(dadosUsuario)))
                    .andDo(print())
                    .andExpect(status().isCreated());
            verify(usuarioCoreController, times(1)).cadastrar(dadosUsuario);
        }

        @DisplayName("Cadastrar novo usuário com erro. Email já cadastrado")
        @Test
        void deveLancarExcecaoSeEmailJaCadastrado() throws Exception {
            // Arrange
            CadastrarUsuarioDto dadosUsuario = cadastrarUsuarioDtoValido();

            doThrow(new EmailDuplicadoException("Já existe um usuário com o email informado.")).when(usuarioCoreController).cadastrar(any(CadastrarUsuarioDto.class));

            //Act & Assert
            mockMvc.perform(post("/usuarios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(dadosUsuario)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("Já existe um usuário com o email informado."));
            verify(usuarioCoreController, times(1)).cadastrar(dadosUsuario);
        }

        @DisplayName("Cadastrar novo usuário com erro. Matricula já cadastrada")
        @Test
        void deveLancarExcecaoSeMatriculaJaCadastrado() throws Exception {
            // Arrange
            CadastrarUsuarioDto dadosUsuario = cadastrarUsuarioDtoValido();

            doThrow(new MatriculaDuplicadaException("Já existe um usuário com a matrícula informada.")).when(usuarioCoreController).cadastrar(any(CadastrarUsuarioDto.class));

            //Act & Assert
            mockMvc.perform(post("/usuarios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(dadosUsuario)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("Já existe um usuário com a matrícula informada."));
            verify(usuarioCoreController, times(1)).cadastrar(dadosUsuario);
        }

        @DisplayName("Cadastrar novo usuário com erro. Dados do usuário inválidos no DTO")
        @ParameterizedTest
        @CsvSource({
                " , thiago@fiapfood.com, 1, O campo nome precisa estar preenchido.",
                "Thiago Motta, , 1, O campo email precisa estar preenchido.",
                "Thiago Motta, thiago@fiapfood.com, ,  O campo perfil precisa estar preenchido.",
                "Thiago Motta, thiago@fiapfood.com, -1,  O campo perfil precisa ter valor maior do que 0."
        })
        void deveLancarExcecaoParaCamposDeUsuarioInvalidos(String nome, String email, Integer perfil, String expectedError) throws Exception {
            // Arrange
            CadastrarUsuarioDto dadosUsuarioInvalidos = new CadastrarUsuarioDto(nome, perfil, loginDtoValido(), email,  dadosEnderecoDtoValido());

            // Act & Assert
            mockMvc.perform(post("/usuarios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(dadosUsuarioInvalidos)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.*").value(hasItem(expectedError)));
            verify(usuarioCoreController, times(0)).cadastrar(any(CadastrarUsuarioDto.class));
        }

        @DisplayName("Cadastrar novo usuário com erro. Dados do endereço inválidos no DTO")
        @ParameterizedTest
        @CsvSource({
                " , 24455450, Nova Cidade, Rua Aquidabã, Rio de Janeiro, O campo cidade precisa ser informado.",
                "São Gonçalo, , Nova Cidade, Rua Aquidabã, Rio de Janeiro, O campo cep precisa ser informado.",
                "São Gonçalo, 24455450, , Rua Aquidabã, Rio de Janeiro, O campo bairro precisa ser informado.",
                "São Gonçalo, 24455450, Nova Cidade, , Rio de Janeiro, O campo endereco precisa ser informado.",
                "São Gonçalo, 24455450, Nova Cidade, Rua Aquidabã, , O campo estado precisa ser informado.",
        })
        void deveLancarExcecaoParaCamposDeEnderecoInvalidos(String cidade, String cep, String bairro, String endereco, String estado, String expectedError) throws Exception {
            // Arrange
            DadosEnderecoDto dadosEnderecoInvalidos = new DadosEnderecoDto(
                    cidade,
                    cep,
                    bairro,
                    endereco,
                    estado,
                    79,
                    "Casa 8"
            );
            CadastrarUsuarioDto dadosUsuarioInvalidos = new CadastrarUsuarioDto("John Doe", 1, loginDtoValido(), "john.doe@email.com", dadosEnderecoInvalidos);

            // Act & Assert
            mockMvc.perform(post("/usuarios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(dadosUsuarioInvalidos)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.*").value(hasItem(expectedError)));
            verify(usuarioCoreController, times(0)).cadastrar(any(CadastrarUsuarioDto.class));
        }

        @DisplayName("Cadastrar novo usuário com erro. Dados do login inválidos no DTO")
        @ParameterizedTest
        @CsvSource({
                " , 123, O campo matricula precisa ser informado.",
                "us0001, , O campo senha precisa ser informado."
        })
        void deveLancarExcecaoParaCamposDeLoginInvalidos(String matricula, String senha, String expectedError) throws Exception {
            // Arrange
            LoginDto dadosLoginInvalidos = new LoginDto(
                    null,
                    matricula,
                    senha
            );
            CadastrarUsuarioDto dadosUsuarioInvalidos = new CadastrarUsuarioDto("John Doe", 1, dadosLoginInvalidos, "john.doe@email.com", dadosEnderecoDtoValido());

            // Act & Assert
            mockMvc.perform(post("/usuarios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(dadosUsuarioInvalidos)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.*").value(hasItem(expectedError)));
            verify(usuarioCoreController, times(0)).cadastrar(any(CadastrarUsuarioDto.class));
        }

        @DisplayName("Cadastrar novo usuário com erro. Perfil informado não cadastrado.")
        @Test
        void deveLancarExcecaoSePerfilNaoCadstrado() throws Exception {
            // Arrange
            CadastrarUsuarioDto dadosUsuarioInvalidos = new CadastrarUsuarioDto("John Doe", 10, loginDtoValido(), "john.doe@email.com", dadosEnderecoDtoValido());

            doThrow(new PerfilNaoEncontradoException("Não foi encontrado nenhum perfil com o id informado.")).when(usuarioCoreController).cadastrar(any(CadastrarUsuarioDto.class));

            // Act & Assert
            mockMvc.perform(post("/usuarios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(dadosUsuarioInvalidos)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.*").value(hasItem("Não foi encontrado nenhum perfil com o id informado.")));
            verify(usuarioCoreController, times(1)).cadastrar(any(CadastrarUsuarioDto.class));
        }
    }

    @Nested
    class InativarUsuarioRequest {
        @DisplayName("Inativar usuário com sucesso.")
        @Test
        void devePermitirInativarUsuarioComSucesso() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();

            doNothing().when(usuarioCoreController).inativar(any(UUID.class));

            // Act & Assert
            // Tive que mockar o Mensagem Util nesse teste para poder checar a mensagem
            try (MockedStatic<MensagensUtil> mensagensMock = mockStatic(MensagensUtil.class)) {
                mensagensMock.when(() -> MensagensUtil.recuperarMensagem(
                                MensagensUtil.SUCESSO_INATIVACAO_USUARIO, new Object[0]))
                        .thenReturn("Usuário inativado com sucesso.");

                mockMvc.perform(patch("/usuarios/{id}/status/inativa", id)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.mensagem").value("Usuário inativado com sucesso."));
            }
            verify(usuarioCoreController, times(1)).inativar(any(UUID.class));
        }

        @DisplayName("Inativar usuário com erro. Usuário não encontrado.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioPorId() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();

            doThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o id informado.")).when(usuarioCoreController).inativar(any(UUID.class));

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/status/inativa", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.mensagem").value("Não foi encontrado nenhum usuário com o id informado."));
            verify(usuarioCoreController, times(1)).inativar(any(UUID.class));
        }

        @DisplayName("Inativar usuário com erro. Usuário já está inativo.")
        @Test
        void deveLancarExcecaoSeUsuarioJaEstiverInativo() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();

            doThrow(new AtualizacaoStatusUsuarioNaoPermitidaException("Não é possível inativar o usuário pois ele já se encontra inativo.")).when(usuarioCoreController).inativar(any(UUID.class));

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/status/inativa", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("Não é possível inativar o usuário pois ele já se encontra inativo."));
            verify(usuarioCoreController, times(1)).inativar(any(UUID.class));
        }
    }

    @Nested
    class AtivarUsuarioRequest {
        @DisplayName("Ativar usuário com sucesso.")
        @Test
        void devePermitirAtivarUsuarioComSucesso() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();

            doNothing().when(usuarioCoreController).reativar(any(UUID.class));

            // Act & Assert
            // Tive que mockar o Mensagem Util nesse teste para poder checar a mensagem
            try (MockedStatic<MensagensUtil> mensagensMock = mockStatic(MensagensUtil.class)) {
                mensagensMock.when(() -> MensagensUtil.recuperarMensagem(
                                MensagensUtil.SUCESSO_REATIVACAO_USUARIO, new Object[0]))
                        .thenReturn("Usuário reativado com sucesso.");

                mockMvc.perform(patch("/usuarios/{id}/status/reativa", id)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.mensagem").value("Usuário reativado com sucesso."));
            }
            verify(usuarioCoreController, times(1)).reativar(any(UUID.class));
        }

        @DisplayName("Ativar usuário com erro. Usuário não encontrado.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioPorId() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();

            doThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o id informado.")).when(usuarioCoreController).reativar(any(UUID.class));

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/status/reativa", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.mensagem").value("Não foi encontrado nenhum usuário com o id informado."));
            verify(usuarioCoreController, times(1)).reativar(any(UUID.class));
        }

        @DisplayName("Ativar usuário com erro. Usuário já está ativo.")
        @Test
        void deveLancarExcecaoSeUsuarioJaEstiverAtivo() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();

            doThrow(new AtualizacaoStatusUsuarioNaoPermitidaException("Não é possível reativar um usuário pois ele já se encontra ativo.")).when(usuarioCoreController).reativar(any(UUID.class));

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/status/reativa", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("Não é possível reativar um usuário pois ele já se encontra ativo."));
            verify(usuarioCoreController, times(1)).reativar(any(UUID.class));
        }
    }

    @Nested
    class BuscarUsuarioPorIdRequest {

        @DisplayName("Busca usuário por id com sucesso.")
        @Test
        void devePermitirBuscarUsuarioPorId() throws Exception {
            // Arrange
            UsuarioDto usuarioEsperado = usuarioDtoValido();
            UUID id = usuarioEsperado.id();

            when(usuarioCoreController.buscarUsuarioPorId(any(UUID.class)))
                    .thenReturn(usuarioEsperado);

            // Act & Assert
            mockMvc.perform(get("/usuarios/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id.toString()))
                    .andExpect(jsonPath("$.nome").value(usuarioEsperado.nome()))
                    .andExpect(jsonPath("$.isAtivo").value(usuarioEsperado.isAtivo()))
                    .andExpect(jsonPath("$.email").value(usuarioEsperado.email()));
            verify(usuarioCoreController, times(1)).buscarUsuarioPorId(any(UUID.class));
        }

        @DisplayName("Busca usuário por id com erro. Usuário não encontrado.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioAtivoPorId() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();

            when(usuarioCoreController.buscarUsuarioPorId(any(UUID.class)))
                    .thenThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o id informado."));

            // Act & Assert
            mockMvc.perform(get("/usuarios/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound());
            verify(usuarioCoreController, times(1)).buscarUsuarioPorId(any(UUID.class));
        }
    }

    @Nested
    class BuscarTodosUsuariosRequest {
        @DisplayName("Buscar todos usuarios com sucesso")
        @Test
        void deveRetornarListaDeUsuariosPaginada() throws Exception {
            // Arrange
            when(usuarioCoreController.buscarTodos(anyInt()))
                    .thenReturn(new UsuarioPaginacaoDto(new ArrayList<>(), new PaginacaoDto(1, 1, 1)));

            // Act & Assert
            mockMvc.perform(get("/usuarios")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(hasKey("usuarios")))
                    .andExpect(jsonPath("$").value(hasKey("paginacao")));
            verify(usuarioCoreController, times(1)).buscarTodos(anyInt());
        }

        @DisplayName("Buscar todos usuarios com erro. Pagina menor ou igual a zero")
        @Test
        void deveLancarExcecaoSePaginaMenorOuIgualAZero() throws Exception {
            // Arrange
            int pagina = 0;

            // Act & Assert
            mockMvc.perform(get("/usuarios?pagina={pagina}", pagina)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.*").value("O parâmetro página precisa ser maior do que 0."));
            verify(usuarioCoreController, times(0)).buscarTodos(anyInt());
        }

        @DisplayName("Buscar todos usuarios com erro. Pagina nao contem nenhum usuario")
        @Test
        void deveRetornarExcecaoSePaginaForMaiorDoQueOLimite() throws Exception {
            // Arrange
            int pagina = 10;

            when(usuarioCoreController.buscarTodos(anyInt())).thenThrow(new UsuarioNaoEncontradoException("Não foram encontrados usuários na base de dados para a página informada.")); // builds inside static mock

            // Act & Assert
            mockMvc.perform(get("/usuarios?pagina={pagina}", pagina)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.mensagem").value("Não foram encontrados usuários na base de dados para a página informada."));
            verify(usuarioCoreController, times(1)).buscarTodos(anyInt());
        }
    }

    @Nested
    class AtualizarEmailRequest {

        @DisplayName("Atualizar email com sucesso")
        @Test
        void devePermitirAtualizarEmail() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            DadosEmailDto novoEmail = new DadosEmailDto("john.doe@email.com");

            doNothing().when(usuarioCoreController).atualizarEmail(any(UUID.class), anyString());

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/email", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoEmail)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
            verify(usuarioCoreController, times(1)).atualizarEmail(any(UUID.class), anyString());
        }

        @DisplayName("Atualizar email com erro. Usuário nao encontrado.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioPorId() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            DadosEmailDto novoEmail = new DadosEmailDto("john.doe@email.com");

            doThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o id informado.")).when(usuarioCoreController).atualizarEmail(any(UUID.class), anyString());

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/email", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoEmail)))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.*").value(hasItem("Não foi encontrado nenhum usuário com o id informado.")));
            verify(usuarioCoreController, times(1)).atualizarEmail(any(UUID.class), anyString());
        }

        @DisplayName("Atualizar email com erro. Usuário encontrado inativo")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioAtivoPorId() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            DadosEmailDto novoEmail = new DadosEmailDto("john.doe@email.com");

            doThrow(new AtualizacaoEmailUsuarioNaoPermitidoException("Não é possível alterar o email de um usuário inativo.")).when(usuarioCoreController).atualizarEmail(any(UUID.class), anyString());

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/email", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoEmail)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.*").value(hasItem("Não é possível alterar o email de um usuário inativo.")));
            verify(usuarioCoreController, times(1)).atualizarEmail(any(UUID.class), anyString());
        }

        @DisplayName("Atualizar email com erro. Novo email ja cadastrado.")
        @Test
        void deveLancarExecaoSeEmailJaCadastrado() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            DadosEmailDto novoEmail = new DadosEmailDto("john.doe@email.com");

            doThrow(new AtualizacaoEmailUsuarioNaoPermitidoException("Já existe um usuário com o email informado.")).when(usuarioCoreController).atualizarEmail(any(UUID.class), anyString());

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/email", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoEmail)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.*").value(hasItem("Já existe um usuário com o email informado.")));
            verify(usuarioCoreController, times(1)).atualizarEmail(any(UUID.class), anyString());
        }

        @DisplayName("Atualizar email com erro. DTO com dados invalidos.")
        @ParameterizedTest
        @CsvSource({
                " , O campo email precisa estar preenchido.",
                "johndoe.email.com, O e-mail precisa ser válido"
        })
        void deveLancarExcecaoParaCamposInvalidosNoDto(String email, String expectedErrorMessage) throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            DadosEmailDto novoEmail = new DadosEmailDto(email);

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/email", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoEmail)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.*").value(hasItem(expectedErrorMessage)));
            verify(usuarioCoreController, times(0)).atualizarEmail(any(UUID.class), anyString());
        }
    }

    @Nested
    class AtualizarPerfilRequest {
        @DisplayName("Atualizar perfil com sucesso.")
        @Test
        void devePermitirAtualizarPerfil() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            DadosPerfilDto novoPerfilId = new DadosPerfilDto(2);

            doNothing().when(usuarioCoreController).atualizarPerfil(any(UUID.class), anyInt());

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/perfil", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoPerfilId)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
            verify(usuarioCoreController, times(1)).atualizarPerfil(any(UUID.class), anyInt());
        }

        @DisplayName("Atualizar perfil com erro. Usuário nao encontrado por id.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioPorId() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            DadosPerfilDto novoPerfilId = new DadosPerfilDto(2);

            doThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o id informado.")).when(usuarioCoreController).atualizarPerfil(any(UUID.class), anyInt());

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/perfil", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoPerfilId)))
                    .andDo(print())
                    .andExpect(status().isNotFound());
            verify(usuarioCoreController, times(1)).atualizarPerfil(any(UUID.class), anyInt());
        }

        @DisplayName("Atualizar perfil com erro. Usuário encontrado esta inativo")
        @Test
        void deveLancarExcecaoSeUsuarioEstiverInativo() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            DadosPerfilDto novoPerfilId = new DadosPerfilDto(2);

            doThrow(new UsuarioInativoException("O perfil selecionado é o mesmo que o usuário já possui.")).when(usuarioCoreController).atualizarPerfil(any(UUID.class), anyInt());

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/perfil", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoPerfilId)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
            verify(usuarioCoreController, times(1)).atualizarPerfil(any(UUID.class), anyInt());
        }

        @DisplayName("Atualizar perfil com erro. Novo perfil é o mesmo do ja cadastrado.")
        @Test
        void deveLancarExcecaoSeNovoPerfilForOMesmoDoRegistrado() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            DadosPerfilDto novoPerfilId = new DadosPerfilDto(1);

            doThrow(new UsuarioInativoException("O perfil selecionado é o mesmo que o usuário já possui.")).when(usuarioCoreController).atualizarPerfil(any(UUID.class), anyInt());

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/perfil", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoPerfilId)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("O perfil selecionado é o mesmo que o usuário já possui."));
            verify(usuarioCoreController, times(1)).atualizarPerfil(any(UUID.class), anyInt());
        }

        @DisplayName("Atualizar perfil com erro. DTO com dados invalidos.")
        @Test
        void deveLancarExcecaoParaCamposInvalidosNoDto() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            DadosPerfilDto novoPerfilIdNulo = new DadosPerfilDto(null);
            DadosPerfilDto novoPerfilIdNegativo = new DadosPerfilDto(-1);

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/perfil", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoPerfilIdNulo)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.*").value(hasItem("O campo idPerfil precisa estar preenchido.")));

            mockMvc.perform(patch("/usuarios/{id}/perfil", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoPerfilIdNegativo)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.*").value(hasItem("O campo idPerfil precisa ter valor maior do que 0.")));

            verify(usuarioCoreController, times(0)).atualizarPerfil(any(UUID.class), anyInt());
        }
    }

    @Nested
    class AtualizarEnderecoRequest {
        @DisplayName("Atualizar endereco com sucesso.")
        @Test
        void devePermitirAtualizarEndereco() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            DadosEnderecoDto novoEndereco = dadosEnderecoDtoValido();

            doNothing().when(usuarioCoreController)
                    .atualizarDadosEndereco(any(UUID.class), any(DadosEnderecoDto.class));

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/endereco", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoEndereco)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
            verify(usuarioCoreController, times(1)).atualizarDadosEndereco(any(UUID.class), any(DadosEnderecoDto.class));
        }

        @DisplayName("Atualizar endereço com erro. Usuario nao encontrado por id.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioPorId() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            DadosEnderecoDto novoEndereco = dadosEnderecoDtoValido();

            doThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o id informado.")).when(usuarioCoreController).atualizarDadosEndereco(any(UUID.class), any(DadosEnderecoDto.class));

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/endereco", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoEndereco)))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.*").value(hasItem("Não foi encontrado nenhum usuário com o id informado.")));
            verify(usuarioCoreController, times(1)).atualizarDadosEndereco(any(UUID.class), any(DadosEnderecoDto.class));
        }

        @DisplayName("Atualizar endereço com erro. Usuario encontrado esta inativo.")
        @Test
        void deveLancarExcecaoSeUsuarioEstiverInativo() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            DadosEnderecoDto novoEndereco = dadosEnderecoDtoValido();

            doThrow(new UsuarioInativoException("Não é possível alterar o nome de um usuário inativo.")).when(usuarioCoreController).atualizarDadosEndereco(any(UUID.class), any(DadosEnderecoDto.class));

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/endereco", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoEndereco)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.*").value(hasItem("Não é possível alterar o nome de um usuário inativo.")));
            verify(usuarioCoreController, times(1)).atualizarDadosEndereco(any(UUID.class), any(DadosEnderecoDto.class));
        }

//        @DisplayName("Atualizar endereço com erro. Endereço nao encontrado por id.")
//        @Test
//        void deveLancarExcecaoSeNaoEncontrarEnderecoPorId() throws Exception {
//            // Arrange
//            UUID id = UUID.randomUUID();
//            DadosEnderecoDto novoEndereco = dadosEnderecoDtoValido();
//
//            doThrow(new Exception("Não foi encontrado nenhum endereço com o id informado.")).when(usuarioCoreController).atualizarDadosEndereco(any(UUID.class), any(DadosEnderecoDto.class));
//
//            // Act & Assert
//            mockMvc.perform(patch("/usuarios/{id}/endereco", id)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(asJsonString(novoEndereco)))
//                    .andDo(print())
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.*").value(hasItem("Não foi encontrado nenhum endereço com o id informado.")));
//            verify(usuarioCoreController, times(1)).atualizarDadosEndereco(any(UUID.class), any(DadosEnderecoDto.class));
//        }

        @DisplayName("Atualizar endereço com erro. DTO com dados inválidos.")
        @ParameterizedTest
        @CsvSource({
                " , 24455450, Nova Cidade, Rua Aquidabã, Rio de Janeiro, O campo cidade precisa ser informado.",
                "São Gonçalo, , Nova Cidade, Rua Aquidabã, Rio de Janeiro, O campo cep precisa ser informado.",
                "São Gonçalo, 24455450, , Rua Aquidabã, Rio de Janeiro, O campo bairro precisa ser informado.",
                "São Gonçalo, 24455450, Nova Cidade, , Rio de Janeiro, O campo endereco precisa ser informado.",
                "São Gonçalo, 24455450, Nova Cidade, Rua Aquidabã, , O campo estado precisa ser informado."
        })
        void deveLancarExcecaoParaCamposDeEnderecoInvalidos(String cidade, String cep, String bairro, String endereco, String estado, String expectedError) throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            DadosEnderecoDto novoEndereco = new DadosEnderecoDto(
                    cidade,
                    cep,
                    bairro,
                    endereco,
                    estado,
                    79,
                    "Casa 8"
            );

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/endereco", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoEndereco)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.*").value(hasItem(expectedError)));
            verify(usuarioCoreController, times(0)).atualizarDadosEndereco(any(UUID.class), any(DadosEnderecoDto.class));
        }

    }

    @Nested
    class AtualizarNomeRequest {
        @DisplayName("Atualizar nome com sucesso.")
        @Test
        void devePermitirAtualizarNome() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            DadosNomeDto novoNome = new DadosNomeDto("John Doe");

            doNothing().when(usuarioCoreController).atualizarNome(any(UUID.class), anyString());

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/nome", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoNome)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
            verify(usuarioCoreController, times(1)).atualizarNome(any(UUID.class), anyString());
        }

        @DisplayName("Atualizar nome com erro. Usuario nao encontrado por id.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioPorId() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            DadosNomeDto novoNome = new DadosNomeDto("John Doe");

            doThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o id informado.")).when(usuarioCoreController).atualizarNome(any(UUID.class), anyString());

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/nome", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoNome)))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.*").value(hasItem("Não foi encontrado nenhum usuário com o id informado.")));
            verify(usuarioCoreController, times(1)).atualizarNome(any(UUID.class), anyString());
        }

        @DisplayName("Atualizar nome com erro. Usuario encontrado esta inativo")
        @Test
        void deveLancarExcecaoSeUsuarioEncontradoEstiverInativo() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            DadosNomeDto novoNome = new DadosNomeDto("John Doe");

            doThrow(new UsuarioInativoException("Não é possível alterar o nome de um usuário inativo.")).when(usuarioCoreController).atualizarNome(any(UUID.class), anyString());

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/nome", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoNome)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.*").value(hasItem("Não é possível alterar o nome de um usuário inativo.")));
            verify(usuarioCoreController, times(1)).atualizarNome(any(UUID.class), anyString());
        }

        @DisplayName("Atualizar nome com erro. Novo nome igual ao atual.")
        @Test
        void deveLancarExcecaoSeNovoNomeForIgualAoAtual() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            DadosNomeDto novoNome = new DadosNomeDto("John Doe");

            doThrow(new AtualizacaoNomeUsuarioNaoPermitidoException("Não é possível alterar o nome do usuário pois ele é igual ao nome do usuário.")).when(usuarioCoreController).atualizarNome(any(UUID.class), anyString());

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/nome", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoNome)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.*").value(hasItem("Não é possível alterar o nome do usuário pois ele é igual ao nome do usuário.")));
            verify(usuarioCoreController, times(1)).atualizarNome(any(UUID.class), anyString());
        }

        @DisplayName("Atualizar nome com erro. DTO com dados invalidos.")
        @ParameterizedTest
        @CsvSource({
                " , O campo nome precisa estar preenchido.",
                "Jo, O campo nome precisa ter entre 3 e 150 caracteres."
        })
        void deveLancarExcecaoParaCamposInvalidosNoDto(String nome, String expectedError) throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            DadosNomeDto novoNome = new DadosNomeDto(nome);

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/nome", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoNome)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.*").value(hasItem(expectedError)));
            verify(usuarioCoreController, times(0)).atualizarNome(any(UUID.class), anyString());
        }

        @DisplayName("Atualizar nome com erro. Novo nome com mais de 150 caracteres.")
        @Test
        void deveLancarExcecaoSeNomeNovoMaiorQue150Characters() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            String nome = "A".repeat(151);
            DadosNomeDto nomeRecordRequest = new DadosNomeDto(nome);

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/nome", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(nomeRecordRequest)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.*").value(hasItem("O campo nome precisa ter entre 3 e 150 caracteres.")));
            verify(usuarioCoreController, times(0)).atualizarNome(any(UUID.class), anyString());
        }
    }

    @Nested
    class AtualizarSenhaRequest {
        @DisplayName("Trocar senha com sucesso")
        @Test
        void devePermitirTrocarSenha() throws Exception {
            // Arrange
            UUID usuarioId = UUID.randomUUID();
            SenhaDto novaSenhaDto = new SenhaDto("124");

            doNothing().when(usuarioCoreController).atualizarSenha(any(UUID.class), anyString());

            // Act & Assert
            // Tive que mockar o Mensagem Util nesse teste para poder checar a mensagem
            try (MockedStatic<MensagensUtil> mensagensMock = mockStatic(MensagensUtil.class)) {
                mensagensMock.when(() -> MensagensUtil.recuperarMensagem(
                                MensagensUtil.SUCESSO_TROCA_SENHA_USUARIO, new Object[0]))
                        .thenReturn("Senha alterada com sucesso.");

                mockMvc.perform(patch("/usuarios/{id}/senha", usuarioId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(novaSenhaDto)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.mensagem").value("Senha alterada com sucesso."));
            }

            verify(usuarioCoreController, times(1)).atualizarSenha(any(UUID.class), anyString());
        }

        @DisplayName("Atualizar senha com erro. Usuario nao encontrado atraves do id")
        @Test
        void deveLancarExcecaoSeUsuarioNaoEncontradoAtravesDoId() throws Exception {
            // Arrange
            UUID usuarioId = UUID.randomUUID();
            SenhaDto novaSenhaDto = new SenhaDto("124");

            doThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o id informado.")).when(usuarioCoreController).atualizarSenha(any(UUID.class), anyString());

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/senha", usuarioId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novaSenhaDto)))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.mensagem").value("Não foi encontrado nenhum usuário com o id informado."));

            verify(usuarioCoreController, times(1)).atualizarSenha(any(UUID.class), anyString());
        }

//        @DisplayName("Atualizar senha com erro. Não encontrado usuário vinculado com o login")
//        @Test
//        void deveLancarExcecaoSeNaoEncontrarUsuarioVinculadoComOLogin() throws Exception {
//            // Arrange
//            String matricula = "us0003";
//            SenhaDto novaSenhaDto = new SenhaDto("124");
//
//            doThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o login informado.")).when(loginCoreController).atualizarSenha(anyString(), anyString());
//
//            // Act & Arrange
//            mockMvc.perform(patch("/login/{matricula}/senha", matricula)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(asJsonString(novaSenhaDto)))
//                    .andDo(print())
//                    .andExpect(status().isNotFound())
//                    .andExpect(jsonPath("$.mensagem").value("Não foi encontrado nenhum usuário com o login informado."));
//
//            verify(loginCoreController, times(1)).atualizarSenha(anyString(), anyString());
//        }

        @DisplayName("Atualizar senha com erro. Usuário encontrado esta inativo")
        @Test
        void deveLancarExcecaoSeUsuarioEstiverInativo() throws Exception {
            // Arrange
            UUID usuarioId = UUID.randomUUID();
            SenhaDto novaSenhaDto = new SenhaDto("124");

            doThrow(new UsuarioInativoException("Não é possível alterar a senha de um usuário inativo.")).when(usuarioCoreController).atualizarSenha(any(UUID.class), anyString());

            // Act & Arrange
            mockMvc.perform(patch("/usuarios/{id}/senha", usuarioId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novaSenhaDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("Não é possível alterar a senha de um usuário inativo."));

            verify(usuarioCoreController, times(1)).atualizarSenha(any(UUID.class), anyString());
        }

        @DisplayName("Alterar senha com erro. Dados inválidos no DTO")
        @Test
        void deveLancarExcecaoParaCamposInvalidos() throws Exception {
            // Arrange
            UUID usuarioId = UUID.randomUUID();
            SenhaDto novaSenhaDto = new SenhaDto("");

            // Act & Arrange
            mockMvc.perform(patch("/usuarios/{id}/senha", usuarioId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novaSenhaDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.senha").value("O campo senha precisa estar preenchido."));

            verify(usuarioCoreController, times(0)).atualizarSenha(any(UUID.class), anyString());
        }
    }

    @Nested
    class AtualizarMatriculaRequest {
        @DisplayName("Trocar matricula com sucesso")
        @Test
        void devePermitirTrocarMatricula() throws Exception {
            // Arrange
            UUID usuarioId = UUID.randomUUID();
            MatriculaDto novaMatriculaDto = new MatriculaDto("us0010");

            doNothing().when(usuarioCoreController).atualizarMatricula(any(UUID.class), anyString());

            // Act & Assert
            // Tive que mockar o Mensagem Util nesse teste para poder checar a mensagem
            try (MockedStatic<MensagensUtil> mensagensMock = mockStatic(MensagensUtil.class)) {
                mensagensMock.when(() -> MensagensUtil.recuperarMensagem(
                                MensagensUtil.SUCESSO_TROCA_MATRICULA_USUARIO, new Object[0]))
                        .thenReturn("Matricula alterada com sucesso.");

                mockMvc.perform(patch("/usuarios/{id}/matricula", usuarioId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(novaMatriculaDto)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.mensagem").value("Matricula alterada com sucesso."));
            }
            verify(usuarioCoreController, times(1)).atualizarMatricula(any(UUID.class), anyString());
        }

        @DisplayName("Atualizar matricula com erro. Usuário nao encontrado através do id.")
        @Test
        void deveLancarExcecaoSeUsuarioNaoEncontradoAtravesDoId() throws Exception {
            // Arrange
            UUID usuarioId = UUID.randomUUID();
            MatriculaDto novaMatriculaDto = new MatriculaDto("us0010");

            doThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o id informado.")).when(usuarioCoreController).atualizarMatricula(any(UUID.class), anyString());

            // Act & Assert
            mockMvc.perform(patch("/usuarios/{id}/matricula", usuarioId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novaMatriculaDto)))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.mensagem").value("Não foi encontrado nenhum usuário com o id informado."));

            verify(usuarioCoreController, times(1)).atualizarMatricula(any(UUID.class), anyString());
        }

//        @DisplayName("Atualizar matricula com erro. Não encontrado usuário vinculado com o login")
//        @Test
//        void deveLancarExcecaoSeNaoEncontrarUsuarioVinculadoComOLogin() throws Exception {
//            // Arrange
//            String matriculaAtual = "us0008";
//            MatriculaDto novaMatriculaDto = new MatriculaDto("us0010");
//
//            doThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o login informado.")).when(loginCoreController).atualizarMatricula(anyString(), anyString());
//
//            // Act & Arrange
//            mockMvc.perform(patch("/login/{matricula}/matricula", matriculaAtual)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(asJsonString(novaMatriculaDto)))
//                    .andDo(print())
//                    .andExpect(status().isNotFound())
//                    .andExpect(jsonPath("$.mensagem").value("Não foi encontrado nenhum usuário com o login informado."));
//
//            verify(loginCoreController, times(1)).atualizarMatricula(anyString(), anyString());
//        }

        @DisplayName("Atualizar matricula com erro. Usuário encontrado está inativo")
        @Test
        void deveLancarExcecaoSeUsuarioEstiverInativo() throws Exception {
            // Arrange
            UUID usuarioId = UUID.randomUUID();
            MatriculaDto novaMatriculaDto = new MatriculaDto("us0010");

            doThrow(new UsuarioInativoException("Não é possível alterar a senha de um usuário inativo.")).when(usuarioCoreController).atualizarMatricula(any(UUID.class), anyString());

            // Act & Arrange
            mockMvc.perform(patch("/usuarios/{id}/matricula", usuarioId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novaMatriculaDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("Não é possível alterar a senha de um usuário inativo."));

            verify(usuarioCoreController, times(1)).atualizarMatricula(any(UUID.class), anyString());
        }

        @DisplayName("Atualizar matricula com erro. Nova matricula ja cadastrada.")
        @Test
        void deveLancarExcecaoSeNovaMatriculaJaCadastrada() throws Exception {
            // Arrange
            UUID usuarioId = UUID.randomUUID();
            MatriculaDto novaMatriculaDto = new MatriculaDto("us0002");

            doThrow(new MatriculaDuplicadaException("Já existe um usuário com a matrícula informada.")).when(usuarioCoreController).atualizarMatricula(any(UUID.class), anyString());

            // Act & Arrange
            mockMvc.perform(patch("/usuarios/{id}/matricula", usuarioId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novaMatriculaDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("Já existe um usuário com a matrícula informada."));

            verify(usuarioCoreController, times(1)).atualizarMatricula(any(UUID.class), anyString());
        }

        @DisplayName("Alterar matricula com erro. Dados inválidos no DTO")
        @Test
        void deveLancarExcecaoParaCamposInvalidos() throws Exception {
            // Arrange
            UUID usuarioId = UUID.randomUUID();
            MatriculaDto novaMatriculaDto = new MatriculaDto("");

            // Act & Arrange
            mockMvc.perform(patch("/usuarios/{id}/matricula", usuarioId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novaMatriculaDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.matricula").value("O campo matricula precisa estar preenchido."));

            verify(usuarioCoreController, times(0)).atualizarMatricula(any(UUID.class), anyString());
        }
    }

    @Nested
    class ValidarLoginRequest {
        @DisplayName("Validação de login com sucesso")
        @Test
        void devePermitirValidarLogin() throws Exception {
            // Arrange
            LoginDto loginRecordRequest = loginDtoValido();

            when(usuarioCoreController.validarAcesso(anyString(), anyString()))
                    .thenReturn("Acesso liberado.");

            // Act & Assert
            mockMvc.perform(post("/usuarios/valida-acesso")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(loginRecordRequest)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.mensagem").value("Acesso liberado."));
            verify(usuarioCoreController, times(1)).validarAcesso(anyString(), anyString());
        }

        @DisplayName("Validação de login com erro. Usuario não encontrado através de matrícula e senha.")
        @Test
        void deveLancarExcecaoSeUsuarioNaoEncontradoAtravesDaMatriculaESenha() throws Exception {
            // Arrange
            LoginDto loginRecordRequest = loginDtoValido();

            when(usuarioCoreController.validarAcesso(anyString(), anyString()))
                    .thenThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com a matrícula e senha informados."));

            // Act & Assert
            mockMvc.perform(post("/usuarios/valida-acesso")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(loginRecordRequest)))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.mensagem").value("Não foi encontrado nenhum usuário com a matrícula e senha informados."));
            verify(usuarioCoreController, times(1)).validarAcesso(anyString(), anyString());
        }

        @DisplayName("Validação de login com erro. Usuario encontrado está inativo")
        @Test
        void deveLancarExcecaoSeUsuarioEncontradoEstiverInativo() throws Exception {
            // Arrange
            LoginDto loginRecordRequest = loginDtoValido();

            when(usuarioCoreController.validarAcesso(anyString(), anyString()))
                    .thenThrow(new UsuarioSemAcessoException("Não é possível realizar o login para usuários inativos."));

            // Act & Assert
            mockMvc.perform(post("/usuarios/valida-acesso")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(loginRecordRequest)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("Não é possível realizar o login para usuários inativos."));
            verify(usuarioCoreController, times(1)).validarAcesso(anyString(), anyString());
        }

//            @DisplayName("Validação de login com erro. Usuário não encontrado através do login id")
//            @Test
//            void deveLancarExcecaoSeNaoEncontrarUsuarioVinculadoComOLogin() throws Exception {
//                // Arrange
//                LoginDto loginRecordRequest = loginDtoValido();
//
//                when(loginCoreController.validar(anyString(), anyString()))
//                        .thenThrow(new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário com o login informado."));
//
//                // Act & Assert
//                mockMvc.perform(post("/login")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(asJsonString(loginRecordRequest)))
//                        .andDo(print())
//                        .andExpect(status().isNotFound())
//                        .andExpect(jsonPath("$.mensagem").value("Não foi encontrado nenhum usuário com o login informado."));
//                verify(loginCoreController, times(1)).validar(anyString(), anyString());
//            }

        @DisplayName("Validação de login com erro. Erro com dados no DTO")
        @ParameterizedTest
        @CsvSource({
                " , 123, O campo matricula precisa ser informado.",
                "us0001, , O campo senha precisa ser informado."
        })
        void deveLancarExcecaoParaCamposInvalidos(String matricula, String senha, String expectedErrorMessage ) throws Exception {
            // Arrange
            LoginDto loginRecordRequest = new LoginDto(null, matricula, senha);

            // Act & Assert
            mockMvc.perform(post("/usuarios/valida-acesso")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(loginRecordRequest)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.*").value(hasItem(expectedErrorMessage)));
            verify(usuarioCoreController, times(0)).validarAcesso(anyString(), anyString());
        }
    }
}
