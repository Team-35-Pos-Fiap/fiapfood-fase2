package br.com.fiapfood.core.usecases.unitarios.perfil;

import br.com.fiapfood.core.entities.dto.perfil.PerfilCoreDto;
import br.com.fiapfood.core.exceptions.perfil.NomePerfilDuplicadoException;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.usecases.perfil.impl.CadastrarPerfilUseCase;
import br.com.fiapfood.core.usecases.perfil.interfaces.ICadastrarPerfilUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CadastrarPerfilUseCaseTest {

    private final String PERFIL_DUPLICADO = "Já existe um perfil com o nome informado.";

    @Mock
    private IPerfilGateway perfilGateway;
    private ICadastrarPerfilUseCase cadastrarPerfilUseCase;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        cadastrarPerfilUseCase = new CadastrarPerfilUseCase(perfilGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @DisplayName("Deve cadastrar perfil com sucesso.")
    @Test
    void deveCadastrarPerfilComSucesso() {
        // Arrange
        String novoNome = "Funcionario";

        when(perfilGateway.nomeJaCadastrado(anyString())).thenReturn(false); // Novo nome nao cadastrado

        ArgumentCaptor<PerfilCoreDto> captor = ArgumentCaptor.forClass(PerfilCoreDto.class);
        // Act
        cadastrarPerfilUseCase.cadastrar(novoNome);

        // Assert
        verify(perfilGateway, times(1)).nomeJaCadastrado(anyString());
        verify(perfilGateway, times(1)).salvar(captor.capture());
        assertThat(captor.getValue()).isNotNull();
        assertThat(captor.getValue().nome()).isEqualTo(novoNome);
        assertThat(captor.getValue().dataCriacao()).isNotNull();
        assertThat(captor.getValue().dataInativacao()).isNull();
        assertThat(captor.getValue().id()).isNull();
    }

    @DisplayName("Deve cadastrar perfil com erro. Novo nome já cadastrado.")
    @Test
    void deveLancarExcecaoSeNovoNomeJaCadastrado() {
        // Arrange
        String novoNome = "Funcionario";

        when(perfilGateway.nomeJaCadastrado(anyString())).thenReturn(true); // Novo nome cadastrado

        // Act & Assert
        assertThatThrownBy(() -> cadastrarPerfilUseCase.cadastrar(novoNome))
                .isInstanceOf(NomePerfilDuplicadoException.class)
                .hasMessage(PERFIL_DUPLICADO);
        verify(perfilGateway, times(1)).nomeJaCadastrado(anyString());
        verify(perfilGateway, times(0)).salvar(any(PerfilCoreDto.class));
    }
}
