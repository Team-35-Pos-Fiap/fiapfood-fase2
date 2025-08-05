package br.com.fiapfood.core.controllers.integracao;

import br.com.fiapfood.core.controllers.impl.UsuarioCoreController;
import br.com.fiapfood.core.controllers.interfaces.IUsuarioCoreController;
import br.com.fiapfood.core.exceptions.perfil.PerfilInvalidoException;
import br.com.fiapfood.core.exceptions.usuario.*;
import br.com.fiapfood.core.gateways.impl.PerfilGateway;
import br.com.fiapfood.core.gateways.impl.UsuarioGateway;
import br.com.fiapfood.core.gateways.interfaces.IPerfilGateway;
import br.com.fiapfood.core.gateways.interfaces.IUsuarioGateway;
import br.com.fiapfood.core.usecases.login.impl.AtualizarMatriculaUseCase;
import br.com.fiapfood.core.usecases.login.impl.AtualizarSenhaUseCase;
import br.com.fiapfood.core.usecases.login.impl.ValidarLoginUseCase;
import br.com.fiapfood.core.usecases.login.interfaces.IAtualizarMatriculaUseCase;
import br.com.fiapfood.core.usecases.login.interfaces.IAtualizarSenhaUseCase;
import br.com.fiapfood.core.usecases.login.interfaces.IValidarAcessoUseCase;
import br.com.fiapfood.core.usecases.usuario.impl.*;
import br.com.fiapfood.core.usecases.usuario.interfaces.*;
import br.com.fiapfood.infraestructure.controllers.request.endereco.DadosEnderecoDto;
import br.com.fiapfood.infraestructure.controllers.request.login.LoginDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.CadastrarUsuarioDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.UsuarioDto;
import br.com.fiapfood.infraestructure.repositories.interfaces.IPerfilRepository;
import br.com.fiapfood.infraestructure.repositories.interfaces.IUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static br.com.fiapfood.utils.DtoDataGenerator.dadosEnderecoDtoValido;
import static br.com.fiapfood.utils.DtoDataGenerator.loginDtoValido;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UsuarioCoreControllerIT {

    private IUsuarioCoreController usuarioCoreController;
    @Autowired
    IUsuarioRepository usuarioRepository;
    @Autowired
    IPerfilRepository perfilRepository;

    @BeforeEach
    void setUp() {
        IPerfilGateway perfilGateway = new PerfilGateway(perfilRepository);
        IUsuarioGateway usuarioGateway = new UsuarioGateway(usuarioRepository);

        IBuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase = new BuscarUsuarioPorIdUseCase(usuarioGateway, perfilGateway);
        IBuscarTodosUsuariosUseCase buscarTodosUsuariosUseCase = new BuscarTodosUsuariosUseCase(usuarioGateway, perfilGateway);
        ICadastrarUsuarioUseCase cadastrarUsuarioUseCase = new CadastrarUsuarioUseCase(usuarioGateway, perfilGateway);
        IAtualizarEmailUsuarioUseCase atualizarEmailUsuarioUseCase =  new AtualizarEmailUsuarioUseCase(usuarioGateway, perfilGateway);
        IAtualizarNomeUsuarioUseCase atualizarNomeUsuarioUseCase = new AtualizarNomeUsuarioUseCase(usuarioGateway, perfilGateway);
        IInativarUsuarioUseCase inativarUsuarioUseCase =  new InativarUsuarioUseCase(usuarioGateway, perfilGateway);
        IReativarUsuarioUseCase reativarUsuarioUseCase =  new ReativarUsuarioUseCase(usuarioGateway, perfilGateway);
        IAtualizarPerfilUsuarioUseCase atualizarPerfilUsuarioUseCase =  new AtualizarPerfilUsuarioUseCase(usuarioGateway, perfilGateway);
        IAtualizarEnderecoUsuarioUseCase atualizarEnderecoUsuarioUseCase =  new AtualizarEnderecoUsuarioUseCase(usuarioGateway, perfilGateway);
        IValidarAcessoUseCase validarAcessoUseCase = new ValidarLoginUseCase(usuarioGateway);
        IAtualizarSenhaUseCase atualizarSenhaUseCase = new AtualizarSenhaUseCase(usuarioGateway, perfilGateway);
        IAtualizarMatriculaUseCase atualizarMatriculaUseCase = new AtualizarMatriculaUseCase(usuarioGateway, perfilGateway);

        usuarioCoreController = new UsuarioCoreController(
                buscarUsuarioPorIdUseCase,
                buscarTodosUsuariosUseCase,
                cadastrarUsuarioUseCase,
                atualizarEmailUsuarioUseCase,
                atualizarNomeUsuarioUseCase,
                inativarUsuarioUseCase,
                reativarUsuarioUseCase,
                atualizarPerfilUsuarioUseCase,
                atualizarEnderecoUsuarioUseCase,
                validarAcessoUseCase,
                atualizarSenhaUseCase,
                atualizarMatriculaUseCase
        );
    }

    @Nested
    class CadastrarUsuarioRequest {
        private List<UsuarioDto> buscarTodosUsuarios() {
            int pagina = 1;
            int totalPaginas;
            List<UsuarioDto> todosUsuarios = new ArrayList<>();

            do {
                var paginaDto = usuarioCoreController.buscarTodos(pagina);
                todosUsuarios.addAll(paginaDto.usuarios());
                totalPaginas = paginaDto.paginacao().totalPaginas();
                pagina++;
            } while (pagina <= totalPaginas);

            return todosUsuarios;
        }

        @DisplayName("Cadastrar novo usuário chamando o use case com sucesso")
        @Test
        void devePermitirCadastrarUsuarioComSucesso(){
            // Arrange
            CadastrarUsuarioDto dadosUsuario = new CadastrarUsuarioDto(
                    "John Doe",
                    1,
                    new LoginDto(
                            null,
                            "us0010",
                            "123"
                    ),
                    "john.doe@email.com",
                    dadosEnderecoDtoValido()
            );
            var listaDeUsuariosAntesDoCadastro = buscarTodosUsuarios();
            boolean usuarioRegistradoAntes =  listaDeUsuariosAntesDoCadastro.stream()
                    .anyMatch(usuario -> usuario.nome().equals(dadosUsuario.nome()));

            // Act
            usuarioCoreController.cadastrar(dadosUsuario);
            var listaDeUsuariosAposCadastro = buscarTodosUsuarios();
            boolean usuarioRegistradoDepois =  listaDeUsuariosAposCadastro.stream()
                    .anyMatch(usuario -> usuario.nome().equals(dadosUsuario.nome()));

            // Assert
            assertThat(listaDeUsuariosAposCadastro.size()).isEqualTo(listaDeUsuariosAntesDoCadastro.size() + 1);
            assertThat(usuarioRegistradoDepois).isNotEqualTo(usuarioRegistradoAntes);
            assertThat(usuarioRegistradoDepois).isTrue();
        }

        @DisplayName("Cadastrar novo usuário chamando o use case com erro. Email já cadastrado")
        @Test
        void deveLancarExcecaoSeEmailJaCadastrado(){
            // Arrange
            CadastrarUsuarioDto dadosUsuario = new CadastrarUsuarioDto(
                    "John Doe",
                    1,
                    new LoginDto(
                            null,
                            "us0010",
                            "123"
                    ),
                    "thiago@fiapfood.com",
                    dadosEnderecoDtoValido()
            );

            //Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.cadastrar(dadosUsuario))
                    .isInstanceOf(EmailDuplicadoException.class)
                    .hasMessage("Já existe um usuário com o email informado.");
        }

        @DisplayName("Cadastrar novo usuário chamando o use case com erro. Matricula já cadastrada")
        @Test
        void deveLancarExcecaoSeMatriculaJaCadastrado(){
            // Arrange
            CadastrarUsuarioDto dadosUsuario = new CadastrarUsuarioDto(
                    "John Doe",
                    1,
                    new LoginDto(
                            null,
                            "us0001",
                            "123"
                    ),
                    "john.doe@email.com",
                    dadosEnderecoDtoValido()
            );

            //Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.cadastrar(dadosUsuario))
                    .isInstanceOf(MatriculaDuplicadaException.class)
                    .hasMessage("Já existe um usuário com a matrícula informada.");
        }

        @DisplayName("Cadastrar novo usuário chamando o use case com erro. Perfil informado não cadastrado.")
        @Test
        void deveLancarExcecaoSePerfilNaoCadstrado(){
            // Arrange
            CadastrarUsuarioDto dadosUsuario = new CadastrarUsuarioDto(
                    "John Doe",
                    10,
                    loginDtoValido(),
                    "john.doe@email.com",
                    dadosEnderecoDtoValido()
            );

            //Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.cadastrar(dadosUsuario))
                    .isInstanceOf(PerfilInvalidoException.class)
                    .hasMessage("Não foi encontrado nenhum perfil com o id informado.");
        }
    }

    @Nested
    class InativarUsuarioRequest {
        @DisplayName("Inativar usuário chamando use case com sucesso.")
        @Test
        void devePermitirInativarUsuarioComSucesso(){
            // Arrange
            UUID id = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");

            // Act
            usuarioCoreController.inativar(id);
            var usuarioAposInativacao = usuarioCoreController.buscarUsuarioPorId(id);

            // Assert
            assertThat(usuarioAposInativacao.isAtivo()).isFalse();
        }

        @DisplayName("Inativar usuário chamando o use case com erro. Usuário não encontrado.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioPorId(){
            // Arrange
            UUID id = UUID.fromString("0fc5db14-7993-4564-bff9-c258b5c73c87");

            //Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.inativar(id))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum usuário.");
        }

        @DisplayName("Inativar usuário chamando o use case com erro. Usuário já está inativo.")
        @Test
        void deveLancarExcecaoSeUsuarioJaEstiverInativo(){
            // Arrange
            UUID id = UUID.fromString("602a4056-68d0-44f0-8284-14b0cf7a75b6");

            //Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.inativar(id))
                    .isInstanceOf(AtualizacaoStatusUsuarioNaoPermitidaException.class)
                    .hasMessage("Não é possível inativar o usuário, pois ele já se encontra inativo.");
        }
    }

    @Nested
    class AtivarUsuarioRequest {
        @DisplayName("Ativar usuário chamando use case com sucesso.")
        @Test
        void devePermitirReativarUsuarioComSucesso(){
            // Arrange
            UUID id = UUID.fromString("602a4056-68d0-44f0-8284-14b0cf7a75b6");

            // Act
            usuarioCoreController.reativar(id);
            var usuarioAposReativacao = usuarioCoreController.buscarUsuarioPorId(id);

            // Assert
            assertThat(usuarioAposReativacao.isAtivo()).isTrue();
        }

        @DisplayName("Reativar usuário chamando o use case com erro. Usuário não encontrado.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioPorId(){
            // Arrange
            UUID id = UUID.fromString("206a4056-68d0-44f0-8284-14b0cf7a76b5");

            //Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.reativar(id))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum usuário.");
        }

        @DisplayName("Reativar usuário chamando o use case com erro. Usuário já está ativo.")
        @Test
        void deveLancarExcecaoSeUsuarioJaEstiverInativo(){
            // Arrange
            UUID id = UUID.fromString("b48bc2dc-fd87-462d-a8a6-6e74674d0338");

            //Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.reativar(id))
                    .isInstanceOf(AtualizacaoStatusUsuarioNaoPermitidaException.class)
                    .hasMessage("Não é possível reativar um usuário, pois ele já se encontra ativo.");
        }
    }

    @Nested
    class BuscarUsuarioPorIdRequest {

        @DisplayName("Busca usuário por id chamando o use case com sucesso.")
        @Test
        void devePermitirBuscarUsuarioPorId(){
            // Arrange
            UUID id = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");

            // Act
            var usuarioRetornadoDoController = usuarioCoreController.buscarUsuarioPorId(id);

            // Assert
            assertThat(usuarioRetornadoDoController.id()).isEqualTo(id);
            assertThat(usuarioRetornadoDoController.nome()).isEqualTo("Thiago Motta");
            assertThat(usuarioRetornadoDoController.email()).isEqualTo("thiago@fiapfood.com");
        }

        @DisplayName("Busca usuário por id chamando o use case com erro. Usuário não encontrado.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioAtivoPorId(){
            // Arrange
            UUID id = UUID.fromString("0fc5db14-7993-4564-bff9-c258b5c737b8");

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.buscarUsuarioPorId(id))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum usuário.");
        }
    }

    @Nested
    class BuscarTodosUsuariosRequest {
        @DisplayName("Buscar todos usuarios chamando o use case com sucesso")
        @Test
        void deveRetornarListaDeUsuariosPaginada(){
            // Arrange

            // Act
            var usuariosRetornadosPeloController = usuarioCoreController.buscarTodos(1);

            // Assert
            assertThat(usuariosRetornadosPeloController).isNotNull();
            assertThat(usuariosRetornadosPeloController.usuarios()).isNotNull();
            assertThat(usuariosRetornadosPeloController.paginacao()).isNotNull();
        }

        @DisplayName("Buscar todos usuarios com erro. Pagina nao contem nenhum usuario")
        @Test
        void deveRetornarExcecaoSePaginaForMaiorDoQueOLimite(){
            // Arrange
            int pagina = 10;

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.buscarTodos(pagina))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foram encontrados usuários na base de dados para a página informada.");
        }
    }

    @Nested
    class AtualizarEmailRequest {

        @DisplayName("Atualizar email chamando o use case com sucesso")
        @Test
        void devePermitirAtualizarEmail(){
            // Arrange
            UUID id = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            String novoEmail = "john.doe@email.com";

            // Act
            usuarioCoreController.atualizarEmail(id, novoEmail);
            var usuarioAposAtualizacao = usuarioCoreController.buscarUsuarioPorId(id);

            // Assert
            assertThat(usuarioAposAtualizacao.id()).isEqualTo(id);
            assertThat(usuarioAposAtualizacao.email()).isEqualTo(novoEmail);
        }

        @DisplayName("Atualizar email chamando o use case com erro. Usuário nao encontrado.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioPorId(){
            // Arrange
            // Arrange
            UUID id = UUID.fromString("0fc5db14-7993-4564-bff9-c258b5c73c87");
            String novoEmail = "john.doe@email.com";

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarEmail(id, novoEmail))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum usuário.");
        }

        @DisplayName("Atualizar email chamando o use case com erro. Usuário encontrado inativo")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioAtivoPorId(){
            // Arrange
            UUID id = UUID.fromString("602a4056-68d0-44f0-8284-14b0cf7a75b6");
            String novoEmail = "john.doe@email.com";

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarEmail(id, novoEmail))
                    .isInstanceOf(AtualizacaoEmailUsuarioNaoPermitidoException.class)
                    .hasMessage("Não é possível alterar o email de um usuário inativo.");
        }

        @DisplayName("Atualizar email com erro. Novo email ja cadastrado.")
        @Test
        void deveLancarExecaoSeEmailJaCadastrado(){
            // Arrange
            UUID id = UUID.fromString("b48bc2dc-fd87-462d-a8a6-6e74674d0338");
            String novoEmail = "rafael.santos@fiapfood.com";

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarEmail(id, novoEmail))
                    .isInstanceOf(AtualizacaoEmailUsuarioNaoPermitidoException.class)
                    .hasMessage("Já existe um usuário com o email informado.");
        }
    }

    @Nested
    class AtualizarPerfilRequest {
        @DisplayName("Atualizar perfil chamando o use case com sucesso.")
        @Test
        void devePermitirAtualizarPerfil(){
            // Arrange
            UUID id = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            int novoPerfilId = 2;

            // Act
            usuarioCoreController.atualizarPerfil(id, novoPerfilId);
            var usuarioAtualizado = usuarioCoreController.buscarUsuarioPorId(id);

            // Assert
            assertThat(usuarioAtualizado.id()).isEqualTo(id);
            assertThat(usuarioAtualizado.perfil().id()).isEqualTo(novoPerfilId);
        }

        @DisplayName("Atualizar perfil chamando o use case com erro. Usuário nao encontrado por id.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioPorId(){
            // Arrange
            UUID id = UUID.fromString("0fc5db14-7993-4564-bff9-c258b5c73c78");
            int novoPerfilId = 2;

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarPerfil(id, novoPerfilId))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum usuário.");
        }

        @DisplayName("Atualizar perfil chamando o use case com erro. Usuário encontrado esta inativo")
        @Test
        void deveLancarExcecaoSeUsuarioEstiverInativo(){
            // Arrange
            UUID id = UUID.fromString("602a4056-68d0-44f0-8284-14b0cf7a75b6");
            int novoPerfilId = 2;

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarPerfil(id, novoPerfilId))
                    .isInstanceOf(AtualizacaoPerfilNaoPermitidaException.class)
                    .hasMessage("Não é possível alterar o perfil de um usuário inativo.");
        }

        @DisplayName("Atualizar perfil com erro. Novo perfil é o mesmo do ja cadastrado.")
        @Test
        void deveLancarExcecaoSeNovoPerfilForOMesmoDoRegistrado(){
            // Arrange
            UUID id = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            int novoPerfilId = 1;

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarPerfil(id, novoPerfilId))
                    .isInstanceOf(AtualizacaoPerfilNaoPermitidaException.class)
                    .hasMessage("O perfil selecionado é o mesmo que o usuário já possui.");
        }
    }

    @Nested
    class AtualizarEnderecoRequest {
        @DisplayName("Atualizar endereco chamando o use case com sucesso.")
        @Test
        void devePermitirAtualizarEndereco(){
            // Arrange
            UUID usuarioId = UUID.fromString("b48bc2dc-fd87-462d-a8a6-6e74674d0338");
            DadosEnderecoDto dadosEnderecoDoController = dadosEnderecoDtoValido();

            // Act
            usuarioCoreController.atualizarDadosEndereco(usuarioId, dadosEnderecoDoController);
            var usuarioAtualizado = usuarioCoreController.buscarUsuarioPorId(usuarioId);

            // Assert
            assertThat(usuarioAtualizado.id()).isEqualTo(usuarioId);
            assertThat(usuarioAtualizado.endereco().endereco()).isEqualTo(dadosEnderecoDoController.endereco());
            assertThat(usuarioAtualizado.endereco().bairro()).isEqualTo(dadosEnderecoDoController.bairro());
            assertThat(usuarioAtualizado.endereco().cep()).isEqualTo(dadosEnderecoDoController.cep());
        }

        @DisplayName("Atualizar endereço chamando o use case com erro. Usuario nao encontrado por id.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioPorId(){
            // Arrange
            UUID usuarioId = UUID.fromString("84bbc2dc-fd87-462d-a8a6-6e74674d0338");
            DadosEnderecoDto dadosEnderecoDoController = dadosEnderecoDtoValido();

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarDadosEndereco(usuarioId, dadosEnderecoDoController))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum usuário.");
        }

        @DisplayName("Atualizar endereço chamando o use case com erro. Usuario encontrado esta inativo.")
        @Test
        void deveLancarExcecaoSeUsuarioEstiverInativo(){
            // Arrange
            UUID usuarioId = UUID.fromString("602a4056-68d0-44f0-8284-14b0cf7a75b6");
            DadosEnderecoDto dadosEnderecoDoController = dadosEnderecoDtoValido();

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarDadosEndereco(usuarioId, dadosEnderecoDoController))
                    .isInstanceOf(UsuarioInativoException.class)
                    .hasMessage("Não é possível alterar o endereço de um usuário inativo.");
        }
    }

    @Nested
    class AtualizarNomeRequest {
        @DisplayName("Atualizar nome chamando o use case com sucesso.")
        @Test
        void devePermitirAtualizarNome(){
            // Arrange
            UUID idUsuario = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            String novoNome = "John Doe";

            // Act
            usuarioCoreController.atualizarNome(idUsuario,novoNome);
            var usuarioAtualizado = usuarioCoreController.buscarUsuarioPorId(idUsuario);

            // Assert
            assertThat(usuarioAtualizado.nome()).isEqualTo(novoNome);
            assertThat(usuarioAtualizado.id()).isEqualTo(idUsuario);
        }

        @DisplayName("Atualizar nome chamando o use case com erro. Usuario nao encontrado por id.")
        @Test
        void deveLancarExcecaoSeNaoEncontrarUsuarioPorId(){
            // Arrange
            UUID idUsuario = UUID.fromString("0fc5db14-7993-4564-bff9-c258b5c73b87");
            String novoNome = "John Doe";

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarNome(idUsuario, novoNome))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum usuário.");
        }

        @DisplayName("Atualizar nome chamando o use case com erro. Usuario encontrado esta inativo")
        @Test
        void deveLancarExcecaoSeUsuarioEncontradoEstiverInativo(){
            // Arrange
            UUID idUsuario = UUID.fromString("602a4056-68d0-44f0-8284-14b0cf7a75b6");
            String novoNome = "John Doe";

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarNome(idUsuario, novoNome))
                    .isInstanceOf(UsuarioInativoException.class)
                    .hasMessage("Não é possível alterar o nome de um usuário inativo.");
        }

        @DisplayName("Atualizar nome chamando o use case com erro. Novo nome igual ao atual.")
        @Test
        void deveLancarExcecaoSeNovoNomeForIgualAoAtual(){
            // Arrange
            UUID idUsuario = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            String novoNome = "Thiago Motta";

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarNome(idUsuario, novoNome))
                    .isInstanceOf(AtualizacaoNomeUsuarioNaoPermitidoException.class)
                    .hasMessage("Não é possível alterar o nome do usuário, pois ele é igual ao nome do usuário.");
        }
    }

    @Nested
    class AtualizarSenhaRequest {
        @DisplayName("Trocar senha chamando o use case com sucesso")
        @Test
        void devePermitirTrocarSenha(){
            // Arrange
            UUID usuarioId = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            String novaSenha = "312";

            // Act
            usuarioCoreController.atualizarSenha(usuarioId, novaSenha);
            var usuarioAtualizado = usuarioCoreController.buscarUsuarioPorId(usuarioId);

            // Assert
            assertThat(usuarioId).isEqualTo(usuarioAtualizado.id());
            assertThat(usuarioAtualizado.login().senha()).isEqualTo(novaSenha);
        }

        @DisplayName("Atualizar senha chamndo o use case com erro. Usuario nao encontrado atraves do id")
        @Test
        void deveLancarExcecaoSeUsuarioNaoEncontradoAtravesDoId(){
            // Arrange
            UUID usuarioId = UUID.fromString("0fc5db14-7993-4564-bff9-c258b5c73b87");
            String novaSenha = "312";

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarSenha(usuarioId, novaSenha))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum usuário.");
        }

        @DisplayName("Atualizar senha chamando o use case com erro. Usuário encontrado esta inativo")
        @Test
        void deveLancarExcecaoSeUsuarioEstiverInativo(){
            // Arrange
            UUID usuarioId = UUID.fromString("0fc5db14-7993-4564-bff9-c258b5c73b87");
            String novaSenha = "312";

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarSenha(usuarioId, novaSenha))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum usuário.");
        }
    }

    @Nested
    class AtualizarMatriculaRequest {
        @DisplayName("Trocar matricula chamando o use case com sucesso")
        @Test
        void devePermitirTrocarMatricula(){
            // Arrange
            UUID usuarioId = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            String novaMatricula = "us0010";

            // Act
            usuarioCoreController.atualizarMatricula(usuarioId, novaMatricula);
            var usuarioAtualizado = usuarioCoreController.buscarUsuarioPorId(usuarioId);

            // Assert
            assertThat(usuarioId).isEqualTo(usuarioAtualizado.id());
            assertThat(usuarioAtualizado.login().matricula()).isEqualTo(novaMatricula);
        }

        @DisplayName("Atualizar matricula chamando o use case com erro. Usuário nao encontrado através do id.")
        @Test
        void deveLancarExcecaoSeUsuarioNaoEncontradoAtravesDoId(){
            // Arrange
            UUID usuarioId = UUID.fromString("0f55db14-7993-4564-bff9-c258b5c73b87");
            String novaMatricula = "us0010";

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarMatricula(usuarioId, novaMatricula))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum usuário.");
        }

        @DisplayName("Atualizar matricula chamando o use case com erro. Usuário encontrado está inativo")
        @Test
        void deveLancarExcecaoSeUsuarioEstiverInativo(){
            // Arrange
            UUID usuarioId = UUID.fromString("602a4056-68d0-44f0-8284-14b0cf7a75b6");
            String novaMatricula = "us0010";

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarMatricula(usuarioId, novaMatricula))
                    .isInstanceOf(UsuarioInativoException.class)
                    .hasMessage("Não é possível alterar a matrícula, pois o usuário está inativo.");
        }

        @DisplayName("Atualizar matricula chamando o use case com erro. Nova matricula ja cadastrada.")
        @Test
        void deveLancarExcecaoSeNovaMatriculaJaCadastrada(){
            // Arrange
            UUID usuarioId = UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c");
            String novaMatricula = "us0002";

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.atualizarMatricula(usuarioId, novaMatricula))
                    .isInstanceOf(MatriculaDuplicadaException.class)
                    .hasMessage("Já existe um usuário com a matrícula informada.");
        }
    }

    @Nested
    class ValidarLoginRequest {
        @DisplayName("Validação de login chamando o use case com sucesso")
        @Test
        void devePermitirValidarLogin(){
            // Arrange
            String matricula = "us0001";
            String senha = "123";

            // Act
            String mensagemRetornada = usuarioCoreController.validarAcesso(matricula, senha);

            // Assert
            assertThat(mensagemRetornada).isNotNull();
            assertThat(mensagemRetornada).isEqualTo("Acesso liberado para o usuário Thiago Motta");
        }

        @DisplayName("Validação de login chamando o use case com erro. Usuario não encontrado através de matrícula e senha.")
        @Test
        void deveLancarExcecaoSeUsuarioNaoEncontradoAtravesDaMatriculaESenha(){
            // Arrange
            String matricula = "us0001";
            String senha = "1234";

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.validarAcesso(matricula, senha))
                    .isInstanceOf(UsuarioNaoEncontradoException.class)
                    .hasMessage("Não foi encontrado nenhum usuário.");
        }

        @DisplayName("Validação de login chamando o use case com erro. Usuario encontrado está inativo")
        @Test
        void deveLancarExcecaoSeUsuarioEncontradoEstiverInativo(){
            // Arrange
            String matricula = "us0003";
            String senha = "123";

            // Act & Assert
            assertThatThrownBy(() -> usuarioCoreController.validarAcesso(matricula, senha))
                    .isInstanceOf(UsuarioSemAcessoException.class)
                    .hasMessage("Não é possível realizar o login para usuários inativos.");
        }
    }
}
