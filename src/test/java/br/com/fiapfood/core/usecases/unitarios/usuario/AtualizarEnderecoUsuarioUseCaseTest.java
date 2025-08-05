package br.com.fiapfood.core.usecases.unitarios.usuario;

import br.com.fiapfood.core.entities.dto.endereco.DadosEnderecoCoreDto;
import br.com.fiapfood.core.entities.dto.usuario.DadosUsuarioCoreDto;
import br.com.fiapfood.core.exceptions.usuario.UsuarioInativoException;
import br.com.fiapfood.core.exceptions.usuario.UsuarioNaoEncontradoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.usuario.impl.AtualizarEnderecoUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarEnderecoUsuarioUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static br.com.fiapfood.utils.CoreEntityDataGenerator.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class AtualizarEnderecoUsuarioUseCaseTest {

    private final String USUARIO_INATIVO = "Não é possível alterar o endereço de um usuário inativo.";
    private final String USUARIO_NAO_ENCONTRADO = "Não foi encontrado nenhum usuário.";

    @Mock
    private IUsuarioGateway usuarioGateway;
    @Mock
    private IPerfilGateway perfilGateway;

    private IAtualizarEnderecoUsuarioUseCase atualizarEnderecoUsuarioUseCase;

    AutoCloseable mock;

    @BeforeEach
    public void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        atualizarEnderecoUsuarioUseCase = new AtualizarEnderecoUsuarioUseCase(usuarioGateway, perfilGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @DisplayName("Deve atualizar o endereço do usuário com sucesso.")
    @Test
    void deveAtualizarEnderecoDoUsuarioComSucesso() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        DadosEnderecoCoreDto novoEndereco = new DadosEnderecoCoreDto(
                "Nova Cidade",
                "12345678",
                "Novo Bairro",
                "Nova Rua",
                "Novo Estado",
                100,
                "1"
        );

        var usuarioExistente = coreUsuarioEntityAtivoValido();

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(usuarioExistente);
        when(perfilGateway.buscarPorId(anyInt())).thenReturn(corePerfilEntityValido());

        ArgumentCaptor<DadosUsuarioCoreDto> captor = ArgumentCaptor.forClass(DadosUsuarioCoreDto.class);

        // Act
        atualizarEnderecoUsuarioUseCase.atualizar(usuarioId,novoEndereco);

        // Assert
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(perfilGateway).buscarPorId(anyInt());
        verify(usuarioGateway).salvar(captor.capture());

        DadosUsuarioCoreDto usuarioSalvo = captor.getValue();

        assertThat(usuarioSalvo).isNotNull();
        assertThat(usuarioSalvo.id()).isEqualTo(usuarioExistente.getId());
        assertThat(usuarioSalvo.endereco().cidade()).isEqualTo(novoEndereco.cidade());
        assertThat(usuarioSalvo.endereco().cep()).isEqualTo(novoEndereco.cep());
        assertThat(usuarioSalvo.endereco().bairro()).isEqualTo(novoEndereco.bairro());
        assertThat(usuarioSalvo.endereco().estado()).isEqualTo(novoEndereco.estado());
        assertThat(usuarioSalvo.endereco().endereco()).isEqualTo(novoEndereco.endereco());
        assertThat(usuarioSalvo.endereco().complemento()).isEqualTo(novoEndereco.complemento());
        assertThat(usuarioSalvo.endereco().numero()).isEqualTo(novoEndereco.numero());
    }

    @DisplayName("Deve atualizar o endereço do usuário com erro. Usuário não encontrado através do ID")
    @Test
    void deveLancarExcecaoSeNaoEncontrarUsuarioAtravesDoID() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        DadosEnderecoCoreDto novoEndereco = new DadosEnderecoCoreDto(
                "Nova Cidade",
                "12345678",
                "Novo Bairro",
                "Nova Rua",
                "Novo Estado",
                100,
                "1"
        );

        when(usuarioGateway.buscarPorId(usuarioId)).thenThrow(new UsuarioNaoEncontradoException(USUARIO_NAO_ENCONTRADO));

        // Act & Assert
        assertThatThrownBy(() -> atualizarEnderecoUsuarioUseCase.atualizar(usuarioId, novoEndereco))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(USUARIO_NAO_ENCONTRADO);
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(perfilGateway, never()).buscarPorId(anyInt());
        verify(usuarioGateway, never()).salvar(any(DadosUsuarioCoreDto.class));
    }

    @DisplayName("Deve atualizar o endereço do usuário com erro. Usuário encontrado está inativo")
    @Test
    void deveLancarExcecaoSeUsuarioEstiverInativo() {
        // Arrange
        UUID usuarioId = UUID.randomUUID();
        DadosEnderecoCoreDto novoEndereco = new DadosEnderecoCoreDto(
                "Nova Cidade",
                "12345678",
                "Novo Bairro",
                "Nova Rua",
                "Novo Estado",
                100,
                "1"
        );

        var usuarioExistente = coreUsuarioEntityInativoValido();

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(usuarioExistente);

        // Act & Assert
        assertThatThrownBy(() -> atualizarEnderecoUsuarioUseCase.atualizar(usuarioId, novoEndereco))
                .isInstanceOf(UsuarioInativoException.class)
                .hasMessage(USUARIO_INATIVO);
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(perfilGateway, never()).buscarPorId(anyInt());
        verify(usuarioGateway, never()).salvar(any(DadosUsuarioCoreDto.class));
    }
}
