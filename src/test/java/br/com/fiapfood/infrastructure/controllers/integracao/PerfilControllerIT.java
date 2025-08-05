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
public class PerfilControllerIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Nested
    class BuscarTodosRequest {
        @DisplayName("Buscar todos os perfis cadastrados")
        @Test
        void deveRetornarListaComPerfisCadastrados() throws Exception {
            // Arrange

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/perfis")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", hasSize(4));
        }
    }

    @Nested
    class BuscarPorIdRequest {
        @DisplayName("Buscar perfil por id com sucesso")
        @Test
        void deveRetornarPerfilPorId() throws Exception {
            // Arrange
            int perfilId = 1;

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/perfis/{id}", perfilId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(perfilId))
                    .body("nome", equalTo("Dono"));
        }

        @DisplayName("Buscar perfil por id com erro. Perfil nao encontrado através do id")
        @Test
        void deveRetornarBadRequestSeNaoEncontrarPerfilPorId() throws Exception {
            // Arrange
            int perfilId = 100;

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/perfis/{id}", perfilId)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Não foi encontrado nenhum perfil com o id informado."));
        }
    }

    @Nested
    class CadastrarPerfilRequest {
        @DisplayName("Cadastrar perfil com sucesso.")
        @Test
        void deveCadastrarPerfilComSucesso() {
            // Arrange
            NomeDto perfilNome = new NomeDto("Funcionário");

            // Act & Assert - Cria perfil novo
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(perfilNome)
                    .when()
                    .post("/perfis")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());

            // Act & Assert - Valida perfil no BD
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/perfis")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("nome", hasItem("Funcionário"));
        }

        @DisplayName("Cadastrar perfil com erro. Nome de perfil ja cadastrado.")
        @Test
        void deveRetornarBadRequestSeNomeDePerfilJaCadstrado() {
            // Arrange
            NomeDto perfilNome = new NomeDto("Cliente");

            // Act & Assert
           given()
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .body(perfilNome)
                   .when()
                   .post("/perfis")
                   .then()
                   .statusCode(HttpStatus.BAD_REQUEST.value())
                   .body("$", hasKey("mensagem"))
                   .body("mensagem", equalTo("Já existe um perfil com o nome informado."));
        }

        @DisplayName("Cadastrar perfil com erro. Dados inválidos no DTO.")
        @Test
        void deveRetornarBadRequestSeDadosInvalidosNoDto() {
            // Arrange
            NomeDto perfilNome = new NomeDto("");

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(perfilNome)
                    .when()
                    .post("/perfis")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("$", hasKey("nome"))
                    .body("nome", equalTo("O campo nome precisa estar preenchido."));
        }
    }

    @Nested
    class AtualizarPerfilNomeRequest {
        @DisplayName("Atualizar nome do perfil com sucesso.")
        @Test
        void deveAtualizarNomeDoPerfilComSucesso() {
            // Arrange
            int perfilId = 1;
            NomeDto perfilNome = new NomeDto("Funcionário");

            // Act & Assert - Atualização do nome do perfil
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(perfilNome)
                    .when()
                    .patch("/perfis/{id}/nome", perfilId)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // Act & Assert - Valida se nome foi atualizado
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/perfis/{id}", perfilId)
                    .then()
                    .body("id", equalTo(perfilId))
                    .body("nome", equalTo(perfilNome.nome()));
        }

        @DisplayName("Atualizar nome do perfil com erro. Perfil não encontrado por id.")
        @Test
        void deveRetornarBadRequestSeNaoEncontrarPerfilPorId() {
            // Arrange
            int perfilId = 100;
            NomeDto perfilNome = new NomeDto("Funcionário");

            // Act & Assert - Atualização do perfil
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(perfilNome)
                    .when()
                    .patch("/perfis/{id}/nome", perfilId)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Não foi encontrado nenhum perfil com o id informado."));
        }

        @DisplayName("Atualizar nome do perfil com erro. Nome de perfil ja cadastrado.")
        @Test
        void deveRetornarBadRequestSeNomeDePerfilJaCadstrado() {
            // Arrange
            int perfilId = 1;
            NomeDto perfilNome = new NomeDto("Cliente");

            // Act & Assert - Atualização do perfil
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(perfilNome)
                    .when()
                    .patch("/perfis/{id}/nome", perfilId)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Já existe um perfil com o nome informado."));
        }

        @DisplayName("Atualizar nome do perfil com erro. Dados inválidos no DTO.")
        @Test
        void deveRetornarBadRequestSeDadosInvalidosNoDto() {
            // Arrange
            int perfilId = 1;
            NomeDto perfilNome = new NomeDto("");

            // Act & Assert - Atualização do perfil
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(perfilNome)
                    .when()
                    .patch("/perfis/{id}/nome", perfilId)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("nome", equalTo("O campo nome precisa estar preenchido."));
        }
    }

    @Nested
    class InativarPerfilRequest{
        @DisplayName("Inativar perfil com sucesso.")
        @Test
        void inativarPerfilComSucesso() {
            // Arrange
            int perfilId = 4;

            // Act & Assert - Inativa usuário
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .patch("/perfis/{id}/inativa", perfilId)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // Act & Assert - Valida inativação
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/perfis/{id}", perfilId)
                    .then()
                    .body("dataInativacao", notNullValue());
        }

        @DisplayName("Inativar perfil com erro. Perfil não encontrado por id")
        @Test
        void deveRetornarBadRequestSeNaoEncontarPerfilPorId() {
            // Arrange
            int perfilId = 100;

            // Act & Assert - Inativa usuário
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .patch("/perfis/{id}/inativa", perfilId)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Não foi encontrado nenhum perfil com o id informado."));
        }

        @DisplayName("Inativar perfil com erro. Usuarios registrados no perfil")
        @Test
        void deveRetornarBadRequestSeUsuariosEstiveremRegistradosNoPerfil() {
            // Arrange
            int perfilId = 1;

            // Act & Assert - Inativa usuário
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .patch("/perfis/{id}/inativa", perfilId)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Não é possível inativar o perfil pois há usuário ativo associado ao perfil."));
        }
    }

    @Nested
    class ReativarPerfilRequest{
        @DisplayName("Reativar perfil com sucesso.")
        @Test
        void reativarPerfilComSucesso() {
            // Arrange
            int perfilId = 3;

            // Act & Assert - Inativa usuário
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .patch("/perfis/{id}/reativa", perfilId)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // Act & Assert - Valida inativação
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/perfis/{id}", perfilId)
                    .then()
                    .body("dataInativacao", nullValue());
        }

        @DisplayName("Reativar perfil com erro. Perfil não encontrado por id")
        @Test
        void deveRetornarBadRequestSeNaoEncontarPerfilPorId() {
            // Arrange
            int perfilId = 100;

            // Act & Assert - Inativa usuário
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .patch("/perfis/{id}/reativa", perfilId)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Não foi encontrado nenhum perfil com o id informado."));
        }
    }
}
