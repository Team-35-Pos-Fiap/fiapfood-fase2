package br.com.fiapfood.infrastructure.controllers.integracao;

import br.com.fiapfood.infraestructure.controllers.request.endereco.DadosEnderecoDto;
import br.com.fiapfood.infraestructure.controllers.request.login.LoginDto;
import br.com.fiapfood.infraestructure.controllers.request.login.MatriculaDto;
import br.com.fiapfood.infraestructure.controllers.request.login.SenhaDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.CadastrarUsuarioDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.DadosEmailDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.DadosNomeDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.DadosPerfilDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static br.com.fiapfood.utils.DtoDataGenerator.dadosEnderecoDtoValido;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UsuarioControllerIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Nested
    class CadastrarUsuarioRequest {
        @DisplayName("Cadastrar novo usuário com sucesso")
        @Test
        void devePermitirCadastrarUsuarioComSucesso() {
            // Arrange
            CadastrarUsuarioDto dadosUsuario = new CadastrarUsuarioDto(
                    "John Doe",
                    1,
                    new LoginDto(
                            null,
                            "us0010",
                            "123"
                    ),
                    "john.doe@email.com",
                    dadosEnderecoDtoValido()
            );

            //Act & Assert - Criando novo usuário
            given()
                    .contentType(ContentType.JSON)
                    .body(dadosUsuario)
                    .when()
                    .post("/usuarios")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());

            // Preciso validar se foi criado


        }

        @DisplayName("Cadastrar novo usuário com erro. Email já cadastrado")
        @Test
        void deveRetornarStatusBadRequestSeEmailJaCadastrado() {
            // Arrange
            CadastrarUsuarioDto dadosUsuarioComEmailCadastrado = new CadastrarUsuarioDto(
                    "John Doe",
                    1,
                    new LoginDto(
                            null,
                            "us0010",
                            "123"
                    ),
                    "thiago@fiapfood.com",
                    dadosEnderecoDtoValido()
            );

            //Act & Assert
            given()
                    .contentType(ContentType.JSON)
                    .body(dadosUsuarioComEmailCadastrado)
                    .when()
                    .post("/usuarios")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Já existe um usuário com o email informado."));
        }

        @DisplayName("Cadastrar novo usuário com erro. Matricula já cadastrada")
        @Test
        void deveRetornarStatusBadRequestSeMatriculaJaCadastrado() {
            // Arrange
            CadastrarUsuarioDto dadosUsuarioComMatriculaCadastrada = new CadastrarUsuarioDto(
                    "John Doe",
                    1,
                    new LoginDto(
                            null,
                            "us0001",
                            "123"
                    ),
                    "john.doe@fiapfood.com",
                    dadosEnderecoDtoValido()
            );

            //Act & Assert
            given()
                    .contentType(ContentType.JSON)
                    .body(dadosUsuarioComMatriculaCadastrada)
                    .when()
                    .post("/usuarios")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Já existe um usuário com a matrícula informada."));
        }

        @DisplayName("Cadastrar novo usuário com erro. Dados do usuário inválidos no DTO")
        @ParameterizedTest
        @CsvSource({
                " , thiago@fiapfood.com, 1, O campo nome precisa estar preenchido.",
                "Thiago Motta, , 1, O campo email precisa estar preenchido.",
                "Thiago Motta, thiago@fiapfood.com, ,  O campo perfil precisa estar preenchido.",
                "Thiago Motta, thiago@fiapfood.com, -1,  O campo perfil precisa ter valor maior do que 0."
        })
        void deveRetornarStatusBadRequestParaCamposDeUsuarioInvalidos(String nome, String email, Integer perfil, String expectedError) {
            // Arrange
            CadastrarUsuarioDto dadosUsuarioInvalidos = new CadastrarUsuarioDto(
                    nome,
                    perfil,
                    new LoginDto(
                            null,
                            "us0010",
                            "123"
                    ),
                    email,
                    dadosEnderecoDtoValido()
            );

            //Act & Assert
            given()
                    .contentType(ContentType.JSON)
                    .body(dadosUsuarioInvalidos)
                    .when()
                    .post("/usuarios")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("$", hasValue(expectedError));
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
        void deveRetornarStatusBadRequestParaCamposDeEnderecoInvalidos(String cidade, String cep, String bairro, String endereco, String estado, String expectedError) {
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
            CadastrarUsuarioDto dadosUsuarioInvalidos = new CadastrarUsuarioDto(
                    "John Doe",
                    1,
                    new LoginDto(
                            null,
                            "us0010",
                            "123"
                    ),
                    "john.doe@email.com",
                    dadosEnderecoInvalidos
            );

            //Act & Assert
            given()
                    .contentType(ContentType.JSON)
                    .body(dadosUsuarioInvalidos)
                    .when()
                    .post("/usuarios")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("$", hasValue(expectedError));
        }

        @DisplayName("Cadastrar novo usuário com erro. Dados do login inválidos no DTO")
        @ParameterizedTest
        @CsvSource({
                " , 123, O campo matricula precisa ser informado.",
                "us0001, , O campo senha precisa ser informado."
        })
        void deveRetornarStatusBadRequestParaCamposDeLoginInvalidos(String matricula, String senha, String expectedError) {
            // Arrange
            LoginDto dadosLoginInvalidos = new LoginDto(
                    null,
                    matricula,
                    senha
            );
            CadastrarUsuarioDto dadosUsuarioInvalidos = new CadastrarUsuarioDto(
                    "John Doe",
                    1,
                    dadosLoginInvalidos,
                    "john.doe@email.com",
                    dadosEnderecoDtoValido()
            );

            //Act & Assert
            given()
                    .contentType(ContentType.JSON)
                    .body(dadosUsuarioInvalidos)
                    .when()
                    .post("/usuarios")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("$", hasValue(expectedError));
        }

        @DisplayName("Cadastrar novo usuário com erro. Perfil informado não cadastrado.")
        @Test
        void deveRetornarStatusBadRequestSePerfilNaoCadstrado() {
            // Arrange
            CadastrarUsuarioDto dadosUsuarioInvalidos = new CadastrarUsuarioDto(
                    "John Doe",
                    100,
                    new LoginDto(
                            null,
                            "us0010",
                            "123"
                    ),
                    "john.doe@email.com",
                    dadosEnderecoDtoValido()
            );

            //Act & Assert
            given()
                    .contentType(ContentType.JSON)
                    .body(dadosUsuarioInvalidos)
                    .when()
                    .post("/usuarios")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("$", hasValue("Não foi encontrado nenhum perfil com o id informado."));
        }
    }

    @Nested
    class InativarUsuarioRequest {
        @DisplayName("Inativar usuário com sucesso.")
        @Test
        void devePermitirInativarUsuarioComSucesso() {
            // Arrange
            UUID idUsuarioAtivo = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");

            // Act & Assert - Inativa usuário
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .patch("/usuarios/{id}/status/inativa", idUsuarioAtivo)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("mensagem", equalTo("Usuário inativado com sucesso."));

            // Act & Assert - Valida inativação do usuário
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/usuarios/{id}", idUsuarioAtivo)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(idUsuarioAtivo.toString()))
                    .body("isAtivo", equalTo(false));
        }

        @DisplayName("Inativar usuário com erro. Usuário não encontrado.")
        @Test
        void deveRetornarStatusNotFoundSeNaoEncontrarUsuarioPorId() {
            // Arrange
            UUID idUsuarioInvalido = UUID.fromString("0fc5db14-7993-4564-bff9-c258b5c73c87");

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .patch("/usuarios/{id}/status/inativa", idUsuarioInvalido)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("mensagem", equalTo("Não foi encontrado nenhum usuário."));
        }

        @DisplayName("Inativar usuário com erro. Usuário já está inativo.")
        @Test
        void deveRetornarStatusBadRequestSeUsuarioJaEstiverInativo() {
            // Arrange
            UUID idUsuarioInativo = UUID.fromString("602a4056-68d0-44f0-8284-14b0cf7a75b6");

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .patch("/usuarios/{id}/status/inativa", idUsuarioInativo)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Não é possível inativar o usuário, pois ele já se encontra inativo."));
        }
    }

    @Nested
    class AtivarUsuarioRequest {
        @DisplayName("Ativar usuário com sucesso.")
        @Test
        void devePermitirAtivarUsuarioComSucesso() {
            // Arrange
            UUID idUsuarioInativo = UUID.fromString("602a4056-68d0-44f0-8284-14b0cf7a75b6");

            // Act & Assert - Inativa usuário
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .patch("/usuarios/{id}/status/reativa", idUsuarioInativo)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("mensagem", equalTo("Usuário reativado com sucesso."));

            // Act & Assert - Valida inativação do usuário
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/usuarios/{id}", idUsuarioInativo)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(idUsuarioInativo.toString()))
                    .body("isAtivo", equalTo(true));
        }

        @DisplayName("Ativar usuário com erro. Usuário não encontrado.")
        @Test
        void deveRetornarStatusNotFoundSeNaoEncontrarUsuarioPorId() {
            // Arrange
            UUID idUsuarioInvalido = UUID.fromString("0fc5db14-7993-4564-bff9-c258b5c73c87");

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .patch("/usuarios/{id}/status/reativa", idUsuarioInvalido)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("mensagem", equalTo("Não foi encontrado nenhum usuário."));
        }

        @DisplayName("Ativar usuário com erro. Usuário já está ativo.")
        @Test
        void deveRetornarStatusBadRequestSeUsuarioJaEstiverAtivo() {
            // Arrange
            UUID idUsuarioAtivo = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .patch("/usuarios/{id}/status/reativa", idUsuarioAtivo)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Não é possível reativar um usuário, pois ele já se encontra ativo."));
        }
    }

    @Nested
    class BuscarUsuarioPorIdRequest {
        @DisplayName("Busca usuário por id com sucesso.")
        @Test
        void devePermitirBuscarUsuarioPorId() {
            // Arrange
            UUID id = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/usuarios/{id}", id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(id.toString()))
                    .body("nome", equalTo("Thiago Motta"))
                    .body("email", equalTo("thiago@fiapfood.com"))
                    .body("isAtivo", equalTo(true));
        }

        @DisplayName("Busca usuário por id com erro. Usuário não encontrado.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioAtivoPorId() {
            // Arrange
            UUID id = UUID.fromString("0fc5db14-7993-4564-bff9-c258b5c73c78");

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/usuarios/{id}", id)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("mensagem", equalTo("Não foi encontrado nenhum usuário."));
        }
    }

    @Nested
    class BuscarTodosUsuariosRequest {
        @DisplayName("Buscar todos usuarios com sucesso")
        @Test
        void deveRetornarListaDeUsuariosPaginada() {
            // Arrange

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/usuarios")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", hasKey("usuarios"))
                    .body("$", hasKey("paginacao"));
        }

        @DisplayName("Buscar todos usuarios com erro. Pagina menor ou igual a zero")
        @Test
        void deveRetornarStatusBadRequestSePaginaMenorOuIgualAZero() {
            // Arrange
            int pagina = 0;

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/usuarios?pagina={pagina}", pagina)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("objeto", equalTo("O parâmetro página precisa ser maior do que 0."));
        }

        @DisplayName("Buscar todos usuarios com erro. Pagina nao contem nenhum usuario")
        @Test
        void deveRetornarStatusNotFoundSePaginaForMaiorDoQueOLimite() {
            // Arrange
            int pagina = 100;

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/usuarios?pagina={pagina}", pagina)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("mensagem", equalTo("Não foram encontrados usuários na base de dados para a página informada."));
        }
    }

    @Nested
    class AtualizarEmailRequest {

        @DisplayName("Atualizar email com sucesso")
        @Test
        void devePermitirAtualizarEmail() {
            // Arrange
            UUID id = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            DadosEmailDto novoEmail = new DadosEmailDto("john.doe@email.com");

            // Act & Assert - Atualiza email
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoEmail)
                    .when()
                    .patch("/usuarios/{id}/email", id)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // Act & Assert - Valida atualizaçao do email
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/usuarios/{id}", id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("email", equalTo(novoEmail.email()));
        }

        @DisplayName("Atualizar email com erro. Usuário nao encontrado.")
        @Test
        void deveRetornarStatisNotFoundSeNaoEncontrarUsuarioPorId() {
            // Arrange
            UUID id = UUID.fromString("0fc5db14-7993-4564-bff9-c258b5c73c78");
            DadosEmailDto novoEmail = new DadosEmailDto("john.doe@email.com");

            // Act & Assert - Atualiza email
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoEmail)
                    .when()
                    .patch("/usuarios/{id}/email", id)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("mensagem", equalTo("Não foi encontrado nenhum usuário."));
        }

        @DisplayName("Atualizar email com erro. Usuário encontrado inativo")
        @Test
        void deveRetornarStatusBadRequestSeNaoEncontrarUsuarioAtivoPorId() {
            // Arrange
            UUID idInativo = UUID.fromString("602a4056-68d0-44f0-8284-14b0cf7a75b6");
            DadosEmailDto novoEmail = new DadosEmailDto("john.doe@email.com");

            // Act & Assert - Atualiza email
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoEmail)
                    .when()
                    .patch("/usuarios/{id}/email", idInativo)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Não é possível alterar o email de um usuário inativo."));
        }

        @DisplayName("Atualizar email com erro. Novo email ja cadastrado.")
        @Test
        void deveRetornarStatusBadRequestSeEmailJaCadastrado() {
            // Arrange
            UUID id = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            DadosEmailDto novoEmail = new DadosEmailDto("rafael.santos@fiapfood.com");

            // Act & Assert - Atualiza email
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoEmail)
                    .when()
                    .patch("/usuarios/{id}/email", id)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Já existe um usuário com o email informado."));
        }

        @DisplayName("Atualizar email com erro. DTO com dados invalidos.")
        @ParameterizedTest
        @CsvSource({
                " , O campo email precisa estar preenchido.",
                "johndoe.email.com, O e-mail precisa ser válido"
        })
        void deveRetornarStatusBadRequestParaCamposInvalidosNoDto(String email, String expectedErrorMessage) {
            // Arrange
            UUID id = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            DadosEmailDto novoEmail = new DadosEmailDto(email);

            // Act & Assert - Atualiza email
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoEmail)
                    .when()
                    .patch("/usuarios/{id}/email", id)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("$", hasValue(expectedErrorMessage));
        }
    }

    @Nested
    class AtualizarPerfilRequest {
        @DisplayName("Atualizar perfil com sucesso.")
        @Test
        void devePermitirAtualizarPerfil() {
            // Arrange
            UUID id = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            DadosPerfilDto novoPerfilId = new DadosPerfilDto(2);

            // Act & Assert - Atualiza perfil
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoPerfilId)
                    .when()
                    .patch("/usuarios/{id}/perfil", id)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // Act & Assert - Valida atualizaçao do perfil
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/usuarios/{id}", id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("perfil.id", equalTo(novoPerfilId.idPerfil()));
        }

        @DisplayName("Atualizar perfil com erro. Usuário nao encontrado por id.")
        @Test
        void deveRetornarStatusNotFoundSeNaoEncontrarUsuarioPorId() {
            // Arrange
            UUID id = UUID.fromString("0fc5db14-7993-4564-bff9-c258b5c73c78");
            DadosPerfilDto novoPerfilId = new DadosPerfilDto(2);

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoPerfilId)
                    .when()
                    .patch("/usuarios/{id}/perfil", id)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("mensagem", equalTo("Não foi encontrado nenhum usuário."));
        }

        @DisplayName("Atualizar perfil com erro. Usuário encontrado esta inativo")
        @Test
        void deveRetornarStatusBadRequestSeUsuarioEstiverInativo() {
            // Arrange
            UUID idInativo = UUID.fromString("602a4056-68d0-44f0-8284-14b0cf7a75b6");
            DadosPerfilDto novoPerfilId = new DadosPerfilDto(2);

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoPerfilId)
                    .when()
                    .patch("/usuarios/{id}/perfil", idInativo)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Não é possível alterar o perfil de um usuário inativo."));
        }

        @DisplayName("Atualizar perfil com erro. Novo perfil é o mesmo do ja cadastrado.")
        @Test
        void deveRetornarStatusBadRequestSeNovoPerfilForOMesmoDoRegistrado() {
            // Arrange
            UUID id = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            DadosPerfilDto novoPerfilId = new DadosPerfilDto(1);

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoPerfilId)
                    .when()
                    .patch("/usuarios/{id}/perfil", id)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("O perfil selecionado é o mesmo que o usuário já possui."));
        }

        @DisplayName("Atualizar perfil com erro. DTO com dados invalidos.")
        @Test
        void deveRetornarStatusBadRequestParaCamposInvalidosNoDto() {

            // Arrange
            UUID id = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            DadosPerfilDto novoPerfilIdNulo = new DadosPerfilDto(null);
            DadosPerfilDto novoPerfilIdNegativo = new DadosPerfilDto(-1);

            // Act & Assert - Novo perfil nulo
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoPerfilIdNulo)
                    .when()
                    .patch("/usuarios/{id}/perfil", id)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("$", hasValue("O campo idPerfil precisa estar preenchido."));

            // Act & Assert - Novo peril id negativo
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoPerfilIdNegativo)
                    .when()
                    .patch("/usuarios/{id}/perfil", id)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("$", hasValue("O campo idPerfil precisa ter valor maior do que 0."));
        }
    }

    @Nested
    class AtualizarEnderecoRequest {
        @DisplayName("Atualizar endereco com sucesso.")
        @Test
        void devePermitirAtualizarEndereco() {
            // Arrange
            UUID idUsuario = UUID.fromString("b48bc2dc-fd87-462d-a8a6-6e74674d0338");
            DadosEnderecoDto novoEndereco = dadosEnderecoDtoValido();

            // Act & Assert - Atualiza o endereço
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoEndereco)
                    .when()
                    .patch("/usuarios/{id}/endereco", idUsuario)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // Act & Assert - Valida endereço atualizado
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/usuarios/{id}", idUsuario)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("endereco.cidade", equalTo(novoEndereco.cidade()))
                    .body("endereco.cep", equalTo(novoEndereco.cep()))
                    .body("endereco.bairro", equalTo(novoEndereco.bairro()))
                    .body("endereco.endereco", equalTo(novoEndereco.endereco()))
                    .body("endereco.estado", equalTo(novoEndereco.estado()))
                    .body("endereco.numero", equalTo(novoEndereco.numero()))
                    .body("endereco.complemento", equalTo(novoEndereco.complemento()));
        }

        @DisplayName("Atualizar endereço com erro. Usuario nao encontrado por id.")
        @Test
        void deveRetornarStatusNotFoundSeNaoEncontrarUsuarioPorId() {
            // Arrange
            UUID idUsuarioInvalido = UUID.fromString("b84bbc2dc-fd87-462d-a8a6-6e74674d033");
            DadosEnderecoDto novoEndereco = dadosEnderecoDtoValido();

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoEndereco)
                    .when()
                    .patch("/usuarios/{id}/endereco", idUsuarioInvalido)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("mensagem", equalTo("Não foi encontrado nenhum usuário."));
        }

        @DisplayName("Atualizar endereço com erro. Usuario encontrado esta inativo.")
        @Test
        void deveRetornarStatusBadRequestSeUsuarioEstiverInativo() {
            // Arrange
            UUID idUsuarioInativo = UUID.fromString("602a4056-68d0-44f0-8284-14b0cf7a75b6");
            DadosEnderecoDto novoEndereco = dadosEnderecoDtoValido();

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoEndereco)
                    .when()
                    .patch("/usuarios/{id}/endereco", idUsuarioInativo)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Não é possível alterar o endereço de um usuário inativo."));
        }

        @DisplayName("Atualizar endereço com erro. DTO com dados inválidos.")
        @ParameterizedTest
        @CsvSource({
                " , 24455450, Nova Cidade, Rua Aquidabã, Rio de Janeiro, O campo cidade precisa ser informado.",
                "São Gonçalo, , Nova Cidade, Rua Aquidabã, Rio de Janeiro, O campo cep precisa ser informado.",
                "São Gonçalo, 24455450, , Rua Aquidabã, Rio de Janeiro, O campo bairro precisa ser informado.",
                "São Gonçalo, 24455450, Nova Cidade, , Rio de Janeiro, O campo endereco precisa ser informado.",
                "São Gonçalo, 24455450, Nova Cidade, Rua Aquidabã, , O campo estado precisa ser informado."
        })
        void deveRetornarStatusBadRequestParaCamposDeEnderecoInvalidos(String cidade, String cep, String bairro, String endereco, String estado, String expectedError) {
            // Arrange
            UUID idUsuario = UUID.fromString("b48bc2dc-fd87-462d-a8a6-6e74674d0338");
            DadosEnderecoDto novoEndereco = new DadosEnderecoDto(
                    cidade,
                    cep,
                    bairro,
                    endereco,
                    estado,
                    79,
                    "Casa 8"
            );

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoEndereco)
                    .when()
                    .patch("/usuarios/{id}/endereco", idUsuario)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("$", hasValue(expectedError));
        }
    }

    @Nested
    class AtualizarNomeRequest {
        @DisplayName("Atualizar nome com sucesso.")
        @Test
        void devePermitirAtualizarNome() {
            // Arrange
            UUID idUsuario = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            DadosNomeDto novoNome = new DadosNomeDto("John Doe");

            // Act & Assert - Atualiza o nome
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoNome)
                    .when()
                    .patch("/usuarios/{id}/nome", idUsuario)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // Act & Assert - Valida nome atualizado
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/usuarios/{id}", idUsuario)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("nome", equalTo(novoNome.nome()));
        }

        @DisplayName("Atualizar nome com erro. Usuario nao encontrado por id.")
        @Test
        void deveRetornarStatusNotFoundSeNaoEncontrarUsuarioPorId() {
            // Arrange
            UUID idUsuarioInvalido = UUID.fromString("0fc5db14-7993-4564-bff9-c258b5c73c87");
            DadosNomeDto novoNome = new DadosNomeDto("John Doe");

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoNome)
                    .when()
                    .patch("/usuarios/{id}/nome", idUsuarioInvalido)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("mensagem", equalTo("Não foi encontrado nenhum usuário."));
        }

        @DisplayName("Atualizar nome com erro. Usuario encontrado esta inativo")
        @Test
        void deveRetornarStautsBadRequestSeUsuarioEncontradoEstiverInativo() {
            // Arrange
            UUID idUsuarioInativo = UUID.fromString("602a4056-68d0-44f0-8284-14b0cf7a75b6");
            DadosNomeDto novoNome = new DadosNomeDto("John Doe");

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoNome)
                    .when()
                    .patch("/usuarios/{id}/nome", idUsuarioInativo)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Não é possível alterar o nome de um usuário inativo."));
        }

        @DisplayName("Atualizar nome com erro. Novo nome igual ao atual.")
        @Test
        void deveRetornarStatusBadRequestSeNovoNomeForIgualAoAtual() {
            // Arrange
            UUID idUsuario = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            DadosNomeDto novoNome = new DadosNomeDto("Thiago Motta");

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoNome)
                    .when()
                    .patch("/usuarios/{id}/nome", idUsuario)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Não é possível alterar o nome do usuário, pois ele é igual ao nome do usuário."));
        }

        @DisplayName("Atualizar nome com erro. DTO com dados invalidos.")
        @ParameterizedTest
        @CsvSource({
                " , O campo nome precisa estar preenchido.",
                "Jo, O campo nome precisa ter entre 3 e 150 caracteres."
        })
        void deveRestornarStatusBadRequestParaCamposInvalidosNoDto(String nome, String expectedError) {
            // Arrange
            UUID idUsuario = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            DadosNomeDto novoNome = new DadosNomeDto(nome);

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoNome)
                    .when()
                    .patch("/usuarios/{id}/nome", idUsuario)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("$", hasValue(expectedError));
        }

        @DisplayName("Atualizar nome com erro. Novo nome com mais de 150 caracteres.")
        @Test
        void deveRestornarStatusBadRequestSeNomeNovoMaiorQue150Characters() {
            // Arrange
            UUID idUsuario = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            String nome = "A".repeat(151);
            DadosNomeDto novoNome = new DadosNomeDto(nome);

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novoNome)
                    .when()
                    .patch("/usuarios/{id}/nome", idUsuario)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("$", hasValue("O campo nome precisa ter entre 3 e 150 caracteres."));
        }
    }

    @Nested
    class AtualizarSenhaRequest {
        @DisplayName("Trocar senha com sucesso")
        @Test
        void devePermitirTrocarSenha() {
            // Arrange
            UUID usuarioId = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            SenhaDto novaSenhaDto = new SenhaDto("124");

            // Act & Assert - Atualiza senha
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novaSenhaDto)
                    .when()
                    .patch("/usuarios/{id}/senha", usuarioId)
                    .then()
                    .statusCode(HttpStatus.OK.value());

            // Act & Assert - Verifica senha atualizada
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/usuarios/{id}", usuarioId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("login.senha", equalTo(novaSenhaDto.senha()));

        }

        @DisplayName("Atualizar senha com erro. Usuario nao encontrado atraves do id")
        @Test
        void deveRetornarStatusNotFoundSeUsuarioNaoEncontradoAtravesDoId() {
            // Arrange
            UUID usuarioIdInvalido = UUID.fromString("0fc5db14-7993-4564-bff9-c258b5c73c78");
            SenhaDto novaSenhaDto = new SenhaDto("124");

            // Act & Assert - Atualiza senha
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novaSenhaDto)
                    .when()
                    .patch("/usuarios/{id}/senha", usuarioIdInvalido)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("mensagem", equalTo("Não foi encontrado nenhum usuário."));
        }

        @DisplayName("Atualizar senha com erro. Usuário encontrado esta inativo")
        @Test
        void deveRetornarStatusBadRequestSeUsuarioEstiverInativo() {
            // Arrange
            UUID usuarioIdInativo = UUID.fromString("602a4056-68d0-44f0-8284-14b0cf7a75b6");
            SenhaDto novaSenhaDto = new SenhaDto("124");

            // Act & Assert - Atualiza senha
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novaSenhaDto)
                    .when()
                    .patch("/usuarios/{id}/senha", usuarioIdInativo)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Não é possível alterar a senha, pois o usuário está inativo."));
        }

        @DisplayName("Alterar senha com erro. Dados inválidos no DTO")
        @Test
        void deveRetornarStatusBadRequestParaCamposInvalidos() {
            // Arrange
            UUID usuarioIdInativo = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            SenhaDto novaSenhaDto = new SenhaDto("");

            // Act & Assert - Atualiza senha
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novaSenhaDto)
                    .when()
                    .patch("/usuarios/{id}/senha", usuarioIdInativo)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("senha", equalTo("O campo senha precisa estar preenchido."));
        }
    }

    @Nested
    class AtualizarMatriculaRequest {
        @DisplayName("Trocar matricula com sucesso")
        @Test
        void devePermitirTrocarMatricula() {
            // Arrange
            UUID usuarioId = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            MatriculaDto novaMatriculaDto = new MatriculaDto("us0010");

            // Act & Assert - Atualiza matricula
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novaMatriculaDto)
                    .when()
                    .patch("/usuarios/{id}/matricula", usuarioId)
                    .then()
                    .statusCode(HttpStatus.OK.value());

            // Act & Assert - Verifica matricula atualizada
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/usuarios/{id}", usuarioId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("login.matricula", equalTo(novaMatriculaDto.matricula()));
        }

        @DisplayName("Atualizar matricula com erro. Usuário nao encontrado através do id.")
        @Test
        void deveRetornarStatusNotFoundSeUsuarioNaoEncontradoAtravesDoId() {
            // Arrange
            UUID usuarioIdInvalido = UUID.fromString("0fc5db14-7993-4564-bff9-c258b5c73c78");
            MatriculaDto novaMatriculaDto = new MatriculaDto("us0010");

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novaMatriculaDto)
                    .when()
                    .patch("/usuarios/{id}/matricula", usuarioIdInvalido)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("mensagem", equalTo("Não foi encontrado nenhum usuário."));
        }

        @DisplayName("Atualizar matricula com erro. Usuário encontrado está inativo")
        @Test
        void deveRetornarStatusBadRequestSeUsuarioEstiverInativo() {
            // Arrange
            UUID usuarioIdInativo = UUID.fromString("602a4056-68d0-44f0-8284-14b0cf7a75b6");
            MatriculaDto novaMatriculaDto = new MatriculaDto("us0010");

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novaMatriculaDto)
                    .when()
                    .patch("/usuarios/{id}/matricula", usuarioIdInativo)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Não é possível alterar a matrícula, pois o usuário está inativo."));
        }

        @DisplayName("Atualizar matricula com erro. Nova matricula ja cadastrada.")
        @Test
        void deveRetornarStatusBadRequestSeNovaMatriculaJaCadastrada() {
            // Arrange
            UUID usuarioIdInativo = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            MatriculaDto novaMatriculaDto = new MatriculaDto("us0002");

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novaMatriculaDto)
                    .when()
                    .patch("/usuarios/{id}/matricula", usuarioIdInativo)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Já existe um usuário com a matrícula informada."));
        }

        @DisplayName("Alterar matricula com erro. Dados inválidos no DTO")
        @Test
        void deveRetornarStatusBadRequestParaCamposInvalidos() {
            // Arrange
            UUID usuarioIdInativo = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            MatriculaDto novaMatriculaDto = new MatriculaDto("");

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(novaMatriculaDto)
                    .when()
                    .patch("/usuarios/{id}/matricula", usuarioIdInativo)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("matricula", equalTo("O campo matricula precisa estar preenchido."));
        }
    }

    @Nested
    class ValidarLoginRequest {
        @DisplayName("Validação de login com sucesso")
        @Test
        void devePermitirValidarLogin() {
            // Arrange
            LoginDto loginRecordRequest = new LoginDto(
                    UUID.fromString("c303266f-9d32-4dde-8f4c-d8ee13b24ae9"),
                    "us0001",
                    "123"
            );

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(loginRecordRequest)
                    .when()
                    .post("/usuarios/valida-acesso")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("mensagem", equalTo("Acesso liberado para o usuário Thiago Motta"));
        }

        @DisplayName("Validação de login com erro. Usuario não encontrado através de matrícula e senha.")
        @Test
        void deveRetornarStatusNotFoundSeUsuarioNaoEncontradoAtravesDaMatriculaESenha() {
            // Arrange
            LoginDto loginRecordRequest = new LoginDto(
                    UUID.fromString("c303266f-9d32-4dde-8f4c-d8ee13b24ae9"),
                    "us0001",
                    "124"
            );

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(loginRecordRequest)
                    .when()
                    .post("/usuarios/valida-acesso")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("mensagem", equalTo("Não foi encontrado nenhum usuário."));
        }

        @DisplayName("Validação de login com erro. Usuario encontrado está inativo")
        @Test
        void deveRetornarStatusBadRequestSeUsuarioEncontradoEstiverInativo() {
            // Arrange
            LoginDto loginRecordRequest = new LoginDto(
                    UUID.fromString("2b84cf36-9333-42af-b013-eccec84a2270"),
                    "us0003",
                    "123"
            );

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(loginRecordRequest)
                    .when()
                    .post("/usuarios/valida-acesso")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("mensagem", equalTo("Não é possível realizar o login para usuários inativos."));
        }

        @DisplayName("Validação de login com erro. Erro com dados no DTO")
        @ParameterizedTest
        @CsvSource({
                " , 123, O campo matricula precisa ser informado.",
                "us0001, , O campo senha precisa ser informado."
        })
        void deveRetornarStatusBadRequestParaCamposInvalidos(String matricula, String senha, String expectedErrorMessage) {
            // Arrange
            LoginDto loginRecordRequest = new LoginDto(
                    null,
                    matricula,
                    senha
            );

            // Act & Assert
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(loginRecordRequest)
                    .when()
                    .post("/usuarios/valida-acesso")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("$", hasValue(expectedErrorMessage));
        }
    }
}
