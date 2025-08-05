package br.com.fiapfood.core.usecases.unitarios.tipo_culinaria;

import br.com.fiapfood.core.entities.dto.tipo_culinaria.TipoCulinariaCoreDto;
import br.com.fiapfood.core.exceptions.perfil.NomePerfilDuplicadoException;
import br.com.fiapfood.core.exceptions.tipo_culinaria.NomeTipoCulinariaInvalidoException;
import br.com.fiapfood.core.exceptions.tipo_culinaria.TipoCulinariaInvalidoException;
import br.com.fiapfood.core.gateways.interfaces.ITipoCulinariaGateway;
import br.com.fiapfood.core.usecases.tipo_culinaria.impl.AtualizarNomeTipoCulinariaUseCase;
import br.com.fiapfood.core.usecases.tipo_culinaria.interfaces.IAtualizarNomeTipoCulinariaUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static br.com.fiapfood.utils.CoreEntityDataGenerator.coreTipoCulinariaEntityValido;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AtualizarNomeTipoCulinariaUseCaseTest {

    private final String PERFIL_DUPLICADO = "Já existe um tipo de culinária com o nome informado.";
    private final String TIPO_CULINARIA_NAO_ENCONTRADO = "Não foi encontrado nenhum tipo de culinária com o id informado.";

    @Mock
    private ITipoCulinariaGateway tipoCulinariaGateway;
    private IAtualizarNomeTipoCulinariaUseCase atualizarNomeTipoCulinariaUseCase;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        atualizarNomeTipoCulinariaUseCase = new AtualizarNomeTipoCulinariaUseCase(tipoCulinariaGateway);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @DisplayName("Deve atualizar nome do tipo culinária com sucesso.")
    @Test
    void deveAtualizarNomeTipoCulinariaComSucesso() {
        // Arrange
        int tipoCulinariaId = 1;
        String novoNome = "Grega";
        var tipoCulinariaRetornado = coreTipoCulinariaEntityValido();

        when(tipoCulinariaGateway.nomeJaCadastrado(anyString())).thenReturn(false); // Nome não cadastrado
        when(tipoCulinariaGateway.buscarPorId(anyInt())).thenReturn(tipoCulinariaRetornado);

        ArgumentCaptor<TipoCulinariaCoreDto> captor = ArgumentCaptor.forClass(TipoCulinariaCoreDto.class);

        // Act
        atualizarNomeTipoCulinariaUseCase.atualizar(tipoCulinariaId, novoNome);

        // Assert
        verify(tipoCulinariaGateway, times(1)).nomeJaCadastrado(anyString());
        verify(tipoCulinariaGateway, times(1)).buscarPorId(anyInt());
        verify(tipoCulinariaGateway, times(1)).salvar(captor.capture());
        assertThat(tipoCulinariaRetornado.getNome()).isEqualTo(novoNome);
        assertThat(captor.getValue()).isNotNull();
        assertThat(captor.getValue().nome()).isEqualTo(novoNome);
    }

    @DisplayName("Deve atualizar nome do tipo culinária com erro. Tipo culinária não encontrado através do ID.")
    @Test
    void deveLancarExcecaoSeTipoCulinariaNaoEncontradoAtravesDoID() {
        // Arrange
        int tipoCulinariaId = 1;
        String novoNome = "Grega";

        when(tipoCulinariaGateway.nomeJaCadastrado(anyString())).thenReturn(false); // Nome não cadastrado
        when(tipoCulinariaGateway.buscarPorId(anyInt())).thenThrow(new TipoCulinariaInvalidoException(TIPO_CULINARIA_NAO_ENCONTRADO));

        // Act & Assert
        assertThatThrownBy(() -> atualizarNomeTipoCulinariaUseCase.atualizar(tipoCulinariaId, novoNome))
                .isInstanceOf(TipoCulinariaInvalidoException.class)
                .hasMessage(TIPO_CULINARIA_NAO_ENCONTRADO);
        verify(tipoCulinariaGateway, times(1)).nomeJaCadastrado(anyString());
        verify(tipoCulinariaGateway, times(1)).buscarPorId(anyInt());
        verify(tipoCulinariaGateway, times(0)).salvar(any(TipoCulinariaCoreDto.class));
    }

    @DisplayName("Deve atualizar nome do tipo culinária com erro. Novo nome já cadastrado.")
    @Test
    void deveLancarExcecaoSeNovoNomeJaCadastrado() {
        // Arrange
        int tipoCulinariaId = 1;
        String novoNome = "Grega";

        when(tipoCulinariaGateway.nomeJaCadastrado(anyString())).thenReturn(true); // Nome ja cadastrado

        // Act & Assert
        assertThatThrownBy(() -> atualizarNomeTipoCulinariaUseCase.atualizar(tipoCulinariaId, novoNome))
                .isInstanceOf(NomePerfilDuplicadoException.class)
                .hasMessage(PERFIL_DUPLICADO);
        verify(tipoCulinariaGateway, times(1)).nomeJaCadastrado(anyString());
        verify(tipoCulinariaGateway, times(0)).buscarPorId(anyInt());
        verify(tipoCulinariaGateway, times(0)).salvar(any(TipoCulinariaCoreDto.class));
    }

    @DisplayName("Deve atualizar nome do tipo culinária com erro. Novo nome inválido")
    @Test
    void deveLancarExcecaoSeNovoNomeForInvalido() {
        // Arrange
        int tipoCulinariaId = 1;
        String novoNome = "";
        var tipoCulinariaRetornado = coreTipoCulinariaEntityValido();

        when(tipoCulinariaGateway.nomeJaCadastrado(anyString())).thenReturn(false); // Nome não cadastrado
        when(tipoCulinariaGateway.buscarPorId(anyInt())).thenReturn(tipoCulinariaRetornado);

        // Act & Assert
        assertThatThrownBy(() -> atualizarNomeTipoCulinariaUseCase.atualizar(tipoCulinariaId, novoNome))
                .isInstanceOf(NomeTipoCulinariaInvalidoException.class)
                .hasMessage("O nome do tipo de culinária é inválido.");
        verify(tipoCulinariaGateway, times(1)).nomeJaCadastrado(anyString());
        verify(tipoCulinariaGateway, times(1)).buscarPorId(anyInt());
        verify(tipoCulinariaGateway, times(0)).salvar(any(TipoCulinariaCoreDto.class));
    }
}
