package br.com.fiapfood.core.usecases.integracao.usuario;

import br.com.fiapfood.core.entities.dto.login.LoginCoreDto;
import br.com.fiapfood.core.entities.dto.usuario.CadastrarUsuarioCoreDto;
import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioInputDto;
import br.com.fiapfood.core.exceptions.perfil.PerfilInvalidoException;
import br.com.fiapfood.core.exceptions.usuario.EmailDuplicadoException;
import br.com.fiapfood.core.exceptions.usuario.EmailUsuarioInvalidoException;
import br.com.fiapfood.core.exceptions.usuario.MatriculaDuplicadaException;
import br.com.fiapfood.core.exceptions.usuario.NomeUsuarioInvalidoException;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.impl.UsuarioGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.usuario.impl.CadastrarUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.ICadastrarUsuarioUseCase;
import br.com.fiapfood.infraestructure.repositories.interfaces.IPerfilRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.IUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static br.com.fiapfood.utils.DtoDataGenerator.dadosEnderecoCoreDtoValido;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CadastrarUsuarioUseCaseIT {

    private final String EMAIL_DUPLICADO = "Já existe um usuário com o email informado.";
    private final String MATRICULA_DUPLICADA = "Já existe um usuário com a matrícula informada.";

    private ICadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    private IUsuarioGateway usuarioGateway;
    private IPerfilGateway perfilGateway;

    @Autowired
    IPerfilRepository perfilRepository;
    @Autowired
    IUsuarioRepository usuarioRepository;

    @BeforeEach
    public void setUp() {
        perfilGateway = new PerfilGateway(perfilRepository);
        usuarioGateway = new UsuarioGateway(usuarioRepository);

        cadastrarUsuarioUseCase = new CadastrarUsuarioUseCase(usuarioGateway, perfilGateway);
    }

    @DisplayName("Deve cadastrar usuário com sucesso.")
    @Test
    void deveCadastrarUsuarioComSucesso() {
        // Arrange
        CadastrarUsuarioCoreDto usuarioDto = new CadastrarUsuarioCoreDto(
                "John Doe",
                1,
                new LoginCoreDto(
                        null,
                        "us0010",
                        "123"
                ),
                "john.doe@email.com",
                dadosEnderecoCoreDtoValido()
        );

        var listaDeUsuariosAntesDoCadastro = buscarTodosUsuarios();
        boolean usuarioRegistradoAntes =  listaDeUsuariosAntesDoCadastro.stream()
                .anyMatch(usuario -> usuario.nome().equals(usuarioDto.nome()));

        // Act
        cadastrarUsuarioUseCase.cadastrar(usuarioDto);
        var listaDeUsuariosAposCadastro = buscarTodosUsuarios();
        boolean usuarioRegistradoDepois =  listaDeUsuariosAposCadastro.stream()
                .anyMatch(usuario -> usuario.nome().equals(usuarioDto.nome()));

        // Assert
        assertThat(listaDeUsuariosAposCadastro.size()).isEqualTo(listaDeUsuariosAntesDoCadastro.size() + 1);
        assertThat(usuarioRegistradoDepois).isNotEqualTo(usuarioRegistradoAntes);
        assertThat(usuarioRegistradoDepois).isTrue();
    }

    @DisplayName("Deve cadastrar usuário com erro. E-mail já cadastrado")
    @Test
    void deveLancarExcecaoSeEmailJaCadastrado() {
        // Arrange
        CadastrarUsuarioCoreDto usuarioDto = new CadastrarUsuarioCoreDto(
                "",
                1,
                new LoginCoreDto(
                        null,
                        "us0010",
                        "123"
                ),
                "thiago@fiapfood.com",
                dadosEnderecoCoreDtoValido()
        );

        // Act & Assert
        assertThatThrownBy(() -> cadastrarUsuarioUseCase.cadastrar(usuarioDto))
                .isInstanceOf(EmailDuplicadoException.class)
                .hasMessage(EMAIL_DUPLICADO);
    }

    @DisplayName("Deve cadastrar usuário com erro. Matricula já cadastrado")
    @Test
    void deveLancarExcecaoSeMatriculaJaCadastrada() {
        // Arrange
        CadastrarUsuarioCoreDto usuarioDto = new CadastrarUsuarioCoreDto(
                "",
                1,
                new LoginCoreDto(
                        null,
                        "us0001",
                        "123"
                ),
                "john.doe@email.com",
                dadosEnderecoCoreDtoValido()
        );

        // Act & Assert
        assertThatThrownBy(() -> cadastrarUsuarioUseCase.cadastrar(usuarioDto))
                .isInstanceOf(MatriculaDuplicadaException.class)
                .hasMessage(MATRICULA_DUPLICADA);
    }

    @DisplayName("Deve cadastrar usuário com erro. Nome invalido.")
    @Test
    void deveLancarExcecaoSeNomeForInvalido() {
        // Arrange
        CadastrarUsuarioCoreDto usuarioDto = new CadastrarUsuarioCoreDto(
                "",
                1,
                new LoginCoreDto(
                        null,
                        "us0010",
                        "123"
                ),
                "john.doe@email.com",
                dadosEnderecoCoreDtoValido()
        );

        // Act & Assert
        assertThatThrownBy(() -> cadastrarUsuarioUseCase.cadastrar(usuarioDto))
                .isInstanceOf(NomeUsuarioInvalidoException.class)
                .hasMessage("O nome do usuário informado é inválido.");
    }

    @DisplayName("Deve cadastrar usuário com erro. Perfil invalido.")
    @Test
    void deveLancarExcecaoSePerfilForInvalido() {
        // Arrange
        CadastrarUsuarioCoreDto usuarioDto = new CadastrarUsuarioCoreDto(
                "John Doe",
                null,
                new LoginCoreDto(
                        null,
                        "us0010",
                        "123"
                ),
                "john.doe@email.com",
                dadosEnderecoCoreDtoValido()
        );

        // Act & Assert
        assertThatThrownBy(() -> cadastrarUsuarioUseCase.cadastrar(usuarioDto))
                .isInstanceOf(PerfilInvalidoException.class)
                .hasMessage("O perfil informado é inválido.");
    }

    @DisplayName("Deve cadastrar usuário com erro. E-mail invalido.")
    @Test
    void deveLancarExcecaoSeEmailForInvalido() {
        // Arrange
        CadastrarUsuarioCoreDto usuarioDto = new CadastrarUsuarioCoreDto(
                "John Doe",
                1,
                new LoginCoreDto(
                        null,
                        "us0010",
                        "123"
                ),
                "",
                dadosEnderecoCoreDtoValido()
        );

        // Act & Assert
        assertThatThrownBy(() -> cadastrarUsuarioUseCase.cadastrar(usuarioDto))
                .isInstanceOf(EmailUsuarioInvalidoException.class)
                .hasMessage("O email do usuário informado é inválido.");
    }

    // Helper method para criar lista com todos os usuários registrados levando em conta a paginação.
    private List<DadosUsuarioInputDto> buscarTodosUsuarios() {
        int pagina = 1;
        int totalPaginas;
        List<DadosUsuarioInputDto> todosUsuarios = new ArrayList<>();

        do {
            var paginaDto = usuarioGateway.buscarTodos(pagina);
            todosUsuarios.addAll(paginaDto.usuarios());
            totalPaginas = paginaDto.paginacao().totalPaginas();
            pagina++;
        } while (pagina <= totalPaginas);

        return todosUsuarios;
    }
}
