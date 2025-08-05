package br.com.fiapfood.core.usecases.unitarios.usuario;

import br.com.fiapfood.core.entities.dto.usuario.CadastrarUsuarioCoreDto;
import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioCoreDto;
import br.com.fiapfood.core.exceptions.perfil.PerfilInvalidoException;
import br.com.fiapfood.core.exceptions.usuario.EmailDuplicadoException;
import br.com.fiapfood.core.exceptions.usuario.EmailUsuarioInvalidoException;
import br.com.fiapfood.core.exceptions.usuario.MatriculaDuplicadaException;
import br.com.fiapfood.core.exceptions.usuario.NomeUsuarioInvalidoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.usuario.impl.CadastrarUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.ICadastrarUsuarioUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static br.com.fiapfood.utils.CoreEntityDataGenerator.corePerfilEntityValido;
import static br.com.fiapfood.utils.DtoDataGenerator.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class CadastrarUsuarioUseCaseTest {

    private final String EMAIL_DUPLICADO = "Já existe um usuário com o email informado.";
    private final String MATRICULA_DUPLICADA = "Já existe um usuário com a matrícula informada.";

    @Mock
    private IUsuarioGateway usuarioGateway;
    @Mock
    private IPerfilGateway perfilGateway;

    private ICadastrarUsuarioUseCase cadastrarUsuarioUseCase;

    AutoCloseable mock;

    @BeforeEach
    public void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        cadastrarUsuarioUseCase = new CadastrarUsuarioUseCase(usuarioGateway, perfilGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @DisplayName("Deve cadastrar usuário com sucesso.")
    @Test
    void deveCadastrarUsuarioComSucesso() {
        // Arrange
        CadastrarUsuarioCoreDto usuarioDto = cadastrarUsuarioCoreDtoValido();

        when(usuarioGateway.emailJaCadastrado(usuarioDto.email())).thenReturn(false); // e-mail não está cadastrado
        when(usuarioGateway.matriculaJaCadastrada(usuarioDto.dadosLogin().matricula())).thenReturn(false); // matricula não está cadastrado
        when(perfilGateway.buscarPorId(anyInt())).thenReturn(corePerfilEntityValido());

        ArgumentCaptor<DadosUsuarioCoreDto> captor = ArgumentCaptor.forClass(DadosUsuarioCoreDto.class);

        // Act
        cadastrarUsuarioUseCase.cadastrar(usuarioDto);

        // Assert
        verify(usuarioGateway).emailJaCadastrado(usuarioDto.email());
        verify(usuarioGateway).matriculaJaCadastrada(usuarioDto.dadosLogin().matricula());
        verify(perfilGateway).buscarPorId(anyInt());
        verify(usuarioGateway).salvar(captor.capture());

        DadosUsuarioCoreDto usuarioSalvo = captor.getValue();

        assertThat(usuarioSalvo).isNotNull();
        assertThat(usuarioSalvo.nome()).isEqualTo(usuarioDto.nome());
        assertThat(usuarioSalvo.email()).isEqualTo(usuarioDto.email());
        assertThat(usuarioSalvo.isAtivo()).isTrue();
    }

    @DisplayName("Deve cadastrar usuário com erro. E-mail já cadastrado")
    @Test
    void deveLancarExcecaoSeEmailJaCadastrado() {
        // Arrange
        CadastrarUsuarioCoreDto usuarioDto = cadastrarUsuarioCoreDtoValido();

        when(usuarioGateway.emailJaCadastrado(usuarioDto.email())).thenReturn(true); // e-mail já está cadastrado

        // Act & Assert
        assertThatThrownBy(() -> cadastrarUsuarioUseCase.cadastrar(usuarioDto))
                .isInstanceOf(EmailDuplicadoException.class)
                .hasMessage(EMAIL_DUPLICADO);
        verify(usuarioGateway).emailJaCadastrado(usuarioDto.email());
        verify(usuarioGateway, never()).matriculaJaCadastrada(usuarioDto.dadosLogin().matricula());
        verify(perfilGateway, never()).buscarPorId(anyInt());
        verify(usuarioGateway, never()).salvar(any(DadosUsuarioCoreDto.class));
    }

    @DisplayName("Deve cadastrar usuário com erro. Matricula já cadastrado")
    @Test
    void deveLancarExcecaoSeMatriculaJaCadastrada() {
        // Arrange
        CadastrarUsuarioCoreDto usuarioDto = cadastrarUsuarioCoreDtoValido();

        when(usuarioGateway.emailJaCadastrado(usuarioDto.email())).thenReturn(false); // e-mail não está cadastrado
        when(usuarioGateway.matriculaJaCadastrada(usuarioDto.dadosLogin().matricula())).thenReturn(true); // matricula já está cadastrado

        // Act & Assert
        assertThatThrownBy(() -> cadastrarUsuarioUseCase.cadastrar(usuarioDto))
                .isInstanceOf(MatriculaDuplicadaException.class)
                .hasMessage(MATRICULA_DUPLICADA);
        verify(usuarioGateway).emailJaCadastrado(usuarioDto.email());
        verify(usuarioGateway).matriculaJaCadastrada(usuarioDto.dadosLogin().matricula());
        verify(perfilGateway, never()).buscarPorId(anyInt());
        verify(usuarioGateway, never()).salvar(any(DadosUsuarioCoreDto.class));
    }

    @DisplayName("Deve cadastrar usuário com erro. Nome invalido.")
    @Test
    void deveLancarExcecaoSeNomeForInvalido() {
        // Arrange
        CadastrarUsuarioCoreDto usuarioDto = new CadastrarUsuarioCoreDto(
                "",
                1,
                loginCoreDtoValido(),
                "john.doe@email.com",
                dadosEnderecoCoreDtoValido()
        );

        when(usuarioGateway.emailJaCadastrado(usuarioDto.email())).thenReturn(false); // e-mail não está cadastrado
        when(usuarioGateway.matriculaJaCadastrada(usuarioDto.dadosLogin().matricula())).thenReturn(false); // matricula não está cadastrado

        // Act & Assert
        assertThatThrownBy(() -> cadastrarUsuarioUseCase.cadastrar(usuarioDto))
                .isInstanceOf(NomeUsuarioInvalidoException.class)
                .hasMessage("O nome do usuário informado é inválido.");
        verify(usuarioGateway).emailJaCadastrado(usuarioDto.email());
        verify(usuarioGateway).matriculaJaCadastrada(usuarioDto.dadosLogin().matricula());
        verify(perfilGateway, never()).buscarPorId(anyInt());
        verify(usuarioGateway, never()).salvar(any(DadosUsuarioCoreDto.class));
    }

    @DisplayName("Deve cadastrar usuário com erro. Perfil invalido.")
    @Test
    void deveLancarExcecaoSePerfilForInvalido() {
        // Arrange
        CadastrarUsuarioCoreDto usuarioDto = new CadastrarUsuarioCoreDto(
                "John Doe",
                null,
                loginCoreDtoValido(),
                "john.doe@email.com",
                dadosEnderecoCoreDtoValido()
        );

        when(usuarioGateway.emailJaCadastrado(usuarioDto.email())).thenReturn(false); // e-mail não está cadastrado
        when(usuarioGateway.matriculaJaCadastrada(usuarioDto.dadosLogin().matricula())).thenReturn(false); // matricula não está cadastrado

        // Act & Assert
        assertThatThrownBy(() -> cadastrarUsuarioUseCase.cadastrar(usuarioDto))
                .isInstanceOf(PerfilInvalidoException.class)
                .hasMessage("O perfil informado é inválido.");
        verify(usuarioGateway).emailJaCadastrado(usuarioDto.email());
        verify(usuarioGateway).matriculaJaCadastrada(usuarioDto.dadosLogin().matricula());
        verify(perfilGateway, never()).buscarPorId(anyInt());
        verify(usuarioGateway, never()).salvar(any(DadosUsuarioCoreDto.class));
    }

    @DisplayName("Deve cadastrar usuário com erro. E-mail invalido.")
    @Test
    void deveLancarExcecaoSeEmailForInvalido() {
        // Arrange
        CadastrarUsuarioCoreDto usuarioDto = new CadastrarUsuarioCoreDto(
                "John Doe",
                1,
                loginCoreDtoValido(),
                "",
                dadosEnderecoCoreDtoValido()
        );

        when(usuarioGateway.emailJaCadastrado(usuarioDto.email())).thenReturn(false); // e-mail não está cadastrado
        when(usuarioGateway.matriculaJaCadastrada(usuarioDto.dadosLogin().matricula())).thenReturn(false); // matricula não está cadastrado

        // Act & Assert
        assertThatThrownBy(() -> cadastrarUsuarioUseCase.cadastrar(usuarioDto))
                .isInstanceOf(EmailUsuarioInvalidoException.class)
                .hasMessage("O email do usuário informado é inválido.");
        verify(usuarioGateway).emailJaCadastrado(usuarioDto.email());
        verify(usuarioGateway).matriculaJaCadastrada(usuarioDto.dadosLogin().matricula());
        verify(perfilGateway, never()).buscarPorId(anyInt());
        verify(usuarioGateway, never()).salvar(any(DadosUsuarioCoreDto.class));
    }
}
