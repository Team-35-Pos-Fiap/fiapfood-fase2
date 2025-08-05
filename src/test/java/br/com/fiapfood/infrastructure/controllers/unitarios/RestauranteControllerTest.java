package br.com.fiapfood.infrastructure.controllers.unitarios;

import br.com.fiapfood.core.controllers.interfaces.IRestauranteCoreController;
import br.com.fiapfood.core.exceptions.item.ItemNaoEncontradoException;
import br.com.fiapfood.infraestructure.controllers.RestauranteController;
import br.com.fiapfood.infraestructure.controllers.exceptions.ErrorHandler;
import br.com.fiapfood.infraestructure.controllers.request.atendimento.AtendimentoDto;
import br.com.fiapfood.infraestructure.controllers.request.endereco.DadosEnderecoDto;
import br.com.fiapfood.infraestructure.controllers.request.endereco.EnderecoDto;
import br.com.fiapfood.infraestructure.controllers.request.item.*;
import br.com.fiapfood.infraestructure.controllers.request.item.NomeDto;
import br.com.fiapfood.infraestructure.controllers.request.paginacao.PaginacaoDto;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.*;
import br.com.fiapfood.infraestructure.controllers.request.tipo_culinaria.TipoCulinariaDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.DadosUsuarioDto;
import br.com.fiapfood.infraestructure.utils.MensagensUtil;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static br.com.fiapfood.utils.JsonToString.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class RestauranteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IRestauranteCoreController restauranteCoreController;

    @Mock
    RestauranteDto restauranteDto;

    @Mock
    EnderecoDto enderecoDto;

    @Mock
    DadosEnderecoDto dadosEnderecoDto;

    @Mock
    DadosUsuarioDto dadosUsuarioDto;

    @Mock
    TipoCulinariaDto tipoCulinariaDto;

    @Mock
    AtendimentoDto atendimentoDto;

    @Mock
    ItemDto itemDto;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        RestauranteController restauranteController = new RestauranteController(restauranteCoreController);

        this.mockMvc = MockMvcBuilders.standaloneSetup(restauranteController)
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
    class GerenciarRestauranteRequest {

        @Test
        @DisplayName("Deve buscar restaurantes com paginação")
        void deveBuscarRestaurantesComSucesso() throws Exception {
            // Arrange
            List<RestauranteDto> restauranteDtos = List.of(restauranteDto, restauranteDto);

            PaginacaoDto paginacao = new PaginacaoDto(1, 1, 2);

            RestaurantePaginacaoDto dadosRestaurante = new RestaurantePaginacaoDto(restauranteDtos, paginacao);
            when(restauranteCoreController.buscarTodos(anyInt())).thenReturn(dadosRestaurante);

            mockMvc.perform(get("/restaurantes?page=1"))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.restaurantes.length()").value(2));
        }

        @Test
        @DisplayName("Deve buscar restaurante por id")
        void deveBuscarRestaurantePorIdComSucesso() throws Exception {
            UUID idRestaurante = UUID.randomUUID();
            List<AtendimentoDto> atendimentos = List.of(atendimentoDto, atendimentoDto);
            RestauranteDto restauranteDto = new RestauranteDto(idRestaurante,
                    "Restaurante Teste",
                    enderecoDto,
                    true,
                    dadosUsuarioDto,
                    tipoCulinariaDto,
                    atendimentos);
            when(restauranteCoreController.buscarPorId(any())).thenReturn(restauranteDto);

            mockMvc.perform(get("/restaurantes/{id}", idRestaurante))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nome").value("Restaurante Teste"))
                    .andDo(print());
        }

        @Test
        @DisplayName("Deve cadastrar restaurante")
        void deveCadastrarRestauranteComSucesso() throws Exception {
            List<AtendimentoDto> atendimentos = List.of(atendimentoDto, atendimentoDto);
            CadastrarRestauranteDto dadosRestauranteDto = new CadastrarRestauranteDto(
                    "Restaurante novo",
                    dadosEnderecoDto,
                    UUID.randomUUID(),
                    1,
                    atendimentos);

            doNothing().when(restauranteCoreController).cadastrar(any(CadastrarRestauranteDto.class));

            mockMvc.perform(post("/restaurantes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(dadosRestauranteDto)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Deve inativar restaurante")
        void deveInativarRestauranteComSucesso() throws Exception {
            UUID id = UUID.randomUUID();

            doNothing().when(restauranteCoreController).inativar(any());

            try (MockedStatic<MensagensUtil> mensagensMock = mockStatic(MensagensUtil.class)) {
                mensagensMock.when(() -> MensagensUtil.recuperarMensagem(
                                MensagensUtil.SUCESSO_INATIVACAO_RESTAURANTE, new Object[0]))
                        .thenReturn("Restaurante inativado com sucesso.");

                mockMvc.perform(patch("/restaurantes/{id}/status/inativa", id)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.mensagem").value("Restaurante inativado com sucesso."));
            }

            verify(restauranteCoreController, times(1)).inativar(any(UUID.class));
        }

        @Test
        @DisplayName("Deve reativar restaurante")
        void deveReativarRestauranteComSucesso() throws Exception {
            UUID id = UUID.randomUUID();

            doNothing().when(restauranteCoreController).reativar(any());

            try (MockedStatic<MensagensUtil> mensagensMock = mockStatic(MensagensUtil.class)) {
                mensagensMock.when(() -> MensagensUtil.recuperarMensagem(
                                MensagensUtil.SUCESSO_REATIVACAO_RESTAURANTE, new Object[0]))
                        .thenReturn("Restaurante reativado com sucesso.");

                mockMvc.perform(patch("/restaurantes/{id}/status/reativa", id)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.mensagem").value("Restaurante reativado com sucesso."));
            }

            verify(restauranteCoreController, times(1)).reativar(any(UUID.class));
        }

        @Test
        @DisplayName("Deve atualizar TipoCulinaria restaurante")
        void deveAtualizarTipoCulinariaRestauranteComSucesso() throws Exception {
            UUID id = UUID.randomUUID();

            DadosTipoCulinariaDto dadosTipoCulinariaDto = new DadosTipoCulinariaDto(1);

            doNothing().when(restauranteCoreController).atualizarTipoCulinaria(any(), anyInt());

            mockMvc.perform(patch("/restaurantes/{id}/tipo-culinaria", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(dadosTipoCulinariaDto)))
                    .andExpect(status().isNoContent());

            verify(restauranteCoreController, times(1)).atualizarTipoCulinaria(any(), anyInt());
        }

        @Test
        @DisplayName("Deve atualizar dono do restaurante")
        void deveAtualizarDonoRestauranteComSucesso() throws Exception {
            UUID id = UUID.randomUUID();
            DadosDonoDto dadosDonoDto = new DadosDonoDto(UUID.randomUUID());

            doNothing().when(restauranteCoreController).atualizarDono(any(), any());

            mockMvc.perform(patch("/restaurantes/{id}/dono", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(dadosDonoDto)))
                    .andExpect(status().isNoContent());

            verify(restauranteCoreController, times(1)).atualizarDono(any(), any());
        }

        @Test
        @DisplayName("Deve atualizar nome do restaurante")
        void deveAtualizarNomeRestauranteComSucesso() throws Exception {
            UUID id = UUID.randomUUID();
            NomeDto novoNome = new NomeDto("Novo Nome do Restaurante");

            doNothing().when(restauranteCoreController).atualizarNome(any(), anyString());

            mockMvc.perform(patch("/restaurantes/{id}/nome", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoNome)))
                    .andExpect(status().isNoContent());

            verify(restauranteCoreController, times(1)).atualizarNome(any(), anyString());
        }

        @Test
        @DisplayName("Deve atualizar endereco do restaurante")
        void deveAtualizarEnderecoRestauranteComSucesso() throws Exception {
            UUID id = UUID.randomUUID();
            DadosEnderecoDto dadosEnderecoDto = new DadosEnderecoDto(
                    "Rua Exemplo",
                    "123",
                    "Bairro Exemplo",
                    "Cidade Exemplo",
                    "Estado Exemplo",
                    210,
                    ""
            );

            doNothing().when(restauranteCoreController).atualizarEndereco(any(), any(DadosEnderecoDto.class));

            mockMvc.perform(patch("/restaurantes/{id}/endereco", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(dadosEnderecoDto)))
                    .andExpect(status().isNoContent());

            verify(restauranteCoreController, times(1)).atualizarEndereco(any(), any(DadosEnderecoDto.class));
        }

        @Test
        @DisplayName("Deve atualizar atendimento do restaurante")
        void deveAtualizarAtendimentoRestauranteComSucesso() throws Exception {
            UUID id = UUID.randomUUID();
            AtendimentoDto atendimentoDto = new AtendimentoDto(
                    UUID.randomUUID(),
                    "SEGUNDA-FEIRA",
                    LocalTime.of(9, 0,0),
                    LocalTime.of(16, 0,0)
            );

            doNothing().when(restauranteCoreController).atualizarAtendimento(any(), any(AtendimentoDto.class));

            mockMvc.perform(patch("/restaurantes/{id-restaurante}/atendimentos", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(atendimentoDto)))
                    .andExpect(status().isNoContent());

            verify(restauranteCoreController, times(1)).atualizarAtendimento(any(), any(AtendimentoDto.class));
        }

        @Test
        @DisplayName("Deve adicionar Atendimento do restaurante")
        void deveAdicionarAtendimentoRestauranteComSucesso() throws Exception {
            UUID id = UUID.randomUUID();
            AtendimentoDto atendimentoDto = new AtendimentoDto(
                    UUID.randomUUID(),
                    "SEGUNDA-FEIRA",
                    LocalTime.of(9, 0,0),
                    LocalTime.of(16, 0,0)
            );

            doNothing().when(restauranteCoreController).adicionarAtendimento(any(), any(AtendimentoDto.class));

            mockMvc.perform(post("/restaurantes/{id-restaurante}/atendimentos", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(atendimentoDto)))
                    .andExpect(status().isNoContent());

            verify(restauranteCoreController, times(1)).adicionarAtendimento(any(), any(AtendimentoDto.class));
        }

        @Test
        @DisplayName("Deve excluir Atendimento do restaurante")
        void deveExcluirAtendimentoRestauranteComSucesso() throws Exception {
            UUID idRestaurante = UUID.randomUUID();
            UUID idAtendimento = UUID.randomUUID();

            doNothing().when(restauranteCoreController).excluirAtendimento(any(), any());

            mockMvc.perform(delete("/restaurantes/{id-restaurante}/atendimentos/{id-atendimento}", idRestaurante, idAtendimento)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

            verify(restauranteCoreController, times(1)).excluirAtendimento(any(), any());
        }

    }

    @Nested
    class GerenciarItemRequest {

        @DisplayName("Buscar todos os itens cadastrados")
        @Test
        void deveRetornarListaComItensCadastradosComSucesso() throws Exception {
            // Arrange

            UUID idRestaurante = UUID.randomUUID();

            List<ItemDto> itens = List.of( itemDto, itemDto );

            when(restauranteCoreController.buscarTodosItens(any(UUID.class))).thenReturn(itens);

            // Act & Assert
            mockMvc.perform(get("/restaurantes/{id-restaurante}/itens", idRestaurante)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2));

            verify(restauranteCoreController, times(1)).buscarTodosItens(any(UUID.class));
        }

        @DisplayName("Buscar item por id com sucesso")
        @Test
        void deveRetornarItemPorIdComSucesso() throws Exception {
            // Arrange
            UUID idRestaurante = UUID.randomUUID();
            UUID idItem = UUID.randomUUID();
            ItemDto item = new ItemDto(idItem,
                    "Feijoada",
                    "Feijoada completa com acompanhamentos",
                    BigDecimal.valueOf(39.9),
                    Boolean.TRUE,
                    Boolean.TRUE,
                    "/var/lib/docker/volumes/images/_datafeijoada.jpg");

            when(restauranteCoreController.buscarItemPorId(any(UUID.class), any(UUID.class))).thenReturn(item);

            // Act & Assert
            mockMvc.perform(get("/restaurantes/{id-restaurante}/itens/{id-item}", idRestaurante, idItem)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nome").value("Feijoada"));

            verify(restauranteCoreController, times(1)).buscarItemPorId(eq(idRestaurante), eq(idItem));
        }

        @DisplayName("Buscar item por id com erro. Item nao encontrado através do id")
        @Test
        void deveLancarExcecaoSeNaoEncontrarItemPorId() throws Exception {
            // Arrange
            UUID idRestaurante = UUID.randomUUID();
            UUID idItem = UUID.randomUUID();

            when(restauranteCoreController.buscarItemPorId(any(UUID.class), any(UUID.class)))
                    .thenThrow(new ItemNaoEncontradoException("Não foi encontrado nenhum item com o id informado."));

            // Act & Assert
            mockMvc.perform(get("/restaurantes/{id-restaurante}/itens/{id-item}", idRestaurante, idItem)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensagem").value("Não foi encontrado nenhum item com o id informado."));

            verify(restauranteCoreController, times(1)).buscarItemPorId(eq(idRestaurante), eq(idItem));
        }

        @DisplayName("Deve cadastrar um novo item com sucesso")
        @Test
        void deveCadastrarItemComSucesso() throws Exception {

            UUID idRestaurante = UUID.randomUUID();
            String nome = "Prato Exemplo";
            String descricao = "Descrição do prato";
            String preco = "29.90";
            String disponivel = "true";
            MockMultipartFile imagem = new MockMultipartFile(
                    "imagem",
                    "imagem_prato.jpg",
                    "image/jpeg",
                    "conteudo".getBytes());

            doNothing().when(restauranteCoreController).cadastrar(any(), any(), any(), any(), any(), any());

            mockMvc.perform(multipart("/restaurantes/{id-restaurante}/itens", idRestaurante)
                            .file(imagem)
                            .param("nome", nome)
                            .param("descricao", descricao)
                            .param("preco", preco)
                            .param("disponivelParaConsumoPresencial", disponivel)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isCreated());

            verify(restauranteCoreController, times(1)).cadastrar(any(), any(), any(), any(), any(), any());
        }

        @Test
        @DisplayName("Deve atualizar a descricao do item com sucesso")
        void deveAtualizarDescricaoItemComSucesso() throws Exception {

            UUID idRestaurante = UUID.randomUUID();
            UUID idItem = UUID.randomUUID();
            DescricaoDto novaDescricao = new DescricaoDto("Feijoada Especial para duas pessoas");

            // Mock do core controller
            doNothing().when(restauranteCoreController).atualizarDescricaoItem(any(), any(), any());

            mockMvc.perform(patch("/restaurantes/{id-restaurante}/itens/{id-item}/descricao", idRestaurante, idItem)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novaDescricao)))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(restauranteCoreController, times(1)).atualizarDescricaoItem(any(UUID.class), any(UUID.class), anyString());
        }

        @Test
        @DisplayName("Deve atualizar o nome do item com sucesso")
        void deveAtualizarNomeItemComSucesso() throws Exception {

            UUID idRestaurante = UUID.randomUUID();
            UUID idItem = UUID.randomUUID();
            NomeDto novoNome = new NomeDto("Feijoada");

            // Mock do core controller
            doNothing().when(restauranteCoreController).atualizarNomeItem(any(), any(), any());

            mockMvc.perform(patch("/restaurantes/{id-restaurante}/itens/{id-item}/nome", idRestaurante, idItem)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoNome)))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(restauranteCoreController, times(1)).atualizarNomeItem(any(UUID.class), any(UUID.class), anyString());
        }

        @Test
        @DisplayName("Deve atualizar o preco do item com sucesso")
        void deveAtualizarPrecoItemComSucesso() throws Exception {

            UUID idRestaurante = UUID.randomUUID();
            UUID idItem = UUID.randomUUID();
            PrecoDto novoPreco = new PrecoDto(BigDecimal.valueOf(30.32));

            // Mock do core controller
            doNothing().when(restauranteCoreController).atualizarPrecoItem(any(), any(), any(BigDecimal.class));

            mockMvc.perform(patch("/restaurantes/{id-restaurante}/itens/{id-item}/preco", idRestaurante, idItem)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(novoPreco)))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(restauranteCoreController, times(1)).atualizarPrecoItem(any(UUID.class), any(UUID.class), any(BigDecimal.class));
        }

        @Test
        @DisplayName("Deve atualizar o disponibilidadeConsumoPresencial do item com sucesso")
        void deveAtualizarDisponibilidadeConsumoPresencialItemComSucesso() throws Exception {

            UUID idRestaurante = UUID.randomUUID();
            UUID idItem = UUID.randomUUID();
            DisponibilidadeDto disponibilidadeConsumoPresencial = new DisponibilidadeDto(true);

            // Mock do core controller
            doNothing().when(restauranteCoreController)
                    .atualizarDisponibilidadeConsumoPresencialItem(any(), any(), anyBoolean());

            mockMvc.perform(
                    patch("/restaurantes/{id-restaurante}/itens/{id-item}/disponibilidade-consumo-presencial", idRestaurante, idItem)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(disponibilidadeConsumoPresencial)))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(restauranteCoreController, times(1))
                    .atualizarDisponibilidadeConsumoPresencialItem(any(UUID.class), any(UUID.class), anyBoolean());
        }


        @Test
        @DisplayName("Deve atualizar o disponibilidade do item com sucesso")
        void deveAtualizarDisponibilidadeComSucesso() throws Exception {

            UUID idRestaurante = UUID.randomUUID();
            UUID idItem = UUID.randomUUID();
            DisponibilidadeDto disponibilidade = new DisponibilidadeDto(false);

            // Mock do core controller
            doNothing().when(restauranteCoreController)
                    .atualizarDisponibilidadeItem(any(), any(), anyBoolean());

            mockMvc.perform(
                            patch("/restaurantes/{id-restaurante}/itens/{id-item}/disponibilidade", idRestaurante, idItem)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(disponibilidade)))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(restauranteCoreController, times(1))
                    .atualizarDisponibilidadeItem(any(UUID.class), any(UUID.class), anyBoolean());
        }

        @Test
        @DisplayName("Deve atualizar a imagem do item com sucesso")
        void deveAtualizarImagemDoItemComSucesso() throws Exception {
            UUID idRestaurante = UUID.randomUUID();
            UUID idItem = UUID.randomUUID();
            MockMultipartFile imagem = new MockMultipartFile(
                    "imagem",
                    "imagem.jpg",
                    "image/jpeg",
                    "conteudo".getBytes());

            doNothing().when(restauranteCoreController).atualizarImagemItem(any(), any(), any(MultipartFile.class));

            mockMvc.perform(multipart("/restaurantes/{id-restaurante}/itens/{id-item}/imagem", idRestaurante, idItem)
                            .file(imagem)
                            .with(request -> { request.setMethod("PATCH"); return request; }))
                    .andExpect(status().isNoContent());

            verify(restauranteCoreController, times(1)).atualizarImagemItem(any(), any(), any(MultipartFile.class));
        }

        @Test
        @DisplayName("Deve baixar a imagem do item com sucesso")
        void deveBaixarImagemDoItemComSucesso() throws Exception {
            UUID idRestaurante = UUID.randomUUID();
            UUID idItem = UUID.randomUUID();
            String nomeArquivo = "imagem.jpg";
            byte[] conteudo = "conteudo da imagem".getBytes();
            ImagemDto imagem = new ImagemDto(UUID.randomUUID(), nomeArquivo, conteudo, "image/jpeg");

            when(restauranteCoreController.baixarImagemItem(any(), any())).thenReturn(imagem);

            mockMvc.perform(get("/restaurantes/{id-restaurante}/itens/{id-item}/imagem/download", idRestaurante, idItem))
                    .andExpect(status().isOk())
                    .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\""))
                    .andExpect(content().bytes(conteudo));

            verify(restauranteCoreController, times(1)).baixarImagemItem(any(), any());
        }

    }
}
