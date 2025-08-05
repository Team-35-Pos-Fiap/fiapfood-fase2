package br.com.fiapfood.infrastructure.controllers.unitarios;

import br.com.fiapfood.core.controllers.interfaces.IPerfilCoreController;
import br.com.fiapfood.core.exceptions.perfil.ExclusaoPerfilNaoPermitidaException;
import br.com.fiapfood.core.exceptions.perfil.NomePerfilDuplicadoException;
import br.com.fiapfood.core.exceptions.perfil.PerfilInvalidoException;
import br.com.fiapfood.infraestructure.controllers.PerfilController;
import br.com.fiapfood.infraestructure.controllers.exceptions.ErrorHandler;
import br.com.fiapfood.infraestructure.controllers.request.perfil.NomeDto;
import br.com.fiapfood.infraestructure.controllers.request.perfil.PerfilDto;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static br.com.fiapfood.utils.DtoDataGenerator.perfilDtoValido;
import static br.com.fiapfood.utils.JsonToString.asJsonString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PerfilControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IPerfilCoreController perfilCoreController;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        PerfilController perfilController = new PerfilController(perfilCoreController);

        this.mockMvc = MockMvcBuilders.standaloneSetup(perfilController)
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
    class BuscarTodosRequest {
        @DisplayName("Buscar todos os perfis cadastrados")
        @Test
        void deveRetornarListaComPerfisCadastrados() throws Exception {
            // Arrange
            List<PerfilDto> perfis = List.of(
                    new PerfilDto(1, "Admin", null, null),
                    new PerfilDto(2, "Cliente", null, null)
            );

            when(perfilCoreController.buscarTodos()).thenReturn(perfis);

            // Act & Assert
            mockMvc.perform(get("/perfis")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].nome").value("Admin"))
                    .andExpect(jsonPath("$[1].id").value(2))
                    .andExpect(jsonPath("$[1].nome").value("Cliente"));

            verify(perfilCoreController, times(1)).buscarTodos();
        }
    }

    @Nested
    class BuscarPorIdRequest {
        @DisplayName("Buscar perfil por id com sucesso")
        @Test
        void deveRetornarPerfilPorId() throws Exception {
            // Arrange
            int perfilId = 1;
            PerfilDto perfil = perfilDtoValido();

            when(perfilCoreController.buscarPorId(anyInt())).thenReturn(perfil);

            // Act & Assert
            mockMvc.perform(get("/perfis/{id}", perfilId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nome").value("Admin"))
                    .andExpect(status().isOk());

            verify(perfilCoreController, times(1)).buscarPorId(anyInt());
        }

        @DisplayName("Buscar perfil por id com erro. Perfil nao encontrado através do id")
        @Test
        void deveLancarExcecaoSeNaoEncontrarPerfilPorId() throws Exception {
            // Arrange
            int perfilId = 3;

            when(perfilCoreController.buscarPorId(anyInt())).thenThrow(new PerfilInvalidoException("Não foi encontrado nenhum perfil com o id informado."));

            // Act & Assert
            mockMvc.perform(get("/perfis/{id}", perfilId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("Não foi encontrado nenhum perfil com o id informado."));

            verify(perfilCoreController, times(1)).buscarPorId(anyInt());
        }
    }

    @Nested
    class CadastrarPerfilRequest {
        @DisplayName("Cadastrar perfil com sucesso.")
        @Test
        void deveCadastrarPerfilComSucesso() throws Exception {
            // Arrange
            NomeDto perfilNome = new NomeDto("Funcionário");

            doNothing().when(perfilCoreController).cadastrar(anyString());

            // Act & Assert
            mockMvc.perform(post("/perfis")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(perfilNome)))
                    .andDo(print())
                    .andExpect(status().isCreated());
            verify(perfilCoreController, times(1)).cadastrar(anyString());
        }

        @DisplayName("Cadastrar perfil com erro. Nome de perfil ja cadastrado.")
        @Test
        void deveLancarExcecaoSeNomeDePerfilJaCadstrado() throws Exception {
            // Arrange
            NomeDto perfilNome = new NomeDto("Funcionário");

            doThrow(new NomePerfilDuplicadoException("Já existe um perfil com o nome informado.")).when(perfilCoreController).cadastrar(anyString());

            // Act & Assert
            mockMvc.perform(post("/perfis")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(perfilNome)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("Já existe um perfil com o nome informado."));
            verify(perfilCoreController, times(1)).cadastrar(anyString());
        }

        @DisplayName("Cadastrar perfil com erro. Dados inválidos no DTO.")
        @Test
        void deveLancarExcecaoSeDadosInvalidosNoDto() throws Exception {
            // Arrange
            NomeDto perfilNome = new NomeDto("");

            // Act & Assert
            mockMvc.perform(post("/perfis")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(perfilNome)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.nome").value("O campo nome precisa estar preenchido."));
            verify(perfilCoreController, times(0)).cadastrar(anyString());
        }
    }

    @Nested
    class AtualizarPerfilNomeRequest {
        @DisplayName("Atualizar nome do perfil com sucesso.")
        @Test
        void deveAtualizarNomeDoPerfilComSucesso() throws Exception {
            // Arrange
            int perfilId = 1;
            NomeDto perfilNome = new NomeDto("Funcionário");

            doNothing().when(perfilCoreController).atualizarNome(anyInt(),anyString());

            // Act & Assert
            mockMvc.perform(patch("/perfis/{id}/nome", perfilId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(perfilNome)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
            verify(perfilCoreController, times(1)).atualizarNome(anyInt(),anyString());
        }

        @DisplayName("Atualizar nome do perfil com erro. Perfil não encontrado por id.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarPerfilPorId() throws Exception {
            // Arrange
            int perfilId = 1;
            NomeDto perfilNome = new NomeDto("Funcionário");

            doThrow(new PerfilInvalidoException("Não foi encontrado nenhum perfil com o id informado.")).when(perfilCoreController).atualizarNome(anyInt(),anyString());

            // Act & Assert
            mockMvc.perform(patch("/perfis/{id}/nome", perfilId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(perfilNome)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("Não foi encontrado nenhum perfil com o id informado."));
            verify(perfilCoreController, times(1)).atualizarNome(anyInt(),anyString());
        }

        @DisplayName("Atualizar nome do perfil com erro. Nome de perfil ja cadastrado.")
        @Test
        void deveLancarExcecaoSeNomeDePerfilJaCadstrado() throws Exception {
            // Arrange
            int perfilId = 1;
            NomeDto perfilNome = new NomeDto("Dono");

            doThrow(new NomePerfilDuplicadoException("Já existe um perfil com o nome informado.")).when(perfilCoreController).atualizarNome(anyInt(), anyString());

            // Act & Assert
            mockMvc.perform(patch("/perfis/{id}/nome", perfilId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(perfilNome)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("Já existe um perfil com o nome informado."));
            verify(perfilCoreController, times(1)).atualizarNome(anyInt(),anyString());
        }

        @DisplayName("Atualizar nome do perfil com erro. Dados inválidos no DTO.")
        @Test
        void deveLancarExcecaoSeDadosInvalidosNoDto() throws Exception {
            // Arrange
            int perfilId = 1;
            NomeDto perfilNome = new NomeDto("");

            // Act & Assert
            mockMvc.perform(patch("/perfis/{id}/nome", perfilId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(perfilNome)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.nome").value("O campo nome precisa estar preenchido."));
            verify(perfilCoreController, times(0)).atualizarNome(anyInt(),anyString());
        }
    }

    @Nested
    class InativarPerfilRequest{
        @DisplayName("Inativar perfil com sucesso.")
        @Test
        void inativarPerfilComSucesso() throws Exception {
            // Arrange
            int perfilId = 1;

            doNothing().when(perfilCoreController).inativar(anyInt());

            // Act & Assert
            mockMvc.perform(patch("/perfis/{id}/inativa", perfilId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNoContent());
            verify(perfilCoreController, times(1)).inativar(anyInt());
        }

        @DisplayName("Inativar perfil com erro. Perfil não encontrado por id")
        @Test
        void deveLancarExcecaoSeNaoEncontarPerfilPorId() throws Exception {
            // Arrange
            int perfilId = 1;

            doThrow(new PerfilInvalidoException("Não foi encontrado nenhum perfil com o id informado.")).when(perfilCoreController).inativar(anyInt());

            // Act & Assert
            mockMvc.perform(patch("/perfis/{id}/inativa", perfilId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("Não foi encontrado nenhum perfil com o id informado."));
            verify(perfilCoreController, times(1)).inativar(anyInt());
        }

        @DisplayName("Inativar perfil com erro. Usuarios registrados no perfil")
        @Test
        void deveLancarExcecaoSeUsuariosEstiveremRegistradosNoPerfil() throws Exception {
            // Arrange
            int perfilId = 1;

            doThrow(new ExclusaoPerfilNaoPermitidaException("Não é possível inativar o perfil pois há usuário ativo associado ao perfil.")).when(perfilCoreController).inativar(anyInt());

            // Act & Assert
            mockMvc.perform(patch("/perfis/{id}/inativa", perfilId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("Não é possível inativar o perfil pois há usuário ativo associado ao perfil."));
            verify(perfilCoreController, times(1)).inativar(anyInt());
        }
    }

    @Nested
    class ReativarPerfilRequest{
        @DisplayName("Reativar perfil com sucesso.")
        @Test
        void reativarPerfilComSucesso() throws Exception {
            // Arrange
            int perfilId = 1;

            doNothing().when(perfilCoreController).reativar(anyInt());

            // Act & Assert
            mockMvc.perform(patch("/perfis/{id}/reativa", perfilId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNoContent());
            verify(perfilCoreController, times(1)).reativar(anyInt());
        }

        @DisplayName("Reativar perfil com erro. Perfil não encontrado por id")
        @Test
        void deveLancarExcecaoSeNaoEncontarPerfilPorId() throws Exception {
            // Arrange
            int perfilId = 1;

            doThrow(new PerfilInvalidoException("Não foi encontrado nenhum perfil com o id informado.")).when(perfilCoreController).reativar(anyInt());

            // Act & Assert
            mockMvc.perform(patch("/perfis/{id}/reativa", perfilId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("Não foi encontrado nenhum perfil com o id informado."));
            verify(perfilCoreController, times(1)).reativar(anyInt());
        }
    }
}
