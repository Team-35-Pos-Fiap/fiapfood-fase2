package br.com.fiapfood.infrastructure.controllers.unitarios;

import br.com.fiapfood.core.controllers.interfaces.ITipoCulinariaCoreController;
import br.com.fiapfood.core.exceptions.perfil.NomePerfilDuplicadoException;
import br.com.fiapfood.core.exceptions.tipo_culinaria.TipoCulinariaInvalidoException;
import br.com.fiapfood.core.exceptions.tipo_culinaria.TipoCulinariaNaoEncontradoException;
import br.com.fiapfood.infraestructure.controllers.TipoCulinariaController;
import br.com.fiapfood.infraestructure.controllers.exceptions.ErrorHandler;
import br.com.fiapfood.infraestructure.controllers.request.perfil.NomeDto;
import br.com.fiapfood.infraestructure.controllers.request.tipo_culinaria.TipoCulinariaDto;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static br.com.fiapfood.utils.DtoDataGenerator.tipoCulinariaDtoValido;
import static br.com.fiapfood.utils.JsonToString.asJsonString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TipoCulinariaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ITipoCulinariaCoreController tipoCulinariaCoreController;

    AutoCloseable mock;

    @BeforeEach
    public void setUp() throws Exception {
        mock = MockitoAnnotations.openMocks(this);
        TipoCulinariaController tipoCulinariaController = new TipoCulinariaController(tipoCulinariaCoreController);
        this.mockMvc = MockMvcBuilders.standaloneSetup(tipoCulinariaController)
                .setControllerAdvice(new ErrorHandler())
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        mock.close();
    }

    @Nested
    class BuscarTodosRequest {
        @DisplayName("Buscar todos tipo culinária com sucesso.")
        @Test
        void buscarTodosRequestComSucesso() throws Exception {
            // Arrange
            List<TipoCulinariaDto> todosTipoCulinaria = new ArrayList<>(List.of(
                    tipoCulinariaDtoValido(),
                    tipoCulinariaDtoValido(),
                    tipoCulinariaDtoValido()
            ));

            when(tipoCulinariaCoreController.buscarTodos()).thenReturn(todosTipoCulinaria);

            // Act & Assert
            mockMvc.perform(get("/tipos-culinaria")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
            verify(tipoCulinariaCoreController, times(1)).buscarTodos();
        }

        @DisplayName("Buscar todos tipo culinária com erro. Nenhum tipo cadastrado.")
        @Test
        void deveLancarExcecaoSeNenhumTipoCadstrado() throws Exception {
            // Arrange
            when(tipoCulinariaCoreController.buscarTodos()).thenThrow(new TipoCulinariaNaoEncontradoException("Não foi encontrado nenhum tipo de culinária na base de dados."));

            // Act & Assert
            mockMvc.perform(get("/tipos-culinaria")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("Não foi encontrado nenhum tipo de culinária na base de dados."));
            verify(tipoCulinariaCoreController, times(1)).buscarTodos();
        }

    }

    @Nested
    class BuscarPorIdRequest {
        @DisplayName("Buscar tipo culinária por id com sucesso.")
        @Test
        void buscarPorIdRequest() throws Exception {
            // Arrange
            int tipoCulinariaId = 1;
            TipoCulinariaDto tipoCulinariaDto = tipoCulinariaDtoValido();

            when(tipoCulinariaCoreController.buscarPorId(anyInt())).thenReturn(tipoCulinariaDto);

            // Act & Assert
            mockMvc.perform(get("/tipos-culinaria/{id}", tipoCulinariaId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(tipoCulinariaDto.id()))
                    .andExpect(jsonPath("$.nome").value(tipoCulinariaDto.nome()));
            verify(tipoCulinariaCoreController, times(1)).buscarPorId(anyInt());
        }

        @DisplayName("Buscar tipo culinária por id com erro. Tipo culinária não encontrado por id.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarTipoCulinariaPorId() throws Exception {
            // Arrange
            int tipoCulinariaId = 1;

            when(tipoCulinariaCoreController.buscarPorId(anyInt())).thenThrow(new TipoCulinariaInvalidoException("Não foi encontrado nenhum tipo de culinária com o id informado."));

            // Act & Assert
            mockMvc.perform(get("/tipos-culinaria/{id}", tipoCulinariaId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("Não foi encontrado nenhum tipo de culinária com o id informado."));
            verify(tipoCulinariaCoreController, times(1)).buscarPorId(anyInt());
        }

    }

    @Nested
    class CadastrarRequest {
        @DisplayName("Cadastrar tipo culinária com sucesso.")
        @Test
        void deveCadastrarNovoTipoCulinariaComSucesso() throws Exception {
            //Arrange
            NomeDto nomeTipoCulinaria = new NomeDto("Tipo Culinaria Teste");

            doNothing().when(tipoCulinariaCoreController).cadastrar(anyString());

            // Act & Assert
            mockMvc.perform(post("/tipos-culinaria")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(nomeTipoCulinaria)))
                    .andDo(print())
                    .andExpect(status().isCreated());
            verify(tipoCulinariaCoreController, times(1)).cadastrar(anyString());
        }

        @DisplayName("Cadastrar tipo culinária com erro. Nome já cadastrado.")
        @Test
        void deveLancarExcecaoSeNomeJaEstiverCadastrado() throws Exception {
            //Arrange
            NomeDto nomeTipoCulinaria = new NomeDto("Tipo Culinaria Teste");

            doThrow(new NomePerfilDuplicadoException("Já existe um tipo de culinária com o nome informado.")).when(tipoCulinariaCoreController).cadastrar(anyString());

            // Act & Assert
            mockMvc.perform(post("/tipos-culinaria")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(nomeTipoCulinaria)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("Já existe um tipo de culinária com o nome informado."));
            verify(tipoCulinariaCoreController, times(1)).cadastrar(anyString());
        }

        @DisplayName("Cadastrar tipo culinária com erro. DTO com dados inválidos.")
        @Test
        void deveLancarExcecaoSeDtoComDadosInvalidos() throws Exception {
            //Arrange
            NomeDto nomeTipoCulinaria = new NomeDto("");

            // Act & Assert
            mockMvc.perform(post("/tipos-culinaria")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(nomeTipoCulinaria)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.nome").value("O campo nome precisa estar preenchido."));
            verify(tipoCulinariaCoreController, times(0)).cadastrar(anyString());
        }
    }

    @Nested
    class AtualizarRequest {
        @DisplayName("Atualizar nome tipo culinária com sucesso.")
        @Test
        void deveAtualizarNomeTipoCulinariaComSucesso() throws Exception {
            //Arrange
            int tipoCulinariaId = 1;
            NomeDto nomeTipoCulinaria = new NomeDto("Tipo Culinária Novo Nome");

            doNothing().when(tipoCulinariaCoreController).atualizar(anyInt(), anyString());

            // Act & Assert
            mockMvc.perform(patch("/tipos-culinaria/{id}", tipoCulinariaId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(nomeTipoCulinaria)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
            verify(tipoCulinariaCoreController, times(1)).atualizar(anyInt(), anyString());
        }

        @DisplayName("Atualizar nome tipo culinária com erro.. Novo nome ja cadastrado.")
        @Test
        void deveLancarExcecaoSeNovoNomeJaCadastrado() throws Exception {
            //Arrange
            int tipoCulinariaId = 1;
            NomeDto nomeTipoCulinaria = new NomeDto("Tipo Culinária Novo Nome");

            doThrow(new NomePerfilDuplicadoException("Já existe um tipo de culinária com o nome informado.")).when(tipoCulinariaCoreController).atualizar(anyInt(), anyString());

            // Act & Assert
            mockMvc.perform(patch("/tipos-culinaria/{id}", tipoCulinariaId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(nomeTipoCulinaria)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("Já existe um tipo de culinária com o nome informado."));
            verify(tipoCulinariaCoreController, times(1)).atualizar(anyInt(), anyString());
        }

        @DisplayName("Atualizar nome tipo culinária com erro. Tipo culinária nao encontrado por id.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarTipoCulinariaPorId() throws Exception {
            //Arrange
            int tipoCulinariaId = 1;
            NomeDto nomeTipoCulinaria = new NomeDto("Tipo Culinária Novo Nome");

            doThrow(new TipoCulinariaInvalidoException("Não foi encontrado nenhum tipo de culinária com o id informado.")).when(tipoCulinariaCoreController).atualizar(anyInt(), anyString());

            // Act & Assert
            mockMvc.perform(patch("/tipos-culinaria/{id}", tipoCulinariaId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(nomeTipoCulinaria)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("Não foi encontrado nenhum tipo de culinária com o id informado."));
            verify(tipoCulinariaCoreController, times(1)).atualizar(anyInt(), anyString());
        }

        @DisplayName("Atualizar nome tipo culinária com erro.. DTO com dados inválidos.")
        @Test
        void deveLancarExcecaoSeDtoComDadosInvalidos() throws Exception {
            //Arrange
            int tipoCulinariaId = 1;
            NomeDto nomeTipoCulinaria = new NomeDto("");

            // Act & Assert
            mockMvc.perform(patch("/tipos-culinaria/{id}", tipoCulinariaId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(nomeTipoCulinaria)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.nome").value("O campo nome precisa estar preenchido."));
            verify(tipoCulinariaCoreController, times(0)).cadastrar(anyString());
        }
    }
}
