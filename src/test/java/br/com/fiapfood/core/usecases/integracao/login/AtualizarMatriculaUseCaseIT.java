package br.com.fiapfood.core.usecases.integracao.login;

import br.com.fiapfood.core.entities.Usuario;
import br.com.fiapfood.core.exceptions.usuario.MatriculaDuplicadaException;
import br.com.fiapfood.core.exceptions.usuario.UsuarioInativoException;
import br.com.fiapfood.core.exceptions.usuario.UsuarioNaoEncontradoException;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.impl.UsuarioGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.login.impl.AtualizarMatriculaUseCase;
import br.com.fiapfood.core.usecases.login.interfaces.IAtualizarMatriculaUseCase;
import br.com.fiapfood.infraestructure.repositories.interfaces.IPerfilRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.IUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class AtualizarMatriculaUseCaseIT {

    private final String USUARIO_INATIVO = "Não é possível alterar a matrícula, pois o usuário está inativo.";
    private final String MATRICULA_DUPLICADA = "Já existe um usuário com a matrícula informada.";
    private final String USUARIO_NAO_ENCONTRADO = "Não foi encontrado nenhum usuário.";

    private IAtualizarMatriculaUseCase atualizarMatriculaUseCase;
    private IPerfilGateway perfilGateway;
    private IUsuarioGateway usuarioGateway;

    @Autowired
    IUsuarioRepository usuarioRepository;
    @Autowired
    IPerfilRepository perfilRepository;

    @BeforeEach
    void setUp() {
        perfilGateway = new PerfilGateway(perfilRepository);
        usuarioGateway = new UsuarioGateway(usuarioRepository);

        atualizarMatriculaUseCase = new AtualizarMatriculaUseCase(usuarioGateway, perfilGateway);
    }

    @DisplayName("Deve atualizar a matricula com sucesso")
    @Test
    void deveAtualizarMatriculaComSucesso(){
        // Arrange
        UUID usuarioId = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
        String novaMatricula = "us0010";

        Usuario usuarioAntesDeAtualizar = usuarioGateway.buscarPorId(usuarioId);
        String matriculaAntes = usuarioAntesDeAtualizar.getLogin().getMatricula();

        // Act
        atualizarMatriculaUseCase.atualizar(usuarioId, novaMatricula);

        var usuarioAposAtualizar = usuarioGateway.buscarPorId(usuarioId);
        String matriculaDepois = usuarioAposAtualizar.getLogin().getMatricula();

        // Assert
        assertThat(matriculaDepois).isNotEqualTo(matriculaAntes);
        assertThat(matriculaDepois).isEqualTo(novaMatricula);
    }

    @DisplayName("Deve atualizar a matricula com erro. Usuário não encontrado através do ID")
    @Test
    void deveLancarExcecaoSeUsuarioNaoEncontradoAtravesDoId() {
        // Arrange
        UUID usuarioId = UUID.fromString("0fc5db14-7993-4564-bff9-c258b5c73c78");
        String novaMatricula = "us0010";

        // Act & Assert
        assertThatThrownBy(() -> atualizarMatriculaUseCase.atualizar(usuarioId, novaMatricula))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(USUARIO_NAO_ENCONTRADO);
    }

    @DisplayName("Deve atualizar a matricula com erro. Nova matricula já cadastrada.")
    @Test
    void deveLancarExcecaoSeNovaMatriculaForDuplicada() {
        // Arrange
        UUID usuarioId = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
        String novaMatricula = "us0002";

        // Act & Assert
        assertThatThrownBy(() -> atualizarMatriculaUseCase.atualizar(usuarioId, novaMatricula))
                .isInstanceOf(MatriculaDuplicadaException.class)
                .hasMessage(MATRICULA_DUPLICADA);
    }

    @DisplayName("Deve atualizar a matricula com erro. Usuário encontrado está inativo.")
    @Test
    void deveLancarExcecaoSeUsuarioEstiverInativo() {
        // Arrange
        UUID usuarioId = UUID.fromString("602a4056-68d0-44f0-8284-14b0cf7a75b6");
        String novaMatricula = "us0010";

        // Act & Assert
        assertThatThrownBy(() -> atualizarMatriculaUseCase.atualizar(usuarioId, novaMatricula))
                .isInstanceOf(UsuarioInativoException.class)
                .hasMessage(USUARIO_INATIVO);
    }

    // nao sei como testar esse case, pois nos so usamos o perfil na hora de salvar, e ele vem direto do Usuario retornado pelo gateway.
//    @DisplayName("Deve atualizar a matricula com erro. Perfil não encontrado através do ID")
//    @Test
//    void deveLancarExcecaoSePerfilNaoEncontradoAtravesDoId() {
//        // Arrange
//        UUID usuarioId = UUID.randomUUID();
//        String novaMatricula = "us0010";
//        Usuario usuarioRetornado = coreUsuarioEntityAtivoValido();
//
//        when(usuarioGateway.buscarPorId(any(UUID.class))).thenReturn(usuarioRetornado);
//        when(usuarioGateway.matriculaJaCadastrada(anyString())).thenReturn(false);
//        when(perfilGateway.buscarPorId(anyInt())).thenThrow(new PerfilInvalidoException(PERFIS_NAO_ENCONTRADOS));
//        doNothing().when(usuarioGateway).salvar(any(DadosUsuarioCoreDto.class));
//
//        // Act & Assert
//        assertThatThrownBy(() -> atualizarMatriculaUseCase.atualizar(usuarioId, novaMatricula))
//                .isInstanceOf(PerfilInvalidoException.class)
//                .hasMessage(PERFIS_NAO_ENCONTRADOS);
//        verify(usuarioGateway, times(1)).buscarPorId(any(UUID.class));
//        verify(usuarioGateway, times(1)).matriculaJaCadastrada(anyString());
//        verify(perfilGateway, times(1)).buscarPorId(anyInt());
//        verify(usuarioGateway, times(0)).salvar(any(DadosUsuarioCoreDto.class));
//    }
}
