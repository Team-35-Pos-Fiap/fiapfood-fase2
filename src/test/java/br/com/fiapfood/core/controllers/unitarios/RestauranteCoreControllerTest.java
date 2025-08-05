package br.com.fiapfood.core.controllers.unitarios;

import br.com.fiapfood.core.controllers.impl.RestauranteCoreController;
import br.com.fiapfood.core.controllers.interfaces.IRestauranteCoreController;
import br.com.fiapfood.core.entities.dto.atendimento.AtendimentoCoreDto;
import br.com.fiapfood.core.entities.dto.endereco.DadosEnderecoCoreDto;
import br.com.fiapfood.core.entities.dto.endereco.EnderecoCoreDto;
import br.com.fiapfood.core.entities.dto.item.ImagemCoreDto;
import br.com.fiapfood.core.entities.dto.item.ItemOutputCoreDto;
import br.com.fiapfood.core.entities.dto.restaurante.CadastrarRestauranteCoreDto;
import br.com.fiapfood.core.entities.dto.restaurante.DadosRestauranteCoreDto;
import br.com.fiapfood.core.entities.dto.tipo_culinaria.TipoCulinariaCoreDto;
import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioResumidoCoreDto;
import br.com.fiapfood.core.exceptions.item.ItemNaoEncontradoException;
import br.com.fiapfood.core.usecases.atendimento.interfaces.*;
import br.com.fiapfood.core.usecases.item.interfaces.*;
import br.com.fiapfood.core.usecases.restaurante.interfaces.*;
import br.com.fiapfood.infraestructure.controllers.request.atendimento.AtendimentoDto;
import br.com.fiapfood.infraestructure.controllers.request.endereco.DadosEnderecoDto;
import br.com.fiapfood.infraestructure.controllers.request.item.ImagemDto;
import br.com.fiapfood.infraestructure.controllers.request.item.ItemDto;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.*;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static br.com.fiapfood.utils.DtoDataGenerator.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.anyInt;

class RestauranteCoreControllerTest {

    @Mock
    private IRestauranteCoreController restauranteCoreController;

    @Mock
    private IBuscarRestaurantePorId buscarRestaurantePorId;

    @Mock
    private IBuscarTodosRestaurantesUseCase buscarTodosRestaurantesUseCase;

    @Mock
    private ICadastrarRestauranteUseCase cadastrarRestauranteUseCase;

    @Mock
    private IReativarRestauranteUseCase reativarRestauranteUseCase;

    @Mock
    private IInativarRestauranteUseCase inativarRestauranteUseCase;

    @Mock
    private IAtualizarDonoRestauranteUseCase atualizarDonoRestauranteUseCase;

    @Mock
    private IAtualizarEnderecoRestauranteUseCase atualizarEnderecoRestauranteUseCase;

    @Mock
    private IAtualizarNomeRestauranteUseCase atualizarNomeRestauranteUseCase;

    @Mock
    private IAtualizarTipoCulinariaRestauranteUseCase atualizarTipoCulinariaRestauranteUseCase;

    @Mock
    private IAtualizarAtendimentoUseCase atualizarAtendimentoUseCase;

    @Mock
    private IAdicionarAtendimentoUseCase adicionarAtendimentoUseCase;

    @Mock
    private IExcluirAtendimentoUseCase excluirAtendimentoUseCase;

    @Mock
    private IAtualizarDescricaoItemUseCase atualizarDescricaoItemUseCase;

    @Mock
    private IAtualizarNomeItemUseCase atualizarNomeItemUseCase;

    @Mock
    private IAtualizarDisponibilidadeConsumoPresencialItemUseCase atualizarDisponibilidadeConsumoPresencialItemUseCase;

    @Mock
    private IAtualizarDisponibilidadeItemUseCase atualizarDisponibilidadeItemUseCase;

    @Mock
    private IAtualizarImagemItemUseCase atualizarImagemItemUseCase;

    @Mock
    private IAtualizarPrecoItemUseCase atualizarPrecoItemUseCase;

    @Mock
    private IBaixarImagemItemUseCase baixarImagemItemUseCase;

    @Mock
    private IBuscarItemPorIdUseCase buscarItemPorIdUseCase;

    @Mock
    private IBuscarTodosItensUseCase buscarTodosItensUseCase;

    @Mock
    private ICadastrarItemUseCase cadastrarItemUseCase;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        restauranteCoreController = new RestauranteCoreController(
                buscarRestaurantePorId,
                buscarTodosRestaurantesUseCase,
                cadastrarRestauranteUseCase,
                reativarRestauranteUseCase,
                inativarRestauranteUseCase,
                atualizarDonoRestauranteUseCase,
                atualizarEnderecoRestauranteUseCase,
                atualizarNomeRestauranteUseCase,
                atualizarTipoCulinariaRestauranteUseCase,
                atualizarAtendimentoUseCase,
                adicionarAtendimentoUseCase,
                excluirAtendimentoUseCase,
                atualizarDescricaoItemUseCase,
                atualizarNomeItemUseCase,
                atualizarDisponibilidadeConsumoPresencialItemUseCase,
                atualizarDisponibilidadeItemUseCase,
                atualizarImagemItemUseCase,
                atualizarPrecoItemUseCase,
                baixarImagemItemUseCase,
                buscarItemPorIdUseCase,
                buscarTodosItensUseCase,
                cadastrarItemUseCase
        );
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
            // Arrange and Act
            when(buscarTodosRestaurantesUseCase.buscar(anyInt())).thenReturn(restaurantePaginacaoCoreDtoValido());
            var restaurantesRetornadosPeloController = restauranteCoreController.buscarTodos(1);

            // Assert
            assertThat(restaurantesRetornadosPeloController).isNotNull();
            verify(buscarTodosRestaurantesUseCase, times(1)).buscar(anyInt());
        }

        @Test
        @DisplayName("Deve buscar restaurante por id")
        void deveBuscarRestaurantePorIdComSucesso() throws Exception {
            // Arrange
            when(buscarRestaurantePorId.buscar(any(UUID.class)))
                    .thenReturn(dadosRestauranteCoreDtoValido());
            ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

            // Act
            var usuarioRetornadoDoController = restauranteCoreController.buscarPorId(dadosRestauranteCoreDtoValido().id());

            // Assert
            verify(buscarRestaurantePorId, times(1)).buscar(captor.capture());
            assertThat(captor.getValue()).isEqualTo(dadosRestauranteCoreDtoValido().id());
            assertThat(dadosRestauranteCoreDtoValido().id()).isEqualTo(usuarioRetornadoDoController.id());
        }

        @Test
        @DisplayName("Deve cadastrar restaurante")
        void deveCadastrarRestauranteComSucesso() throws Exception {
            // Arrange
            CadastrarRestauranteDto cadastrarRestauranteDto = cadastrarRestauranteDtoValido();
            CadastrarRestauranteCoreDto cadastrarRestauranteCoreDto = cadastrarRestauranteCoreDtoValido();

            doNothing().when(cadastrarRestauranteUseCase).cadastrar(any(CadastrarRestauranteCoreDto.class));
            ArgumentCaptor<CadastrarRestauranteCoreDto> captor = ArgumentCaptor.forClass(CadastrarRestauranteCoreDto.class);

            // Act
            restauranteCoreController.cadastrar(cadastrarRestauranteDto);

            // Assert
            verify(cadastrarRestauranteUseCase, times(1)).cadastrar(captor.capture());
            assertThat(captor.getValue()).isEqualTo(cadastrarRestauranteCoreDto);
        }

        @Test
        @DisplayName("Deve inativar restaurante")
        void deveInativarRestauranteComSucesso() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();

            doNothing().when(inativarRestauranteUseCase).inativar(any(UUID.class));
            ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

            // Act
            restauranteCoreController.inativar(id);

            // Assert
            verify(inativarRestauranteUseCase, times(1)).inativar(captor.capture());
            assertThat(captor.getValue()).isEqualTo(id);
        }

        @Test
        @DisplayName("Deve reativar restaurante")
        void deveReativarRestauranteComSucesso() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();

            doNothing().when(reativarRestauranteUseCase).reativar(any(UUID.class));
            ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

            // Act
            restauranteCoreController.reativar(id);

            // Assert
            verify(reativarRestauranteUseCase, times(1)).reativar(captor.capture());
            assertThat(captor.getValue()).isEqualTo(id);

        }

        @Test
        @DisplayName("Deve atualizar TipoCulinaria restaurante")
        void deveAtualizarTipoCulinariaRestauranteComSucesso() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            int novoTipo = 2;

            doNothing().when(atualizarTipoCulinariaRestauranteUseCase).atualizar(any(UUID.class), anyInt());
            ArgumentCaptor<Integer> tipoCulinariaIdCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<UUID> restauranteIdCaptor = ArgumentCaptor.forClass(UUID.class);

            // Act
            restauranteCoreController.atualizarTipoCulinaria(id, novoTipo);

            // Assert
            verify(atualizarTipoCulinariaRestauranteUseCase, times(1)).atualizar(restauranteIdCaptor.capture(), tipoCulinariaIdCaptor.capture());
            assertThat(tipoCulinariaIdCaptor.getValue()).isEqualTo(novoTipo);
            assertThat(restauranteIdCaptor.getValue()).isEqualTo(id);
        }

        @Test
        @DisplayName("Deve atualizar dono do restaurante")
        void deveAtualizarDonoRestauranteComSucesso() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            UUID idDono = UUID.randomUUID();

            doNothing().when(atualizarDonoRestauranteUseCase).atualizar(any(UUID.class), any(UUID.class));
            ArgumentCaptor<UUID> donoIdCaptor = ArgumentCaptor.forClass(UUID.class);
            ArgumentCaptor<UUID> restauranteIdCaptor = ArgumentCaptor.forClass(UUID.class);

            // Act
            restauranteCoreController.atualizarDono(id, idDono);

            // Assert
            verify(atualizarDonoRestauranteUseCase, times(1)).atualizar(restauranteIdCaptor.capture(), donoIdCaptor.capture());
            assertThat(donoIdCaptor.getValue()).isEqualTo(idDono);
            assertThat(restauranteIdCaptor.getValue()).isEqualTo(id);
        }

        @Test
        @DisplayName("Deve atualizar nome do restaurante")
        void deveAtualizarNomeRestauranteComSucesso() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            String novoNome = "Novo Restaurante";

            doNothing().when(atualizarNomeRestauranteUseCase).atualizar(any(UUID.class), anyString());
            ArgumentCaptor<String> nomeCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<UUID> restauranteIdCaptor = ArgumentCaptor.forClass(UUID.class);

            // Act
            restauranteCoreController.atualizarNome(id, novoNome);

            // Assert
            verify(atualizarNomeRestauranteUseCase, times(1)).atualizar(restauranteIdCaptor.capture(), nomeCaptor.capture());
            assertThat(nomeCaptor.getValue()).isEqualTo(novoNome);
            assertThat(restauranteIdCaptor.getValue()).isEqualTo(id);
        }

        @Test
        @DisplayName("Deve atualizar endereco do restaurante")
        void deveAtualizarEnderecoRestauranteComSucesso() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            DadosEnderecoDto dadosEndereco = dadosEnderecoDtoValido();
            DadosEnderecoCoreDto dadosEnderecoCore = dadosEnderecoCoreDtoValido();

            doNothing().when(atualizarEnderecoRestauranteUseCase).atualizar(any(UUID.class), any(DadosEnderecoCoreDto.class));
            ArgumentCaptor<DadosEnderecoCoreDto> enderecoCaptor = ArgumentCaptor.forClass(DadosEnderecoCoreDto.class);
            ArgumentCaptor<UUID> restauranteIdCaptor = ArgumentCaptor.forClass(UUID.class);

            // Act
            restauranteCoreController.atualizarEndereco(id, dadosEndereco);

            // Assert
            verify(atualizarEnderecoRestauranteUseCase, times(1)).atualizar(restauranteIdCaptor.capture(), enderecoCaptor.capture());
            assertThat(enderecoCaptor.getValue()).isEqualTo(dadosEnderecoCore);
            assertThat(restauranteIdCaptor.getValue()).isEqualTo(id);
        }

        @Test
        @DisplayName("Deve atualizar atendimento do restaurante")
        void deveAtualizarAtendimentoRestauranteComSucesso() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            AtendimentoDto atendimento = atendimentoDtoValido();
            AtendimentoCoreDto atendimentoCore = atendimentoCoreDtoValido();

            doNothing().when(atualizarAtendimentoUseCase).atualizar(any(UUID.class), any(AtendimentoCoreDto.class));
            ArgumentCaptor<AtendimentoCoreDto> atendimentoCaptor = ArgumentCaptor.forClass(AtendimentoCoreDto.class);
            ArgumentCaptor<UUID> restauranteIdCaptor = ArgumentCaptor.forClass(UUID.class);

            // Act
            restauranteCoreController.atualizarAtendimento(id, atendimento);

            // Assert
            verify(atualizarAtendimentoUseCase, times(1)).atualizar(restauranteIdCaptor.capture(), atendimentoCaptor.capture());
            assertThat(atendimentoCaptor.getValue()).isEqualTo(atendimentoCore);
            assertThat(restauranteIdCaptor.getValue()).isEqualTo(id);
        }

        @Test
        @DisplayName("Deve adicionar Atendimento do restaurante")
        void deveAdicionarAtendimentoRestauranteComSucesso() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            AtendimentoDto atendimento = atendimentoDtoValido();
            AtendimentoCoreDto atendimentoCore = atendimentoCoreDtoValido();

            doNothing().when(adicionarAtendimentoUseCase).adicionar(any(UUID.class), any(AtendimentoCoreDto.class));
            ArgumentCaptor<AtendimentoCoreDto> atendimentoCaptor = ArgumentCaptor.forClass(AtendimentoCoreDto.class);
            ArgumentCaptor<UUID> restauranteIdCaptor = ArgumentCaptor.forClass(UUID.class);

            // Act
            restauranteCoreController.adicionarAtendimento(id, atendimento);

            // Assert
            verify(adicionarAtendimentoUseCase, times(1)).adicionar(restauranteIdCaptor.capture(), atendimentoCaptor.capture());
            assertThat(atendimentoCaptor.getValue()).isEqualTo(atendimentoCore);
            assertThat(restauranteIdCaptor.getValue()).isEqualTo(id);
        }

        @Test
        @DisplayName("Deve excluir Atendimento do restaurante")
        void deveExcluirAtendimentoRestauranteComSucesso() throws Exception {
            // Arrange
            UUID id = UUID.randomUUID();
            UUID idAtendimento = UUID.randomUUID();

            doNothing().when(excluirAtendimentoUseCase).excluir(any(UUID.class), any(UUID.class));
            ArgumentCaptor<UUID> idAtendimentoCaptor = ArgumentCaptor.forClass(UUID.class);
            ArgumentCaptor<UUID> restauranteIdCaptor = ArgumentCaptor.forClass(UUID.class);

            // Act
            restauranteCoreController.excluirAtendimento(id, idAtendimento);

            // Assert
            verify(excluirAtendimentoUseCase, times(1)).excluir(restauranteIdCaptor.capture(), idAtendimentoCaptor.capture());
            assertThat(idAtendimentoCaptor.getValue()).isEqualTo(idAtendimento);
            assertThat(restauranteIdCaptor.getValue()).isEqualTo(id);
        }

    }

    @Nested
    class GerenciarItemRequest {

        @DisplayName("Buscar todos os itens cadastrados")
        @Test
        void deveRetornarListaComItensCadastradosComSucesso() throws Exception {
            // Arrange
            UUID idRestaurante = UUID.randomUUID();
            List<ItemOutputCoreDto> itens = List.of(itemOutputCoreDtoValido(), itemOutputCoreDtoValido());

            when(buscarTodosItensUseCase.buscar(any(UUID.class))).thenReturn(itens);
            ArgumentCaptor<UUID> restauranteIdCaptor = ArgumentCaptor.forClass(UUID.class);

            // Act
            List<?> resultado = restauranteCoreController.buscarTodosItens(idRestaurante);

            // Assert
            verify(buscarTodosItensUseCase, times(1)).buscar(restauranteIdCaptor.capture());
            assertThat(restauranteIdCaptor.getValue()).isEqualTo(idRestaurante);
            assertThat(resultado).isNotNull();
            assertThat(resultado.size()).isEqualTo(itens.size());

        }

        @DisplayName("Buscar item por id com sucesso")
        @Test
        void deveRetornarItemPorIdComSucesso() throws Exception {
            // Arrange
            UUID idRestaurante = UUID.randomUUID();
            UUID idItem = UUID.randomUUID();
            ItemOutputCoreDto item = itemOutputCoreDtoValido();
            ItemDto itemDto = itemDtoValido();

            when(buscarItemPorIdUseCase.buscar(any(UUID.class), any(UUID.class)))
                    .thenReturn(item);
            ArgumentCaptor<UUID> restauranteIdCaptor = ArgumentCaptor.forClass(UUID.class);
            ArgumentCaptor<UUID> itemIdCaptor = ArgumentCaptor.forClass(UUID.class);

            // Act
            var resultado = restauranteCoreController.buscarItemPorId(idRestaurante, idItem);

            // Assert
            verify(buscarItemPorIdUseCase, times(1)).buscar(restauranteIdCaptor.capture(), itemIdCaptor.capture());
            assertThat(restauranteIdCaptor.getValue()).isEqualTo(idRestaurante);
            assertThat(itemIdCaptor.getValue()).isEqualTo(idItem);
            assertThat(resultado).isEqualTo(itemDto);

        }

        @DisplayName("Buscar item por id com erro. Item nao encontrado através do id")
        @Test
        void deveLancarExcecaoSeNaoEncontrarItemPorId() throws Exception {
            // Arrange
            UUID idRestaurante = UUID.randomUUID();
            UUID idItem = UUID.randomUUID();

            when(buscarItemPorIdUseCase.buscar(any(UUID.class), any(UUID.class)))
                    .thenThrow(new ItemNaoEncontradoException("Não foi encontrado nenhum item com o id informado para o restaurante."));

            // Act & Assert
            assertThatThrownBy(() -> restauranteCoreController.buscarItemPorId(idRestaurante, idItem))
                    .isInstanceOf(ItemNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum item com o id informado para o restaurante.");
            verify(buscarItemPorIdUseCase, times(1)).buscar(any(UUID.class), any(UUID.class));
        }

        @DisplayName("Deve cadastrar um novo item com sucesso")
        @Test
        void deveCadastrarItemComSucesso() throws Exception {

            // Arrange
            UUID idRestaurante = UUID.randomUUID();
            String nome = "Prato Exemplo";
            String descricao = "Descrição do prato";
            BigDecimal preco = BigDecimal.valueOf(29.90);
            Boolean disponivel = true;
            MockMultipartFile imagem = new MockMultipartFile(
                    "imagem",
                    "imagem_prato.jpg",
                    "image/jpeg",
                    "conteudo".getBytes());

            doNothing().when(cadastrarItemUseCase).cadastrar(any(), any(), any(), any(), any(), any());

            ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);
            ArgumentCaptor<String> nomeCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<String> descricaoCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<BigDecimal> precoCaptor = ArgumentCaptor.forClass(BigDecimal.class);
            ArgumentCaptor<Boolean> disponivelCaptor = ArgumentCaptor.forClass(Boolean.class);
            ArgumentCaptor<ImagemCoreDto> imagemCaptor = ArgumentCaptor.forClass(ImagemCoreDto.class);

            // Act
            restauranteCoreController.cadastrar(
                    idRestaurante,
                    nome,
                    descricao,
                    preco,
                    disponivel,
                    imagem
            );

            // Assert
            verify(cadastrarItemUseCase, times(1)).cadastrar(
                    idCaptor.capture(),
                    nomeCaptor.capture(),
                    descricaoCaptor.capture(),
                    precoCaptor.capture(),
                    disponivelCaptor.capture(),
                    imagemCaptor.capture()
            );

            assertThat(idCaptor.getValue()).isEqualTo(idRestaurante);
            assertThat(nomeCaptor.getValue()).isEqualTo(nome);
            assertThat(descricaoCaptor.getValue()).isEqualTo(descricao);
            assertThat(precoCaptor.getValue()).isEqualTo(preco);
            assertThat(disponivelCaptor.getValue()).isEqualTo(disponivel);
        }

        @Test
        @DisplayName("Deve atualizar a descricao do item com sucesso")
        void deveAtualizarDescricaoItemComSucesso() throws Exception {

            // Arrange
            UUID idRestaurante = UUID.randomUUID();
            UUID idItem = UUID.randomUUID();
            String novaDescricao = "Nova descrição do item";

            doNothing().when(atualizarDescricaoItemUseCase).atualizar(any(UUID.class), any(UUID.class), anyString());
            ArgumentCaptor<String> descricaoCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<UUID> itemIdCaptor = ArgumentCaptor.forClass(UUID.class);
            ArgumentCaptor<UUID> restauranteIdCaptor = ArgumentCaptor.forClass(UUID.class);

            // Act
            restauranteCoreController.atualizarDescricaoItem(idRestaurante, idItem, novaDescricao);

            // Assert
            verify(atualizarDescricaoItemUseCase, times(1)).atualizar(restauranteIdCaptor.capture(), itemIdCaptor.capture(), descricaoCaptor.capture());
            assertThat(descricaoCaptor.getValue()).isEqualTo(novaDescricao);
            assertThat(itemIdCaptor.getValue()).isEqualTo(idItem);
            assertThat(restauranteIdCaptor.getValue()).isEqualTo(idRestaurante);
        }

        @Test
        @DisplayName("Deve atualizar o nome do item com sucesso")
        void deveAtualizarNomeItemComSucesso() throws Exception {

            // Arrange
            UUID idRestaurante = UUID.randomUUID();
            UUID idItem = UUID.randomUUID();
            String novoNome = "Novo nome";

            doNothing().when(atualizarNomeItemUseCase).atualizar(any(UUID.class), any(UUID.class), anyString());
            ArgumentCaptor<String> nomeCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<UUID> itemIdCaptor = ArgumentCaptor.forClass(UUID.class);
            ArgumentCaptor<UUID> restauranteIdCaptor = ArgumentCaptor.forClass(UUID.class);

            // Act
            restauranteCoreController.atualizarNomeItem(idRestaurante, idItem, novoNome);

            // Assert
            verify(atualizarNomeItemUseCase, times(1)).atualizar(restauranteIdCaptor.capture(), itemIdCaptor.capture(), nomeCaptor.capture());
            assertThat(nomeCaptor.getValue()).isEqualTo(novoNome);
            assertThat(itemIdCaptor.getValue()).isEqualTo(idItem);
            assertThat(restauranteIdCaptor.getValue()).isEqualTo(idRestaurante);
        }

        @Test
        @DisplayName("Deve atualizar o preco do item com sucesso")
        void deveAtualizarPrecoItemComSucesso() throws Exception {

            // Arrange
            UUID idRestaurante = UUID.randomUUID();
            UUID idItem = UUID.randomUUID();
            BigDecimal novoPreco = BigDecimal.valueOf(30.10);

            doNothing().when(atualizarPrecoItemUseCase).atualizar(any(UUID.class), any(UUID.class), any(BigDecimal.class));
            ArgumentCaptor<BigDecimal> precoCaptor = ArgumentCaptor.forClass(BigDecimal.class);
            ArgumentCaptor<UUID> itemIdCaptor = ArgumentCaptor.forClass(UUID.class);
            ArgumentCaptor<UUID> restauranteIdCaptor = ArgumentCaptor.forClass(UUID.class);

            // Act
            restauranteCoreController.atualizarPrecoItem(idRestaurante, idItem, novoPreco);

            // Assert
            verify(atualizarPrecoItemUseCase, times(1)).atualizar(restauranteIdCaptor.capture(), itemIdCaptor.capture(), precoCaptor.capture());
            assertThat(precoCaptor.getValue()).isEqualTo(novoPreco);
            assertThat(itemIdCaptor.getValue()).isEqualTo(idItem);
            assertThat(restauranteIdCaptor.getValue()).isEqualTo(idRestaurante);
        }

        @Test
        @DisplayName("Deve atualizar o disponibilidadeConsumoPresencial do item com sucesso")
        void deveAtualizarDisponibilidadeConsumoPresencialItemComSucesso() throws Exception {

            // Arrange
            UUID idRestaurante = UUID.randomUUID();
            UUID idItem = UUID.randomUUID();
            Boolean disponibilidade = true;

            doNothing().when(atualizarDisponibilidadeConsumoPresencialItemUseCase).atualizar(any(UUID.class), any(UUID.class), anyBoolean());
            ArgumentCaptor<Boolean> disponibilidadeCaptor = ArgumentCaptor.forClass(Boolean.class);
            ArgumentCaptor<UUID> itemIdCaptor = ArgumentCaptor.forClass(UUID.class);
            ArgumentCaptor<UUID> restauranteIdCaptor = ArgumentCaptor.forClass(UUID.class);

            // Act
            restauranteCoreController.atualizarDisponibilidadeConsumoPresencialItem(idRestaurante, idItem, disponibilidade);

            // Assert
            verify(atualizarDisponibilidadeConsumoPresencialItemUseCase, times(1))
                    .atualizar(restauranteIdCaptor.capture(), itemIdCaptor.capture(), disponibilidadeCaptor.capture());
            assertThat(disponibilidadeCaptor.getValue()).isEqualTo(disponibilidade);
            assertThat(itemIdCaptor.getValue()).isEqualTo(idItem);
            assertThat(restauranteIdCaptor.getValue()).isEqualTo(idRestaurante);
        }


        @Test
        @DisplayName("Deve atualizar o disponibilidade do item com sucesso")
        void deveAtualizarDisponibilidadeComSucesso() throws Exception {

            // Arrange
            UUID idRestaurante = UUID.randomUUID();
            UUID idItem = UUID.randomUUID();
            Boolean disponibilidade = false;

            doNothing().when(atualizarDisponibilidadeItemUseCase).atualizar(any(UUID.class), any(UUID.class), anyBoolean());
            ArgumentCaptor<Boolean> disponibilidadeCaptor = ArgumentCaptor.forClass(Boolean.class);
            ArgumentCaptor<UUID> itemIdCaptor = ArgumentCaptor.forClass(UUID.class);
            ArgumentCaptor<UUID> restauranteIdCaptor = ArgumentCaptor.forClass(UUID.class);

            // Act
            restauranteCoreController.atualizarDisponibilidadeItem(idRestaurante, idItem, disponibilidade);

            // Assert
            verify(atualizarDisponibilidadeItemUseCase, times(1))
                    .atualizar(restauranteIdCaptor.capture(), itemIdCaptor.capture(), disponibilidadeCaptor.capture());
            assertThat(disponibilidadeCaptor.getValue()).isEqualTo(disponibilidade);
            assertThat(itemIdCaptor.getValue()).isEqualTo(idItem);
            assertThat(restauranteIdCaptor.getValue()).isEqualTo(idRestaurante);
        }

        @Test
        @DisplayName("Deve atualizar a imagem do item com sucesso")
        void deveAtualizarImagemDoItemComSucesso() throws Exception {

            // Arrange
            UUID idRestaurante = UUID.randomUUID();
            UUID idItem = UUID.randomUUID();
            ImagemCoreDto imagem = new ImagemCoreDto(UUID.randomUUID(),
                    "imagem",
                    "conteudo".getBytes(),
                    "imagem_prato.jpg"
            );

            MockMultipartFile imagemMultipartFile = new MockMultipartFile(
                    "imagem",
                    "imagem_prato.jpg",
                    "image/jpeg",
                    "conteudo".getBytes());

            doNothing().when(atualizarImagemItemUseCase).atualizar(any(UUID.class), any(UUID.class), any(ImagemCoreDto.class));
            ArgumentCaptor<ImagemCoreDto> imagemCaptor = ArgumentCaptor.forClass(ImagemCoreDto.class);
            ArgumentCaptor<UUID> itemIdCaptor = ArgumentCaptor.forClass(UUID.class);
            ArgumentCaptor<UUID> restauranteIdCaptor = ArgumentCaptor.forClass(UUID.class);

            // Act
            restauranteCoreController.atualizarImagemItem(idRestaurante, idItem, imagemMultipartFile);

            // Assert
            verify(atualizarImagemItemUseCase, times(1))
                    .atualizar(restauranteIdCaptor.capture(), itemIdCaptor.capture(), imagemCaptor.capture());
            assertThat(imagemCaptor.getValue().conteudo()).isEqualTo(imagem.conteudo());
            assertThat(itemIdCaptor.getValue()).isEqualTo(idItem);
            assertThat(restauranteIdCaptor.getValue()).isEqualTo(idRestaurante);
        }

        @Test
        @DisplayName("Deve baixar a imagem do item com sucesso")
        void deveBaixarImagemDoItemComSucesso() throws Exception {

            // Arrange
            UUID idRestaurante = UUID.randomUUID();
            UUID idItem = UUID.randomUUID();
            ImagemCoreDto imagem = new ImagemCoreDto(UUID.randomUUID(),
                    "imagem",
                    "conteudo".getBytes(),
                    "imagem_prato.jpg"
            );

            ImagemDto imagemDto = new ImagemDto(UUID.randomUUID(),
                    "imagem",
                    "conteudo".getBytes(),
                    "imagem_prato.jpg"
            );

            when(baixarImagemItemUseCase.baixar(any(UUID.class), any(UUID.class)))
                    .thenReturn(imagem);
            ArgumentCaptor<UUID> restauranteIdCaptor = ArgumentCaptor.forClass(UUID.class);
            ArgumentCaptor<UUID> itemIdCaptor = ArgumentCaptor.forClass(UUID.class);

            // Act
            var resultado = restauranteCoreController.baixarImagemItem(idRestaurante, idItem);

            // Assert
            verify(baixarImagemItemUseCase, times(1)).baixar(restauranteIdCaptor.capture(), itemIdCaptor.capture());
            assertThat(restauranteIdCaptor.getValue()).isEqualTo(idRestaurante);
            assertThat(itemIdCaptor.getValue()).isEqualTo(idItem);
            assertThat(resultado.conteudo()).isEqualTo(imagemDto.conteudo());
        }

    }
}
