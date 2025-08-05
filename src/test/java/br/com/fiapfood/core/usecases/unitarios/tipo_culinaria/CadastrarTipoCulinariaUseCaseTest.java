package br.com.fiapfood.core.usecases.unitarios.tipo_culinaria;

import br.com.fiapfood.core.entities.dto.tipo_culinaria.TipoCulinariaCoreDto;
import br.com.fiapfood.core.exceptions.perfil.NomePerfilDuplicadoException;
import br.com.fiapfood.core.exceptions.tipo_culinaria.NomeTipoCulinariaInvalidoException;
import br.com.fiapfood.core.gateways.interfaces.ITipoCulinariaGateway;
import br.com.fiapfood.core.usecases.tipo_culinaria.impl.CadastrarTipoCulinariaUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.ICadastrarTipoCulinariaUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CadastrarTipoCulinariaUseCaseTest {
    private final String PERFIL_DUPLICADO = "Já existe um tipo de culinária com o nome informado.";

    @Mock
    private ITipoCulinariaGateway tipoCulinariaGateway;
    private ICadastrarTipoCulinariaUseCase cadastrarTipoCulinariaUseCase;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        cadastrarTipoCulinariaUseCase = new CadastrarTipoCulinariaUseCase(tipoCulinariaGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @DisplayName("Deve cadastrar tipo culinária com sucesso.")
    @Test
    void deveCadastrarTipoCulinariaComSucesso() {
        // Arrange
        String novoNome = "Grega";

        when(tipoCulinariaGateway.nomeJaCadastrado(anyString())).thenReturn(false); // Nome não cadastrado

        ArgumentCaptor<TipoCulinariaCoreDto> captor = ArgumentCaptor.forClass(TipoCulinariaCoreDto.class);

        // Act
        cadastrarTipoCulinariaUseCase.cadastrar(novoNome);

        // Assert
        verify(tipoCulinariaGateway, times(1)).nomeJaCadastrado(anyString());
        verify(tipoCulinariaGateway, times(1)).salvar(captor.capture());
        assertThat(captor.getValue()).isNotNull();
        assertThat(captor.getValue().nome()).isEqualTo(novoNome);
        assertThat(captor.getValue().id()).isNull();
    }

    @DisplayName("Deve cadastrar tipo culinária com erro. Novo nome já cadastrado.")
    @Test
    void deveLancarExcecaoSeNomeJaCadastrado() {
        // Arrange
        String novoNome = "Grega";

        when(tipoCulinariaGateway.nomeJaCadastrado(anyString())).thenReturn(true); // Nome ja cadastrado

        // Act & Assert
        assertThatThrownBy(() -> cadastrarTipoCulinariaUseCase.cadastrar(novoNome))
                .isInstanceOf(NomePerfilDuplicadoException.class)
                .hasMessage(PERFIL_DUPLICADO);
        verify(tipoCulinariaGateway, times(1)).nomeJaCadastrado(anyString());
        verify(tipoCulinariaGateway, times(0)).salvar(any(TipoCulinariaCoreDto.class));
    }

    @DisplayName("Deve cadastrar tipo culinária com erro. Novo nome inválido")
    @Test
    void deveLancarExcecaoSeNomeForInvalido() {
        // Arrange
        String novoNome = "";

        when(tipoCulinariaGateway.nomeJaCadastrado(anyString())).thenReturn(false); // Nome não cadastrado

        // Act & Assert
        assertThatThrownBy(() -> cadastrarTipoCulinariaUseCase.cadastrar( novoNome))
                .isInstanceOf(NomeTipoCulinariaInvalidoException.class)
                .hasMessage("O nome do tipo de culinária é inválido.");
        verify(tipoCulinariaGateway, times(1)).nomeJaCadastrado(anyString());
        verify(tipoCulinariaGateway, times(0)).salvar(any(TipoCulinariaCoreDto.class));
    }
}
