package br.com.fiapfood.core.usecases.integracao.usuario;

import br.com.fiapfood.core.entities.dto.endereco.DadosEnderecoCoreDto;
import br.com.fiapfood.core.exceptions.usuario.UsuarioInativoException;
import br.com.fiapfood.core.exceptions.usuario.UsuarioNaoEncontradoException;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.impl.UsuarioGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.usuario.impl.AtualizarEnderecoUsuarioUseCase;
import br.com.fiapfood.core.usecases.usuario.interfaces.IAtualizarEnderecoUsuarioUseCase;
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
public class AtualizarEnderecoUsuarioUseCaseIT {

    private final String USUARIO_INATIVO = "Não é possível alterar o endereço de um usuário inativo.";
    private final String USUARIO_NAO_ENCONTRADO = "Não foi encontrado nenhum usuário.";

    private IAtualizarEnderecoUsuarioUseCase atualizarEnderecoUsuarioUseCase;
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

        atualizarEnderecoUsuarioUseCase = new AtualizarEnderecoUsuarioUseCase(usuarioGateway, perfilGateway);
    }

    @DisplayName("Deve atualizar o endereço do usuário com sucesso.")
    @Test
    void deveAtualizarEnderecoDoUsuarioComSucesso() {
        // Arrange
        UUID usuarioId = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
        DadosEnderecoCoreDto novoEndereco = new DadosEnderecoCoreDto(
                "Nova Cidade",
                "12345678",
                "Novo Bairro",
                "Nova Rua",
                "Novo Estado",
                100,
                "1"
        );

        // Act
        atualizarEnderecoUsuarioUseCase.atualizar(usuarioId,novoEndereco);

        var usuarioDepoisDeAtualizar = usuarioGateway.buscarPorId(usuarioId);

        assertThat(usuarioDepoisDeAtualizar).isNotNull();
        assertThat(usuarioDepoisDeAtualizar.getId()).isEqualTo(usuarioId);
        assertThat(usuarioDepoisDeAtualizar.getDadosEndereco().getCidade()).isEqualTo(novoEndereco.cidade());
        assertThat(usuarioDepoisDeAtualizar.getDadosEndereco().getCep()).isEqualTo(novoEndereco.cep());
        assertThat(usuarioDepoisDeAtualizar.getDadosEndereco().getBairro()).isEqualTo(novoEndereco.bairro());
        assertThat(usuarioDepoisDeAtualizar.getDadosEndereco().getEstado()).isEqualTo(novoEndereco.estado());
        assertThat(usuarioDepoisDeAtualizar.getDadosEndereco().getEndereco()).isEqualTo(novoEndereco.endereco());
        assertThat(usuarioDepoisDeAtualizar.getDadosEndereco().getComplemento()).isEqualTo(novoEndereco.complemento());
        assertThat(usuarioDepoisDeAtualizar.getDadosEndereco().getNumero()).isEqualTo(novoEndereco.numero());
    }

    @DisplayName("Deve atualizar o endereço do usuário com erro. Usuário não encontrado através do ID")
    @Test
    void deveLancarExcecaoSeNaoEncontrarUsuarioAtravesDoID() {
        // Arrange
        UUID usuarioId = UUID.fromString("0fc5db14-7993-4564-bff9-c258b5c73c87");
        DadosEnderecoCoreDto novoEndereco = new DadosEnderecoCoreDto(
                "Nova Cidade",
                "12345678",
                "Novo Bairro",
                "Nova Rua",
                "Novo Estado",
                100,
                "1"
        );

        // Act & Assert
        assertThatThrownBy(() -> atualizarEnderecoUsuarioUseCase.atualizar(usuarioId, novoEndereco))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage(USUARIO_NAO_ENCONTRADO);
    }

    @DisplayName("Deve atualizar o endereço do usuário com erro. Usuário encontrado está inativo")
    @Test
    void deveLancarExcecaoSeUsuarioEstiverInativo() {
        // Arrange
        UUID usuarioId = UUID.fromString("602a4056-68d0-44f0-8284-14b0cf7a75b6");
        DadosEnderecoCoreDto novoEndereco = new DadosEnderecoCoreDto(
                "Nova Cidade",
                "12345678",
                "Novo Bairro",
                "Nova Rua",
                "Novo Estado",
                100,
                "1"
        );

        // Act & Assert
        assertThatThrownBy(() -> atualizarEnderecoUsuarioUseCase.atualizar(usuarioId, novoEndereco))
                .isInstanceOf(UsuarioInativoException.class)
                .hasMessage(USUARIO_INATIVO);
    }
}
