package br.com.fiapfood.core.usecases.unitarios.perfil;

import br.com.fiapfood.core.entities.dto.perfil.PerfilCoreDto;
import br.com.fiapfood.core.exceptions.perfil.NomePerfilDuplicadoException;
import br.com.fiapfood.core.exceptions.perfil.PerfilInvalidoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.usecases.perfil.impl.AtualizarNomePerfilUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.IAtualizarNomePerfilUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static br.com.fiapfood.utils.CoreEntityDataGenerator.corePerfilEntityValido;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class AtualizarNomePerfilUseCaseTest {

    private final String PERFIL_DUPLICADO = "Já existe um perfil com o nome informado.";
    private final String PERFIS_NAO_ENCONTRADOS = "Não foi encontrado nenhum perfil com o id informado.";

    @Mock
    private IPerfilGateway perfilGateway;
    private IAtualizarNomePerfilUseCase atualizarNomePerfilUseCase;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        atualizarNomePerfilUseCase = new AtualizarNomePerfilUseCase(perfilGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @DisplayName("Deve atualizar nome do perfil com sucesso.")
    @Test
    void deveAtualizarNomePerfilComSucesso() {
        // Arrange
        int perfilId = 1;
        String novoNome = "Funcionario";
        var perfilRetornado = corePerfilEntityValido();

        when(perfilGateway.nomeJaCadastrado(anyString())).thenReturn(false); // Novo nome nao cadastrado
        when(perfilGateway.buscarPorId(anyInt())).thenReturn(perfilRetornado);

        ArgumentCaptor<PerfilCoreDto> captor = ArgumentCaptor.forClass(PerfilCoreDto.class);
        // Act
        atualizarNomePerfilUseCase.atualizar(perfilId,novoNome);

        // Assert
        assertThat(perfilRetornado.getNome()).isEqualTo(novoNome);
        verify(perfilGateway, times(1)).nomeJaCadastrado(anyString());
        verify(perfilGateway, times(1)).buscarPorId(anyInt());
        verify(perfilGateway, times(1)).salvar(captor.capture());
        Assertions.assertThat(captor.getValue()).isNotNull();
        Assertions.assertThat(captor.getValue().nome()).isEqualTo(novoNome);
        Assertions.assertThat(captor.getValue().dataCriacao()).isNotNull();
        Assertions.assertThat(captor.getValue().dataInativacao()).isNull();
        Assertions.assertThat(captor.getValue().id()).isNotNull();
        Assertions.assertThat(captor.getValue().id()).isEqualTo(perfilId);
    }

    @DisplayName("Deve atualizar nome do perfil com erro. Novo nome já cadastrado.")
    @Test
    void deveLancarExcecaoSeNovoNomeJaCadastrado() {
        // Arrange
        int perfilId = 1;
        String novoNome = "Funcionario";

        when(perfilGateway.nomeJaCadastrado(anyString())).thenReturn(true); // Novo nome nao cadastrado

        // Act & Assert
        assertThatThrownBy(() -> atualizarNomePerfilUseCase.atualizar(perfilId, novoNome))
                .isInstanceOf(NomePerfilDuplicadoException.class)
                .hasMessage(PERFIL_DUPLICADO);
        verify(perfilGateway, times(1)).nomeJaCadastrado(anyString());
        verify(perfilGateway, times(0)).buscarPorId(anyInt());
        verify(perfilGateway, times(0)).salvar(any(PerfilCoreDto.class));
    }

    @DisplayName("Deve atualizar nome do perfil com erro. Perfil náo encontrado através do ID.")
    @Test
    void deveLancarExcecaoSeNaoEncontrarPerfilAtravesDoId() {
        // Arrange
        int perfilId = 1;
        String novoNome = "Funcionario";

        when(perfilGateway.nomeJaCadastrado(anyString())).thenReturn(false); // Novo nome nao cadastrado
        when(perfilGateway.buscarPorId(anyInt())).thenThrow(new PerfilInvalidoException(PERFIS_NAO_ENCONTRADOS));

        //Act & Assert
        assertThatThrownBy(() -> atualizarNomePerfilUseCase.atualizar(perfilId, novoNome))
                .isInstanceOf(PerfilInvalidoException.class)
                .hasMessage(PERFIS_NAO_ENCONTRADOS);
        verify(perfilGateway, times(1)).nomeJaCadastrado(anyString());
        verify(perfilGateway, times(1)).buscarPorId(anyInt());
        verify(perfilGateway, times(0)).salvar(any(PerfilCoreDto.class));
    }
}
