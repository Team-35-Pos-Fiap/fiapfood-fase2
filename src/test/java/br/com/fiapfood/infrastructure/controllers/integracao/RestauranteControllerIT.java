package br.com.fiapfood.infrastructure.controllers.integracao;


import br.com.fiapfood.infraestructure.controllers.request.atendimento.AtendimentoDto;
import br.com.fiapfood.infraestructure.controllers.request.endereco.DadosEnderecoDto;
import br.com.fiapfood.infraestructure.controllers.request.item.DescricaoDto;
import br.com.fiapfood.infraestructure.controllers.request.item.DisponibilidadeDto;
import br.com.fiapfood.infraestructure.controllers.request.item.PrecoDto;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.CadastrarRestauranteDto;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.DadosDonoDto;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.DadosTipoCulinariaDto;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.NomeDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static br.com.fiapfood.utils.DtoDataGenerator.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class RestauranteControllerIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Nested
    class GerenciarRestauranteRequest {

        @Test
        @DisplayName("Deve buscar restaurantes com paginação")
        void deveBuscarRestaurantesComSucesso() throws Exception {
            // Arrange

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/restaurantes")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", hasKey("restaurantes"))
                    .body("$", hasKey("paginacao"))
                    .body("restaurantes", hasSize(5));
        }

        @Test
        @DisplayName("Deve buscar restaurante por id")
        void deveBuscarRestaurantePorIdComSucesso() throws Exception {
            // Arrange
            UUID id = UUID.fromString("a72181a6-7699-4686-a5ec-1a0431764e62");

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/restaurantes/{id}", id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(id.toString()));
        }

        @Test
        @DisplayName("Deve cadastrar restaurante com sucesso")
        void deveCadastrarRestauranteComSucesso() throws Exception {
            // Arrange

            AtendimentoDto atendimento1 = new AtendimentoDto(
                    null,
                    "SEGUNDA-FEIRA",
                    LocalTime.of(9, 0, 0),
                    LocalTime.of(16, 0, 0)
            );

            AtendimentoDto atendimento2 = new AtendimentoDto(
                    null,
                    "TERÇA-FEIRA",
                    LocalTime.of(9, 0, 0),
                    LocalTime.of(16, 0, 0)
            );

            List<AtendimentoDto> atendimentos = List.of(atendimento1, atendimento2);

            CadastrarRestauranteDto dadosRestaurante = new CadastrarRestauranteDto(
                    "Restaurante novo",
                    dadosEnderecoDtoValido(),
                    UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c"),
                    1,
                    atendimentos);

            //Act & Assert
            given()
                    .contentType(ContentType.JSON)
                    .body(dadosRestaurante)
                    .when()
                    .post("/restaurantes")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());


        }

        @Test
        @DisplayName("Deve inativar restaurante")
        void deveInativarRestauranteComSucesso() throws Exception {
            // Arrange
            UUID idRestauranteAtivo = UUID.fromString("a72181a6-7699-4686-a5ec-1a0431764e62");

            // Act & Assert - Inativa restaurante
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .patch("/restaurantes/{id}/status/inativa", idRestauranteAtivo)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("mensagem", equalTo("Restaurante inativado com sucesso."));

            // Act & Assert - Valida inativação do restaurante
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/restaurantes/{id}", idRestauranteAtivo)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(idRestauranteAtivo.toString()))
                    .body("isAtivo", equalTo(false));

        }

        @Test
        @DisplayName("Deve reativar restaurante")
        void deveReativarRestauranteComSucesso() throws Exception {
            // Arrange
            UUID idRestauranteInativo = UUID.fromString("fc8a9535-d6be-465f-8bf1-d9885e91c91d");

            // Act & Assert - reativa restaurante
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .patch("/restaurantes/{id}/status/reativa", idRestauranteInativo)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("mensagem", equalTo("Restaurante reativado com sucesso."));

            // Act & Assert - Valida reativação do restaurante
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/restaurantes/{id}", idRestauranteInativo)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(idRestauranteInativo.toString()))
                    .body("isAtivo", equalTo(true));
        }

        @Test
        @DisplayName("Deve atualizar TipoCulinaria restaurante")
        void deveAtualizarTipoCulinariaRestauranteComSucesso() throws Exception {

            // Arrange
            UUID idRestaurante = UUID.fromString("a72181a6-7699-4686-a5ec-1a0431764e62");
            DadosTipoCulinariaDto novoTipoCulinaria = new DadosTipoCulinariaDto(2);

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoTipoCulinaria)
                    .when()
                    .patch("/restaurantes/{id}/tipo-culinaria", idRestaurante)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // Valida atualização do tipo de culinária
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/restaurantes/{id}", idRestaurante)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(idRestaurante.toString()))
                    .body("tipoCulinaria.id", equalTo(2));
        }

        @Test
        @DisplayName("Deve atualizar dono do restaurante")
        void deveAtualizarDonoRestauranteComSucesso() throws Exception {
            // Arrange
            UUID idRestaurante = UUID.fromString("a72181a6-7699-4686-a5ec-1a0431764e62");
            DadosDonoDto dadosDono = new DadosDonoDto(UUID.fromString("507bd6e5-ab40-4e91-818d-c9567f8b534e"));

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(dadosDono)
                    .when()
                    .patch("/restaurantes/{id}/dono", idRestaurante)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // Valida atualização do dono
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/restaurantes/{id}", idRestaurante)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(idRestaurante.toString()))
                    .body("dono.id", equalTo(dadosDono.idDono().toString()));

        }

        @Test
        @DisplayName("Deve atualizar nome do restaurante")
        void deveAtualizarNomeRestauranteComSucesso() throws Exception {
            // Arrange
            UUID idRestaurante = UUID.fromString("a72181a6-7699-4686-a5ec-1a0431764e62");
            NomeDto novoNome = new NomeDto("Burguer King");

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoNome)
                    .when()
                    .patch("/restaurantes/{id}/nome", idRestaurante)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // Valida atualização do nome
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/restaurantes/{id}", idRestaurante)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(idRestaurante.toString()))
                    .body("nome", equalTo(novoNome.nome()));
        }

        @Test
        @DisplayName("Deve atualizar endereco do restaurante")
        void deveAtualizarEnderecoRestauranteComSucesso() throws Exception {
            // Arrange
            UUID idRestaurante = UUID.fromString("a72181a6-7699-4686-a5ec-1a0431764e62");
            DadosEnderecoDto novoEndereco = new DadosEnderecoDto(
                    "Sao paulo",
                    "12345678",
                    "Nova SP",
                    "Rua sao joao",
                    "SP",
                    210,
                    "Complemento Novo"
            );

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoEndereco)
                    .when()
                    .patch("/restaurantes/{id}/endereco", idRestaurante)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // Valida atualização do nome
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/restaurantes/{id}", idRestaurante)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(idRestaurante.toString()))
                    .body("endereco.cep", equalTo(novoEndereco.cep()));
        }

        @Test
        @DisplayName("Deve atualizar atendimento do restaurante")
        void deveAtualizarAtendimentoRestauranteComSucesso() throws Exception {
            // Arrange
            UUID idRestaurante = UUID.fromString("21adec7d-d4f7-4999-a4dd-eaf0c242b3bd");
            AtendimentoDto novoAtendimento = new AtendimentoDto(
                    UUID.fromString("4f59e8f6-0ef4-45de-a8e6-6a1f33a3a765"),
                    "Terça-feira",
                    LocalTime.of(10, 0, 0),
                    LocalTime.of(18, 0, 0)
            );

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoAtendimento)
                    .when()
                    .patch("/restaurantes/{id}/atendimentos", idRestaurante)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // Valida atualização do atendimento
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/restaurantes/{id}", idRestaurante)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(idRestaurante.toString()))
                    .body("atendimentos[0].terminoAtendimento", equalTo("18:00:00"));
        }

        @Test
        @DisplayName("Deve adicionar Atendimento do restaurante")
        void deveAdicionarAtendimentoRestauranteComSucesso() throws Exception {
            // Arrange
            UUID idRestaurante = UUID.fromString("21adec7d-d4f7-4999-a4dd-eaf0c242b3bd");
            AtendimentoDto novoAtendimento = new AtendimentoDto(
                    null,
                    "Quinta-feira",
                    LocalTime.of(10, 0, 0),
                    LocalTime.of(18, 0, 0)
            );

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoAtendimento)
                    .when()
                    .post("/restaurantes/{id}/atendimentos", idRestaurante)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // Valida atualização do atendimento
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/restaurantes/{id}", idRestaurante)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(idRestaurante.toString()))
                    .body("atendimentos[0].terminoAtendimento", equalTo("18:00:00"));

        }

        @Test
        @DisplayName("Deve excluir Atendimento do restaurante")
        void deveExcluirAtendimentoRestauranteComSucesso() throws Exception {
            // Arrange
            UUID idRestaurante = UUID.fromString("21adec7d-d4f7-4999-a4dd-eaf0c242b3bd");
            UUID idAtendimento = UUID.fromString("4f59e8f6-0ef4-45de-a8e6-6a1f33a3a765");

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .delete("/restaurantes/{id-restaurante}/atendimentos/{id-atendimento}", idRestaurante, idAtendimento)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

        }

    }

    @Nested
    class GerenciarItemRequest {

        @DisplayName("Buscar todos os itens cadastrados")
        @Test
        void deveRetornarListaComItensCadastradosComSucesso() throws Exception {
            // Arrange
            UUID idRestaurante = UUID.fromString("21adec7d-d4f7-4999-a4dd-eaf0c242b3bd");

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/restaurantes/{id-restaurante}/itens", idRestaurante)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("[0].id", equalTo("bce5f3c0-d4e4-4957-8f99-b95f12354d13"));

        }

        @DisplayName("Buscar item por id com sucesso")
        @Test
        void deveRetornarItemPorIdComSucesso() throws Exception {
            // Arrange
            UUID idRestaurante = UUID.fromString("21adec7d-d4f7-4999-a4dd-eaf0c242b3bd");
            UUID idItem = UUID.fromString("bce5f3c0-d4e4-4957-8f99-b95f12354d13");

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/restaurantes/{id-restaurante}/itens/{id-item}", idRestaurante, idItem)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(idItem.toString()));
        }

        @DisplayName("Buscar item por id com erro. Item nao encontrado através do id")
        @Test
        void deveLancarExcecaoSeNaoEncontrarItemPorId() throws Exception {
            // Arrange
            UUID idRestaurante = UUID.fromString("21adec7d-d4f7-4999-a4dd-eaf0c242b3bd");
            UUID idItem = UUID.randomUUID();

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/restaurantes/{id-restaurante}/itens/{id-item}", idRestaurante, idItem)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Não foi encontrado nenhum item com o id informado para o restaurante."));

        }

        @DisplayName("Deve cadastrar um novo item com sucesso")
        @Test
        void deveCadastrarItemComSucesso() throws Exception {
            // Arrange
            UUID idRestaurante = UUID.fromString("21adec7d-d4f7-4999-a4dd-eaf0c242b3bd");

            String nome = "Prato Exemplo";
            String descricao = "Descrição do prato";
            String preco = "29.90";
            String disponivel = "true";
            MockMultipartFile imagem = new MockMultipartFile(
                    "imagem",
                    "imagem_prato.jpg",
                    "image/jpeg",
                    "conteudo".getBytes());

            // Act & Assert
            given()
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .multiPart("imagem", "imagem_prato.jpg", "conteudo".getBytes(), "image/jpeg")
                    .param("nome", nome)
                    .param("descricao", descricao)
                    .param("preco", preco)
                    .param("disponivelParaConsumoPresencial", disponivel)
                    .when()
                    .post("/restaurantes/{id-restaurante}/itens", idRestaurante)
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
        }

        @Test
        @DisplayName("Deve atualizar a descricao do item com sucesso")
        void deveAtualizarDescricaoItemComSucesso() throws Exception {
            // Arrange
            UUID idRestaurante = UUID.fromString("21adec7d-d4f7-4999-a4dd-eaf0c242b3bd");
            UUID idItem = UUID.fromString("bce5f3c0-d4e4-4957-8f99-b95f12354d13");

            DescricaoDto descricaoDto = new DescricaoDto("Descrição atualizada do prato");

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(descricaoDto)
                    .when()
                    .patch("/restaurantes/{id-restaurante}/itens/{id-item}/descricao", idRestaurante, idItem)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // Valida atualização da descrição
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/restaurantes/{id-restaurante}/itens/{id-item}", idRestaurante, idItem)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("descricao", equalTo(descricaoDto.descricao()));
        }

        @Test
        @DisplayName("Deve atualizar o nome do item com sucesso")
        void deveAtualizarNomeItemComSucesso() throws Exception {
            // Arrange
            UUID idRestaurante = UUID.fromString("21adec7d-d4f7-4999-a4dd-eaf0c242b3bd");
            UUID idItem = UUID.fromString("bce5f3c0-d4e4-4957-8f99-b95f12354d13");

            NomeDto nomeDto = new NomeDto("Nome atualizado do prato");

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(nomeDto)
                    .when()
                    .patch("/restaurantes/{id-restaurante}/itens/{id-item}/nome", idRestaurante, idItem)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // Valida atualização da descrição
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/restaurantes/{id-restaurante}/itens/{id-item}", idRestaurante, idItem)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("nome", equalTo(nomeDto.nome()));
        }

        @Test
        @DisplayName("Deve atualizar o preco do item com sucesso")
        void deveAtualizarPrecoItemComSucesso() throws Exception {
            // Arrange
            UUID idRestaurante = UUID.fromString("21adec7d-d4f7-4999-a4dd-eaf0c242b3bd");
            UUID idItem = UUID.fromString("bce5f3c0-d4e4-4957-8f99-b95f12354d13");

            PrecoDto precoDto = new PrecoDto(BigDecimal.valueOf(35.5));

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(precoDto)
                    .when()
                    .patch("/restaurantes/{id-restaurante}/itens/{id-item}/preco", idRestaurante, idItem)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // Valida atualização da descrição
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/restaurantes/{id-restaurante}/itens/{id-item}", idRestaurante, idItem)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("preco", equalTo(precoDto.preco().floatValue()));
        }

        @Test
        @DisplayName("Deve atualizar o disponibilidadeConsumoPresencial do item com sucesso")
        void deveAtualizarDisponibilidadeConsumoPresencialItemComSucesso() throws Exception {
            // Arrange
            UUID idRestaurante = UUID.fromString("21adec7d-d4f7-4999-a4dd-eaf0c242b3bd");
            UUID idItem = UUID.fromString("bce5f3c0-d4e4-4957-8f99-b95f12354d13");

            DisponibilidadeDto disponibilidade = new DisponibilidadeDto(true);

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(disponibilidade)
                    .when()
                    .patch("/restaurantes/{id-restaurante}/itens/{id-item}/disponibilidade-consumo-presencial", idRestaurante, idItem)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // Valida atualização da descrição
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/restaurantes/{id-restaurante}/itens/{id-item}", idRestaurante, idItem)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("isDisponivelConsumoPresencial", equalTo(true));
        }


        @Test
        @DisplayName("Deve atualizar o disponibilidade do item com sucesso")
        void deveAtualizarDisponibilidadeComSucesso() throws Exception {
            // Arrange
            UUID idRestaurante = UUID.fromString("21adec7d-d4f7-4999-a4dd-eaf0c242b3bd");
            UUID idItem = UUID.fromString("bce5f3c0-d4e4-4957-8f99-b95f12354d13");

            DisponibilidadeDto disponibilidade = new DisponibilidadeDto(true);

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(disponibilidade)
                    .when()
                    .patch("/restaurantes/{id-restaurante}/itens/{id-item}/disponibilidade", idRestaurante, idItem)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // Valida atualização da descrição
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/restaurantes/{id-restaurante}/itens/{id-item}", idRestaurante, idItem)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("isDisponivel", equalTo(true));
        }

        @Test
        @DisplayName("Deve atualizar a imagem do item com sucesso")
        void deveAtualizarImagemDoItemComSucesso() throws Exception {
            // Arrange
            UUID idRestaurante = UUID.fromString("21adec7d-d4f7-4999-a4dd-eaf0c242b3bd");
            UUID idItem = UUID.fromString("bce5f3c0-d4e4-4957-8f99-b95f12354d13");

            MockMultipartFile imagem = new MockMultipartFile(
                    "imagem",
                    "imagem_prato.jpg",
                    "image/jpeg",
                    "conteudo".getBytes());

            // Act & Assert
            given()
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .multiPart("imagem", "imagem_prato.jpg", "conteudo".getBytes(), "image/jpeg")
                    .when()
                    .patch("/restaurantes/{id-restaurante}/itens/{id-item}/imagem", idRestaurante, idItem)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        }

        @Test
        @DisplayName("Deve baixar a imagem do item com sucesso")
        void deveBaixarImagemDoItemComSucesso() throws Exception {
            // Arrange
            UUID idRestaurante = UUID.fromString("21adec7d-d4f7-4999-a4dd-eaf0c242b3bd");
            UUID idItem = UUID.fromString("bce5f3c0-d4e4-4957-8f99-b95f12354d13");

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/restaurantes/{id-restaurante}/itens/{id-item}/imagem/download", idRestaurante, idItem)
                    .then()
                    .statusCode(HttpStatus.OK.value());

        }

    }

}
