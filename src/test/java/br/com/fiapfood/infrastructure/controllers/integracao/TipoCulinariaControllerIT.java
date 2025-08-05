package br.com.fiapfood.infrastructure.controllers.integracao;

import br.com.fiapfood.infraestructure.controllers.request.perfil.NomeDto;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class TipoCulinariaControllerIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Nested
    class BuscarTodosRequest {
        @DisplayName("Buscar todos tipo culinária com sucesso.")
        @Test
        void buscarTodosRequestComSucesso() {
            // Arrange
            
            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/tipos-culinaria")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", hasSize(5));
        }

        @DisplayName("Buscar todos tipo culinária com erro. Nenhum tipo cadastrado.")
        @Test
        @Sql(scripts = "/db_clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // Limpando o BD para retornar o erro
        void deveRetornarBadRequestSeNenhumTipoCadstrado() {
            // Arrange
            
            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/tipos-culinaria")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Não foi encontrado nenhum tipo de culinária na base de dados."));
        }
    }

    @Nested
    class BuscarPorIdRequest {
        @DisplayName("Buscar tipo culinária por id com sucesso.")
        @Test
        void buscarPorIdRequest() {
            // Arrange
            int tipoCulinariaId = 1;

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/tipos-culinaria/{id}", tipoCulinariaId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(1))
                    .body("nome", equalTo("Brasileira"));
        }

        @DisplayName("Buscar tipo culinária por id com erro. Tipo culinária não encontrado por id.")
        @Test
        void deveRetornarStatusBadRequestSeNaoEncontrarTipoCulinariaPorId() {
            // Arrange
            int tipoCulinariaId = 100;

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/tipos-culinaria/{id}", tipoCulinariaId)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Não foi encontrado nenhum tipo de culinária com o id informado."));
        }
    }

    @Nested
    class CadastrarRequest {
        @DisplayName("Cadastrar tipo culinária com sucesso.")
        @Test
        void deveCadastrarNovoTipoCulinariaComSucesso() {
            //Arrange
            NomeDto nomeTipoCulinaria = new NomeDto("Grega");

            // Act & Assert - Cadastra novo tipo culinária
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(nomeTipoCulinaria)
                    .when()
                    .post("/tipos-culinaria")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());

            // Act & Assert - Valida tipo culinária no BD
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/tipos-culinaria")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("nome", hasItem("Grega"));
        }

        @DisplayName("Cadastrar tipo culinária com erro. Nome já cadastrado.")
        @Test
        void deveRetornarStatusBadRequestSeNomeJaEstiverCadastrado() {
            //Arrange
            NomeDto nomeTipoCulinaria = new NomeDto("Brasileira");

            // Act & Assert - Cadastra novo tipo culinária
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(nomeTipoCulinaria)
                    .when()
                    .post("/tipos-culinaria")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Já existe um tipo de culinária com o nome informado."));
        }

        @DisplayName("Cadastrar tipo culinária com erro. DTO com dados inválidos.")
        @Test
        void deveRetornarStatusBadRequestSeDtoComDadosInvalidos() {
            //Arrange
            NomeDto nomeTipoCulinaria = new NomeDto("");

            // Act & Assert - Cadastra novo tipo culinária
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(nomeTipoCulinaria)
                    .when()
                    .post("/tipos-culinaria")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("nome", equalTo("O campo nome precisa estar preenchido."));
        }
    }

    @Nested
    class AtualizarRequest {
        @DisplayName("Atualizar nome tipo culinária com sucesso.")
        @Test
        void deveAtualizarNomeTipoCulinariaComSucesso() {
            //Arrange
            int tipoCulinariaId = 5;
            NomeDto nomeTipoCulinaria = new NomeDto("Vegana");

            // Act & Assert - Atualiza o nome
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(nomeTipoCulinaria)
                    .when()
                    .patch("/tipos-culinaria/{id}", tipoCulinariaId)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // Act & Assert - Valida atualização
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/tipos-culinaria/{id}", tipoCulinariaId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("nome", equalTo("Vegana"));
        }

        @DisplayName("Atualizar nome tipo culinária com erro. Novo nome ja cadastrado.")
        @Test
        void deveRetornarStatusBadRequestSeNovoNomeJaCadastrado() {
            //Arrange
            int tipoCulinariaId = 5;
            NomeDto nomeTipoCulinaria = new NomeDto("Churrasco");

            // Act & Assert - Atualiza o nome
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(nomeTipoCulinaria)
                    .when()
                    .patch("/tipos-culinaria/{id}", tipoCulinariaId)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Já existe um tipo de culinária com o nome informado."));
        }

        @DisplayName("Atualizar nome tipo culinária com erro. Tipo culinária nao encontrado por id.")
        @Test
        void deveRetornarStatusBadRequestSeNaoEncontrarTipoCulinariaPorId() {
            //Arrange
            int tipoCulinariaId = 100;
            NomeDto nomeTipoCulinaria = new NomeDto("Churrasco");

            // Act & Assert - Atualiza o nome
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(nomeTipoCulinaria)
                    .when()
                    .patch("/tipos-culinaria/{id}", tipoCulinariaId)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Já existe um tipo de culinária com o nome informado."));
        }

        @DisplayName("Atualizar nome tipo culinária com erro. DTO com dados inválidos.")
        @Test
        void deveRetornarStatusBadRequestSeDtoComDadosInvalidos() {
            //Arrange
            int tipoCulinariaId = 5;
            NomeDto nomeTipoCulinaria = new NomeDto("");

            // Act & Assert - Atualiza o nome
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(nomeTipoCulinaria)
                    .when()
                    .patch("/tipos-culinaria/{id}", tipoCulinariaId)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("nome", equalTo("O campo nome precisa estar preenchido."));
        }
    }
}
