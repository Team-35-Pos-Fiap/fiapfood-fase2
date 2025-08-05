package br.com.fiapfood.core.gateways.unitarios;

import br.com.fiapfood.core.entities.dto.perfil.PerfilCoreDto;
import br.com.fiapfood.core.exceptions.perfil.PerfilInvalidoException;
import br.com.fiapfood.core.exceptions.perfil.PerfilNaoEncontradoException;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.infraestructure.entities.PerfilEntity;
import br.com.fiapfood.infraestructure.repositories.interfaces.IPerfilRepository;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static br.com.fiapfood.utils.DtoDataGenerator.perfilCoreDtoValido;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PerfilGatewayTest {

    private final String PERFIL_NAO_ENCONTRADO = "Não foi encontrado nenhum perfil na base de dados.";
    private final String PERFIS_NAO_ENCONTRADOS = "Não foi encontrado nenhum perfil com o id informado.";

    @Mock
    private IPerfilRepository perfilRepository;
    private IPerfilGateway perfilGateway;

    AutoCloseable mock;

    @BeforeEach
    public void setup() {
        mock = MockitoAnnotations.openMocks(this);
        perfilGateway = new PerfilGateway(perfilRepository);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mock.close();
    }

    @Nested
    class BuscarTodos{
        @DisplayName("Deve buscar todos os perfis chamando o repository com sucesso.")
        @Test
        void deveBuscarTodosOsPerfisComSucesso(){
            // Arrange
            List<PerfilCoreDto> perfisRetornadosPeloRepository = List.of(
                    perfilCoreDtoValido(),
                    perfilCoreDtoValido(),
                    perfilCoreDtoValido(),
                    perfilCoreDtoValido()
            );

            when(perfilRepository.buscarTodos()).thenReturn(perfisRetornadosPeloRepository);

            // Act
            var perfis = perfilGateway.buscarTodos();

            // Assert
            assertThat(perfis).isNotNull();
            assertThat(perfis.size()).isEqualTo(perfisRetornadosPeloRepository.size());
            verify(perfilRepository).buscarTodos();
        }

        @DisplayName("Deve buscar todos os perfis chamando o repository com erro. Nenhum perfil encontrado.")
        @Test
        void deveLancarExcecaoSeNenhumPerfilRetornadoPeloGateway(){
            // Arrange
            List<PerfilCoreDto> perfisRetornadosPeloRepository = List.of();

            when(perfilRepository.buscarTodos()).thenReturn(perfisRetornadosPeloRepository);

            // Act & Assert
            assertThatThrownBy(() -> perfilGateway.buscarTodos())
                    .isInstanceOf(PerfilNaoEncontradoException.class)
                    .hasMessage(PERFIL_NAO_ENCONTRADO);
            verify(perfilRepository).buscarTodos();
        }
    }

    @Nested
    class BuscarPorId{
        @DisplayName("Deve buscar perfil por ID chamando o repository com sucesso.")
        @Test
        void deveBuscarPerfilPorIdComSucesso() {
            // Arrange
            int perfilId = 1;
            PerfilCoreDto perfilRetornadoPeloRepository = perfilCoreDtoValido();

            when(perfilRepository.buscarPorId(perfilId)).thenReturn(perfilRetornadoPeloRepository);

            // Act
            var perfil = perfilGateway.buscarPorId(perfilId);

            // Assert
            assertThat(perfil).isNotNull();
            assertThat(perfil.getId()).isEqualTo(perfilRetornadoPeloRepository.id());
            assertThat(perfil.getNome()).isEqualTo(perfilRetornadoPeloRepository.nome());
            verify(perfilRepository).buscarPorId(perfilId);
        }

        @DisplayName("Deve buscar perfil por ID chamando o repository com erro. Usuário nao encontrado.")
        @Test
        void deveLancarExcecaoSeNenhumPerfilRetornadoPeloGateway() {
            // Arrange
            int perfilId = 1;

            when(perfilRepository.buscarPorId(perfilId)).thenReturn(null);

            // Act & Assert
            assertThatThrownBy(() -> perfilGateway.buscarPorId(perfilId))
                    .isInstanceOf(PerfilInvalidoException.class)
                    .hasMessage(PERFIS_NAO_ENCONTRADOS);
            verify(perfilRepository).buscarPorId(perfilId);
        }
    }

    @Nested
    class Salvar{
        @DisplayName("Deve salvar perfil com sucesso.")
        @Test
        void deveSalvarPerfilComSucesso(){
            // Arrange
            PerfilCoreDto perfil = perfilCoreDtoValido();

            doNothing().when(perfilRepository).salvar(any(PerfilEntity.class));

            ArgumentCaptor<PerfilEntity> captor = ArgumentCaptor.forClass(PerfilEntity.class);

            // Act
            perfilGateway.salvar(perfil);

            // Assert
            verify(perfilRepository).salvar(captor.capture());

            var perfilSalvo = captor.getValue();

            assertThat(perfilSalvo).isNotNull();
            assertThat(perfilSalvo.getNome()).isEqualTo(perfil.nome());
            assertThat(perfilSalvo.getId()).isEqualTo(perfil.id());
        }
    }

    @Nested
    class NomeJaCadastrado{
        @DisplayName("Deve checar se nome de perfil já cadastrado chamando o repository com sucesso.")
        @Test
        void deveChecarNomePerfilJaCadastrado(){
            // Arrange
            String nomePerfil = "Funcionario";

            when(perfilRepository.nomeJaCadastrado(nomePerfil)).thenReturn(false);

            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

            // Act
            boolean result = perfilGateway.nomeJaCadastrado(nomePerfil);

            // Assert
            verify(perfilRepository).nomeJaCadastrado(captor.capture());
            assertThat(captor.getValue()).isEqualTo(nomePerfil);
            assertThat(result).isFalse();
        }
    }
}
